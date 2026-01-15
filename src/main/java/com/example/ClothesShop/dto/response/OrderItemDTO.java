package com.example.ClothesShop.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderItemDTO {
    private String skuCode;
    private String size;
    private String color;
    private int quantity;
    private double price;
}
