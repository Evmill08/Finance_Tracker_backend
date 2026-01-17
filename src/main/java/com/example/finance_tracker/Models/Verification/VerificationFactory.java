package com.example.finance_tracker.Models.Verification;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

public class VerificationFactory {
    
    public static String generateToken(){
        return UUID.randomUUID().toString();
    }

    public static String generateCode(){
        return String.format("%06d", new SecureRandom().nextInt(1_000_000));
    }

    public static Instant generateExpiration(int amountToAdd, ChronoUnit unit){
        return Instant.now().plus(amountToAdd, unit);
    }
}
