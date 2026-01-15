package com.example.ClothesShop.service;

import com.example.ClothesShop.entity.Account;
import com.example.ClothesShop.entity.Orders;

public interface MailService {
    void sendOrderTrackingMail(String to, Orders order);
}
