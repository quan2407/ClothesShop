package com.example.ClothesShop.dto.response;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartItemResponse {
    private Long skuId;
    private String skuCode;
    private Long productId;
    private String productName;
    private String color;
    private String size;
    private double price;
    private int quantity;
    private double totalPrice;
}
