package com.example.ClothesShop.repository;

import com.example.ClothesShop.entity.Orders;
import com.example.ClothesShop.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


public interface OrdersRepository extends JpaRepository<Orders,Long> {
    @EntityGraph(attributePaths = {"items", "items.sku", "items.sku.product"})
    Optional<Orders> findByTrackingOrder(String trackingOrder);

    Page<Orders> findByOrderStatus(
            OrderStatus orderStatus,
            Pageable pageable
    );

    @EntityGraph(attributePaths = {
            "items",
            "items.sku"
    })
    Optional<Orders> findDetailById(Long id);

    List<Orders> findByOrderStatusAndCreatedDateBefore(
            OrderStatus orderStatus,
            Instant time
    );
}
