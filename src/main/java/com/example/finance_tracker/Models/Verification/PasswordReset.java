package com.example.finance_tracker.Models.Verification;

import java.time.Instant;

import com.example.finance_tracker.Models.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="password_resets")
public class PasswordReset {
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(unique=true, nullable=false)
    private String resetToken;

    @Column(nullable=false)
    private String resetCode;

    @Column(nullable=false)
    private Instant expiresAt;

    @OneToOne
    @JoinColumn(name="user_id", nullable=false, unique=true)
    private User user;

    public String getResetToken(){
        return this.resetToken;
    }

    public Instant getExpiresAt(){
        return this.expiresAt;
    }

    public String getResetCode(){
        return this.resetCode;
    }

    public User getUser(){
        return this.user;
    }

    public void setResetToken(String resetToken){
        this.resetToken = resetToken;
    }

    public void setExpiresAt(Instant expiresAt){
        this.expiresAt = expiresAt;
    }

    public void setResetCode(String resetCode){
        this.resetCode = resetCode;
    }

    public void setUser(User user){
        this.user = user;
    }

    public PasswordReset(String resetToken, String resetCode, Instant expiresAt, User user){
        this.resetToken = resetToken;
        this.resetCode = resetCode;
        this.expiresAt = expiresAt;
        this.user = user;
    }
    
    public PasswordReset() {}
}
