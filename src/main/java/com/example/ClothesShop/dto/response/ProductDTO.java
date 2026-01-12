package com.example.ClothesShop.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ProductDTO {
    private Long id;
    private String name;
    private String description;
    private boolean inStock;
    private String categoryName;
}
