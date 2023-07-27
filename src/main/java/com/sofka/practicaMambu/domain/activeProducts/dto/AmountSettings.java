package com.sofka.practicaMambu.domain.activeProducts.dto;

public class AmountSettings {
    private NumericSettingsRange loanAmount;

    public NumericSettingsRange getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(NumericSettingsRange loanAmount) {
        this.loanAmount = loanAmount;
    }
}
