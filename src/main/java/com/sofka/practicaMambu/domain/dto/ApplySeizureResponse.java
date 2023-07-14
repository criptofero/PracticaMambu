package com.sofka.practicaMambu.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.sofka.practicaMambu.domain.model.TransactionDetail;
import org.springframework.http.HttpStatusCode;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApplySeizureResponse {
    private String encodedKey;
    private String id;
    private String externalId;
    private String creationDate;
    private String notes;
    private String parentAccountKey;
    private String type;
    private String currencyCode;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private Long amount;

    private String blockId;

    private TransactionDetail transactionDetails;

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

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getParentAccountKey() {
        return parentAccountKey;
    }

    public void setParentAccountKey(String parentAccountKey) {
        this.parentAccountKey = parentAccountKey;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

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

    public TransactionDetail getTransactionDetails() {
        return transactionDetails;
    }

    public void setTransactionDetails(TransactionDetail transactionDetails) {
        this.transactionDetails = transactionDetails;
    }

    private MambuErrorResponse[] errors;

    @JsonIgnore
    private HttpStatusCode statusCode;

    public MambuErrorResponse[] getErrors() {
        return errors;
    }

    public void setErrors(MambuErrorResponse[] errors) {
        this.errors = errors;
    }

    public HttpStatusCode getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(HttpStatusCode statusCode) {
        this.statusCode = statusCode;
    }
}
