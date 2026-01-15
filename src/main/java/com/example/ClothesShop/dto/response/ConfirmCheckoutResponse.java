package com.example.ClothesShop.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ConfirmCheckoutResponse {
    private String message;
    private Long orderId;
    private String trackingOrder;
}
