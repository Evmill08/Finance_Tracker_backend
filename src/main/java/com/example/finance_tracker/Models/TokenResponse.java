package com.example.finance_tracker.Models;


// TODO: Think about if we want getters/setters for tokens for all things dealing with them
public class TokenResponse {
    private String token;
    private EmailResult emailResult;

    public String getToken(){
        return this.token;
    }
    public EmailResult getEmailResult(){
        return this.emailResult;
    }

    public void setToken(String token){
        this.token = token;
    }
    public void setMessage(EmailResult emailResult){
        this.emailResult = emailResult;
    }

    public static TokenResponse success(String token){
        TokenResponse response = new TokenResponse();
        EmailResult result = new EmailResult(true, "Email Successfully Sent");
        response.emailResult = result;
        response.token = token;
        return response;
    }

    public static TokenResponse failure(String errorMessage){
        TokenResponse response = new TokenResponse();
        EmailResult result = new EmailResult(false, errorMessage);
        response.emailResult = result;
        return response;
    }
}
