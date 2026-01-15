package com.example.finance_tracker.Models;
import java.sql.Date;

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
    private String verificationToken;

    @Column(nullable=true)
    private String resetToken;

    @Column(nullable=true)
    private Date resetTokenExpiry;

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

    public String getVerificationToken(){
        return this.verificationToken;
    }

    public String getResetToken(){
        return this.resetToken;
    }

    public Date getResetTokenExpiry(){
        return this.resetTokenExpiry;
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

    public void setVerificationToken(String verificationToken){
        this.verificationToken = verificationToken;
    }

    public void setResetToken(String resetToken){
        this.resetToken = resetToken;
    }
    

    public void setResetTokenExpiry(Date resetTokenExpiry){
        this.resetTokenExpiry = resetTokenExpiry;
    }
}
