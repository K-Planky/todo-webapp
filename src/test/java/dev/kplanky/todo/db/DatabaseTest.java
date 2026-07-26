package dev.kplanky.todo.db;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.function.UnaryOperator;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseTest {

    private static UnaryOperator<String> env(String... pairs) {
        Map<String, String> values = new java.util.HashMap<>();
        for (int i = 0; i < pairs.length; i += 2) {
            values.put(pairs[i], pairs[i + 1]);
        }
        return values::get;
    }

    @Test
    @DisplayName("a whole DB_URL wins, so local runs and the README stay valid")
    void wholeUrlWins() {
        String url = Database.jdbcUrl(env(
                "DB_URL", "jdbc:postgresql://localhost:5432/todoapp",
                "DB_HOST", "db", "DB_NAME", "ignored"));

        assertEquals("jdbc:postgresql://localhost:5432/todoapp", url);
    }

    @Test
    @DisplayName("assembles the URL from the parts the platform injects")
    void assemblesFromParts() {
        String url = Database.jdbcUrl(env("DB_HOST", "db", "DB_PORT", "6543", "DB_NAME", "todo"));

        assertEquals("jdbc:postgresql://db:6543/todo", url);
    }

    @Test
    @DisplayName("DB_PORT defaults to 5432 when absent or blank")
    void portDefaults() {
        assertEquals("jdbc:postgresql://db:5432/todo",
                Database.jdbcUrl(env("DB_HOST", "db", "DB_NAME", "todo")));
        assertEquals("jdbc:postgresql://db:5432/todo",
                Database.jdbcUrl(env("DB_HOST", "db", "DB_NAME", "todo", "DB_PORT", "  ")));
    }

    @Test
    @DisplayName("a blank DB_URL falls through to the parts rather than being used")
    void blankUrlFallsThrough() {
        assertEquals("jdbc:postgresql://db:5432/todo",
                Database.jdbcUrl(env("DB_URL", "   ", "DB_HOST", "db", "DB_NAME", "todo")));
    }

    @Test
    @DisplayName("fails loudly at startup when the database is not configured at all")
    void failsWhenUnconfigured() {
        // Better to refuse to boot than to start and serve errors on the first request.
        assertThrows(IllegalStateException.class, () -> Database.jdbcUrl(env()));
        assertThrows(IllegalStateException.class, () -> Database.jdbcUrl(env("DB_HOST", "db")));
        assertThrows(IllegalStateException.class, () -> Database.jdbcUrl(env("DB_NAME", "todo")));
    }

}
