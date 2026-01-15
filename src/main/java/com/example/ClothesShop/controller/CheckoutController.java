package com.example.ClothesShop.controller;

import com.example.ClothesShop.dto.response.CheckoutReservationView;
import com.example.ClothesShop.entity.Account;
import com.example.ClothesShop.entity.redis.cart.Cart;
import com.example.ClothesShop.service.CheckoutService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/checkout")
@RequiredArgsConstructor
public class CheckoutController {
    private final CheckoutService checkoutService;
    @PostMapping
    public ResponseEntity<?> checkoutFromCart(@AuthenticationPrincipal Account account){
        checkoutService.checkout(account.getId());
        return ResponseEntity.ok().build();
    }
    @GetMapping
    public CheckoutReservationView viewCheckout(
            @AuthenticationPrincipal Account account
    ) {
        return checkoutService.view(account.getId());
    }
}
