package dev.am.bookstore.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;

@Entity
@Table(name = "products")
@AllArgsConstructor
@Data
class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_id_generator")
    @SequenceGenerator(name = "product_id_generator", sequenceName = "product_id_seq", allocationSize = 1)
    private Long id;

    @Column(unique = true, nullable = false)
    @NotBlank(message = "Product code is required") private String code;

    @Column(nullable = false)
    @NotBlank(message = "Product name is required") private String name;

    private String description;

    @Column(name = "image_url")
    private String imageUrl;

    @NotNull(message = "Product price is required") @DecimalMin(value = "0.1", message = "Product price must be greater than 0.1") private BigDecimal price;

    public ProductEntity() {}
}
