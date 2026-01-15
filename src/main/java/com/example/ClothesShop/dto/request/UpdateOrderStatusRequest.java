package com.example.ClothesShop.dto.request;

import com.example.ClothesShop.enums.OrderStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateOrderStatusRequest {
    private OrderStatus newStatus;
    private String reason;
}

