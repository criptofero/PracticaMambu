package com.sofka.practicaMambu.domain.activeProducts.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.sofka.practicaMambu.domain.dto.MambuErrorResponse;
import com.sofka.practicaMambu.domain.model.contracts.MambuResponse;
import org.springframework.http.HttpStatusCode;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoanAccountResponse implements MambuResponse {
    private String encodedKey;
    private String id;
    private String loanName;
    private String accountHolderKey;
    private String accountState;
    private Long loanAmount;
    private String creationDate;
    private String approvedDate;

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

    public String getLoanName() {
        return loanName;
    }

    public void setLoanName(String loanName) {
        this.loanName = loanName;
    }

    public String getAccountHolderKey() {
        return accountHolderKey;
    }

    public void setAccountHolderKey(String accountHolderKey) {
        this.accountHolderKey = accountHolderKey;
    }

    public String getAccountState() {
        return accountState;
    }

    public void setAccountState(String accountState) {
        this.accountState = accountState;
    }

    public Long getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(Long loanAmount) {
        this.loanAmount = loanAmount;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getApprovedDate() {
        return approvedDate;
    }

    public void setApprovedDate(String approvedDate) {
        this.approvedDate = approvedDate;
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
