package dev.kplanky.todo.service;

import dev.kplanky.todo.model.User;
import dev.kplanky.todo.repository.UserRepository;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class UserService {

    private static final int MAX_USERNAME_LENGTH = 50;
    private static final int MAX_PASSWORD_BYTES = 72;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final String dummyHash;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.dummyHash = passwordEncoder.encode("not-a-real-password");
    }

    public User register(String username, String rawPassword) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username is required");
        }
        String trimmedUsername = username.trim();
        if (trimmedUsername.length() > MAX_USERNAME_LENGTH) {
            throw new IllegalArgumentException("Username must be " + MAX_USERNAME_LENGTH + " characters or fewer");
        }
        if (rawPassword == null || rawPassword.length() < 8) {
            throw new IllegalArgumentException("Password must be at least 8 characters");
        }
        if (rawPassword.getBytes(StandardCharsets.UTF_8).length > MAX_PASSWORD_BYTES) {
            throw new IllegalArgumentException("Password must be " + MAX_PASSWORD_BYTES + " characters or fewer");
        }

        String hash = passwordEncoder.encode(rawPassword);

        return userRepository.insert(trimmedUsername, hash);
    }

    public Optional<User> authenticate(String username, String rawPassword) {
        Optional<User> found = userRepository.findByUsername(username);

        if (found.isPresent()) {
            if (passwordEncoder.matches(rawPassword, found.get().passwordHash())) {
                return found;
            }
            return Optional.empty();
        }

        passwordEncoder.matches(rawPassword, dummyHash);
        return Optional.empty();
    }

}
