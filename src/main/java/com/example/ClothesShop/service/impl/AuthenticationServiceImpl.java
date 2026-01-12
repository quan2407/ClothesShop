package com.example.ClothesShop.service.impl;

import com.example.ClothesShop.dto.JwtInfo;
import com.example.ClothesShop.dto.request.LoginRequest;
import com.example.ClothesShop.dto.response.LoginResponse;
import com.example.ClothesShop.entity.Account;
import com.example.ClothesShop.entity.redis.RedisToken;
import com.example.ClothesShop.exception.AuthenticationFailedException;
import com.example.ClothesShop.exception.InvalidTokenException;
import com.example.ClothesShop.repository.RedisTokenRepository;
import com.example.ClothesShop.service.AuthenticationService;
import com.example.ClothesShop.service.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.*;
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

        try {
            Authentication authentication =
                    authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    loginRequest.getEmail(),
                                    loginRequest.getPassword()
                            )
                    );

            Account account = (Account) authentication.getPrincipal();

            return LoginResponse.builder()
                    .accessToken(jwtService.generateAccessToken(account))
                    .refreshToken(jwtService.generateRefreshToken(account))
                    .build();

        } catch (BadCredentialsException e) {
            throw new AuthenticationFailedException("Invalid email or password");

        } catch (DisabledException e) {
            throw new AuthenticationFailedException("Account is disabled");

        } catch (LockedException e) {
            throw new AuthenticationFailedException("Account is locked");
        }
    }


    @Override
    public void logout(String token) {
        try {
            JwtInfo jwtInfo = jwtService.parseToken(token);

            if (jwtInfo.getExpiredTime().before(new Date())) {
                return;
            }

            redisTokenRepository.save(
                    RedisToken.builder()
                            .jwtId(jwtInfo.getJwtId())
                            .expiration(
                                    jwtInfo.getExpiredTime().getTime()
                                            - jwtInfo.getIssueTime().getTime()
                            )
                            .build()
            );

        } catch (ParseException e) {
            throw new InvalidTokenException("Invalid JWT token");
        }
    }


}
