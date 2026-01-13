package dev.am.bookstore.orders.domain.clients;

import dev.am.bookstore.orders.dto.ProductDTO;
import dev.am.bookstore.orders.web.exceptions.ApiHttpClientsException;
import dev.am.bookstore.orders.web.exceptions.InvalidOrderException;
import dev.am.bookstore.orders.web.exceptions.ProductNotFoundException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange("/api/products")
public interface ProductClient {

    @GetExchange("/{code}")
    ProductDTO getProductByCode(@PathVariable String code);

    default ProductDTO getProductByCodeWithErrorHandling(String code) {
        try {
            return getProductByCode(code);
        } catch (Exception e) {
            var rootCause = ExceptionUtils.getRootCause(e);
            if (rootCause instanceof HttpClientErrorException.NotFound) {
                throw ProductNotFoundException.of(code);
            } else if (rootCause instanceof HttpClientErrorException) {
                throw new ApiHttpClientsException(
                        ((HttpClientErrorException) rootCause).getStatusCode(), e.getMessage());
            } else {
                throw new InvalidOrderException("Error getting product by code: " + code + ".");
            }
        }
    }
}
