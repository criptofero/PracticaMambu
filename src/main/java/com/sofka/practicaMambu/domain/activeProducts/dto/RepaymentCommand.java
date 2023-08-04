package com.sofka.practicaMambu.domain.activeProducts.dto;

public class RepaymentCommand {
    private Long amount;
    private String notes;

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
