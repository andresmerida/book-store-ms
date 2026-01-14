package dev.am.bookstore.orders.domain.clients;

import dev.am.bookstore.orders.dto.ProductDTO;
import dev.am.bookstore.orders.web.exceptions.ApiHttpClientsException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange("/api/products")
public interface ProductClient {
    Logger log = LoggerFactory.getLogger(ProductClient.class);

    @CircuitBreaker(name = "catalog-service")
    @GetExchange("/{code}")
    @Retry(name = "catalog-service", fallbackMethod = "getProductByCodeFallBack")
    ProductDTO getProductByCode(@PathVariable String code);

    /**
     * Fallback Pattern
     * The fallback pattern provides an alternative response or service when a primary service fails or is unavailable.
     * This ensures that the system can continue operating, even if the quality of the response is degraded.
     * In this case just I am going to throw an exception.
     *
     * @param code of product
     * @param t error
     */
    default ProductDTO getProductByCodeFallBack(String code, Throwable t) {
        log.error("ProductClient.getProductByCodeFallBack: Error getting product by code: {}", code, t);
        throw new ApiHttpClientsException(HttpStatus.GATEWAY_TIMEOUT, "Error getting product by code: " + code + ".");
    }
}
