package com.example.ClothesShop.controller;

import com.example.ClothesShop.dto.response.ProductDTO;
import com.example.ClothesShop.entity.Product;
import com.example.ClothesShop.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/product")
public class ProductController {
    private final ProductService productService;
    @GetMapping
    public ResponseEntity<Page<ProductDTO>> getAllProducts(Pageable pageable) {
        Page<ProductDTO> page = productService.getAllProducts(pageable);
        return ResponseEntity.ok(page);
    }
}
