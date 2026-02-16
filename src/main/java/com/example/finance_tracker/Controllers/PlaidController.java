package com.example.finance_tracker.Controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.finance_tracker.Models.Plaid.AccountResponse;
import com.example.finance_tracker.Models.Plaid.LinkTokenResponse;
import com.example.finance_tracker.Models.Plaid.PlaidApiResponse;
import com.example.finance_tracker.Models.Plaid.TokenExchangeResponse;
import com.example.finance_tracker.Models.Plaid.TokenRequest;
import com.example.finance_tracker.Models.User.User;
import com.example.finance_tracker.Services.PlaidService;
import com.example.finance_tracker.Services.UserService;

@RestController
@RequestMapping("plaid")
public class PlaidController {
    private final UserService _userService;
    private final PlaidService _plaidService;

    public PlaidController(UserService userService, PlaidService plaidService){
        this._userService = userService;
        this._plaidService = plaidService;
    }
    
    // Gets initial token to use for linkage
    @GetMapping("/create-link-token")
    public ResponseEntity<PlaidApiResponse<LinkTokenResponse>> getLinkToken(@AuthenticationPrincipal User userRequest) {
        User user = _userService.getUserById(userRequest.getId());
        LinkTokenResponse response = _plaidService.createLinkToken(user);
        return ResponseEntity.ok(PlaidApiResponse.success(response));
    }

    
    // Gets the accessToken to be used in all future requests, as well as itemId to start getting financial data
    @PostMapping("/exchange")
    public ResponseEntity<PlaidApiResponse<TokenExchangeResponse>> exchangeToken(@AuthenticationPrincipal User userRequest, @RequestBody TokenRequest request ) {
        User user = _userService.getUserById(userRequest.getId());
        TokenExchangeResponse response = _plaidService.exchangeToken(user, request.getToken());
        _userService.linkPlaid(user.getId());
        _plaidService.savePlaidAccount(user, response.getAccessToken(), response.getItemId());
        return ResponseEntity.ok(PlaidApiResponse.success(response));
    }

    @PostMapping("/accounts")
    public ResponseEntity<PlaidApiResponse<List<AccountResponse>>> getAccounts(@AuthenticationPrincipal User userRequest){
        User user = _userService.getUserById(userRequest.getId());
        List<AccountResponse> response = _plaidService.getAccounts(user);
        return ResponseEntity.ok(PlaidApiResponse.success(response));
    }

}
