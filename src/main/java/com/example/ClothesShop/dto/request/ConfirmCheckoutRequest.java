package com.example.ClothesShop.dto.request;

import com.example.ClothesShop.enums.PaymentMethod;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ConfirmCheckoutRequest {
    private String address;
    private String phoneNumber;
    private PaymentMethod paymentMethod;
}
