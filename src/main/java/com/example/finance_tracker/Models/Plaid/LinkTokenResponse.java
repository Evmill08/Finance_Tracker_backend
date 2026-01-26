package com.example.finance_tracker.Models.Plaid;

import java.time.Instant;

public class LinkTokenResponse {
    private String linkToken;
    private Instant expiresAt;

    public String getLinkToken() {
        return this.linkToken;
    }
    public Instant getExpiresAt() {
        return this.expiresAt;
    }
  
    public void setLinkToken(String linkToken){
        this.linkToken = linkToken;
    }
    public void setExpiresAt(Instant expiresAt){
        this.expiresAt = expiresAt;
    }

    public LinkTokenResponse(String linkToken, Instant expiresAt){
        this.linkToken = linkToken;
        this.expiresAt = expiresAt;
    }
}
