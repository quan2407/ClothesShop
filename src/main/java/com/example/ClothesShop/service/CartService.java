package com.example.ClothesShop.service;

import com.example.ClothesShop.dto.response.CartResponse;

public interface CartService {
    public void addToCart(Long accountId, Long skuId, int quantity);
    public void updateQuantity(Long accountId, Long skuId, int quantity);
    public void removeFromCart(Long accountId, Long skuId);
    public CartResponse getCart(Long accountId);
}
