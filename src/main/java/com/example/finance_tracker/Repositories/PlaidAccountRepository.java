package com.example.finance_tracker.Repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.finance_tracker.Models.Plaid.PlaidAccount;

@Repository
public interface PlaidAccountRepository extends JpaRepository<PlaidAccount, Long> {
    Optional<PlaidAccount> findByUserId(Long userId);
}
