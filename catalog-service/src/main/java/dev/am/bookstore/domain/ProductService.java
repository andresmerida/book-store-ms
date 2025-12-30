package dev.am.bookstore.domain;

import dev.am.bookstore.domain.records.AppProperties;
import dev.am.bookstore.domain.records.PagedResult;
import dev.am.bookstore.domain.records.ProductDTO;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final AppProperties appProperties;

    @Transactional(readOnly = true)
    public PagedResult<ProductDTO> getProducts(int pageNo) {
        Sort sort = Sort.by(Sort.Direction.ASC, "name");
        pageNo = pageNo <= 1 ? 0 : pageNo - 1;
        Pageable pageable = PageRequest.of(pageNo, appProperties.pageSize(), sort);
        Page<ProductDTO> products = productRepository.findAll(pageable).map(ProductMapper::toDTO);

        return new PagedResult<>(
                products.getContent(),
                products.getTotalElements(),
                pageNo + 1,
                products.getTotalPages(),
                products.isFirst(),
                products.isLast(),
                products.hasNext(),
                products.hasPrevious());
    }

    @Transactional(readOnly = true)
    public Optional<ProductDTO> getProductByCode(String code) {
        return productRepository.findByCode(code).map(ProductMapper::toDTO);
    }
}
