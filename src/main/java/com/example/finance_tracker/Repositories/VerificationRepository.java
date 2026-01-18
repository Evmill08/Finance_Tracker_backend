package com.example.finance_tracker.Repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.finance_tracker.Models.Email.EmailVerification;

@Repository
public interface VerificationRepository extends JpaRepository<EmailVerification, Long>{

    Optional<EmailVerification> findByUserId(Long userId);

    Optional<EmailVerification> findByVerificationToken(String verificationToken);
}
