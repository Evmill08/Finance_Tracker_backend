package com.example.finance_tracker.Services;

import java.sql.Date;
import java.time.Instant;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.finance_tracker.Models.AuthResult;
import com.example.finance_tracker.Models.User;
import com.example.finance_tracker.Repositories.UserRepository;


@Service
public class AuthService {
    private final UserRepository _userRepository;
    private final PasswordEncoder _passwordEncoder;
    private final EmailService _emailService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, EmailService emailService){
        this._userRepository = userRepository;
        this._passwordEncoder = passwordEncoder;
        this._emailService = emailService;
    }

    public AuthResult login(String email, String password){
        User user = _userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Invalid Credentials"));

        if (!_passwordEncoder.matches(password, user.getPasswordHash())){
            throw new RuntimeException("Invalid Password");
        }

        return new AuthResult(user.getId(), user.getEmail());
    }

    public AuthResult signup(String email, String password){
        if (_userRepository.findByEmail(email).isPresent()){
            throw new RuntimeException("Email already exists");
        }

        User user = new User();

        user.setEmail(email);
        user.setPassword(_passwordEncoder.encode(password));
        
        boolean emailverified = false;
        String emailVerificationToken = "";

        user.setEmailVerified(emailverified);
        user.setEmailVerificationToken(emailVerificationToken);
        user.setEmailVerificationTokenExpiresAt( Instant.now()); //TODO: Change this to 1 hour from now

        var emailResult = _emailService.sendVerificationEmail(email, emailVerificationToken);

        User saved = _userRepository.save(user);

        return new AuthResult(saved.getId(), saved.getEmail());
    }

    public void verifyEmail(String email){
        
    }

    public void requestPasswordReset(String email){

    }

    public AuthResult resetPassword(String token, String newPassword){

    }
}
