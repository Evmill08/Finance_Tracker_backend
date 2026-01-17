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
@Table(name="password_resets")
public class PasswordReset {
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long Id;

    @Column(unique=true, nullable=false)
    private String resetToken;

    @Column(nullable=false)
    private Instant expiresAt;

    @OneToOne
    @JoinColumn(name="user_id", nullable=false)
    private User user;
}
