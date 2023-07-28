package com.sofka.practicaMambu.domain.activeProducts.dto;

import com.sofka.practicaMambu.domain.model.activeProducts.InterestSettings;
import com.sofka.practicaMambu.domain.model.activeProducts.LoanScheduleSettings;

public class LoanAccountQueryResponse {

    private String encodedKey;
    private String id;
    private String accountHolderType;
    private String accountHolderKey;
    private String creationDate;
    private String approvedDate;
    private String lastModifiedDate;
    private String lastLockedDate;
    private String accountState;
    private String accountSubState;
    private String productTypeKey;
    private Long loanAmount;
    private LoanBalanceInfo balances;
    private InterestSettings interestSettings;
    private LoanScheduleSettings scheduleSettings;

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

    public String getAccountHolderType() {
        return accountHolderType;
    }

    public void setAccountHolderType(String accountHolderType) {
        this.accountHolderType = accountHolderType;
    }

    public String getAccountHolderKey() {
        return accountHolderKey;
    }

    public void setAccountHolderKey(String accountHolderKey) {
        this.accountHolderKey = accountHolderKey;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getApprovedDate() {
        return approvedDate;
    }

    public void setApprovedDate(String approvedDate) {
        this.approvedDate = approvedDate;
    }

    public String getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(String lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getLastLockedDate() {
        return lastLockedDate;
    }

    public void setLastLockedDate(String lastLockedDate) {
        this.lastLockedDate = lastLockedDate;
    }

    public String getAccountState() {
        return accountState;
    }

    public void setAccountState(String accountState) {
        this.accountState = accountState;
    }

    public String getAccountSubState() {
        return accountSubState;
    }

    public void setAccountSubState(String accountSubState) {
        this.accountSubState = accountSubState;
    }

    public String getProductTypeKey() {
        return productTypeKey;
    }

    public void setProductTypeKey(String productTypeKey) {
        this.productTypeKey = productTypeKey;
    }

    public Long getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(Long loanAmount) {
        this.loanAmount = loanAmount;
    }

    public LoanBalanceInfo getBalances() {
        return balances;
    }

    public void setBalances(LoanBalanceInfo balances) {
        this.balances = balances;
    }

    public InterestSettings getInterestSettings() {
        return interestSettings;
    }

    public void setInterestSettings(InterestSettings interestSettings) {
        this.interestSettings = interestSettings;
    }

    public LoanScheduleSettings getScheduleSettings() {
        return scheduleSettings;
    }

    public void setScheduleSettings(LoanScheduleSettings scheduleSettings) {
        this.scheduleSettings = scheduleSettings;
    }
}
