package com.sofka.practicaMambu.domain.dto;

public class CreateSeizureCommand {
    private Long amount;
    private String blockId;
    private String externalId;
    private String notes;
    private String transactionChannelId;

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public String getBlockId() {
        return blockId;
    }

    public void setBlockId(String blockId) {
        this.blockId = blockId;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getTransactionChannelId() {
        return transactionChannelId;
    }

    public void setTransactionChannelId(String transactionChannelId) {
        this.transactionChannelId = transactionChannelId;
    }
}
