package com.example.demo.security;

import java.nio.charset.StandardCharsets;
import java.security.Key;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;

@Service
public class JwtService {

    private static final String SECRET =
            "my-secret-key-my-secret-key-my-secret-key-my-secret-key";//This secret must be same as auth-service.

    private static SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(
                SECRET.getBytes(StandardCharsets.UTF_8)
        );
    }

    public static Claims extractAllClaims(String token) {

        return Jwts.parser()
                .verifyWith(getSigningKey())   // ✅ now NOT red
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}

