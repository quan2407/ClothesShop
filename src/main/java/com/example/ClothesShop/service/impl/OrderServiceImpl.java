package com.example.ClothesShop.service.impl;

import com.example.ClothesShop.dto.response.OrderDetailDTO;
import com.example.ClothesShop.dto.response.OrderItemDTO;
import com.example.ClothesShop.dto.response.OrderListDTO;
import com.example.ClothesShop.entity.Account;
import com.example.ClothesShop.entity.InventoryReservation;
import com.example.ClothesShop.entity.Orders;
import com.example.ClothesShop.entity.OrdersItem;
import com.example.ClothesShop.enums.OrderStatus;
import com.example.ClothesShop.enums.PaymentMethod;
import com.example.ClothesShop.enums.PaymentStatus;
import com.example.ClothesShop.exception.IllegalStateException;
import com.example.ClothesShop.exception.NotFoundException;
import com.example.ClothesShop.repository.OrdersRepository;
import com.example.ClothesShop.service.InventoryReservationService;
import com.example.ClothesShop.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrdersRepository ordersRepository;
    private final InventoryReservationService inventoryReservationService;
    @Override
    @Transactional
    public Orders createOrder(
            Account account,
            List<InventoryReservation> reservations,
            String address,
            String phone,
            PaymentMethod paymentMethod) {

        Orders order = Orders.builder()
                .trackingOrder(generateTrackingOrder())
                .account(account)
                .orderStatus(OrderStatus.PENDING)
                .address(address)
                .phone(phone)
                .totalPrice(
                        reservations.stream()
                                .mapToDouble(r ->
                                        r.getQuantity() * r.getSku().getPrice()
                                )
                                .sum()
                )
                .paymentMethod(paymentMethod)
                .paymentStatus(PaymentStatus.UNPAID)
                .build();

        List<OrdersItem> items = reservations.stream()
                .map(r -> OrdersItem.builder()
                        .order(order)
                        .sku(r.getSku())
                        .quantity(r.getQuantity())
                        .price(r.getSku().getPrice())
                        .build()
                )
                .toList();

        order.setItems(items);

        return ordersRepository.save(order);
    }

    @Override
    public Orders findByTrackingCode(String trackingCode) {
        return ordersRepository.findByTrackingOrder(trackingCode)
                .orElseThrow(() -> new NotFoundException("Order not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrderListDTO> getOrders(OrderStatus status, Pageable pageable) {
        Page<Orders> page = (status == null)
                ? ordersRepository.findAll(pageable)
                : ordersRepository.findByOrderStatus(status, pageable);

        return page.map(this::toListDTO);
    }
    private OrderListDTO toListDTO(Orders o) {
        return OrderListDTO.builder()
                .id(o.getId())
                .trackingOrder(o.getTrackingOrder())
                .customerEmail(o.getAccount().getEmail())
                .orderStatus(o.getOrderStatus())
                .totalPrice(o.getTotalPrice())
                .createdDate(o.getCreatedDate())
                .build();
    }


    @Override
    public Orders updateOrderStatus(Long orderId, OrderStatus newStatus, String reason) {
        Orders order  = ordersRepository.findById(orderId).orElseThrow(() -> new NotFoundException("Order not found"));
        OrderStatus currentStatus = order.getOrderStatus();
        if (currentStatus == OrderStatus.CANCELLED || currentStatus == OrderStatus.DELIVERED) {
            throw new IllegalStateException("Order already finished");
        }
        if (currentStatus == OrderStatus.PENDING && newStatus == OrderStatus.DELIVERED) {
            throw new IllegalStateException("Invalid order status");
        }
        if (newStatus == OrderStatus.CANCELLED) {
            order.setCancelReason(reason);
            inventoryReservationService.releaseStock(order);
        }
        order.setOrderStatus(newStatus);
        return ordersRepository.save(order);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderDetailDTO getOrderDetail(Long orderId) {
        Orders order = ordersRepository.findDetailById(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found"));

        return toDetailDTO(order);
    }

    private OrderDetailDTO toDetailDTO(Orders order) {
        return OrderDetailDTO.builder()
                .id(order.getId())
                .trackingOrder(order.getTrackingOrder())
                .address(order.getAddress())
                .phone(order.getPhone())
                .orderStatus(order.getOrderStatus())
                .totalPrice(order.getTotalPrice())
                .items(
                        order.getItems().stream()
                                .map(this::toItemDTO)
                                .toList()
                )
                .build();
    }

    private OrderItemDTO toItemDTO(OrdersItem item) {
        return OrderItemDTO.builder()
                .skuCode(item.getSku().getSkuCode())
                .size(item.getSku().getClothingSize().name())
                .color(item.getSku().getColor())
                .quantity(item.getQuantity())
                .price(item.getPrice())
                .build();
    }


    private String generateTrackingOrder() {
        return "ORD-" +
                LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE)
                + "-" +
                UUID.randomUUID().toString().substring(0, 6).toUpperCase();
    }
}
