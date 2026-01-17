package com.example.finance_tracker.Utils.AuthExceptions;


public class TokenExpiredException extends AuthException{
    public TokenExpiredException(String message){
        super(message);
    }
};
