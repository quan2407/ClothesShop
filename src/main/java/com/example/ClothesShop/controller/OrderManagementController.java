package com.example.ClothesShop.controller;

import com.example.ClothesShop.dto.request.UpdateOrderStatusRequest;
import com.example.ClothesShop.dto.response.OrderDetailDTO;
import com.example.ClothesShop.dto.response.OrderListDTO;
import com.example.ClothesShop.entity.Orders;
import com.example.ClothesShop.enums.OrderStatus;
import com.example.ClothesShop.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/staff/orders")
@RequiredArgsConstructor
public class OrderManagementController {
    private final OrderService orderService;
    @GetMapping
    public ResponseEntity<Page<OrderListDTO>> getOrders(
            @RequestParam(required = false) OrderStatus status,
            @PageableDefault(
                    size = 20,
                    sort = "createdDate",
                    direction = Sort.Direction.DESC
            ) Pageable pageable
    ) {
        return ResponseEntity.ok(
                orderService.getOrders(status, pageable)
        );
    }
    @GetMapping("/{orderId}")
    @PreAuthorize("hasAnyRole('STAFF','ADMIN')")
    public ResponseEntity<OrderDetailDTO> getOrderDetail(
            @PathVariable Long orderId
    ) {
        return ResponseEntity.ok(
                orderService.getOrderDetail(orderId)
        );
    }

    @PatchMapping("/{orderId}/status")
    public ResponseEntity<?> updateOrderStatus(
            @PathVariable Long orderId,
            @RequestBody UpdateOrderStatusRequest request
    ) {
        Orders updated = orderService.updateOrderStatus(
                orderId,
                request.getNewStatus(),
                request.getReason()
        );

        return ResponseEntity.ok(
                Map.of(
                        "message", "Order status updated successfully",
                        "orderId", updated.getId(),
                        "trackingOrder", updated.getTrackingOrder(),
                        "newStatus", updated.getOrderStatus(),
                        "cancelReason", updated.getCancelReason()
                )
        );
    }

}
