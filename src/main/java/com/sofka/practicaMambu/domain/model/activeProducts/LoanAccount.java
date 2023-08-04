package com.sofka.practicaMambu.domain.model.activeProducts;

public class LoanAccount {
    private Long loanAmount;
    private String accountHolderKey;
    private String accountHolderType;
    private String productTypeKey;
    private InterestSettings interestSettings;
    private LoanScheduleSettings scheduleSettings;

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

    public String getAccountHolderType() {
        return accountHolderType;
    }

    public void setAccountHolderType(String accountHolderType) {
        this.accountHolderType = accountHolderType;
    }

    public String getProductTypeKey() {
        return productTypeKey;
    }

    public void setProductTypeKey(String productTypeKey) {
        this.productTypeKey = productTypeKey;
    }

    public InterestSettings getInterestSettings() {
        return interestSettings;
    }

    public void setInterestSettings(InterestSettings interestSettings) {
        this.interestSettings = interestSettings;
    }

    public LoanScheduleSettings getScheduleSettings() {
        return scheduleSettings;
    }

    public void setScheduleSettings(LoanScheduleSettings scheduleSettings) {
        this.scheduleSettings = scheduleSettings;
    }
}
