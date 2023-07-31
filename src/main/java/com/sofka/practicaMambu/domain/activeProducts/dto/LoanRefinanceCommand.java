package com.sofka.practicaMambu.domain.activeProducts.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoanRefinanceCommand {
    private LoanRefinanceDetail loanAccount;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
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
