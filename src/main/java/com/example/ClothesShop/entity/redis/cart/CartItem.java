package com.example.ClothesShop.entity.redis.cart;

import com.example.ClothesShop.enums.ClothingSize;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItem {
    private Long skuId;
    private String skuCode;

    private Long productId;
    private String productName;

    private String color;
    private ClothingSize size;

    private double price;
    private int quantity;
}
