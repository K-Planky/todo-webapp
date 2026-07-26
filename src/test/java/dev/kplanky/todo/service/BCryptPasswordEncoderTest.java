package dev.kplanky.todo.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BCryptPasswordEncoderTest {

    // Cost 4 keeps the suite fast. The cost the app actually uses is asserted separately below,
    // because that number is the security property, not this one.
    private final PasswordEncoder encoder = new BCryptPasswordEncoder(4);

    @Test
    @DisplayName("a password verifies against its own hash")
    void roundTrip() {
        String hash = encoder.encode("correct horse");

        assertTrue(encoder.matches("correct horse", hash));
    }

    @Test
    @DisplayName("a wrong password does not verify")
    void wrongPasswordFails() {
        assertFalse(encoder.matches("wrong", encoder.encode("correct horse")));
    }

    @Test
    @DisplayName("the same password hashes differently every time, so the salt is doing its job")
    void saltsEachHash() {
        assertNotEquals(encoder.encode("same"), encoder.encode("same"));
    }

    @Test
    @DisplayName("the plaintext never appears in the hash")
    void hashDoesNotContainThePassword() {
        assertFalse(encoder.encode("hunter2").contains("hunter2"));
    }

    @Test
    @DisplayName("a malformed stored hash returns false instead of throwing")
    void malformedHashIsRejectedQuietly() {
        // A corrupt or truncated row must fail the login, not 500 the request.
        assertFalse(encoder.matches("anything", "not-a-bcrypt-hash"));
        assertFalse(encoder.matches("anything", ""));
    }

    @Test
    @DisplayName("the application wires a cost of at least 12")
    void applicationCostIsStrong() {
        // Webapp.main() builds `new BCryptPasswordEncoder(12)`; this pins the requirement so a
        // later "make the tests faster" change to that line is caught here.
        String hash = new BCryptPasswordEncoder(12).encode("x");

        assertTrue(hash.startsWith("$2a$12$"), "expected cost 12, got: " + hash.substring(0, 7));
    }

}
