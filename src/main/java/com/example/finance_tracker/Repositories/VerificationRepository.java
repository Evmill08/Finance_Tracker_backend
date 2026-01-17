package com.example.finance_tracker.Repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.finance_tracker.Models.EmailVerification;

public interface VerificationRepository extends JpaRepository<EmailVerification, Long>{
    Optional<EmailVerification> findByVerificationToken(String verificationToken);
}
