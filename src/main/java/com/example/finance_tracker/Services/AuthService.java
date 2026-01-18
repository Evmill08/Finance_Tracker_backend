package com.example.finance_tracker.Services;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.finance_tracker.Models.Auth.AuthResult;
import com.example.finance_tracker.Models.Email.EmailResult;
import com.example.finance_tracker.Models.Email.EmailVerification;
import com.example.finance_tracker.Models.User;
import com.example.finance_tracker.Models.Verification.PasswordReset;
import com.example.finance_tracker.Models.Verification.TokenResult;
import com.example.finance_tracker.Models.Verification.VerificationFactory;
import com.example.finance_tracker.Repositories.PasswordRepository;
import com.example.finance_tracker.Repositories.UserRepository;
import com.example.finance_tracker.Repositories.VerificationRepository;
import com.example.finance_tracker.Utils.AuthExceptions.InvalidCredentialsException;
import com.example.finance_tracker.Utils.AuthExceptions.TokenExpiredException;


@Service
public class AuthService {
    private final UserRepository _userRepository;
    private final PasswordEncoder _passwordEncoder;
    private final EmailService _emailService;
    private final VerificationRepository _verificationRepository;
    private final PasswordRepository _passwordRepository;
    private final AuthValidationService _authValidationService;

    public AuthService(
        UserRepository userRepository, 
        PasswordEncoder passwordEncoder, 
        EmailService emailService, 
        VerificationRepository verificationRepository, 
        PasswordRepository passwordRepository,
        AuthValidationService authValidationService
    ){
        this._userRepository = userRepository;
        this._passwordEncoder = passwordEncoder;
        this._emailService = emailService;
        this._verificationRepository = verificationRepository;
        this._passwordRepository = passwordRepository;
        this._authValidationService = authValidationService;
    }

    // TODO: If failed login, reset to email verification steps not just error
    public AuthResult login(String email, String password){
        User user = _userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Invalid Credentials"));

        if (!_passwordEncoder.matches(password, user.getPasswordHash())){
            throw new InvalidCredentialsException("Invalid Password");
        }

        if (!user.getEmailVerified()){
            throw new InvalidCredentialsException("Invalid user");
        }

        if (!_authValidationService.validateLogin(email, password)) {
            throw new InvalidCredentialsException("Invalid email or password");
        }

        return new AuthResult(user.getId(), user.getEmail());
    }

    public TokenResult signup(String email, String password){
        if (_userRepository.findByEmail(email).isPresent()){
            throw new InvalidCredentialsException("Email already exists");
        }

        if (!_authValidationService.validateLogin(email, password)) {
            throw new InvalidCredentialsException("Invalid email or password");
        }

        User user = new User();
        user.setEmail(email);
        user.setPassword(_passwordEncoder.encode(password));
        user.setEmailVerified(false);
        
        String token = VerificationFactory.generateToken();
        String code = VerificationFactory.generateCode();
        Instant expiration = VerificationFactory.generateExpiration(15, ChronoUnit.MINUTES);

        EmailVerification verification = new EmailVerification(
            token,
            code,
            expiration,
            user
        );

        EmailResult emailResult = _emailService.sendVerificationEmail(email, code);

        _userRepository.save(user);
        _verificationRepository.save(verification);

        return new TokenResult(token, emailResult.message);
    }

    // Takes temporary token, verifies it
    public AuthResult verifyEmail(String email, String code){

        User user = _userRepository.findByEmail(email)
            .orElseThrow(() -> new InvalidCredentialsException("User not found"));

        EmailVerification verification = _verificationRepository.findByUserId(user.getId())
            .orElseThrow(() -> new RuntimeException("Invalid Token"));

        if (verification.getExpiresAt().isBefore(Instant.now())){
            throw new TokenExpiredException("Token has expired");
        }

        if (!verification.getVerificationCode().equals(code)){
            throw new RuntimeException("Token is invalid");
        }

        user.setEmailVerified(true);

        _userRepository.save(user);
        _verificationRepository.save(verification);

        _verificationRepository.delete(verification);

        return new AuthResult(user.getId(), user.getEmail());
    }

    public TokenResult requestPasswordReset(String email){
        User user = _userRepository.findByEmail(email)
            .orElseThrow(() -> new InvalidCredentialsException("User not found"));

        String token = VerificationFactory.generateToken();
        String code = VerificationFactory.generateCode();
        Instant expiration = VerificationFactory.generateExpiration(15, ChronoUnit.MINUTES);

        PasswordReset reset = new PasswordReset(
            token,
            code,
            expiration,
            user
        );

        _passwordRepository.save(reset);

        EmailResult emailResult = _emailService.sendPasswordResetEmail(email, code);

        return new TokenResult(token, emailResult.message);
    }

    public void resetPassword(String verificationToken, String verificationCode, String newPassword){
        PasswordReset reset = _passwordRepository.findByResetToken(verificationToken)
            .orElseThrow(() -> new RuntimeException("Invalid Token"));

        if (reset.getExpiresAt().isBefore(Instant.now())){
            throw new TokenExpiredException("Token has expired");
        }

        if (!reset.getResetCode().equals(verificationCode)){
            throw new RuntimeException("Token is invalid");
        }

        if (!_authValidationService.validatePassword(newPassword)){
            throw new InvalidCredentialsException("Password is invalid");
        }

        User user = reset.getUser();

        if (_passwordEncoder.matches(newPassword, user.getPasswordHash())){
            throw new RuntimeException("Password can not be the same as the previous password");
        }

        user.setPassword(_passwordEncoder.encode(newPassword));
        _userRepository.save(user);
        _passwordRepository.save(reset);
    }
}
