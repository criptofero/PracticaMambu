package com.sofka.practicaMambu.domain.activeProducts.dto;

public class LoanProductResponse {
    private String encodedKey;

    private String id;
    private String name;

    private AmountSettings loanAmountSettings;

    private ScheduleSettings scheduleSettings;

    public ScheduleSettings getScheduleSettings() {
        return scheduleSettings;
    }

    public void setScheduleSettings(ScheduleSettings scheduleSettings) {
        this.scheduleSettings = scheduleSettings;
    }

    public AmountSettings getLoanAmountSettings() {
        return loanAmountSettings;
    }

    public void setLoanAmountSettings(AmountSettings loanAmountSettings) {
        this.loanAmountSettings = loanAmountSettings;
    }

    public String getEncodedKey() {
        return encodedKey;
    }

    public void setEncodedKey(String encodedKey) {
        this.encodedKey = encodedKey;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    private String type;
}
