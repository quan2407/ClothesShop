package com.example.ClothesShop.service;

public interface CartService {
    public void addToCart(Long accountId, Long skuId, int quantity);
}
