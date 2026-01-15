package com.example.ClothesShop.controller;

import com.example.ClothesShop.dto.response.OrderTrackingResponse;
import com.example.ClothesShop.entity.Orders;
import com.example.ClothesShop.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/order")
@RequiredArgsConstructor
public class OrderTrackingController {
    private final OrderService orderService;
    @GetMapping("/tracking/{trackingCode}")
    public ResponseEntity<OrderTrackingResponse> trackOrder(
            @PathVariable String trackingCode
    ) {
        Orders order = orderService.findByTrackingCode(trackingCode);

        OrderTrackingResponse response =
                OrderTrackingResponse.from(order);

        return ResponseEntity.ok(response);
    }
}
