package com.example.finance_tracker.Models;

public class PasswordResetRequest {
    private String verificationToken;
    private String verificationCode;
    private String newPassword;

    public String getVerificationToken(){
        return this.verificationToken;
    }
    public String getVerificationCode(){
        return this.verificationCode;
    }
    public String getNewPassword(){
        return this.newPassword;
    }

    public void setVerificationToken(String verificationToken){
        this.verificationToken = verificationToken;
    }

    public void setVerificationCode(String verificationCode){
        this.verificationCode = verificationCode;
    }

    public void setNewPassword(String newPassword){
        this.newPassword = newPassword;
    }

    public PasswordResetRequest(String verificationToken, String verificationCode, String newPassword){
        this.verificationToken = verificationToken;
        this.verificationCode = verificationCode;
        this.newPassword = newPassword;
    }
}
