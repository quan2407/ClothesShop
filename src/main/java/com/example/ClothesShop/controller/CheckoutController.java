package com.example.ClothesShop.controller;

import com.example.ClothesShop.entity.Account;
import com.example.ClothesShop.entity.redis.cart.Cart;
import com.example.ClothesShop.service.CheckoutService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/checkout")
@RequiredArgsConstructor
public class CheckoutController {
    private final CheckoutService checkoutService;
    @PostMapping
    public ResponseEntity<?> checkoutFromCart(@AuthenticationPrincipal Account account){
        String reservationId = checkoutService.checkoutFromCart(account.getId());
        return ResponseEntity.ok().body(reservationId);
    }
}
