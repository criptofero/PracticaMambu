package com.sofka.practicaMambu.domain.activeProducts.dto;

public class ApproveLoanAccountCommand {
    private String action;
    private String notes;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
