package com.example.finance_tracker.Models;
import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="users")
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(unique=true, nullable=false)
    private String email;

    @Column(nullable=false)
    private String passwordHash;

    @Column(nullable=false)
    private boolean emailVerified;

    @Column(nullable=true)
    private String emailVerificationToken;

    @Column(nullable=true)
    private Instant emailVerificationExpiresAt;

    @Column(nullable=true)
    private String passwordResetToken;

    @Column(nullable=true)
    private Instant passwordResetTokenExpiresAt;

    public Long getId()
    {
        return this.Id;
    }

    public String getEmail(){
        return this.email;
    }

    public String getPasswordHash(){
        return this.passwordHash;
    }

    public boolean getEmailVerified(){
        return this.emailVerified;
    }

    public String getEmailVerificationToken(){
        return this.emailVerificationToken;
    }

    public Instant getEmailVerificationExpiresAt(){
        return this.emailVerificationExpiresAt;
    }

    public String getPasswordResetToken(){
        return this.passwordResetToken;
    }

    public Instant getPasswordResetTokenExpiresAt(){
        return this.passwordResetTokenExpiresAt;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public void setPassword(String hashedPassword){
        this.passwordHash = hashedPassword;
    }

    public void setEmailVerified(boolean emailVerified){
        this.emailVerified = emailVerified;
    }

    public void setEmailVerificationToken(String emailVerificationToken){
        this.emailVerificationToken = emailVerificationToken;
    }

    public void setEmailVerificationTokenExpiresAt(Instant emailVerificationTokenExpiresAt){
        this.emailVerificationExpiresAt = emailVerificationTokenExpiresAt;
    }

    public void setPasswordResetToken(String passwordResetToken){
        this.passwordResetToken = passwordResetToken;
    }
    
    public void setResetTokenExpiry(Instant passwordResetTokenExpiresAt){
        this.passwordResetTokenExpiresAt = passwordResetTokenExpiresAt;
    }
}
