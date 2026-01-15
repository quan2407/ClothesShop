package com.example.ClothesShop.repository;

import com.example.ClothesShop.entity.InventoryReservation;
import com.example.ClothesShop.enums.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface InventoryReservationRepository extends JpaRepository<InventoryReservation, Long> {
    List<InventoryReservation> findByStatusAndExpireAtBefore(ReservationStatus status, LocalDateTime now);
    boolean existsByAccount_IdAndSku_IdAndStatus(
            Long accountId,
            Long skuId,
            ReservationStatus status
    );
    List<InventoryReservation> findByAccount_IdAndStatus(
            Long accountId,
            ReservationStatus status
    );

}
