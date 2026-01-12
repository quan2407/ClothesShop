package com.example.ClothesShop.repository;

import com.example.ClothesShop.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product,Long>
{
}
