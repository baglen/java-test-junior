package com.java.test.junior.product.exception;

/**
 * @author artiom.spac
 * @version java-test-junior
 * @apiNote 08.03.2023
 */
public class ProductNotFoundException extends RuntimeException {
    private static final String message = "Product with id: %d not found";

    public ProductNotFoundException(Long productId) {
        super(String.format(message, productId));
    }
}
