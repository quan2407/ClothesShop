package com.example.ClothesShop.service.impl;

import com.example.ClothesShop.entity.Account;
import com.example.ClothesShop.entity.InventoryReservation;
import com.example.ClothesShop.entity.SKU;
import com.example.ClothesShop.enums.ReservationStatus;
import com.example.ClothesShop.exception.NotFoundException;
import com.example.ClothesShop.exception.OutOfStockException;
import com.example.ClothesShop.repository.AccountRepository;
import com.example.ClothesShop.repository.InventoryReservationRepository;
import com.example.ClothesShop.repository.SKURepository;
import com.example.ClothesShop.service.InventoryReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class InventoryReservationServiceImpl implements InventoryReservationService {
    private final InventoryReservationRepository inventoryReservationRepository;
    private final SKURepository skuRepository;
    private final AccountRepository accountRepository;
    @Override
    @Transactional
    public void reserve(Long accountId, Long skuId, int quantity) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new NotFoundException("Account not found"));
        SKU sku = skuRepository.findByIdForUpdate(skuId).orElseThrow(() -> new NotFoundException("SKU NOT FOUND"));
        if (sku.getQuantity() < quantity) {
            throw new OutOfStockException("SKU stock is not enough");
        }
        sku.setQuantity(sku.getQuantity() - quantity);
        InventoryReservation inventoryReservation = InventoryReservation.builder()
                .sku(sku)
                .quantity(quantity)
                .account(account)
                .status(ReservationStatus.HOLD)
                .expireAt(LocalDateTime.now().plusMinutes(15))
                .build();
        inventoryReservationRepository.save(inventoryReservation);
    }

    @Override
    @Transactional
    public void releaseExpired() {
        var expiredReservations = inventoryReservationRepository.findByStatusAndExpireAtBefore(ReservationStatus.HOLD, LocalDateTime.now());
        for (InventoryReservation inventoryReservation : expiredReservations) {
            SKU sku = skuRepository.findByIdForUpdate(inventoryReservation.getSku().getId()).orElseThrow(() -> new NotFoundException("SKU NOT FOUND"));
            sku.setQuantity(sku.getQuantity() + inventoryReservation.getQuantity());
            inventoryReservation.setStatus(ReservationStatus.EXPIRED);
        }
    }
}
