package com.example.ClothesShop.service;

import com.example.ClothesShop.dto.response.ProductDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {
    Page<ProductDTO> getAllProducts(Pageable pageable);
}
