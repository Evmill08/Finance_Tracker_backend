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
    public ResponseEntity<AuthResponse> signup(@RequestBody AuthRequest request){
       try {
        AuthResult auth = _authService.signup(request.email, request.password);
        String jwt = _jwtService.generate(auth.getUserId(), auth.getEmail()); 

        return ResponseEntity.ok(AuthResponse.success(jwt));
       } catch (Exception e){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body(AuthResponse.failure("Invalid email or password"));
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
}
