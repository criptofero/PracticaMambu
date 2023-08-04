package com.sofka.practicaMambu.domain.dto;

import com.sofka.practicaMambu.domain.model.Client;
import com.sofka.practicaMambu.domain.model.DepositAccount;

public class DepositClientOnboardingCommand {
    private Client clientInfo;
    private DepositAccount accountInfo;

    public Client getClientInfo() {
        return clientInfo;
    }

    public void setClientInfo(Client clientInfo) {
        this.clientInfo = clientInfo;
    }

    public DepositAccount getAccountInfo() {
        return accountInfo;
    }

    public void setAccountInfo(DepositAccount accountInfo) {
        this.accountInfo = accountInfo;
    }
}
