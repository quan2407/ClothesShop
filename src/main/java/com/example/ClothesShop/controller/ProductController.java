package com.example.ClothesShop.controller;

import com.example.ClothesShop.dto.response.ProductDTO;
import com.example.ClothesShop.entity.Product;
import com.example.ClothesShop.enums.ClothingSize;
import com.example.ClothesShop.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/product")
public class ProductController {
    private final ProductService productService;
    @GetMapping
    public ResponseEntity<Page<ProductDTO>> getProducts(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false, name = "clothingSize") ClothingSize clothingSize,
            Pageable pageable
    ) {
        Page<ProductDTO> page = productService.filterProducts(
                categoryId,
                keyword,
                minPrice,
                maxPrice,
                clothingSize,
                pageable
        );
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    public ProductDTO getProduct(@PathVariable Long id) {
        return productService.getProductById(id);
    }
}
