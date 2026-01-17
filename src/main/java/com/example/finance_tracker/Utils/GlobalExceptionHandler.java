package com.example.finance_tracker.Utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.finance_tracker.Models.Auth.AuthApiResponse;
import com.example.finance_tracker.Utils.AuthExceptions.InvalidCredentialsException;
import com.example.finance_tracker.Utils.AuthExceptions.TokenExpiredException;
import com.example.finance_tracker.Utils.AuthExceptions.ValidationException;


@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<AuthApiResponse<String>> handleInvalidCredentials(Exception e){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body(AuthApiResponse.failure(e.getMessage()));
    }

    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<AuthApiResponse<String>> handleTokenExpiredCredentials(Exception e){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body(AuthApiResponse.failure(e.getMessage()));
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<AuthApiResponse<String>> handleValidationCredentials(Exception e){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body(AuthApiResponse.failure(e.getMessage()));
    }
}
