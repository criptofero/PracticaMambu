package com.sofka.practicaMambu.domain.activeProducts.dto;

import com.sofka.practicaMambu.domain.model.activeProducts.InstallmentBalanceInfo;

public class LoanInstallmentInfo {
    private String encodedKey;
    private String parentAccountKey;
    private int number;
    private String dueDate;
    private String lastPaidDate;
    private String repaidDate;
    private String state;
    private InstallmentBalanceInfo principal;
    private InstallmentBalanceInfo interest;
    private InstallmentBalanceInfo penalty;

    public String getEncodedKey() {
        return encodedKey;
    }

    public void setEncodedKey(String encodedKey) {
        this.encodedKey = encodedKey;
    }

    public String getParentAccountKey() {
        return parentAccountKey;
    }

    public void setParentAccountKey(String parentAccountKey) {
        this.parentAccountKey = parentAccountKey;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getLastPaidDate() {
        return lastPaidDate;
    }

    public void setLastPaidDate(String lastPaidDate) {
        this.lastPaidDate = lastPaidDate;
    }

    public String getRepaidDate() {
        return repaidDate;
    }

    public void setRepaidDate(String repaidDate) {
        this.repaidDate = repaidDate;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public InstallmentBalanceInfo getPrincipal() {
        return principal;
    }

    public void setPrincipal(InstallmentBalanceInfo principal) {
        this.principal = principal;
    }

    public InstallmentBalanceInfo getInterest() {
        return interest;
    }

    public void setInterest(InstallmentBalanceInfo interest) {
        this.interest = interest;
    }

    public InstallmentBalanceInfo getPenalty() {
        return penalty;
    }

    public void setPenalty(InstallmentBalanceInfo penalty) {
        this.penalty = penalty;
    }
}
