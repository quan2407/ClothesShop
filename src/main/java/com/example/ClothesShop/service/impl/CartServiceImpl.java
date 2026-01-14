package com.example.ClothesShop.service.impl;

import com.example.ClothesShop.dto.response.CartItemResponse;
import com.example.ClothesShop.dto.response.CartResponse;
import com.example.ClothesShop.entity.SKU;
import com.example.ClothesShop.entity.redis.cart.Cart;
import com.example.ClothesShop.entity.redis.cart.CartItem;
import com.example.ClothesShop.enums.SkuStatus;
import com.example.ClothesShop.exception.InvalidRequestException;
import com.example.ClothesShop.exception.NotFoundException;
import com.example.ClothesShop.repository.redis.CartRepository;
import com.example.ClothesShop.repository.SKURepository;
import com.example.ClothesShop.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private static final long CART_TTL = 7 * 24 * 60 * 60; // 7 days
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
                        .timeToLive(CART_TTL)
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
                            .price(sku.getPrice())
                            .quantity(quantity)
                            .build()
            );
        }
        cartRepository.save(cart);
    }

    @Override
    public void updateQuantity(Long accountId, Long skuId, int quantity) {
        Cart cart = cartRepository.findById(accountId).orElseThrow(() -> new NotFoundException("CART NOT FOUND"));
        SKU sku = skuRepository.findById(skuId).orElseThrow(() -> new NotFoundException("SKU NOT FOUND"));
        if (sku.getQuantity() < quantity){
            throw new InvalidRequestException("Not enough stock");
        }
        boolean found = false;

        for (CartItem item : cart.getItems()) {
            if (item.getSkuId().equals(skuId)) {
                item.setQuantity(quantity);
                found = true;
                break;
            }
        }

        if (!found) {
            throw new InvalidRequestException("Item not found in cart");
        }

        cart.setTimeToLive(CART_TTL);
        cartRepository.save(cart);

    }

    @Override
    public void removeFromCart(Long accountId, Long skuId) {
        Cart cart = cartRepository.findById(accountId).orElseThrow(() -> new NotFoundException("CART NOT FOUND"));
        boolean removed = cart.getItems().removeIf(item -> item.getSkuId().equals(skuId));
        if (!removed){
            throw new InvalidRequestException("Item not found in cart");
        }
        cart.setTimeToLive(CART_TTL);
        cartRepository.save(cart);
    }

    @Override
    public CartResponse getCart(Long accountId) {
        Cart cart = cartRepository.findById(accountId).orElse(
                Cart.builder()
                        .accountId(accountId)
                        .items(List.of())
                        .build()
        );
        List<CartItemResponse> cartItems = cart.getItems()
                .stream()
                .map(item -> CartItemResponse.builder()
                        .skuId(item.getSkuId())
                        .skuCode(item.getSkuCode())
                        .productId(item.getProductId())
                        .productName(item.getProductName())
                        .color(item.getColor())
                        .size(String.valueOf(item.getSize()))
                        .price(item.getPrice())
                        .quantity(item.getQuantity())
                        .totalPrice(item.getPrice() * item.getQuantity())
                        .build())
                .toList();
        int totalQuantity = cartItems.stream().mapToInt(CartItemResponse::getQuantity).sum();
        double totalAmount = cartItems.stream()
                .mapToDouble(CartItemResponse::getTotalPrice)
                .sum();
        cart.setTimeToLive(CART_TTL);
        return CartResponse.builder()
                .items(cartItems)
                .totalQuantity(totalQuantity)
                .totalAmount(totalAmount)
                .build();
    }
}
