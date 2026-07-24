package dev.kplanky.todo.repository;

import dev.kplanky.todo.model.User;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class UserRepository {

    private final DataSource dataSource;

    public UserRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Optional<User> findByUsername(String username) {

        String sql = "SELECT id, username, password_hash FROM users WHERE username = ?";

        try (Connection conn = dataSource.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new User(rs.getLong("id"), rs.getString("username"), rs.getString("password_hash")));
                }
                return Optional.empty();
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to look up user: " + username, e);
        }
    }

    public User insert(String username, String passwordHash) {

        String sql = "INSERT INTO users (username, password_hash) VALUES (?, ?) RETURNING id";

        try (Connection conn = dataSource.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, passwordHash);

            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                long id = rs.getLong("id");
                return new User(id, username, passwordHash);
            }

        } catch (SQLException e) {
            if ("23505".equals(e.getSQLState())) {
                throw new DuplicateUsernameException(username);
            }
            throw new RuntimeException("Failed to insert user: " + username, e);
        }
    }

}
