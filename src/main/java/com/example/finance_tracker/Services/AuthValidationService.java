package com.example.finance_tracker.Services;

import org.springframework.stereotype.Service;

@Service
public class AuthValidationService {
    
private boolean validateEmail(String email) {
    boolean matches = email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");
    return matches;
}

    public boolean validatePassword(String password) {
        if (password.length() < 8) return false;              
        if (!password.matches(".*[A-Z].*")) return false;     
        if (!password.matches(".*[a-z].*")) return false;      
        if (!password.matches(".*[0-9].*")) return false;      
        if (!password.matches(".*[^A-Za-z0-9].*")) return false; 
        return true; // passed all checks
    }

    public boolean validateLogin(String email, String password){
        return validateEmail(email) && validatePassword(password);
    }
}
