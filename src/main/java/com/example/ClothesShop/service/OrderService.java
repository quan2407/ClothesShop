package com.example.ClothesShop.service;

import com.example.ClothesShop.entity.Account;
import com.example.ClothesShop.entity.InventoryReservation;
import com.example.ClothesShop.entity.Orders;

import java.util.List;

public interface OrderService {
    public Orders createOrder(Account account, List<InventoryReservation> inventoryReservations,String address,String phoneNumber);

    Orders findByTrackingCode(String trackingCode);
}
