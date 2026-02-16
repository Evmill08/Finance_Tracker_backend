package com.example.finance_tracker.Models.Plaid;

import com.example.finance_tracker.Models.User.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="plaid_accounts")
public class PlaidAccount {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name="user_id", nullable=false, unique=true)
    private User user;

    @Column(nullable=false)
    private String accessToken;

    @Column(nullable=false)
    private String itemId;

    public PlaidAccount() {}

    public PlaidAccount(User user, String accessToken, String itemId) {
        this.user = user;
        this.accessToken = accessToken;
        this.itemId = itemId;
    }

    public User getUser(){
        return this.user;
    }

    public String getAccessToken(){
        return this.accessToken;
    }

    public String getItemId(){
        return this.itemId;
    }

    public void setUser(User user){
        this.user = user;
    }

    public void setAccessToken(String accessToken){
        this.accessToken = accessToken;
    }

    public void setItemId(String itemId){
        this.itemId = itemId;
    }
}
