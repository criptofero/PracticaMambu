package com.sofka.practicaMambu.domain.activeProducts.dto;

public class LoanBalanceInfo {
    private Long redrawBalance;
    private Long principalDue;
    private Long principalPaid;
    private Long principalBalance;
    private Long interestPaid;
    private Long interestBalance;

    public Long getRedrawBalance() {
        return redrawBalance;
    }

    public void setRedrawBalance(Long redrawBalance) {
        this.redrawBalance = redrawBalance;
    }

    public Long getPrincipalDue() {
        return principalDue;
    }

    public void setPrincipalDue(Long principalDue) {
        this.principalDue = principalDue;
    }

    public Long getPrincipalPaid() {
        return principalPaid;
    }

    public void setPrincipalPaid(Long principalPaid) {
        this.principalPaid = principalPaid;
    }

    public Long getPrincipalBalance() {
        return principalBalance;
    }

    public void setPrincipalBalance(Long principalBalance) {
        this.principalBalance = principalBalance;
    }

    public Long getInterestPaid() {
        return interestPaid;
    }

    public void setInterestPaid(Long interestPaid) {
        this.interestPaid = interestPaid;
    }

    public Long getInterestBalance() {
        return interestBalance;
    }

    public void setInterestBalance(Long interestBalance) {
        this.interestBalance = interestBalance;
    }
}
