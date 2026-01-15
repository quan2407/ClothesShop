package com.example.ClothesShop.dto.response;

import com.example.ClothesShop.entity.OrdersItem;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OrderItemView {

    private String productName;
    private String size;
    private String color;
    private int quantity;
    private double price;

    public static OrderItemView from(OrdersItem item) {
        return OrderItemView.builder()
                .productName(item.getSku().getProduct().getName())
                .size(item.getSku().getClothingSize().name())
                .color(item.getSku().getColor())
                .quantity(item.getQuantity())
                .price(item.getPrice())
                .build();
    }
}

