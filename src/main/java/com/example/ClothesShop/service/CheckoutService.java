package com.example.ClothesShop.service;

import com.example.ClothesShop.dto.response.CheckoutReservationView;
import com.example.ClothesShop.entity.Orders;
import com.example.ClothesShop.enums.PaymentMethod;

public interface CheckoutService {
    public void checkout(Long accountId);
    public CheckoutReservationView view(Long accountId);
    public void cancel(Long accountId);
    public Orders confirm(Long accountId, String address, String phoneNumber, PaymentMethod paymentMethod);
}
