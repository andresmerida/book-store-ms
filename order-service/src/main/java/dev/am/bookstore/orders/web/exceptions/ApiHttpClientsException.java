package dev.am.bookstore.orders.web.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatusCode;

@Getter
public class ApiHttpClientsException extends RuntimeException {
    private final HttpStatusCode statusCode;

    public ApiHttpClientsException(HttpStatusCode statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }
}
