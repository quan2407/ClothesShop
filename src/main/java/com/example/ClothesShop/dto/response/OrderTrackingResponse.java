package com.example.ClothesShop.dto.response;

import com.example.ClothesShop.entity.Orders;
import com.example.ClothesShop.enums.OrderStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.List;

@Getter
@Builder
public class OrderTrackingResponse {

    private String trackingCode;
    private OrderStatus status;
    private double totalPrice;
    private String address;
    private Instant createdDate;
    private List<OrderItemView> items;

    public static OrderTrackingResponse from(Orders order) {
        return OrderTrackingResponse.builder()
                .trackingCode(order.getTrackingOrder())
                .status(order.getOrderStatus())
                .totalPrice(order.getTotalPrice())
                .address(order.getAddress())
                .createdDate(order.getCreatedDate())
                .items(
                        order.getItems().stream()
                                .map(OrderItemView::from)
                                .toList()
                )
                .build();
    }
}

