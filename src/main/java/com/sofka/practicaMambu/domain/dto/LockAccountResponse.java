package com.sofka.practicaMambu.domain.dto;

import com.sofka.practicaMambu.domain.model.AccountBalanceInfo;

public class LockAccountResponse {
    private String encodedKey;
    private String id;
    private String name;
    private String accountHolderKey;
    private String accountType;
    private String activationDate;
    private String lockedDate;

    private AccountBalanceInfo balances;

    public AccountBalanceInfo getBalances() {
        return balances;
    }

    public void setBalances(AccountBalanceInfo balances) {
        this.balances = balances;
    }

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccountHolderKey() {
        return accountHolderKey;
    }

    public void setAccountHolderKey(String accountHolderKey) {
        this.accountHolderKey = accountHolderKey;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getActivationDate() {
        return activationDate;
    }

    public void setActivationDate(String activationDate) {
        this.activationDate = activationDate;
    }

    public String getLockedDate() {
        return lockedDate;
    }

    public void setLockedDate(String lockedDate) {
        this.lockedDate = lockedDate;
    }
}
