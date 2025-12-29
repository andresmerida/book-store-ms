package dev.am.bookstore;

import org.springframework.boot.SpringApplication;

public class TestCatalogServiceApplication {

    static void main(String[] args) {
        SpringApplication.from(CatalogServiceApplication::main)
                .with(TestcontainersConfiguration.class)
                .run(args);
    }
}
