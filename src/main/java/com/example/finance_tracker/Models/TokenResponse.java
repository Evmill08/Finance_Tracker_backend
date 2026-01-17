package com.example.finance_tracker.Models;


// TODO: Think about if we want getters/setters for tokens for all things dealing with them
public class TokenResponse {
    private String token;
    private boolean emailSuccess;
    private String message;

    public String getToken(){
        return this.token;
    }

    public boolean getEmailSuccess(){
        return this.emailSuccess;
    }

    public String getMessage(){
        return this.message;
    }

    public void setToken(String token){
        this.token = token;
    }

    public void setEmailSuccess(boolean emailStaus){
        this.emailSuccess = emailStaus;
    }

    public void setMessage(String message){
        this.message = message;
    }

    public static TokenResponse success(String token){
        TokenResponse response = new TokenResponse();
        response.emailSuccess = true;
        response.token = token;
        return response;
    }

    public static TokenResponse failure(String errorMessage){
        TokenResponse response = new TokenResponse();
        response.emailSuccess = false;
        response.message = errorMessage;
        return response;
    }
}
