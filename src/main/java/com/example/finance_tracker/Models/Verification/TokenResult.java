package com.example.finance_tracker.Models.Verification;

public class TokenResult {
    private String token;
    private String message;

    public String getToken(){
        return this.token;
    }

    public String getMessage(){
        return this.message;
    }

    public void setToken(String token){
        this.token = token;
    }

    public void setMessage(String message){
        this.message = message;
    }

    public TokenResult(String token, String message){
        this.token = token;
        this.message = message;
    }
}
