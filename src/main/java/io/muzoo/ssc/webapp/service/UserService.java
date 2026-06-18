package io.muzoo.ssc.webapp.service;

import io.muzoo.ssc.webapp.model.User;
import io.muzoo.ssc.webapp.repository.UserRepository;

import java.util.Optional;

public class UserService {

    private static final int MAX_USERNAME_LENGTH = 50;

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
