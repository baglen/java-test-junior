package com.java.test.junior.product.exception;

/**
 * @author artiom.spac
 * @version java-test-junior
 * @apiNote 08.03.2023
 */
public class UserIsNotProductOwnerException extends RuntimeException {
    public UserIsNotProductOwnerException() {
        super("You are not the Product owner");
    }
}
