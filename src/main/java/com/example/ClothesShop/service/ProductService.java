package com.example.ClothesShop.service;

import com.example.ClothesShop.dto.response.ProductDTO;
import com.example.ClothesShop.enums.ClothingSize;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {
    Page<ProductDTO> getAllProducts(Pageable pageable);

    ProductDTO getProductById(Long id);
    public Page<ProductDTO> filterProducts(
            Long categoryId,
            String keyword,
            Double minPrice,
            Double maxPrice,
            ClothingSize size,
            Pageable pageable
    );
}
