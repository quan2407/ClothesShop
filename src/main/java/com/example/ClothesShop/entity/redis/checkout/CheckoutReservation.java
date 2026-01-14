package com.example.ClothesShop.entity.redis.checkout;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.util.List;

@RedisHash("checkout_reservation")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CheckoutReservation {
    @Id
    private String reservationId;
    private Long accountId;
    private Long checkoutId;
    private List<CheckoutReservationItem> items;
    private double totalAmount;

}
