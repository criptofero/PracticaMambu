package com.sofka.practicaMambu.domain.dto;

public class CreateBalanceBlockResponse {
    private Long amount;
    private String externalReferenceId;
    private String accountkey;
    private String state;
    private Long seizedAmount;
    private String creationDate;
    private String lastModifiedDate;
    private String notes;

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public String getExternalReferenceId() {
        return externalReferenceId;
    }

    public void setExternalReferenceId(String externalReferenceId) {
        this.externalReferenceId = externalReferenceId;
    }

    public String getAccountkey() {
        return accountkey;
    }

    public void setAccountkey(String accountkey) {
        this.accountkey = accountkey;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Long getSeizedAmount() {
        return seizedAmount;
    }

    public void setSeizedAmount(Long seizedAmount) {
        this.seizedAmount = seizedAmount;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(String lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
