package com.example.ClothesShop.entity.redis.checkout;
import com.example.ClothesShop.enums.ClothingSize;
import lombok.*;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CheckoutReservationItem {
    private Long skuId;
    private int quantity;

    private Long productId;
    private String productName;
    private String skuCode;
    private double price;
    private String color;
    private ClothingSize size;
}
