package com.example.ClothesShop.controller;

import com.example.ClothesShop.dto.request.AccountCreateRequest;
import com.example.ClothesShop.dto.request.LoginRequest;
import com.example.ClothesShop.dto.response.AccountCreateResponse;
import com.example.ClothesShop.dto.response.LoginResponse;
import com.example.ClothesShop.entity.Account;
import com.example.ClothesShop.service.AccountService;
import com.example.ClothesShop.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/account")
@Slf4j
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

    @PostMapping("/logout")
    public void logout(@RequestHeader("Authorization") String authHeader) throws ParseException {
        String token = authHeader.substring("Bearer ".length());
        authenticationService.logout(token);
log.info("token:{}",token);
    }
    @GetMapping("/me")
    public ResponseEntity<?> me(@AuthenticationPrincipal Account account) {

        return ResponseEntity.ok(account.getEmail());
    }

}
