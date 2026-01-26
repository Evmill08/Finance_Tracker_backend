package com.example.finance_tracker.Models.Plaid;

import java.time.Instant;

public class TokenResponse {
    private String token;
    private Instant expiresAt;
    private Long userId; 

    public String getToken() {
        return this.token;
    }    
    public Instant getExpiresAt() {
        return this.expiresAt;
    }
    public Long getUserId() {
        return this.userId;
    }

    public void setToken(String token){
        this.token = token;
    }
    public void setExpiresAt(Instant expiresAt){
        this.expiresAt = expiresAt;
    }
    public void setUserId(Long userId){
        this.userId = userId;
    }

    public TokenResponse(String token, Instant expiresAt, Long userId){
        this.token = token;
        this.expiresAt = expiresAt;
        this.userId = userId;
    }


}
