package com.example.finance_tracker.Services;

import java.io.IOException;
import java.util.Arrays;

import org.springframework.stereotype.Service;

import com.example.finance_tracker.Models.Plaid.LinkTokenResponse;
import com.example.finance_tracker.Models.User.User;
import com.plaid.client.model.CountryCode;
import com.plaid.client.model.CreditAccountSubtype;
import com.plaid.client.model.CreditFilter;
import com.plaid.client.model.DepositoryAccountSubtype;
import com.plaid.client.model.DepositoryFilter;
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

    public PlaidService(PlaidApi plaidApi){
        this._plaidApi = plaidApi;
    }
    
    public LinkTokenResponse createLinkToken(User user){
        LinkTokenCreateRequestUser plaidUser = new LinkTokenCreateRequestUser().clientUserId(user.getId().toString());

        // Don't need this, but this will be helpful later
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
}
