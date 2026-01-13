package dev.am.bookstore.orders.web.exceptions;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(String message) {
        super(message);
    }

    public static ProductNotFoundException of(String code) {
        return new ProductNotFoundException("From catalog-service: Product with code " + code + " not found.");
    }
}
