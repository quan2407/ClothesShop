package com.example.ClothesShop.config;

import com.example.ClothesShop.entity.Account;
import com.example.ClothesShop.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomJwtAuthenticationConverter
        implements Converter<Jwt, AbstractAuthenticationToken> {

    private final AccountRepository accountRepository;

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {

        String email = jwt.getSubject();

        Account account = accountRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        return new UsernamePasswordAuthenticationToken(
                account,                     // 👈 principal
                null,                        // credentials (không cần)
                account.getAuthorities()     // 👈 ROLE nằm đây
        );
    }
}

