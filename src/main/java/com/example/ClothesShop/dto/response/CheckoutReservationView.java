package com.example.ClothesShop.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
@Getter
@Setter
@Builder
public class CheckoutReservationView {
    private List<CheckoutReservationItem> items;
    private double totalAmount;
    private LocalDateTime expireAt;
}
