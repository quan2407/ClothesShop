package com.example.ClothesShop.repository.redis;

import com.example.ClothesShop.entity.redis.checkout.CheckoutReservation;
import org.springframework.data.repository.CrudRepository;

public interface CheckoutReservationRepository extends CrudRepository<CheckoutReservation, String> {
}
