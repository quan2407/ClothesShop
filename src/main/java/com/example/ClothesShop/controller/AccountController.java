package com.example.ClothesShop.controller;

import com.example.ClothesShop.dto.request.AccountCreateRequest;
import com.example.ClothesShop.dto.request.LoginRequest;
import com.example.ClothesShop.dto.response.AccountCreateResponse;
import com.example.ClothesShop.dto.response.LoginResponse;
import com.example.ClothesShop.service.AccountService;
import com.example.ClothesShop.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/account")
public class AccountController {
    private final AccountService accountService;
    private final AuthenticationService authenticationService;
    @PostMapping("/register")
    public AccountCreateResponse registerAccount(@RequestBody AccountCreateRequest request){
        return accountService.createAccount(request);
    }
    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request){
        return authenticationService.login(request);
    }
}
