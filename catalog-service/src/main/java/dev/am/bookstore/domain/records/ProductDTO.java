package dev.am.bookstore.domain.records;

import java.math.BigDecimal;

public record ProductDTO(Long id, String code, String name, String description, String imageUrl, BigDecimal price) {}
