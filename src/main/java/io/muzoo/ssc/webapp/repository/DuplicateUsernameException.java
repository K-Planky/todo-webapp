package io.muzoo.ssc.webapp.repository;

public class DuplicateUsernameException extends RuntimeException {

    public DuplicateUsernameException(String username) {
        super("Username already taken: " + username);
    }

}
