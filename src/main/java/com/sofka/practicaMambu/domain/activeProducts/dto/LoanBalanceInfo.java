package com.sofka.practicaMambu.domain.activeProducts.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class LoanBalanceInfo {
    private BigDecimal redrawBalance;
    private BigDecimal principalDue;
    private BigDecimal principalPaid;
    private BigDecimal principalBalance;
    private BigDecimal interestPaid;
    private BigDecimal interestBalance;
    private BigDecimal totalBalance;

    public BigDecimal getRedrawBalance() {
        return redrawBalance;
    }

    public void setRedrawBalance(BigDecimal redrawBalance) {
        this.redrawBalance = redrawBalance;
    }

    public BigDecimal getPrincipalDue() {
        return principalDue;
    }

    public void setPrincipalDue(BigDecimal principalDue) {
        this.principalDue = principalDue;
    }

    public BigDecimal getPrincipalPaid() {
        return principalPaid;
    }

    public void setPrincipalPaid(BigDecimal principalPaid) {
        this.principalPaid = principalPaid;
    }

    public BigDecimal getPrincipalBalance() {
        return principalBalance;
    }

    public void setPrincipalBalance(BigDecimal principalBalance) {
        this.principalBalance = principalBalance;
    }

    public BigDecimal getInterestPaid() {
        return interestPaid;
    }

    public void setInterestPaid(BigDecimal interestPaid) {
        this.interestPaid = interestPaid;
    }

    public BigDecimal getInterestBalance() {
        return interestBalance;
    }

    public void setInterestBalance(BigDecimal interestBalance) {
        this.interestBalance = interestBalance;
    }

    public BigDecimal getTotalBalance() {
        return totalBalance;
    }

    public void setTotalBalance(BigDecimal totalBalance) {
        this.totalBalance = totalBalance;
    }
}
