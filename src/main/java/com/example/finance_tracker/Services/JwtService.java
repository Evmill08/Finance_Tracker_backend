package com.example.finance_tracker.Services;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.finance_tracker.Models.User.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;


@Service
public class JwtService {
    private final Key signingKey;

    public JwtService(@Value("${jwt.secret}") String secret){
        this.signingKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    // Builds a jwt that expires after 1 hour
    public String generate(User user) {
        return Jwts.builder()
            .setSubject(user.getId().toString())
            .claim("email", user.getEmail())
            .claim("roles", user.getRoles()) 
            .claim("email_verified", user.isEmailVerified())
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
            .signWith(signingKey, SignatureAlgorithm.HS256)
            .compact();
    }

    public Long getUserIdFromToken(String token){
        return Long.valueOf(validateAndGetClaims(token).getSubject());
    }

    public String getUserEmailFromToken(String token){
        return validateAndGetClaims(token).get("email", String.class);
    }

    public Claims validateAndGetClaims(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(signingKey)
            .build()
            .parseClaimsJws(token)
            .getBody();
    }
}
