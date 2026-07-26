package dev.kplanky.todo.db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import dev.kplanky.todo.model.Todo;
import dev.kplanky.todo.repository.DuplicateUsernameException;
import dev.kplanky.todo.repository.TodoRepository;
import dev.kplanky.todo.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.testcontainers.DockerClientFactory;
import org.testcontainers.postgresql.PostgreSQLContainer;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

/**
 * Runs against a real PostgreSQL. The owner-scoping asserted here is the app's authorization
 * boundary: every servlet takes the user id from the session and the id from the request, and it
 * is these queries that stop the two being combined into someone else's data. A fake datasource
 * would prove nothing about that, since the rule lives in the SQL.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SchemaAndRepositoryTest {

    // Non-generic in Testcontainers 2.x; the self-type parameter was dropped.
    private PostgreSQLContainer postgres;
    private HikariDataSource dataSource;
    private UserRepository users;
    private TodoRepository todos;

    @BeforeAll
    void startDatabase() {
        // Skip rather than fail where there is no Docker, so `mvn package` still works on a
        // machine without it. CI has Docker, so this suite does run there.
        assumeTrue(DockerClientFactory.instance().isDockerAvailable(),
                "Docker is not available, skipping the real-database tests");

        postgres = new PostgreSQLContainer("postgres:16-alpine");
        postgres.start();

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(postgres.getJdbcUrl());
        config.setUsername(postgres.getUsername());
        config.setPassword(postgres.getPassword());
        config.setMaximumPoolSize(4);
        dataSource = new HikariDataSource(config);

        users = new UserRepository(dataSource);
        todos = new TodoRepository(dataSource);
    }

    @AfterAll
    void stopDatabase() {
        if (dataSource != null) dataSource.close();
        if (postgres != null) postgres.stop();
    }

    @BeforeEach
    void freshSchema() throws Exception {
        try (Connection c = dataSource.getConnection(); Statement s = c.createStatement()) {
            s.execute("DROP TABLE IF EXISTS todos; DROP TABLE IF EXISTS users;");
        }
        Schema.apply(dataSource);

        // Push todo ids into a range that cannot collide with user ids. Without this both
        // sequences start at 1, and an ownership test can pass for the wrong reason: a query that
        // wrongly compared `id` against the user id would still return the right rows whenever the
        // two happen to be equal. Verified by mutation: with overlapping ids, breaking the
        // scoping in findByUserIdAndId left the whole suite green.
        try (Connection c = dataSource.getConnection(); Statement s = c.createStatement()) {
            s.execute("ALTER SEQUENCE todos_id_seq RESTART WITH 5000");
        }
    }

    @Test
    @DisplayName("applies the schema to an empty database, so a fresh volume comes up usable")
    void appliesToEmptyDatabase() throws Exception {
        try (Connection c = dataSource.getConnection();
             Statement s = c.createStatement();
             ResultSet rs = s.executeQuery(
                     "SELECT tablename FROM pg_tables WHERE schemaname = 'public' ORDER BY tablename")) {
            assertTrue(rs.next());
            assertEquals("todos", rs.getString(1));
            assertTrue(rs.next());
            assertEquals("users", rs.getString(1));
        }
    }

    @Test
    @DisplayName("re-applying is a no-op, so every restart is safe")
    void isIdempotent() {
        var user = users.insert("alice", "hash");
        todos.insert(user.id(), "survive the restart");

        assertDoesNotThrow(() -> Schema.apply(dataSource));
        assertDoesNotThrow(() -> Schema.apply(dataSource));

        assertEquals(1, todos.findByUserId(user.id()).size(), "re-applying must not wipe data");
    }

    @Test
    @DisplayName("usernames are unique, enforced by the database rather than by a check-then-insert")
    void usernamesAreUnique() {
        users.insert("alice", "hash");

        assertThrows(DuplicateUsernameException.class, () -> users.insert("alice", "other"));
    }

    @Test
    @DisplayName("a user's list contains only their own todos")
    void listIsScopedToTheOwner() {
        long alice = users.insert("alice", "h").id();
        long bob = users.insert("bob", "h").id();
        todos.insert(alice, "alice's secret");
        todos.insert(bob, "bob's errand");

        List<Todo> mine = todos.findByUserId(alice);

        assertEquals(1, mine.size());
        assertEquals("alice's secret", mine.getFirst().getTitle());
    }

    @Test
    @DisplayName("fetching another user's todo by id returns nothing")
    void cannotReadSomeoneElsesTodo() {
        long alice = users.insert("alice", "h").id();
        long bob = users.insert("bob", "h").id();
        todos.insert(alice, "alice's secret");
        long aliceTodoId = todos.findByUserId(alice).getFirst().getId();

        assertTrue(todos.findByUserIdAndId(bob, aliceTodoId).isEmpty(),
                "guessing the id must not be enough to read it");
        assertTrue(todos.findByUserIdAndId(alice, aliceTodoId).isPresent());
    }

    @Test
    @DisplayName("editing, deleting or toggling another user's todo changes nothing")
    void cannotWriteSomeoneElsesTodo() {
        long alice = users.insert("alice", "h").id();
        long bob = users.insert("bob", "h").id();
        todos.insert(alice, "alice's secret");
        Todo original = todos.findByUserId(alice).getFirst();

        todos.updateTitle(bob, original.getId(), "defaced");
        todos.toggleCompleted(bob, original.getId());
        todos.delete(bob, original.getId());

        Todo after = todos.findByUserIdAndId(alice, original.getId()).orElseThrow(
                () -> new AssertionError("another user managed to delete it"));
        assertEquals("alice's secret", after.getTitle(), "another user managed to edit it");
        assertEquals(original.isCompleted(), after.isCompleted(), "another user managed to toggle it");
    }

    @Test
    @DisplayName("the owner can edit, toggle and delete their own todo")
    void ownerCanStillDoEverything() {
        long alice = users.insert("alice", "h").id();
        todos.insert(alice, "original");
        long id = todos.findByUserId(alice).getFirst().getId();

        todos.updateTitle(alice, id, "renamed");
        todos.toggleCompleted(alice, id);
        Todo edited = todos.findByUserIdAndId(alice, id).orElseThrow();
        assertEquals("renamed", edited.getTitle());
        assertTrue(edited.isCompleted());

        todos.delete(alice, id);
        assertTrue(todos.findByUserIdAndId(alice, id).isEmpty());
    }

    @Test
    @DisplayName("deleting a user takes their todos with them")
    void todosCascadeWithTheUser() throws Exception {
        long alice = users.insert("alice", "h").id();
        todos.insert(alice, "goes away");

        try (Connection c = dataSource.getConnection(); Statement s = c.createStatement()) {
            s.executeUpdate("DELETE FROM users WHERE id = " + alice);
        }

        assertTrue(todos.findByUserId(alice).isEmpty(), "orphaned rows would outlive the account");
    }

    @Test
    @DisplayName("a quote in a title is data, not SQL")
    void titlesAreParameterised() {
        long alice = users.insert("alice", "h").id();
        String nasty = "'; DROP TABLE todos; --";

        todos.insert(alice, nasty);

        assertEquals(nasty, todos.findByUserId(alice).getFirst().getTitle());
    }

}
