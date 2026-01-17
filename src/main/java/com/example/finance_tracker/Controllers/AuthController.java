package com.example.finance_tracker.Controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.finance_tracker.Models.AuthApiResponse;
import com.example.finance_tracker.Models.AuthRequest;
import com.example.finance_tracker.Models.AuthResult;
import com.example.finance_tracker.Models.PasswordResetRequest;
import com.example.finance_tracker.Models.TokenResult;
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

    @PostMapping("/signup")
    public ResponseEntity<AuthApiResponse<String>> signup(@RequestBody AuthRequest request){
        TokenResult result = _authService.signup(request.email, request.password);
        return ResponseEntity.ok(AuthApiResponse.success(result.getToken()));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthApiResponse<String>> login(@RequestBody AuthRequest request){
        AuthResult auth = _authService.login(request.email, request.password);
        String jwt = _jwtService.generate(auth.getUserId(), auth.getEmail()); 
        return ResponseEntity.ok(AuthApiResponse.success(jwt));
    }

    @PostMapping("/verify-email")
    public ResponseEntity<AuthApiResponse<String>> verifyEmail(@RequestBody VerificationRequest request) {
        AuthResult auth = _authService.verifyEmail(request.verificationToken, request.verificationCode);
        String jwt = _jwtService.generate(auth.getUserId(), auth.getEmail());
        return ResponseEntity.ok(AuthApiResponse.success(jwt));
    }

    @PostMapping("/request-password-reset")
    public ResponseEntity<AuthApiResponse<String>> requestPasswordReset(@RequestBody String email) {
        TokenResult result = _authService.requestPasswordReset(email);
        return ResponseEntity.ok(AuthApiResponse.success(result.getToken()));
    }

    @PostMapping("/confirm-reset-password")
    public ResponseEntity<AuthApiResponse<Object>> resetPassword(@RequestBody PasswordResetRequest request) {
        _authService.resetPassword(request.getVerificationToken(), request.getVerificationCode(), request.getNewPassword());
        return ResponseEntity.ok(AuthApiResponse.success(null));        
    }
}
