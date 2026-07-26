package dev.kplanky.todo.service;

import dev.kplanky.todo.model.User;
import dev.kplanky.todo.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class UserServiceTest {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);
        when(passwordEncoder.encode(any())).thenReturn("$2a$12$hash");
        userService = new UserService(userRepository, passwordEncoder);
    }

    @Test
    @DisplayName("stores a hash, never the password itself")
    void storesOnlyTheHash() {
        userService.register("alice", "correct horse battery");

        verify(passwordEncoder).encode("correct horse battery");
        verify(userRepository).insert("alice", "$2a$12$hash");
        verify(userRepository, never()).insert(any(), eq("correct horse battery"));
    }

    @Test
    @DisplayName("trims the username so ' alice' and 'alice' are not two accounts")
    void trimsUsername() {
        userService.register("  alice  ", "long enough password");

        verify(userRepository).insert(eq("alice"), any());
    }

    @Test
    @DisplayName("refuses a blank username")
    void refusesBlankUsername() {
        assertThrows(IllegalArgumentException.class, () -> userService.register("   ", "password12"));
        assertThrows(IllegalArgumentException.class, () -> userService.register(null, "password12"));
        verifyNoInteractions(userRepository);
    }

    @Test
    @DisplayName("enforces the length bounds the column and bcrypt impose")
    void enforcesLengthBounds() {
        assertThrows(IllegalArgumentException.class,
                () -> userService.register("a".repeat(51), "password12"),
                "username longer than the column would be truncated or error at the DB");
        assertThrows(IllegalArgumentException.class,
                () -> userService.register("alice", "short"),
                "a short password must be refused");
        assertThrows(IllegalArgumentException.class,
                () -> userService.register("alice", "p".repeat(73)),
                "bcrypt silently ignores bytes past 72, so anything longer must be refused");
        verifyNoInteractions(userRepository);
    }

    @Test
    @DisplayName("a 72 byte password is accepted, one byte more is not")
    void boundaryIsExact() {
        assertDoesNotThrow(() -> userService.register("alice", "p".repeat(72)));
        assertThrows(IllegalArgumentException.class, () -> userService.register("alice", "p".repeat(73)));
    }

    @Test
    @DisplayName("authenticates a user whose password matches")
    void authenticatesGoodPassword() {
        User stored = new User(1L, "alice", "$2a$12$hash");
        when(userRepository.findByUsername("alice")).thenReturn(Optional.of(stored));
        when(passwordEncoder.matches("secret", "$2a$12$hash")).thenReturn(true);

        assertEquals(Optional.of(stored), userService.authenticate("alice", "secret"));
    }

    @Test
    @DisplayName("rejects the right user with the wrong password")
    void rejectsWrongPassword() {
        when(userRepository.findByUsername("alice"))
                .thenReturn(Optional.of(new User(1L, "alice", "$2a$12$hash")));
        when(passwordEncoder.matches(any(), any())).thenReturn(false);

        assertTrue(userService.authenticate("alice", "wrong").isEmpty());
    }

    @Test
    @DisplayName("still hashes when the user does not exist, so timing does not leak who is registered")
    void unknownUserStillCostsAHash() {
        when(userRepository.findByUsername("nobody")).thenReturn(Optional.empty());

        assertTrue(userService.authenticate("nobody", "secret").isEmpty());
        // The dummy-hash comparison is the whole point: without it, a missing user returns
        // noticeably faster than a wrong password and the login form becomes a user enumerator.
        verify(passwordEncoder).matches(eq("secret"), any());
    }

}
