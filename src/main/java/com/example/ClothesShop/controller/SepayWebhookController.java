package com.example.ClothesShop.controller;

import com.example.ClothesShop.entity.Orders;
import com.example.ClothesShop.enums.OrderStatus;
import com.example.ClothesShop.enums.PaymentStatus;
import com.example.ClothesShop.repository.OrdersRepository;
import com.example.ClothesShop.service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/payments/sepay")
@RequiredArgsConstructor
public class SepayWebhookController {

    private final OrdersRepository orderRepository;
    private final MailService mailService;

    @PostMapping("/webhook")
    public void webhook(@RequestParam String trackingOrder) {
        Orders order = orderRepository.findByTrackingOrder(trackingOrder)
                .orElseThrow();

        if (order.getPaymentStatus() == PaymentStatus.PAID) {
            return;
        }

        order.setPaymentStatus(PaymentStatus.PAID);
        order.setOrderStatus(OrderStatus.CONFIRMED);

        mailService.sendOrderTrackingMail(order.getAccount().getEmail(), order);
    }

}

