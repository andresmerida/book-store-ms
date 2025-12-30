package dev.am.bookstore.web.exceptions;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(String message) {
        super(message);
    }

    public static ProductNotFoundException of(String code) {
        return new ProductNotFoundException("Product with code " + code + " not found");
    }
}
