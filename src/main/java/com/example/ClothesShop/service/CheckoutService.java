package com.example.ClothesShop.service;

import com.example.ClothesShop.dto.response.CheckoutReservationView;
import com.example.ClothesShop.entity.Account;

public interface CheckoutService {
    public void checkout(Long accountId);
    public CheckoutReservationView view(Long accountId);
    public void cancel(Long accountId);
}
