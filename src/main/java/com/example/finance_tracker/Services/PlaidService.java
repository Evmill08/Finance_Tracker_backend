package com.example.finance_tracker.Services;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.finance_tracker.Models.Plaid.AccountResponse;
import com.example.finance_tracker.Models.Plaid.LinkTokenResponse;
import com.example.finance_tracker.Models.Plaid.PlaidAccount;
import com.example.finance_tracker.Models.Plaid.TokenExchangeResponse;
import com.example.finance_tracker.Models.User.User;
import com.example.finance_tracker.Repositories.AccountRepository;
import com.example.finance_tracker.Repositories.PlaidAccountRepository;
import com.example.finance_tracker.Repositories.UserRepository;
import com.plaid.client.model.AccountsGetRequest;
import com.plaid.client.model.AccountsGetResponse;
import com.plaid.client.model.CountryCode;
import com.plaid.client.model.CreditAccountSubtype;
import com.plaid.client.model.CreditFilter;
import com.plaid.client.model.DepositoryAccountSubtype;
import com.plaid.client.model.DepositoryFilter;
import com.plaid.client.model.ItemPublicTokenExchangeRequest;
import com.plaid.client.model.ItemPublicTokenExchangeResponse;
import com.plaid.client.model.LinkTokenAccountFilters;
import com.plaid.client.model.LinkTokenCreateRequest;
import com.plaid.client.model.LinkTokenCreateRequestUser;
import com.plaid.client.model.LinkTokenCreateResponse;
import com.plaid.client.model.LinkTokenTransactions;
import com.plaid.client.model.Products;
import com.plaid.client.request.PlaidApi;

import retrofit2.Response;


// TODO: This will eventually be split up probably a lot (same with controller)
// For now, just keep everything in one spot 
@Service
public class PlaidService {

    private final PlaidApi _plaidApi;
    private final PlaidAccountRepository _plaidAccountRepository;
    private final UserRepository _userRepository;
    private final AccountRepository _accountRepository;

    public PlaidService(PlaidApi plaidApi, PlaidAccountRepository plaidAccountRepository, UserRepository userRepository, AccountRepository accountRepository){
        this._plaidApi = plaidApi;
        this._plaidAccountRepository = plaidAccountRepository;
        this._userRepository = userRepository;
        this._accountRepository = accountRepository;
    }
    
    // TODO: think about saving this or if we dont want to
    // Is this something that should have to be re-instantiated if something were to fail??
    public LinkTokenResponse createLinkToken(User user){
        LinkTokenCreateRequestUser plaidUser = new LinkTokenCreateRequestUser().clientUserId(user.getId().toString());

        // Don't need this, but this will be helpful to reference later
        LinkTokenTransactions transactions =  new LinkTokenTransactions()
            .daysRequested(730);

        DepositoryFilter depository =  new DepositoryFilter()
            .accountSubtypes(Arrays.asList(
                DepositoryAccountSubtype.CHECKING,
                DepositoryAccountSubtype.SAVINGS
            ));

        CreditFilter credit =  new CreditFilter()
            .accountSubtypes(Arrays.asList(CreditAccountSubtype.CREDIT_CARD));

        LinkTokenAccountFilters accountFilters =  new LinkTokenAccountFilters()
            .depository(depository)
            .credit(credit);

        LinkTokenCreateRequest request = new LinkTokenCreateRequest()
            .user(plaidUser)
            .clientName("Personal Finance App")
            .transactions(transactions)
            .products(Arrays.asList(Products.TRANSACTIONS))
            .countryCodes(Arrays.asList(CountryCode.US))
            .language("en")
            .accountFilters(accountFilters);

        try {
            Response<LinkTokenCreateResponse> response = _plaidApi.linkTokenCreate(request).execute();

            if (!response.isSuccessful()){
                throw new RuntimeException("Plaid link token failed");
            }

            LinkTokenCreateResponse body = response.body();
            return new LinkTokenResponse(body.getLinkToken(), body.getExpiration().toInstant());

        } catch (IOException e){
            throw new RuntimeException("Plaid link token creation failed", e);
        }
    }

    public TokenExchangeResponse exchangeToken(User user, String linkToken){
        ItemPublicTokenExchangeRequest request = new ItemPublicTokenExchangeRequest()
            .publicToken(linkToken);

        try {
            Response<ItemPublicTokenExchangeResponse> response = _plaidApi.itemPublicTokenExchange(request).execute();

            if (!response.isSuccessful()){
                throw new RuntimeException("Plaid token exchange was unsuccessful");
            }

            ItemPublicTokenExchangeResponse body = response.body();
            return new TokenExchangeResponse(body.getAccessToken(), body.getItemId()); 

        } catch (IOException e) {
            throw new RuntimeException("Plaid token exchange failed", e);
        }
    }

    public void savePlaidAccount(User user, String accessToken, String itemId){
        User storedUser = _userRepository.findById(user.getId())
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        PlaidAccount plaidAccount = new PlaidAccount(storedUser, accessToken, itemId);

        if (_plaidAccountRepository.findByUserId(user.getId()).isPresent()){
            throw new RuntimeException("Plaid account already exists for user");
        }

        _plaidAccountRepository.save(plaidAccount);
    }

    public List<AccountResponse> getAccounts(User user){
        User storedUser = _userRepository.findById(user.getId())
            .orElseThrow(() -> new RuntimeException("User not found"));

        PlaidAccount plaidAccount = _plaidAccountRepository.findByUserId(user.getId())
            .orElseThrow(() -> new RuntimeException("Plaid account not found for user"));

        AccountsGetRequest request = new AccountsGetRequest()
            .accessToken(plaidAccount.getAccessToken());

        try {
            Response<AccountsGetResponse> response = _plaidApi.accountsGet(request).execute();

            List<AccountResponse> accounts = response.body().getAccounts().stream()
                .map(account -> new AccountResponse(
                    storedUser,
                    account.getAccountId(),
                    account.getBalances().getCurrent(),
                    account.getMask(),
                    account.getName(),
                    account.getOfficialName(),
                    account.getType().getValue(),
                    account.getSubtype() != null ? account.getSubtype().getValue() : null
                )).toList();

            for (AccountResponse account : accounts){
                Optional<AccountResponse> existingAccount = _accountRepository.findByAccountId(account.getAccountId());

                if (existingAccount.isPresent()){
                    AccountResponse existing = existingAccount.get();
                    existing.setCurrentBalance(account.getCurrentBalance());
                    existing.setName(account.getName());
                    existing.setOfficialName(account.getOfficialName());
                    _accountRepository.save(existing);
                } else {
                    _accountRepository.save(account);
                }
            }

            return accounts;
        } catch (Exception e) {
            throw new RuntimeException("Failed to get accounts from Plaid", e);
        }
    }



    /*
        - Get the accounts: store each account linked to the user in the data base
        - Get the transactions for each account type: store each transaction linked to the account
        - Get the investments: 
    
    
    */
}
