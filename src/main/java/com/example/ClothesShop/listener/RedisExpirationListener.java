package com.example.ClothesShop.listener;

import com.example.ClothesShop.service.InventoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
@Slf4j
public class RedisExpirationListener extends KeyExpirationEventMessageListener {
    private final InventoryService inventoryService;
    public RedisExpirationListener(RedisMessageListenerContainer listenerContainer
    , InventoryService inventoryService) {
        super(listenerContainer);
        this.inventoryService = inventoryService;
    }
    @Override
    public void onMessage(Message message, byte[] pattern) {

        String expiredKey =
                new String(message.getBody(), StandardCharsets.UTF_8);
        log.info("TTL expired key = {}", expiredKey);


        // Lắng nghe Shadow Key hết hạn
        if (expiredKey.startsWith("checkout_reservation_shadow:")) {
            String reservationId = expiredKey.replace("checkout_reservation_shadow:", "");

            // Lúc này, Data thực trong "checkout_reservation_data" vẫn còn!
            inventoryService.releaseStock(reservationId);
        }
    }
}
