package com.example.ClothesShop.service.impl;

import com.example.ClothesShop.dto.JwtInfo;
import com.example.ClothesShop.dto.request.LoginRequest;
import com.example.ClothesShop.dto.response.LoginResponse;
import com.example.ClothesShop.entity.Account;
import com.example.ClothesShop.entity.RedisToken;
import com.example.ClothesShop.repository.RedisTokenRepository;
import com.example.ClothesShop.service.AuthenticationService;
import com.example.ClothesShop.service.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Date;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RedisTokenRepository redisTokenRepository;
    @Override
    public LoginResponse login(LoginRequest loginRequest) {

        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                );

        Authentication authentication =
                authenticationManager.authenticate(authToken);

        Account account = (Account) authentication.getPrincipal();

        String accessToken = jwtService.generateAccessToken(account);
        String refreshToken = jwtService.generateRefreshToken(account);

        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public void logout(String token) throws ParseException {
        JwtInfo jwtInfo = jwtService.parseToken(token);
        String jwtId = jwtInfo.getJwtId();
        Date issueTime = jwtInfo.getIssueTime();
        Date expiredTime = jwtInfo.getExpiredTime();
        if (expiredTime.before(new Date())) {
            return;
        }

        RedisToken redisToken = RedisToken.builder()
                .jwtId(jwtId)
                .expiration(expiredTime.getTime() - issueTime.getTime())
                .build();
        redisTokenRepository.save(redisToken);
        log.info("Logout successfully");
    }

}
