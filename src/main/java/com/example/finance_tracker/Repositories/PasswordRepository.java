package com.example.finance_tracker.Repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.finance_tracker.Models.Verification.PasswordReset;

public interface PasswordRepository extends JpaRepository<PasswordReset, Long>{
    Optional<PasswordReset> findByResetToken(String resetToken);
}
