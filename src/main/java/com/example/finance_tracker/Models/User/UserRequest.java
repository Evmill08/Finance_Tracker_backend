package com.example.finance_tracker.Models.User;

public class UserRequest {
    private String JwtToken;

    public String getJwtToken(){
        return this.JwtToken;
    }

    public void setJwtToken(String JwtToken){
        this.JwtToken = JwtToken;
    }
}
