package com.sofka.practicaMambu.domain.activeProducts.dto;

public class CreateLoanAccountCommand {
    private Long loanAmount;
    private String accountHolderKey;
    private double interestRate;
    private int installmentsNumber;

    public Long getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(Long loanAmount) {
        this.loanAmount = loanAmount;
    }

    public String getAccountHolderKey() {
        return accountHolderKey;
    }

    public void setAccountHolderKey(String accountHolderKey) {
        this.accountHolderKey = accountHolderKey;
    }
    public double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }

    public int getInstallmentsNumber() {
        return installmentsNumber;
    }

    public void setInstallmentsNumber(int installmentsNumber) {
        this.installmentsNumber = installmentsNumber;
    }
}
