package com.example.ClothesShop.service;

import com.example.ClothesShop.entity.Account;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
public class JwtService {
    private String secretKey="x3QmR7FvB8Wn9uYp5A1C2dE4G6H0JkLZsTqP8oM5rVYwXfK9eS7U6iN+DaRbC4hE2";
    public String generateAccessToken(Account account) {
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS512);
        Date issueTime = new Date();
        Date expirationTime = Date.from(issueTime.toInstant().plus(30, ChronoUnit.MINUTES));
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(account.getEmail())
                .issueTime(issueTime)
                .expirationTime(expirationTime)
                .build();

    Payload payload = new Payload(claimsSet.toJSONObject());
    JWSObject jwsObject = new JWSObject(jwsHeader,payload);
    try {
        jwsObject.sign(new MACSigner(secretKey));

    }  catch (JOSEException e) {
        throw new RuntimeException(e);
    }
    return  jwsObject.serialize();
    }

    public String generateRefreshToken(Account account) {
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS512);
        Date issueTime = new Date();
        Date expirationTime = Date.from(issueTime.toInstant().plus(30, ChronoUnit.DAYS));
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(account.getEmail())
                .issueTime(issueTime)
                .expirationTime(expirationTime)
                .build();

        Payload payload = new Payload(claimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(jwsHeader,payload);
        try {
            jwsObject.sign(new MACSigner(secretKey));

        }  catch (JOSEException e) {
            throw new RuntimeException(e);
        }
        return  jwsObject.serialize();
    }
}
