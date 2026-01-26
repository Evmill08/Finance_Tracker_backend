package com.example.finance_tracker.Models.Plaid;

public class TokenExchangeResponse {
    private String accessToken;
    private String itemId;

    public String getAccessToken(){
        return this.accessToken;
    }

    public String getItemId(){
        return this.itemId;
    }

    public void setAccessToken(String accessToken){
        this.accessToken = accessToken;
    }

    public void setItemId(String itemId){
        this.itemId = itemId;
    }

    public TokenExchangeResponse(String accessToken, String itemId){
        this.accessToken = accessToken;
        this.itemId = itemId;
    }
}
