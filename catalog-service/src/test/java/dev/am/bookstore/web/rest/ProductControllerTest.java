package dev.am.bookstore.web.rest;

import static org.junit.jupiter.api.Assertions.*;

import dev.am.bookstore.AbstractIntegrationTest;
import dev.am.bookstore.domain.records.PagedResult;
import dev.am.bookstore.domain.records.ProductDTO;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ProblemDetail;
import org.springframework.test.context.jdbc.Sql;

@Sql("/test-data.sql")
class ProductControllerTest extends AbstractIntegrationTest {

    @Test
    void getProducts() {
        PagedResult<ProductDTO> pagedResult = restTestClient
                .get()
                .uri("/api/products?page=1")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<PagedResult<ProductDTO>>() {})
                .returnResult()
                .getResponseBody();

        assert pagedResult != null;
        assertEquals(10, pagedResult.data().size());
        assertEquals(15, pagedResult.totalElements());
        assertEquals("A Game of Thrones", pagedResult.data().getFirst().name());
    }

    @Test
    void getProductByCode() {
        restTestClient
                .get()
                .uri("/api/products/P111")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(ProductDTO.class)
                .value(productDTO -> {
                    assert productDTO != null;
                    assertEquals("P111", productDTO.code());
                    assertEquals("A Game of Thrones", productDTO.name());
                });
    }

    @Test
    void shouldReturnNotFoundWhenProductNotFound() {
        restTestClient
                .get()
                .uri("/api/products/invalid_product_code")
                .exchange()
                .expectStatus()
                .isNotFound()
                .expectBody(ProblemDetail.class)
                .value(problemDetail -> {
                    assert problemDetail != null;
                    assertEquals("Product not found", problemDetail.getTitle());
                    assertEquals(404, problemDetail.getStatus());
                    assertEquals("Product with code invalid_product_code not found", problemDetail.getDetail());
                });
    }
}
