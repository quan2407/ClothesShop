package com.example.ClothesShop.service.impl;

import com.example.ClothesShop.repository.SKURepository;
import com.example.ClothesShop.service.InventoryBootstrapService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InventoryBootstrapServiceImpl implements InventoryBootstrapService {
    private final StringRedisTemplate redisTemplate;
    private final SKURepository skuRepository;
    @Override
    @PostConstruct
    public void initStockIfAbsent() {
        skuRepository.findAll().forEach(sku -> {
            String stockKey = "stock:sku:" + sku.getId();
            redisTemplate.opsForValue()
                    .setIfAbsent(stockKey, String.valueOf(sku.getQuantity()));
        });
    }
}
