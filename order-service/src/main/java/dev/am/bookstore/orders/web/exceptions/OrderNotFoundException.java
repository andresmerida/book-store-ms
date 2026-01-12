package dev.am.bookstore.orders.web.exceptions;

public class OrderNotFoundException extends RuntimeException {
    public OrderNotFoundException(String message) {
        super(message);
    }

    public static OrderNotFoundException of(String orderId) {
        return new OrderNotFoundException("Order with id " + orderId + " not found");
    }
}
