package com.example.finance_tracker.Services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.finance_tracker.Models.Plaid.AccountResponse;
import com.example.finance_tracker.Models.Plaid.LinkTokenResponse;
import com.example.finance_tracker.Models.Plaid.Location;
import com.example.finance_tracker.Models.Plaid.PlaidAccount;
import com.example.finance_tracker.Models.Plaid.TokenExchangeResponse;
import com.example.finance_tracker.Models.Plaid.TransactionResponse;
import com.example.finance_tracker.Models.User.User;
import com.example.finance_tracker.Repositories.AccountRepository;
import com.example.finance_tracker.Repositories.PlaidAccountRepository;
import com.example.finance_tracker.Repositories.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    private static final String PLAID_CLIENT_ID = System.getenv("PLAID_CLIENT_ID");
    private static final String PLAID_SECRET = System.getenv("PLAID_SECRET");

    public PlaidService(
            PlaidApi plaidApi,
            PlaidAccountRepository plaidAccountRepository,
            UserRepository userRepository,
            AccountRepository accountRepository
    ) {
        this._plaidApi = plaidApi;
        this._plaidAccountRepository = plaidAccountRepository;
        this._userRepository = userRepository;
        this._accountRepository = accountRepository;
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
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

    public List<TransactionResponse> getTransactionsByAccount(User user, String accountId) {
        User storedUser = _userRepository.findById(user.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        AccountResponse account = _accountRepository.findByAccountId(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        PlaidAccount plaidAccount = _plaidAccountRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Plaid account not found for user"));

        List<TransactionResponse> transactions = new ArrayList<>();
        String cursor = null;
        boolean hasMore = true;

        while (hasMore) {
            try {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);

                Map<String, Object> body = new HashMap<>();
                body.put("client_id", PLAID_CLIENT_ID);
                body.put("secret", PLAID_SECRET);
                body.put("access_token", plaidAccount.getAccessToken());
                body.put("account_id", accountId);
                body.put("days_requested", 180);
                body.put("include_original_description", true);
                if (cursor != null) {
                    body.put("cursor", cursor);
                }

                HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

                ResponseEntity<String> response = restTemplate.postForEntity(
                        "https://sandbox.plaid.com/transactions/sync",
                        request,
                        String.class
                );

                JsonNode root = objectMapper.readTree(response.getBody());

                // Update cursor and has_more
                cursor = root.has("next_cursor") && !root.get("next_cursor").isNull() ? root.get("next_cursor").asText() : null;
                hasMore = root.get("has_more").asBoolean(false);

                // Map added transactions
                JsonNode addedArray = root.get("added");
                if (addedArray != null && addedArray.isArray()) {
                    for (JsonNode t : addedArray) {
                        // Filter by account_id
                        if (!accountId.equals(t.get("account_id").asText())) continue;

                        TransactionResponse tr = new TransactionResponse(
                                t.get("amount").asDouble(),
                                t.has("counterparties") && t.get("counterparties").isArray() && t.get("counterparties").size() > 0
                                        ? t.get("counterparties").get(0).get("name").asText(null) : null,
                                t.has("counterparties") && t.get("counterparties").isArray() && t.get("counterparties").size() > 0
                                        ? t.get("counterparties").get(0).get("logo_url").asText(null) : null,
                                t.has("counterparties") && t.get("counterparties").isArray() && t.get("counterparties").size() > 0
                                        ? t.get("counterparties").get(0).get("confidence_level").asText(null) : null,
                                new Date(t.has("date") ? java.time.LocalDate.parse(t.get("date").asText()).toEpochDay() * 24 * 60 * 60 * 1000 : 0),
                                t.has("pending") && !t.get("pending").isNull() ? t.get("pending").asBoolean() : false,
                                t.has("location") && !t.get("location").isNull() ? new Location(
                                        t.get("location").get("address").asText(null),
                                        t.get("location").get("city").asText(null),
                                        t.get("location").get("region").asText(null),
                                        t.get("location").get("postal_code").asText(null),
                                        t.get("location").get("country").asText(null)
                                ) : null,
                                t.has("name") ? t.get("name").asText(null) : null,
                                t.has("payment_channel") && !t.get("payment_channel").isNull() ? t.get("payment_channel").asText(null) : null,
                                t.has("personal_finance_category") && !t.get("personal_finance_category").isNull() && t.get("personal_finance_category").has("primary")
                                        ? t.get("personal_finance_category").get("primary").asText(null) : null,
                                t.has("transaction_id") ? t.get("transaction_id").asText(null) : null
                        );
                        transactions.add(tr);
                    }
                }

            } catch (IOException e) {
                throw new RuntimeException("Failed to parse Plaid transactions JSON", e);
            } catch (Exception e) {
                throw new RuntimeException("Failed to sync transactions from Plaid", e);
            }
        }

        return transactions;
    }

    /*
        - Get the accounts: store each account linked to the user in the data base
        - Get the transactions for each account type: store each transaction linked to the account
        - Get the investments: 
    
    
    */
}
