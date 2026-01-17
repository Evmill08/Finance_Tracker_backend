package com.example.finance_tracker.Services;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.finance_tracker.Models.AuthResult;
import com.example.finance_tracker.Models.EmailResult;
import com.example.finance_tracker.Models.EmailVerification;
import com.example.finance_tracker.Models.SignupResponse;
import com.example.finance_tracker.Models.User;
import com.example.finance_tracker.Repositories.UserRepository;
import com.example.finance_tracker.Repositories.VerificationRepository;


@Service
public class AuthService {
    private final UserRepository _userRepository;
    private final PasswordEncoder _passwordEncoder;
    private final EmailService _emailService;
    private final VerificationRepository _verificationRepository;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, EmailService emailService, VerificationRepository verificationRepository){
        this._userRepository = userRepository;
        this._passwordEncoder = passwordEncoder;
        this._emailService = emailService;
        this._verificationRepository = verificationRepository;
    }

    public AuthResult login(String email, String password){
        User user = _userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Invalid Credentials"));

        if (!_passwordEncoder.matches(password, user.getPasswordHash())){
            throw new RuntimeException("Invalid Password");
        }

        return new AuthResult(user.getId(), user.getEmail());
    }

    public SignupResponse signup(String email, String password){
        if (_userRepository.findByEmail(email).isPresent()){
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        user.setEmail(email);
        user.setPassword(_passwordEncoder.encode(password));
        user.setEmailVerified(false);

        String verificationToken = UUID.randomUUID().toString();
        String code = String.format("%06d", new SecureRandom().nextInt(1_000_000));

        EmailVerification verification = new EmailVerification(
            verificationToken,
            code,
            Instant.now().plus(15, ChronoUnit.MINUTES),
            user
        );

        _verificationRepository.save(verification);

        // This should contain success status, error message
        EmailResult emailResult = _emailService.sendVerificationEmail(email, code);
        
        return new SignupResponse(user.getId(), user.getEmail(), verificationToken, emailResult);
    }

    public AuthResult verifyEmail(String verifcationToken, String code){
        EmailVerification verification = _verificationRepository.findByVerificationToken(verifcationToken)
            .orElseThrow(() -> new RuntimeException("Invalid Token"));

        if (verification.getExpiresAt().isBefore(Instant.now())){
            throw new RuntimeException("Token has expired");
        }

        if (!verification.getVerificationCode().equals(code)){
            throw new RuntimeException("Token is invalid");
        }

        User user = verification.getVerificationUser();
        user.setEmailVerified(true);

        _userRepository.save(user);
        _verificationRepository.save(verification);

        return new AuthResult(user.getId(), user.getEmail());
    }

    public void requestPasswordReset(String email){

    }

    public void resetPassword(String token, String newPassword){

    }
}
