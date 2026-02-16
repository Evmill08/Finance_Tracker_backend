package com.example.finance_tracker.Repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.finance_tracker.Models.Plaid.AccountResponse;

@Repository
public interface AccountRepository extends JpaRepository<AccountResponse, Long> {
    Optional<AccountResponse> findByUserId(Long userId);

    Optional<AccountResponse> findByAccountId(String accountId);
}
