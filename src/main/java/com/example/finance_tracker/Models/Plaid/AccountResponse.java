package com.example.finance_tracker.Models.Plaid;

import com.example.finance_tracker.Models.User.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="user_accounts")
public class AccountResponse {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private User user;

    @Column(nullable=false, unique=true)
    private String accountId;

    @Column(nullable=false)
    private Double currentBalance;

    @Column(nullable=false)
    private String mask;

    @Column(nullable=false)
    private String name;

    @Column(nullable=false)
    private String officialName;

    @Column(nullable=false)
    private String type;

    @Column(nullable=true)
    private String subtype;

    public AccountResponse(User user, String accountId, Double currentBalance, String mask, String name, String officialName, String type, String subtype){
        this.user = user;
        this.accountId = accountId;
        this.currentBalance = currentBalance;
        this.mask = mask;
        this.name = name;
        this.officialName = officialName;
        this.type = type;
        this.subtype = subtype;
    }

    public String getAccountId() {
        return accountId;
    }

    public Double getCurrentBalance() {
        return currentBalance;
    }

    public String getName() {
        return name;
    }

    public String getOfficialName() {
        return officialName;
    }

    public String getType() {
        return type;
    }

    public String getSubtype() {
        return subtype;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public void setCurrentBalance(Double currentBalance) {
        this.currentBalance = currentBalance;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setOfficialName(String officialName) {
        this.officialName = officialName;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setSubtype(String subtype) {
        this.subtype = subtype;
    }
}
