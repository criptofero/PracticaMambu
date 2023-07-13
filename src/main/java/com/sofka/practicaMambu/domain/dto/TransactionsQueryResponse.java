package com.sofka.practicaMambu.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.sofka.practicaMambu.domain.model.DepositTransaction;
import org.springframework.http.HttpStatusCode;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransactionsQueryResponse {
    private DepositTransaction[] transactions;

    private MambuErrorResponse[] errors;

    @JsonIgnore
    private HttpStatusCode statusCode;

    public DepositTransaction[] getTransactions() {
        return transactions;
    }

    public void setTransactions(DepositTransaction[] transactions) {
        this.transactions = transactions;
    }

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
