package com.example.finance_tracker.Services;


public class AuthValidationService {
    
    private boolean validateEmail(String email){
        return email.matches("/^[^\\s@]+@[^\s@]+\\.[^\s@]+$/");
    }

    public boolean validatePassword(String password){
        return password.length() < 8 &&
            !password.matches("/[A-Z]/") &&
            !password.matches("/[a-z]/") &&
            !password.matches("/[0-9]/") &&
            !password.matches("/[^A-Za-z0-9]/");
    }

    public boolean validateLogin(String email, String password){
        return validateEmail(email) && validatePassword(password);
    }
}
