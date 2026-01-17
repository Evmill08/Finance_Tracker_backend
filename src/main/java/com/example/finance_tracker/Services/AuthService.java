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
import com.example.finance_tracker.Models.PasswordReset;
import com.example.finance_tracker.Models.SignupResponse;
import com.example.finance_tracker.Models.User;
import com.example.finance_tracker.Repositories.PasswordRepository;
import com.example.finance_tracker.Repositories.UserRepository;
import com.example.finance_tracker.Repositories.VerificationRepository;


@Service
public class AuthService {
    private final UserRepository _userRepository;
    private final PasswordEncoder _passwordEncoder;
    private final EmailService _emailService;
    private final VerificationRepository _verificationRepository;
    private final PasswordRepository _passwordRepository;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, EmailService emailService, VerificationRepository verificationRepository, PasswordRepository passwordRepository){
        this._userRepository = userRepository;
        this._passwordEncoder = passwordEncoder;
        this._emailService = emailService;
        this._verificationRepository = verificationRepository;
        this._passwordRepository = passwordRepository;
    }

    public AuthResult login(String email, String password){
        User user = _userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Invalid Credentials"));

        if (!_passwordEncoder.matches(password, user.getPasswordHash())){
            throw new RuntimeException("Invalid Password");
        }

        return new AuthResult(user.getId(), user.getEmail());
    }

    // Returns a temporary token to use when verifying email
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

        EmailResult emailResult = _emailService.sendVerificationEmail(email, code);
        
        return new SignupResponse(user.getId(), user.getEmail(), verificationToken, emailResult);
    }

    // Takes temporary token, verifies it
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
        User user = _userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));

        String verificationToken = UUID.randomUUID().toString();
        String code = String.format("%06d", new SecureRandom().nextInt(1_000_000));

        PasswordReset reset = new PasswordReset(
            verificationToken,
            code,
            Instant.now().plus(15, ChronoUnit.MINUTES),
            user
        );
    }

    public AuthResult resetPassword(String verificationToken, String verificationCode, String newPassword){
        PasswordReset reset = _passwordRepository.findByResetToken(verificationToken)
            .orElseThrow(() -> new RuntimeException("Invalid Token"));

        if (reset.getExpiresAt().isBefore(Instant.now())){
            throw new RuntimeException("Token has expired");
        }

        if (!reset.getResetCode().equals(verificationCode)){
            throw new RuntimeException("Token is invalid");
        }

        User user = reset.getUser();

        if (_passwordEncoder.matches(newPassword, user.getPasswordHash())){
            throw new RuntimeException("Password can not be the same as the previous password");
        }

        user.setPassword(newPassword);
        _userRepository.save(user);
        _passwordRepository.save(reset);

        return new AuthResult(user.getId(), user.getEmail());
    }
}
