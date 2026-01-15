package com.example.ClothesShop.service.impl;

import com.example.ClothesShop.entity.Account;
import com.example.ClothesShop.entity.InventoryReservation;
import com.example.ClothesShop.entity.Orders;
import com.example.ClothesShop.entity.OrdersItem;
import com.example.ClothesShop.enums.OrderStatus;
import com.example.ClothesShop.exception.NotFoundException;
import com.example.ClothesShop.repository.OrdersRepository;
import com.example.ClothesShop.service.OrderService;
import lombok.RequiredArgsConstructor;
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
    @Override
    @Transactional
    public Orders createOrder(
            Account account,
            List<InventoryReservation> reservations,
            String address,
            String phone
    ) {

        Orders order = Orders.builder()
                .trackingOrder(generateTrackingOrder())
                .account(account)
                .orderStatus(OrderStatus.CONFIRMED)
                .address(address)
                .phone(phone)
                .totalPrice(
                        reservations.stream()
                                .mapToDouble(r ->
                                        r.getQuantity() * r.getSku().getPrice()
                                )
                                .sum()
                )
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


    private String generateTrackingOrder() {
        return "ORD-" +
                LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE)
                + "-" +
                UUID.randomUUID().toString().substring(0, 6).toUpperCase();
    }
}
