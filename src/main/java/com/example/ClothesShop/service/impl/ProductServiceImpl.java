package com.example.ClothesShop.service.impl;

import com.example.ClothesShop.dto.response.ProductDTO;
import com.example.ClothesShop.entity.Product;
import com.example.ClothesShop.enums.ClothingSize;
import com.example.ClothesShop.exception.NotFoundException;
import com.example.ClothesShop.repository.ProductRepository;
import com.example.ClothesShop.service.ProductService;
import com.example.ClothesShop.specification.product.ProductSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    @Override
    public Page<ProductDTO> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable).map(this::toDTO);
    }

    @Override
    public ProductDTO getProductById(Long id) {
        return productRepository.findById(id).map(this::toDTO)
                .orElseThrow(() ->
                        new NotFoundException("Product not found with id: " + id)
                );
    }

    @Override
    public Page<ProductDTO> filterProducts(
            Long categoryId,
            String keyword,
            Double minPrice,
            Double maxPrice,
            ClothingSize size,
            Pageable pageable
    ) {
        Specification<Product> spec = (root, query, cb) -> cb.conjunction();

        spec = spec.and(ProductSpecification.hasCategory(categoryId));
        spec = spec.and(ProductSpecification.nameContains(keyword));
        spec = spec.and(ProductSpecification.priceBetween(minPrice, maxPrice));
        spec = spec.and(ProductSpecification.hasSize(size));
        spec = spec.and(ProductSpecification.hasAvailableSku());

        Page<Product> productPage = productRepository.findAll(spec, pageable);
        return productPage.map(ProductDTO::toDto);
    }



    private ProductDTO toDTO(Product product) {
        return ProductDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .inStock(product.isInStock())
                .categoryName(product.getCategory().getName())
                .build();
    }
}
