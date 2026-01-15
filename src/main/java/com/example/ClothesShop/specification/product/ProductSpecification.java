package com.example.ClothesShop.specification.product;

import com.example.ClothesShop.entity.Product;
import com.example.ClothesShop.entity.SKU;
import com.example.ClothesShop.enums.ClothingSize;
import com.example.ClothesShop.enums.SkuStatus;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

public class ProductSpecification {
    //Specification Tutorial
    // root = FROM : represent entity querying
    // criterialBuilder = Where
    // query = SELECT/DISTINCT/ORDER
    public static Specification<Product> hasCategory(Long categoryId) {
        return ((root, query, criteriaBuilder) ->
                categoryId == null ? null : criteriaBuilder.equal(root.get("category").get("id"), categoryId));
    }

    public static Specification<Product> nameContains(String name) {
        return ((root, query, criteriaBuilder) ->
                (name == null || name.isBlank()) ? null
                        : criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
    }

    public static Specification<Product> priceBetween(
            Double minPrice,
            Double maxPrice
    ) {
        return (root, query, criteriaBuilder) -> {
            if (minPrice == null && maxPrice == null) return null;

            Join<Product, SKU> skuJoin = root.join("skus");
            query.distinct(true);

            if (minPrice != null && maxPrice != null) {
                return criteriaBuilder.between(skuJoin.get("price"), minPrice, maxPrice);
            }

            if (minPrice != null) {
                return criteriaBuilder.greaterThanOrEqualTo(skuJoin.get("price"), minPrice);
            }

            return criteriaBuilder.lessThanOrEqualTo(skuJoin.get("price"), maxPrice);
        };
    }

    public static Specification<Product> hasAvailableSku(){
        return (root, query, criteriaBuilder) -> {
            Join<Product, SKU> skuJoin = root.join("skus");
            query.distinct(true);
            return criteriaBuilder.and(
                    criteriaBuilder.equal(skuJoin.get("status"), SkuStatus.ACTIVE),
                    criteriaBuilder.greaterThan(skuJoin.get("quantity"), 0)
            );
        };
    }
    public static Specification<Product> hasSize(ClothingSize size){
        return  (root, query, criteriaBuilder) -> {
            if (size == null) return null;
            Join<Product, SKU> skuJoin = root.join("skus");
            query.distinct(true);
            return criteriaBuilder.equal(skuJoin.get("clothingSize"), size);
        };
    }
}
