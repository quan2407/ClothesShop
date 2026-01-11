package com.example.ClothesShop.service;

import com.example.ClothesShop.entity.Account;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
public class JwtService {

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
       return signedJWT.verify(new MACVerifier(secretKey));
    }
}

