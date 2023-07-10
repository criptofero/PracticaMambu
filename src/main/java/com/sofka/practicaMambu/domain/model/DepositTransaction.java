package com.sofka.practicaMambu.domain.model;

public class DepositTransaction {
    private Long amount;
    private String notes;
    private String paymentOrderId;
    private String externalId;
    private TransactionDetail transactionDetails;

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

    public String getPaymentOrderId() {
        return paymentOrderId;
    }

    public void setPaymentOrderId(String paymentOrderId) {
        this.paymentOrderId = paymentOrderId;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public TransactionDetail getTransactionDetails() {
        return transactionDetails;
    }

    public void setTransactionDetails(TransactionDetail transactionDetails) {
        this.transactionDetails = transactionDetails;
    }
}
