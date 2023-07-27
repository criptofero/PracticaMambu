package com.sofka.practicaMambu.domain.activeProducts.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.sofka.practicaMambu.domain.dto.ClientCreateResponseDTO;
import com.sofka.practicaMambu.domain.dto.MambuErrorResponse;
import com.sofka.practicaMambu.domain.model.contracts.MambuResponse;
import org.springframework.http.HttpStatusCode;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoanClientOnboardingResponse implements MambuResponse {
    private ClientCreateResponseDTO clientInfo;
    private LoanAccountResponse loanAccountInfo;
    private MambuErrorResponse[] errors;

    public ClientCreateResponseDTO getClientInfo() {
        return clientInfo;
    }

    public void setClientInfo(ClientCreateResponseDTO clientInfo) {
        this.clientInfo = clientInfo;
    }

    public LoanAccountResponse getLoanAccountInfo() {
        return loanAccountInfo;
    }

    public void setLoanAccountInfo(LoanAccountResponse loanAccountInfo) {
        this.loanAccountInfo = loanAccountInfo;
    }

    @JsonIgnore
    private HttpStatusCode statusCode;

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
