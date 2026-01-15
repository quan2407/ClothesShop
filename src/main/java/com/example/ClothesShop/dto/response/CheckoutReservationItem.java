package com.example.ClothesShop.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class CheckoutReservationItem {
    private Long reservationId;
    private Long skuId;
    private String productName;
    private String size;
    private String color;
    private double price;
    private int quantity;
    private double itemTotal;
    private LocalDateTime expireAt;
}
