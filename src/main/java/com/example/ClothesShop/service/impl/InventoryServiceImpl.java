package com.example.ClothesShop.service.impl;

import com.example.ClothesShop.entity.redis.checkout.CheckoutReservation;
import com.example.ClothesShop.entity.redis.checkout.CheckoutReservationItem;
import com.example.ClothesShop.repository.redis.CheckoutReservationRepository;
import com.example.ClothesShop.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {
    private final StringRedisTemplate stringRedisTemplate;
    private final CheckoutReservationRepository checkoutReservationRepository;

    public void releaseStock(String reservationId){
        CheckoutReservation reservation = checkoutReservationRepository.findById(reservationId).orElse(null);
        if(reservation == null){
            return;
        }
        for (CheckoutReservationItem item : reservation.getItems()){
            String stockKey = "stock:sku:" + item.getSkuId();
            stringRedisTemplate.opsForValue().increment(stockKey,item.getQuantity());
        }
        checkoutReservationRepository.delete(reservation);

    }
}
