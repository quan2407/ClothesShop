package com.example.ClothesShop.service.impl;

import com.example.ClothesShop.dto.request.AccountCreateRequest;
import com.example.ClothesShop.dto.response.AccountCreateResponse;
import com.example.ClothesShop.entity.Account;
import com.example.ClothesShop.entity.Role;
import com.example.ClothesShop.repository.AccountRepository;
import com.example.ClothesShop.repository.RoleRepository;
import com.example.ClothesShop.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public AccountCreateResponse createAccount(AccountCreateRequest request) {

        if (accountRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("ROLE_USER not found"));

        Account account = Account.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(userRole)
                .build();

        accountRepository.save(account);

        return AccountCreateResponse.builder()
                .email(account.getEmail())
                .build();
    }

}
