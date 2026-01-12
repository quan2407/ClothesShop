package com.example.ClothesShop.service.impl;

import com.example.ClothesShop.entity.SKU;
import com.example.ClothesShop.entity.redis.cart.Cart;
import com.example.ClothesShop.entity.redis.cart.CartItem;
import com.example.ClothesShop.enums.SkuStatus;
import com.example.ClothesShop.exception.InvalidRequestException;
import com.example.ClothesShop.exception.NotFoundException;
import com.example.ClothesShop.repository.CartRepository;
import com.example.ClothesShop.repository.SKURepository;
import com.example.ClothesShop.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final SKURepository skuRepository;
    @Override
    public void addToCart(Long accountId, Long skuId, int quantity) {
        SKU sku = skuRepository.findById(skuId).orElseThrow(() -> new NotFoundException("SKU NOT FOUND"));
        if (sku.getStatus() != SkuStatus.ACTIVE){
            throw new InvalidRequestException("Product is not available");
        }
        if (sku.getQuantity() < quantity){
            throw new InvalidRequestException("Not enough stock");
        }
        // create new cart if not exist cart
        Cart cart = cartRepository.findById(accountId).orElse(
                Cart.builder()
                        .accountId(accountId)
                        .items(new ArrayList<>())
                        .build()
        );
        // check sku exist in a cart
        Optional<CartItem> existingItem = cart.getItems()
                .stream()
                .filter(item -> item.getSkuId().equals(sku.getId()))
                .findFirst();
        if (existingItem.isPresent()){
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + quantity);
        } else {
            cart.getItems().add(
                    CartItem.builder()
                            .skuId(sku.getId())
                            .skuCode(sku.getSkuCode())
                            .productId(sku.getProduct().getId())
                            .productName(sku.getProduct().getName())
                            .color(sku.getColor())
                            .quantity(quantity)
                            .build()
            );
        }
        cartRepository.save(cart);
    }
}
