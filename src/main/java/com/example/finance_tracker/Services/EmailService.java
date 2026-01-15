package com.example.finance_tracker.Services;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.finance_tracker.Models.EmailResult;
import com.example.finance_tracker.Models.User;
import com.example.finance_tracker.Repositories.UserRepository;

@Service
public class EmailService {
    private final UserRepository _userRepository;
    private final PasswordEncoder _passwordEncoder;

    public EmailService(PasswordEncoder passwordEncoder, UserRepository userRepository){
        this._userRepository = userRepository;
        this._passwordEncoder = passwordEncoder;
    }

    public EmailResult sendVerificationEmail(String email, String token){

    }

    public EmailResult sendPasswordResetEmail(String email, String token){

    }
}
