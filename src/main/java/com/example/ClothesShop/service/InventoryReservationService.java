package com.example.ClothesShop.service;

import com.example.ClothesShop.entity.InventoryReservation;

import java.util.List;

public interface InventoryReservationService {
    public void reserve(Long accountId, Long skuId, int quantity);
    public void releaseExpired();
    public void cancelAllHoldByAccount(Long accountId);
    List<InventoryReservation> confirmAllByAccount(Long accountId);
}
