package com.example.ClothesShop.dto.response;

import com.example.ClothesShop.enums.ClothingSize;
import com.example.ClothesShop.enums.SkuStatus;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SkuResponseDTO {
    private Long id;
    private String skuCode;
    private double price;
    private ClothingSize clothingSize;
    private String color;
    private int quantity;
    private SkuStatus status;
}
