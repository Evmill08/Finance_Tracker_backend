package com.example.finance_tracker.Models;

public class AuthResponse {
    private boolean success;
    private String token;
    private String errorMessage;

    public static AuthResponse success(String token){
        AuthResponse response = new AuthResponse();
        response.success = true;
        response.token = token;
        return response;
    }

    public static AuthResponse failure(String errorMessage){
        AuthResponse response = new AuthResponse();
        response.success = false;
        response.errorMessage = errorMessage;
        return response;
    }
}
