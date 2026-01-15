package com.example.ClothesShop.scheduler;

import com.example.ClothesShop.service.InventoryReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReservationExpireScheduler {
    private final InventoryReservationService inventoryReservationService;
    @Scheduled(fixedRate = 60_000)
    public void scheduledReservationExpire() {
        inventoryReservationService.releaseExpired();
    }
}
