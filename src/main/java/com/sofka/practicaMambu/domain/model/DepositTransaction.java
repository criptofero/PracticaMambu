package com.sofka.practicaMambu.domain.model;

public class DepositTransaction {
    private Long amount;
    private String notes;
    private String paymentOrderId;
    private String externalId;
    private TransactionDetail transactionDetails;
    private String creationDate;
    private String type;
    private String currencyCode;

    private String id;
    private String userKey;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserKey() {
        return userKey;
    }

    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getPaymentOrderId() {
        return paymentOrderId;
    }

    public void setPaymentOrderId(String paymentOrderId) {
        this.paymentOrderId = paymentOrderId;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public TransactionDetail getTransactionDetails() {
        return transactionDetails;
    }

    public void setTransactionDetails(TransactionDetail transactionDetails) {
        this.transactionDetails = transactionDetails;
    }
}
