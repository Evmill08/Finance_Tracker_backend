package com.example.finance_tracker.Controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.finance_tracker.Models.Plaid.AccountResponse;
import com.example.finance_tracker.Models.Plaid.PlaidApiResponse;
import com.example.finance_tracker.Models.Plaid.TransactionResponse;
import com.example.finance_tracker.Models.User.User;
import com.example.finance_tracker.Services.PlaidService;
import com.example.finance_tracker.Services.UserService;

@RestController
@RequestMapping("/accounts")
public class AccountController {
    private final UserService _userService;
    private final PlaidService _plaidService;

    public AccountController(UserService userService, PlaidService plaidService){
        this._userService = userService;
        this._plaidService = plaidService;
    }

    @PostMapping("")
    public ResponseEntity<PlaidApiResponse<List<AccountResponse>>> getAccounts(@AuthenticationPrincipal User userRequest){
        User user = _userService.getUserById(userRequest.getId());
        List<AccountResponse> response = _plaidService.getAccounts(user);
        return ResponseEntity.ok(PlaidApiResponse.success(response));
    }

    @PostMapping("/{accountId}/transactions")
    public ResponseEntity<PlaidApiResponse<List<TransactionResponse>>> getTransactionsByAccount(@AuthenticationPrincipal User userRequest, @PathVariable String accountId){
        User user = _userService.getUserById(userRequest.getId());
        List<TransactionResponse> response = _plaidService.getTransactionsByAccount(user, accountId);
        return ResponseEntity.ok(PlaidApiResponse.success(response));
    }
}
