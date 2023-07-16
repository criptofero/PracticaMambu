package com.sofka.practicaMambu.domain.model;

public class AccountBalanceInfo {
    private Long totalBalance;
    private Long overdraftAmount;
    private Long lockedBalance;
    private Long availableBalance;

    public Long getTotalBalance() {
        return totalBalance;
    }

    public void setTotalBalance(Long totalBalance) {
        this.totalBalance = totalBalance;
    }

    public Long getOverdraftAmount() {
        return overdraftAmount;
    }

    public void setOverdraftAmount(Long overdraftAmount) {
        this.overdraftAmount = overdraftAmount;
    }

    public Long getLockedBalance() {
        return lockedBalance;
    }

    public void setLockedBalance(Long lockedBalance) {
        this.lockedBalance = lockedBalance;
    }

    public Long getAvailableBalance() {
        return availableBalance;
    }

    public void setAvailableBalance(Long availableBalance) {
        this.availableBalance = availableBalance;
    }
}
