package dev.am.bookstore.orders.web.exceptions;

import java.net.URI;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
@Slf4j
class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    private static final URI NOT_FOUND_TYPE = URI.create("https://api.bookstore.com/errors/not-found");
    private static final URI ISE_FOUND_TYPE = URI.create("https://api.bookstore.com/errors/server-error");
    private static final URI HTTP_CLIENT_TYPE = URI.create("https://api.bookstore.com/errors/http-client-error");
    private static final URI BAD_REQUEST_TYPE = URI.create("https://api.bookstore.com/errors/bad-request");
    private static final String SERVICE_NAME = "order-service";

    @ExceptionHandler(InvalidOrderException.class)
    ProblemDetail handleInvalidOrderException(InvalidOrderException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());
        problemDetail.setTitle("Invalid Order Creation Request");
        problemDetail.setType(BAD_REQUEST_TYPE);
        problemDetail.setProperty("service", SERVICE_NAME);
        problemDetail.setProperty("error_category", HttpStatus.BAD_REQUEST.getReasonPhrase());
        problemDetail.setProperty("timestamp", Instant.now().toString());
        return problemDetail;
    }

    @ExceptionHandler(ProductNotFoundException.class)
    ProblemDetail handleProductNotFoundException(ProductNotFoundException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        problemDetail.setTitle("From catalog-service: Product Not Found Exception");
        problemDetail.setType(NOT_FOUND_TYPE);
        problemDetail.setProperty("service", SERVICE_NAME);
        problemDetail.setProperty("error_category", HttpStatus.NOT_FOUND.getReasonPhrase());
        problemDetail.setProperty("timestamp", Instant.now().toString());

        log.error("OrderNotFoundException -> Product not found: {}", ex.getMessage());
        return problemDetail;
    }

    @ExceptionHandler(ApiHttpClientsException.class)
    ProblemDetail handleApiHttpClientsException(ApiHttpClientsException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(ex.getStatusCode(), ex.getMessage());
        problemDetail.setTitle("API Client Exception:");
        problemDetail.setType(HTTP_CLIENT_TYPE);
        problemDetail.setProperty("service", SERVICE_NAME);
        problemDetail.setProperty("error_category", "client generic error");
        problemDetail.setProperty("timestamp", Instant.now().toString());

        log.error("ApiHttpClientsException -> {}", ex.getMessage());
        return problemDetail;
    }

    @ExceptionHandler(JsonMappingObjException.class)
    ProblemDetail handleJsonMappingObjException(JsonMappingObjException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.UNPROCESSABLE_CONTENT, ex.getMessage());
        problemDetail.setTitle("Json Mapping Exception:");
        problemDetail.setType(ISE_FOUND_TYPE);
        problemDetail.setProperty("service", SERVICE_NAME);
        problemDetail.setProperty("error_category", HttpStatus.UNPROCESSABLE_CONTENT.getReasonPhrase());
        problemDetail.setProperty("timestamp", Instant.now().toString());
        log.error("JsonMappingObjException -> {}", ex.getMessage(), ex.getCause());

        return problemDetail;
    }

    @ExceptionHandler(OrderNotFoundException.class)
    ProblemDetail handleOrderNotFoundException(OrderNotFoundException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        problemDetail.setTitle("Order Not Found Exception");
        problemDetail.setType(NOT_FOUND_TYPE);
        problemDetail.setProperty("service", SERVICE_NAME);
        problemDetail.setProperty("error_category", HttpStatus.NOT_FOUND.getReasonPhrase());
        problemDetail.setProperty("timestamp", Instant.now().toString());
        return problemDetail;
    }

    @Override
    protected @Nullable ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatusCode status,
            @NonNull WebRequest request) {
        List<String> errors = new ArrayList<>();
        ex.getBindingResult().getAllErrors().forEach(error -> errors.add(error.getDefaultMessage()));
        ProblemDetail problemDetail =
                ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Invalid request payload");
        problemDetail.setTitle("Bad Request");
        problemDetail.setType(BAD_REQUEST_TYPE);
        problemDetail.setProperty("errors", errors);
        problemDetail.setProperty("service", SERVICE_NAME);
        problemDetail.setProperty("error_category", HttpStatus.BAD_REQUEST.getReasonPhrase());
        problemDetail.setProperty("timestamp", Instant.now().toString());

        return ResponseEntity.badRequest().body(problemDetail);
    }

    @ExceptionHandler(Exception.class)
    ProblemDetail handleUnhandledException(Exception e) {
        ProblemDetail problemDetail =
                ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        problemDetail.setTitle("Internal Server Error");
        problemDetail.setType(ISE_FOUND_TYPE);
        problemDetail.setProperty("service", SERVICE_NAME);
        problemDetail.setProperty("error_category", "INTERNAL_SERVER_ERROR");
        problemDetail.setProperty("timestamp", Instant.now().toString());
        log.error("Unhandled exception: {0}", e.fillInStackTrace());
        return problemDetail;
    }
}
