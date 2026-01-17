package com.example.finance_tracker.Models;

public class PasswordResetResponse {
    public boolean success;
    public String message;

    public static PasswordResetResponse success(){
        PasswordResetResponse response = new PasswordResetResponse();
        response.success = true;
        return response;
    }

    public static PasswordResetResponse failure(String errorMessage){
        PasswordResetResponse response = new PasswordResetResponse();
        response.success = false;
        response.message = errorMessage;
        return response;
    }
}
