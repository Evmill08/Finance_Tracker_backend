package com.example.finance_tracker.Models;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="email_verifications")
public class EmailVerification {
    
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long Id;

    @Column(unique=true, nullable=false)
    private String verificationToken;

    @Column(nullable=false)
    private String verificationCode;

    @Column(nullable=false)
    private Instant expiresAt;

    @OneToOne
    @JoinColumn(name="user_id", nullable=false)
    private User user;

    public String getVerificationToken(){
        return this.verificationToken;
    }

    public String getVerificationCode(){
        return this.verificationCode;
    }

    public Instant getExpiresAt(){
        return this.expiresAt;
    }

    public User getVerificationUser(){
        return this.user;
    }
    
    public void setVerificationToken(String verificationToken){
        this.verificationToken = verificationToken;
    }

    public void setVerificationCode(String verificationCode){
        this.verificationCode = verificationCode;
    }

    public void setExpiresAt(Instant expiresAt){
        this.expiresAt = expiresAt;
    }

    public void setVerificationUser(User user){
        this.user = user;
    }

    public EmailVerification(String verificationToken, String verificationCode, Instant expiresAt, User user){
        this.verificationToken = verificationToken;
        this.verificationCode = verificationCode;
        this.expiresAt = expiresAt;
        this.user = user;
    }
}
