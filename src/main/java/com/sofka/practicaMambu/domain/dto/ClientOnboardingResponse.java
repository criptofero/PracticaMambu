package com.sofka.practicaMambu.domain.dto;

public class ClientOnboardingResponse {
    private ClientCreateResponseDTO clientInfo;
    private CreateDepositAccountResponse accountInfo;

    public ClientCreateResponseDTO getClientInfo() {
        return clientInfo;
    }

    public void setClientInfo(ClientCreateResponseDTO clientInfo) {
        this.clientInfo = clientInfo;
    }

    public CreateDepositAccountResponse getAccountInfo() {
        return accountInfo;
    }

    public void setAccountInfo(CreateDepositAccountResponse accountInfo) {
        this.accountInfo = accountInfo;
    }
}
