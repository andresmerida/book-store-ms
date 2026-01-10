package dev.am.bookstore.domain;

import static org.junit.jupiter.api.Assertions.*;

import dev.am.bookstore.DatabaseTestcontainersConfig;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // avoid embedded in-memory test db
@Import(DatabaseTestcontainersConfig.class)
class ProductRepositoryTest {

    @Autowired
    ProductRepository productRepository;

    // You do not need to test the methods provided by JPA Repository

    @Test
    void shouldGetAllProducts() {
        assertEquals(15, productRepository.findAll().size());
    }

    @Test
    void shouldGetProductByCode() {
        ProductEntity productEntity = productRepository.findByCode("P100").orElseThrow();

        assertEquals("P100", productEntity.getCode());
        assertEquals("The Hunger Games", productEntity.getName());
        assertEquals(new BigDecimal("34.0"), productEntity.getPrice());
    }

    @Test
    void shouldReturnEmptyWhenProductNotFound() {
        assertFalse(productRepository.findByCode("invalid_product_code").isPresent());
    }
}
