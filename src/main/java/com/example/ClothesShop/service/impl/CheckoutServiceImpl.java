package com.example.ClothesShop.service.impl;

import com.example.ClothesShop.entity.redis.cart.Cart;
import com.example.ClothesShop.entity.redis.cart.CartItem;
import com.example.ClothesShop.entity.redis.checkout.CheckoutReservation;
import com.example.ClothesShop.entity.redis.checkout.CheckoutReservationItem;
import com.example.ClothesShop.exception.InvalidRequestException;
import com.example.ClothesShop.exception.NotFoundException;
import com.example.ClothesShop.repository.redis.CartRepository;
import com.example.ClothesShop.repository.redis.CheckoutReservationRepository;
import com.example.ClothesShop.service.CheckoutService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CheckoutServiceImpl implements CheckoutService {
    private final CheckoutReservationRepository checkoutReservationRepository;
    private final StringRedisTemplate stringRedisTemplate;
    private final CartRepository cartRepository;
    private final DefaultRedisScript<Long> reserveStockScript;

    @Override
    public String checkoutFromCart(Long accountId) {
        Cart cart = cartRepository.findById(accountId).orElseThrow(()->new NotFoundException("Cart not found!"));
        String reservationId = UUID.randomUUID().toString();

        List<CartItem> items = cart.getItems();

        List<String> stockKeys = items.stream()
                .map(i -> "stock:sku:" + i.getSkuId())
                .toList();

        List<String> quantities = items.stream()
                .map(i -> String.valueOf(i.getQuantity()))
                .toList();

        Long result = stringRedisTemplate.execute(
                reserveStockScript,
                stockKeys,
                quantities.toArray()
        );

        if (result == null || result == -1) {
            throw new InvalidRequestException("Một hoặc nhiều sản phẩm trong giỏ hàng đã hết hàng");
        }

        CheckoutReservation checkoutReservation =
                CheckoutReservation.builder()
                        .reservationId(reservationId)
                        .accountId(cart.getAccountId())
                        .items(
                                cart.getItems().stream()
                                        .map(i -> CheckoutReservationItem.builder()
                                                .skuId(i.getSkuId())
                                                .quantity(i.getQuantity())
                                                .productId(i.getProductId())
                                                .skuCode(i.getSkuCode())
                                                .price(i.getPrice())
                                                .color(i.getColor())
                                                .size(i.getSize())
                                                .build()).toList()
                        )
                        .totalAmount(
                                cart.getItems().stream()
                                        .mapToDouble(i -> i.getPrice() * i.getQuantity())
                                        .sum()
                        )
                        .build();
        checkoutReservationRepository.save(checkoutReservation);
        String shadowKey = "checkout_reservation_shadow:" + checkoutReservation.getReservationId();
        stringRedisTemplate.opsForValue().set(shadowKey, "", Duration.ofMinutes(15));
        return reservationId;
    }
}
