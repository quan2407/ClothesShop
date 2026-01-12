package com.example.ClothesShop.repository;

import com.example.ClothesShop.entity.SKU;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SKURepository extends JpaRepository<SKU,Long> {
    List<SKU> findByProduct_Id(Long productId);
}
