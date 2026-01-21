package com.example.ClothesShop.controller;

import com.example.ClothesShop.dto.request.ConfirmCheckoutRequest;
import com.example.ClothesShop.dto.response.CheckoutReservationView;
import com.example.ClothesShop.dto.response.ConfirmCheckoutResponse;
import com.example.ClothesShop.entity.Account;
import com.example.ClothesShop.entity.Orders;
import com.example.ClothesShop.service.CheckoutService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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
    @PostMapping("/cancel")
    public void cancelCheckout(@AuthenticationPrincipal Account account) {
        checkoutService.cancel(account.getId());
    }
    @PostMapping("/confirm")
    public ResponseEntity<ConfirmCheckoutResponse> confirmCheckout(
            @AuthenticationPrincipal Account account,
            @RequestBody ConfirmCheckoutRequest req
    ) {
        Orders order = checkoutService.confirm(
                account.getId(),
                req.getAddress(),
                req.getPhoneNumber(),
                req.getPaymentMethod()
        );

        ConfirmCheckoutResponse response =
                new ConfirmCheckoutResponse(
                        "Checkout confirmed successfully",
                        order.getId(),
                        order.getTrackingOrder()
                );

        return ResponseEntity.ok(response);
    }


}
