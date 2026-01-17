package com.example.finance_tracker.Controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.finance_tracker.Models.AuthRequest;
import com.example.finance_tracker.Models.AuthResponse;
import com.example.finance_tracker.Models.AuthResult;
import com.example.finance_tracker.Models.PasswordResetRequest;
import com.example.finance_tracker.Models.SignupResponse;
import com.example.finance_tracker.Models.TokenResponse;
import com.example.finance_tracker.Models.VerificationRequest;
import com.example.finance_tracker.Services.AuthService;
import com.example.finance_tracker.Services.JwtService;


@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService _authService;
    private final JwtService _jwtService;

    public AuthController(AuthService authService, JwtService jwtService){
        this._authService = authService;
        this._jwtService = jwtService;
    }

    //TODO: Do not return jwt
    @PostMapping("/signup")
    public ResponseEntity<TokenResponse> signup(@RequestBody AuthRequest request){
        try {
            SignupResponse response = _authService.signup(request.email, request.password);

            return ResponseEntity.ok(TokenResponse.success(response.getVerificationToken()));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(TokenResponse.failure("Invalid email verification"));
       }
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request){
        try {
            AuthResult auth = _authService.login(request.email, request.password);
            String jwt = _jwtService.generate(auth.getUserId(), auth.getEmail()); 

            return ResponseEntity.ok(AuthResponse.success(jwt));
       } catch (Exception e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(AuthResponse.failure("Invalid email or password"));
       }
    }

    // Returns the full use JWT that expires after 24 hours
    @PostMapping("/verify-email")
    public ResponseEntity<AuthResponse> verifyEmail(@RequestBody VerificationRequest request) {
        try {
            AuthResult auth = _authService.verifyEmail(request.verificationToken, request.verificationCode);

            String jwt = _jwtService.generate(auth.getUserId(), auth.getEmail());
            return ResponseEntity.ok(AuthResponse.success(jwt));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(AuthResponse.failure("Email can not be verified"));
        }
    }

    // Need to take an email, issue temp token, send verification code email, return temp token
    @PostMapping("/request-password-reset")
    public ResponseEntity<TokenResponse> requestPasswordReset(@RequestBody String email) {
        try{
            SignupResponse response = _authService.requestPasswordReset(email);
           
            return ResponseEntity.ok(TokenResponse.success(response.getVerificationToken()));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(TokenResponse.failure("Error resetting password"));
        }
    }

    // TODO: Fix response type, don' love it
    // Takes in the code and token, verifies them, resets password
    @PostMapping("/confirm-reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody PasswordResetRequest request) {
        try{
            AuthResult auth = _authService.resetPassword(request.getVerificationToken(), request.getVerificationCode(), request.getNewPassword());

            return ResponseEntity.ok("Password reset successful");
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("Password reset unsuccessful");
        }
    }
}
