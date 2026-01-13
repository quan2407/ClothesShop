package com.example.ClothesShop.service;

import com.example.ClothesShop.dto.JwtInfo;
import com.example.ClothesShop.entity.Account;
import com.example.ClothesShop.entity.redis.RedisToken;
import com.example.ClothesShop.repository.redis.RedisTokenRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JwtService {
private final RedisTokenRepository redisTokenRepository;
    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.access-token-expiration-minutes}")
    private long accessTokenExpirationMinutes;

    @Value("${jwt.refresh-token-expiration-days}")
    private long refreshTokenExpirationDays;

    public String generateAccessToken(Account account) {
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS512);

        Date issueTime = new Date();
        Date expirationTime = Date.from(
                issueTime.toInstant().plus(accessTokenExpirationMinutes, ChronoUnit.MINUTES)
        );

        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(account.getEmail())
                .issueTime(issueTime)
                .expirationTime(expirationTime)
                .jwtID(UUID.randomUUID().toString())
                .build();

        return sign(claimsSet, jwsHeader);
    }

    public String generateRefreshToken(Account account) {
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS512);

        Date issueTime = new Date();
        Date expirationTime = Date.from(
                issueTime.toInstant().plus(refreshTokenExpirationDays, ChronoUnit.DAYS)
        );

        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(account.getEmail())
                .issueTime(issueTime)
                .expirationTime(expirationTime)
                .build();

        return sign(claimsSet, jwsHeader);
    }

    private String sign(JWTClaimsSet claimsSet, JWSHeader jwsHeader) {
        Payload payload = new Payload(claimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(jwsHeader, payload);
        try {
            jwsObject.sign(new MACSigner(secretKey));
        } catch (JOSEException e) {
            throw new RuntimeException("Cannot sign JWT", e);
        }
        return jwsObject.serialize();
    }

    public boolean verify(String token) throws ParseException, JOSEException {
        SignedJWT signedJWT = SignedJWT.parse(token);
        Date expirationTime = signedJWT.getJWTClaimsSet().getExpirationTime();
        if (expirationTime.before(new Date())) {
return false;
        }
        String jwtId = signedJWT.getJWTClaimsSet().getJWTID();
       Optional<RedisToken> byId= redisTokenRepository.findById(jwtId);
       if (byId.isPresent()) {
           throw new RuntimeException("Token invalid");
       }
       return signedJWT.verify(new MACVerifier(secretKey));
    }

    public JwtInfo parseToken(String token) throws ParseException {
        SignedJWT signedJWT = SignedJWT.parse(token);
        String jwtId = signedJWT.getJWTClaimsSet().getJWTID();
        Date issueTime = signedJWT.getJWTClaimsSet().getIssueTime();
        Date expirationTime = signedJWT.getJWTClaimsSet().getExpirationTime();
        return JwtInfo.builder()
                .jwtId(jwtId)
                .issueTime(issueTime)
                .expiredTime(expirationTime)
                .build();
    }
}

