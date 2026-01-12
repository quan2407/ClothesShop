package com.example.ClothesShop.service.impl;

import com.example.ClothesShop.dto.response.ProductDTO;
import com.example.ClothesShop.entity.Product;
import com.example.ClothesShop.exception.NotFoundException;
import com.example.ClothesShop.repository.ProductRepository;
import com.example.ClothesShop.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
