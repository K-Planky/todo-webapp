package io.muzoo.ssc.webapp.repository;

import io.muzoo.ssc.webapp.model.Todo;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TodoRepository {

    private final DataSource dataSource;

    public TodoRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private Todo mapRow(ResultSet rs) throws SQLException {
        return new Todo(
                rs.getLong("id"),
                rs.getLong("user_id"),
                rs.getString("title"),
                rs.getBoolean("completed"),
                rs.getObject("created_at", OffsetDateTime.class));
    }

    public List<Todo> findByUserId(long userId) {

        String sql = "SELECT id, user_id, title, completed, created_at FROM todos WHERE user_id = ? ORDER BY created_at DESC, id DESC";

        List<Todo> todos = new ArrayList<>();

        try (Connection conn = dataSource.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    todos.add(mapRow(rs));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to list todos for user: " + userId, e);
        }

        return todos;
    }

    public Optional<Todo> findByUserIdAndId(long userId, long id) {

        String sql = "SELECT id, user_id, title, completed, created_at FROM todos WHERE user_id = ? AND id = ?";

        try (Connection conn = dataSource.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, userId);
            ps.setLong(2, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to load todo " + id, e);
        }
    }

    public Todo insert(long userId, String title) {

        String sql = "INSERT INTO todos (user_id, title) VALUES (?, ?) RETURNING id, completed, created_at";

        try (Connection conn = dataSource.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, userId);
            ps.setString(2, title);

            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return new Todo(
                        rs.getLong("id"),
                        userId,
                        title,
                        rs.getBoolean("completed"),
                        rs.getObject("created_at", OffsetDateTime.class)
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to insert todo for user " + userId, e);
        }
    }

    public int updateTitle(long userId, long id, String title) {

        String sql = "UPDATE todos SET title = ? WHERE user_id = ? AND id = ?";

        try (Connection conn = dataSource.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, title);
            ps.setLong(2, userId);
            ps.setLong(3, id);
            return ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update todo " + id, e);
        }
    }

    public int delete(long userId, long id) {

        String sql = "DELETE FROM todos WHERE user_id = ? AND id = ?";

        try (Connection conn = dataSource.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, userId);
            ps.setLong(2, id);
            return ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete todo " + id, e);
        }
    }

    public int toggleCompleted(long userId, long id) {

        String sql = "UPDATE todos SET completed = NOT completed WHERE user_id = ? AND id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, userId);
            ps.setLong(2, id);
            return ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to toggle todo " + id, e);
        }
    }

}
