package com.example.finance_tracker.Models.Plaid;

import java.time.Instant;
import com.fasterxml.jackson.annotation.JsonFormat;

public class LinkTokenResponse {
    private String linkToken;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Instant expiresAt;

    public String getLinkToken() {
        return this.linkToken;
    }
    public Instant getExpiresAt() {
        return this.expiresAt;
    }

    public LinkTokenResponse() {}
  
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
