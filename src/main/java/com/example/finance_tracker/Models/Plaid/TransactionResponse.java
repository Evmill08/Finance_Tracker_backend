package com.example.finance_tracker.Models.Plaid;

import java.util.Date;

public class TransactionResponse {
    private Double amount;
    private String merchantName;
    private String merchantLogo;
    private String confidenceLevel; // TODO: enum this
    private Date date;
    private boolean pending;
    private Location location;
    private String name;
    private String paymentChannel; // TODO: enum this
    private String purchasecategory; // TODO: enum this
    private String transactionId;

    public Double getAmount() {
        return amount;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public String getMerchantLogo() {
        return merchantLogo;
    }

    public String getConfidenceLevel() {
        return confidenceLevel;
    }

    public Date getDate() {
        return date;
    }

    public boolean isPending() {
        return pending;
    }

    public Location getLocation() {
        return location;
    }

    public String getName() {
        return name;
    }

    public String getPaymentChannel() {
        return paymentChannel;
    }

    public String getPurchasecategory() {
        return purchasecategory;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public void setMerchantLogo(String merchantLogo) {
        this.merchantLogo = merchantLogo;
    }

    public void setConfidenceLevel(String confidenceLevel) {
        this.confidenceLevel = confidenceLevel;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setPending(boolean pending) {
        this.pending = pending;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPaymentChannel(String paymentChannel) {
        this.paymentChannel = paymentChannel;
    }

    public void setPurchasecategory(String purchasecategory) {
        this.purchasecategory = purchasecategory;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public TransactionResponse(Double amount, String merchantName, String merchantLogo, String confidenceLevel, Date date, boolean pending, Location location, String name, String paymentChannel, String purchasecategory, String transactionId) {
        this.amount = amount;
        this.merchantName = merchantName;
        this.merchantLogo = merchantLogo;
        this.confidenceLevel = confidenceLevel;
        this.date = date;
        this.pending = pending;
        this.name = name;
        this.paymentChannel = paymentChannel;
        this.purchasecategory = purchasecategory;
        this.transactionId = transactionId;
    }
}
