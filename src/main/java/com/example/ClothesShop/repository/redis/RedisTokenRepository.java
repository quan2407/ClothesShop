package com.example.ClothesShop.repository.redis;

import com.example.ClothesShop.entity.redis.RedisToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RedisTokenRepository extends CrudRepository<RedisToken, String> {
}
