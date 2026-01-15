package com.example.ClothesShop.service;

public interface InventoryReservationService {
    public void reserve(Long accountId, Long skuId, int quantity);
    public void releaseExpired();
}
