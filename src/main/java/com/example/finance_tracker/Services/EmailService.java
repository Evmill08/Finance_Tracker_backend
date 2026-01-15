package com.example.finance_tracker.Services;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.finance_tracker.Repositories.UserRepository;
import com.example.finance_tracker.Models.User;




@Service
public class EmailService {
    private final UserRepository _userRepository;
    private final PasswordEncoder _passwordEncoder;

    public EmailService(PasswordEncoder passwordEncoder, UserRepository userRepository){
        this._userRepository = userRepository;
        this._passwordEncoder = passwordEncoder;
    }

    public void verifyEmail(){

    }

    public void requestReset(){

    }

    public void resetPassword(String email, String newPassword){
        User user = _userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Invalid Email"));

        
    }
}
