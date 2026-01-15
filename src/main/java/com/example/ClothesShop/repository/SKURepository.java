package com.example.ClothesShop.repository;

import com.example.ClothesShop.entity.SKU;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SKURepository extends JpaRepository<SKU,Long> {
    List<SKU> findByProduct_Id(Long productId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select s from SKU s where s.id = :id")
    Optional<SKU> findByIdForUpdate(@Param("id") Long id);
}
