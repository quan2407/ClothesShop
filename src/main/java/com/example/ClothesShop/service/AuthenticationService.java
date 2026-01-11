package com.example.ClothesShop.service;

import com.example.ClothesShop.dto.request.LoginRequest;
import com.example.ClothesShop.dto.response.LoginResponse;

import java.text.ParseException;

public interface AuthenticationService {
    public LoginResponse login(LoginRequest loginRequest);
    public void logout(String token) throws ParseException;
}
