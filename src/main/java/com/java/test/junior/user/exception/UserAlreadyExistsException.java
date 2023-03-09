package com.java.test.junior.user.exception;

/**
 * @author artiom.spac
 * @version java-test-junior
 * @apiNote 08.03.2023
 */
public class UserAlreadyExistsException extends RuntimeException {
    private static final String message = "User with username: %s already exists";

    public UserAlreadyExistsException(String username) {
        super(String.format(message, username));
    }
}
