package com.example.ClothesShop.controller;

import com.example.ClothesShop.dto.request.CartItemRequest;
import com.example.ClothesShop.dto.response.CartResponse;
import com.example.ClothesShop.entity.Account;
import com.example.ClothesShop.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;
    @PostMapping("/add")
    public ResponseEntity<?> addToCart(
            @RequestBody @Valid CartItemRequest request,
            @AuthenticationPrincipal Account account
    ) {
        cartService.addToCart(
                account.getId(),
                request.getSkuId(),
                request.getQuantity()
        );
        return ResponseEntity.ok("Added to cart");
    }
    @PutMapping("/update")
    public ResponseEntity<?> updateQuantity(
            @RequestBody @Valid CartItemRequest request,
            @AuthenticationPrincipal Account account
    ){
        cartService.updateQuantity(
                account.getId(),
                request.getSkuId(),
                request.getQuantity());
        return ResponseEntity.ok("Updated quantity cart");
    }
    @DeleteMapping("/{skuId}")
    public ResponseEntity<?> removeItem(
            @PathVariable Long skuId,
            @AuthenticationPrincipal Account account
    ) {
        cartService.removeFromCart(account.getId(), skuId);
        return ResponseEntity.ok("Removed");
    }
    @GetMapping
    public ResponseEntity<CartResponse> viewCart(@AuthenticationPrincipal Account account){
        return ResponseEntity.ok(cartService.getCart(account.getId()));
    }

}
