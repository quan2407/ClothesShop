package com.example.ClothesShop.dto.response;

import com.example.ClothesShop.enums.OrderStatus;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class OrderDetailDTO {
    private Long id;
    private String trackingOrder;
    private String address;
    private String phone;
    private OrderStatus orderStatus;
    private double totalPrice;
    private List<OrderItemDTO> items;
}
