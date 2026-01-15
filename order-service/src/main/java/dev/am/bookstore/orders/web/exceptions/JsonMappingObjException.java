package dev.am.bookstore.orders.web.exceptions;

public class JsonMappingObjException extends RuntimeException {
    public JsonMappingObjException(String message, Throwable cause) {
        super(message, cause);
    }
}
