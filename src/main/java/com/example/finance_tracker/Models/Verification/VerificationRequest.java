package com.example.finance_tracker.Models.Verification;

public class VerificationRequest {
    private String email;
    private String verificationCode;

    public String getEmail(){
        return this.email;
    }

    public String getVerificationCode(){
        return this.verificationCode;
    }
}
