package com.example.ClothesShop.controller;

import com.example.ClothesShop.dto.response.SkuResponseDTO;
import com.example.ClothesShop.service.SkuService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/sku")
public class SkuController {

    private final SkuService skuService;

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<SkuResponseDTO>> getSkusByProduct(
            @PathVariable Long productId
    ) {
        return ResponseEntity.ok(
                skuService.getSkusByProductId(productId)
        );
    }
}
