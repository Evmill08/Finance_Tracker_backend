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

    public void setEmail(String email){
        this.email = email;
    }

    public void setPassword(String hashedPassword){
        this.passwordHash = hashedPassword;
    }

    public void setEmailVerified(boolean emailVerified){
        this.emailVerified = emailVerified;
    }
}
