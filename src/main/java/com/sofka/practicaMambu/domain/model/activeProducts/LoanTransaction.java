package com.sofka.practicaMambu.domain.model.activeProducts;

import com.sofka.practicaMambu.domain.activeProducts.dto.LoanBalanceInfo;
import com.sofka.practicaMambu.domain.model.TransactionDetail;

public class LoanTransaction {
    private String encodedKey;
    private String id;
    private String creationDate;
    private String notes;
    private String parentAccountKey;
    private String type;
    private Long amount;
    private TransactionDetail transactionDetails;

    private LoanBalanceInfo accountBalances;

    public String getEncodedKey() {
        return encodedKey;
    }

    public void setEncodedKey(String encodedKey) {
        this.encodedKey = encodedKey;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getParentAccountKey() {
        return parentAccountKey;
    }

    public void setParentAccountKey(String parentAccountKey) {
        this.parentAccountKey = parentAccountKey;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public TransactionDetail getTransactionDetails() {
        return transactionDetails;
    }

    public void setTransactionDetails(TransactionDetail transactionDetails) {
        this.transactionDetails = transactionDetails;
    }

    public LoanBalanceInfo getAccountBalances() {
        return accountBalances;
    }

    public void setAccountBalances(LoanBalanceInfo accountBalances) {
        this.accountBalances = accountBalances;
    }
}
