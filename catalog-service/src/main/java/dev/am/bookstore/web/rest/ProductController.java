package dev.am.bookstore.web.rest;

import dev.am.bookstore.domain.ProductService;
import dev.am.bookstore.domain.records.PagedResult;
import dev.am.bookstore.domain.records.ProductDTO;
import dev.am.bookstore.web.exceptions.ProductNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
@Slf4j
@RequiredArgsConstructor
class ProductController {
    private final ProductService productService;

    @RequestMapping()
    PagedResult<ProductDTO> getProducts(@RequestParam(name = "page", defaultValue = "1") int pageNo) {
        log.info("Getting products page {}", pageNo);
        return productService.getProducts(pageNo);
    }

    @RequestMapping("/{code}")
    ResponseEntity<ProductDTO> getProductByCode(@PathVariable String code) {
        log.info("Getting product by code {}", code);
        return productService
                .getProductByCode(code)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> ProductNotFoundException.of(code));
    }
}
