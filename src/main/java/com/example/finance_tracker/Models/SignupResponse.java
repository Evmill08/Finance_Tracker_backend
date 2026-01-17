package com.example.finance_tracker.Models;

public class SignupResponse {
    private String verificationToken;
    private String email;
    private Long userId;
    private EmailResult emailResult;

    public String getVerificationToken(){
        return this.verificationToken;
    }

    public Long getUserId(){
        return this.userId;
    }

    public String getUserEmail(){
        return this.email;
    }

    public EmailResult getEmailResult(){
        return this.emailResult;
    }

    public void setVerificationToken(String verificationToken){
        this.verificationToken = verificationToken;
    }

    public SignupResponse(Long userId, String email, String verificationToken, EmailResult emailResult){
        this.userId = userId;
        this.email = email;
        this.verificationToken = verificationToken;
        this.emailResult = emailResult;
    }
}
