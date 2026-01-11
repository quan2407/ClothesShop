package com.example.ClothesShop.service;

import com.example.ClothesShop.dto.request.AccountCreateRequest;
import com.example.ClothesShop.dto.response.AccountCreateResponse;

public interface AccountService {
    public AccountCreateResponse createAccount(AccountCreateRequest request);
}
