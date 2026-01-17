package com.example.finance_tracker.Models.Email;

public class EmailResult {
    public boolean success;
    public String message;

    public EmailResult(boolean success, String message){
        this.success = success;
        this.message = message;
    }
} 

