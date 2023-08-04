package com.sofka.practicaMambu.domain.activeProducts.dto;

import com.sofka.practicaMambu.domain.model.Client;

public class LoanClientOnboardingCommand {
    private Client clientInfo;
    private CreateLoanAccountCommand loanAccountInfo;

    public Client getClientInfo() {
        return clientInfo;
    }

    public void setClientInfo(Client clientInfo) {
        this.clientInfo = clientInfo;
    }

    public CreateLoanAccountCommand getLoanAccountInfo() {
        return loanAccountInfo;
    }

    public void setLoanAccountInfo(CreateLoanAccountCommand loanAccountInfo) {
        this.loanAccountInfo = loanAccountInfo;
    }
}
