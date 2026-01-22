package com.example.ClothesShop.scheduler;

import com.example.ClothesShop.entity.Orders;
import com.example.ClothesShop.enums.OrderStatus;
import com.example.ClothesShop.enums.PaymentStatus;
import com.example.ClothesShop.repository.OrdersRepository;
import com.example.ClothesShop.service.InventoryReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class OrderReservationExpireScheduler {
    private final OrdersRepository ordersRepository;
    private final InventoryReservationService inventoryReservationService;
    @Scheduled(fixedDelay = 60000)
    @Transactional
    public void cancelExpiredPayments() {
        Instant threshold = Instant.now().minus(Duration.ofMinutes(15));

        List<Orders> orders =
                ordersRepository.findByOrderStatusAndCreatedDateBefore(
                        OrderStatus.WAITING_FOR_PAYMENT,
                        threshold
                );

        for (Orders order : orders) {
            order.setOrderStatus(OrderStatus.CANCELLED);
            order.setPaymentStatus(PaymentStatus.FAILED);

            inventoryReservationService.releaseStock(order);
        }
    }

}
