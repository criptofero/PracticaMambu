package com.sofka.practicaMambu.domain.activeProducts.dto;

public class LoanRefinanceCommand {
    private LoanRefinanceDetail loanAccount;
    private Long topUpAmount;

    public LoanRefinanceDetail getLoanAccount() {
        return loanAccount;
    }

    public void setLoanAccount(LoanRefinanceDetail loanAccount) {
        this.loanAccount = loanAccount;
    }

    public Long getTopUpAmount() {
        return topUpAmount;
    }

    public void setTopUpAmount(Long topUpAmount) {
        this.topUpAmount = topUpAmount;
    }
}
