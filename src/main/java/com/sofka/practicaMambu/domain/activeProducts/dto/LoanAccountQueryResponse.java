package com.sofka.practicaMambu.domain.activeProducts.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.sofka.practicaMambu.domain.dto.MambuErrorResponse;
import com.sofka.practicaMambu.domain.model.activeProducts.InterestSettings;
import com.sofka.practicaMambu.domain.model.activeProducts.LoanScheduleSettings;
import com.sofka.practicaMambu.domain.model.contracts.MambuResponse;
import org.springframework.http.HttpStatusCode;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoanAccountQueryResponse implements MambuResponse {

    private String encodedKey;
    private String id;
    private String accountHolderType;
    private String accountHolderKey;
    private String creationDate;
    private String approvedDate;
    private String lastModifiedDate;
    private String lastLockedDate;
    private String accountState;
    private String accountSubState;
    private String productTypeKey;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private Long loanAmount;
    private LoanBalanceInfo balances;
    private InterestSettings interestSettings;
    private LoanScheduleSettings scheduleSettings;
    private String originalAccountKey;

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

    public String getAccountHolderType() {
        return accountHolderType;
    }

    public void setAccountHolderType(String accountHolderType) {
        this.accountHolderType = accountHolderType;
    }

    public String getAccountHolderKey() {
        return accountHolderKey;
    }

    public void setAccountHolderKey(String accountHolderKey) {
        this.accountHolderKey = accountHolderKey;
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

    public String getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(String lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getLastLockedDate() {
        return lastLockedDate;
    }

    public void setLastLockedDate(String lastLockedDate) {
        this.lastLockedDate = lastLockedDate;
    }

    public String getAccountState() {
        return accountState;
    }

    public void setAccountState(String accountState) {
        this.accountState = accountState;
    }

    public String getAccountSubState() {
        return accountSubState;
    }

    public void setAccountSubState(String accountSubState) {
        this.accountSubState = accountSubState;
    }

    public String getProductTypeKey() {
        return productTypeKey;
    }

    public void setProductTypeKey(String productTypeKey) {
        this.productTypeKey = productTypeKey;
    }

    public Long getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(Long loanAmount) {
        this.loanAmount = loanAmount;
    }

    public LoanBalanceInfo getBalances() {
        return balances;
    }

    public void setBalances(LoanBalanceInfo balances) {
        this.balances = balances;
    }

    public InterestSettings getInterestSettings() {
        return interestSettings;
    }

    public void setInterestSettings(InterestSettings interestSettings) {
        this.interestSettings = interestSettings;
    }

    public LoanScheduleSettings getScheduleSettings() {
        return scheduleSettings;
    }

    public void setScheduleSettings(LoanScheduleSettings scheduleSettings) {
        this.scheduleSettings = scheduleSettings;
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

    public String getOriginalAccountKey() {
        return originalAccountKey;
    }

    public void setOriginalAccountKey(String originalAccountKey) {
        this.originalAccountKey = originalAccountKey;
    }
}
