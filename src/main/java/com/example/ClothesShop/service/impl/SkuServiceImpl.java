package com.example.ClothesShop.service.impl;

import com.example.ClothesShop.dto.response.SkuResponseDTO;
import com.example.ClothesShop.entity.SKU;
import com.example.ClothesShop.repository.SKURepository;
import com.example.ClothesShop.service.SkuService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SkuServiceImpl implements SkuService {

    private final SKURepository skuRepository;

    @Override
    public List<SkuResponseDTO> getSkusByProductId(Long productId) {

        List<SKU> skus = skuRepository.findByProduct_Id(productId);

        return skus.stream()
                .map(sku -> SkuResponseDTO.builder()
                        .id(sku.getId())
                        .skuCode(sku.getSkuCode())
                        .price(sku.getPrice())
                        .clothingSize(sku.getClothingSize())
                        .color(sku.getColor())
                        .quantity(sku.getQuantity())
                        .status(sku.getStatus())
                        .build()
                )
                .toList();
    }
}
