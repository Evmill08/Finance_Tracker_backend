package com.example.finance_tracker.Models;

public class AuthResult{
    private final Long userId;
    private final String email;

    public AuthResult(Long userId, String email){
        this.userId = userId;
        this.email = email;
    }

    public Long getUserId() { return userId;}
    public String getEmail() {return email;}
}