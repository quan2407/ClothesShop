package com.example.ClothesShop.service;

import com.example.ClothesShop.dto.request.LoginRequest;
import com.example.ClothesShop.dto.response.LoginResponse;

public interface AuthenticationService {
    public LoginResponse login(LoginRequest loginRequest);
}
