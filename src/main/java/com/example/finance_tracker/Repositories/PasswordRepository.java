package com.example.finance_tracker.Repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.finance_tracker.Models.Verification.PasswordReset;

@Repository
public interface PasswordRepository extends JpaRepository<PasswordReset, Long>{
    Optional<PasswordReset> findByUserId(Long id);

    Optional<PasswordReset> findByResetToken(String resetToken);
}
