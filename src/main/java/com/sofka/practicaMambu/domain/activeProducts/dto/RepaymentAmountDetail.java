package com.sofka.practicaMambu.domain.activeProducts.dto;

import java.math.BigDecimal;

public class RepaymentAmountDetail {
    private BigDecimal principalAmount;
    private BigDecimal interestAmount;

    public BigDecimal getPrincipalAmount() {
        return principalAmount;
    }

    public void setPrincipalAmount(BigDecimal principalAmount) {
        this.principalAmount = principalAmount;
    }

    public BigDecimal getInterestAmount() {
        return interestAmount;
    }

    public void setInterestAmount(BigDecimal interestAmount) {
        this.interestAmount = interestAmount;
    }
}
