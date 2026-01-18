package com.example.finance_tracker.Models.Verification;

public class PasswordResetRequest {
    private String email;
    private String verificationCode;
    private String newPassword;

    public String getEmail(){
        return this.email;
    }
    public String getVerificationCode(){
        return this.verificationCode;
    }
    public String getNewPassword(){
        return this.newPassword;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public void setVerificationCode(String verificationCode){
        this.verificationCode = verificationCode;
    }

    public void setNewPassword(String newPassword){
        this.newPassword = newPassword;
    }

    public PasswordResetRequest(String email, String verificationCode, String newPassword){
        this.email = email;
        this.verificationCode = verificationCode;
        this.newPassword = newPassword;
    }
}
