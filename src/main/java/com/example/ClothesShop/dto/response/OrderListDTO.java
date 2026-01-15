package com.example.ClothesShop.dto.response;

import com.example.ClothesShop.enums.OrderStatus;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class OrderListDTO {
    private Long id;
    private String trackingOrder;
    private String customerEmail;
    private OrderStatus orderStatus;
    private double totalPrice;
    private Instant createdDate;
}
