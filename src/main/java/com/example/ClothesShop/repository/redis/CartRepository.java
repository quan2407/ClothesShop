package com.example.ClothesShop.repository.redis;

import com.example.ClothesShop.entity.redis.cart.Cart;
import org.springframework.data.repository.CrudRepository;

public interface CartRepository extends CrudRepository<Cart,Long> {
}
