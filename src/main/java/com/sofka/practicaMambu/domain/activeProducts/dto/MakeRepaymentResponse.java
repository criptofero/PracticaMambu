package com.sofka.practicaMambu.domain.activeProducts.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.sofka.practicaMambu.domain.dto.MambuErrorResponse;
import com.sofka.practicaMambu.domain.model.contracts.MambuResponse;
import org.springframework.http.HttpStatusCode;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class MakeRepaymentResponse implements MambuResponse {
    private String encodedKey;
    private String id;
    private String creationDate;
    private String parentAccountKey;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private Long amount;
    private RepaymentAmountDetail affectedAmounts;
    private LoanBalanceInfo accountBalances;

    private MambuErrorResponse[] errors;

    @JsonIgnore
    private HttpStatusCode statusCode;

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

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getParentAccountKey() {
        return parentAccountKey;
    }

    public void setParentAccountKey(String parentAccountKey) {
        this.parentAccountKey = parentAccountKey;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public RepaymentAmountDetail getAffectedAmounts() {
        return affectedAmounts;
    }

    public void setAffectedAmounts(RepaymentAmountDetail affectedAmounts) {
        this.affectedAmounts = affectedAmounts;
    }

    public LoanBalanceInfo getAccountBalances() {
        return accountBalances;
    }

    public void setAccountBalances(LoanBalanceInfo accountBalances) {
        this.accountBalances = accountBalances;
    }

    @Override
    public MambuErrorResponse[] getErrors() {
        return errors;
    }

    @Override
    public void setErrors(MambuErrorResponse[] errors) {
        this.errors = errors;
    }

    @Override
    public HttpStatusCode getStatusCode() {
        return statusCode;
    }

    @Override
    public void setStatusCode(HttpStatusCode statusCode) {
        this.statusCode = statusCode;
    }
}
