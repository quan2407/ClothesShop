package com.example.ClothesShop.repository;

import com.example.ClothesShop.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product,Long>
{
    @Query("SELECT p FROM Product p LEFT JOIN FETCH p.skus")
    List<Product> findAllWithSkus();
    Optional<Product> findById(Long id);


}
