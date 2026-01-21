package com.example.ClothesShop.entity.redis.cart;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.util.List;

@RedisHash("Cart")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cart {
    @Id
    private Long accountId;
    private List<CartItem> items;
    @TimeToLive
    private Long timeToLive;
}
