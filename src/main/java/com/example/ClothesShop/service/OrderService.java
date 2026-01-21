package com.example.ClothesShop.service;

import com.example.ClothesShop.dto.response.OrderDetailDTO;
import com.example.ClothesShop.dto.response.OrderListDTO;
import com.example.ClothesShop.entity.Account;
import com.example.ClothesShop.entity.InventoryReservation;
import com.example.ClothesShop.entity.Orders;
import com.example.ClothesShop.enums.OrderStatus;
import com.example.ClothesShop.enums.PaymentMethod;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderService {
    public Orders createOrder(Account account, List<InventoryReservation> inventoryReservations, String address, String phoneNumber, PaymentMethod paymentMethod);

    Orders findByTrackingCode(String trackingCode);

    public Page<OrderListDTO> getOrders(
            OrderStatus status,
            Pageable pageable
    );
    public Orders updateOrderStatus(
            Long orderId,
            OrderStatus newStatus,
            String reason
    );
    OrderDetailDTO getOrderDetail(Long orderId);

}
