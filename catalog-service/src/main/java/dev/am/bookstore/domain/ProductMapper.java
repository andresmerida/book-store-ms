package dev.am.bookstore.domain;

import dev.am.bookstore.domain.records.ProductDTO;

class ProductMapper {
    private ProductMapper() {}

    static ProductDTO toDTO(ProductEntity product) {
        return new ProductDTO(
                product.getId(),
                product.getCode(),
                product.getName(),
                product.getDescription(),
                product.getImageUrl(),
                product.getPrice());
    }
}
