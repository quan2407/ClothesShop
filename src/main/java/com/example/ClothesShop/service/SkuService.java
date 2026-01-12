package com.example.ClothesShop.service;

import com.example.ClothesShop.dto.response.SkuResponseDTO;

import java.util.List;

public interface SkuService {

    List<SkuResponseDTO> getSkusByProductId(Long productId);

}
