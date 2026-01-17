package com.example.finance_tracker.Utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.finance_tracker.Models.AuthResponse;
import com.example.finance_tracker.Utils.AuthExceptions.InvalidCredentialsException;
import com.example.finance_tracker.Utils.AuthExceptions.TokenExpiredException;
import com.example.finance_tracker.Utils.AuthExceptions.ValidationException;


@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<AuthResponse> handleInvalidCredentials(Exception e){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body(AuthResponse.failure(e.getMessage()));
    }

    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<AuthResponse> handleTokenExpiredCredentials(Exception e){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body(AuthResponse.failure(e.getMessage()));
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<AuthResponse> handleValidationCredentials(Exception e){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body(AuthResponse.failure(e.getMessage()));
    }
}
