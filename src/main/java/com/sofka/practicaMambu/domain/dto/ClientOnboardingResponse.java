package com.sofka.practicaMambu.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.HttpStatusCode;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClientOnboardingResponse {
    private ClientCreateResponseDTO clientInfo;
    private CreateDepositAccountResponse accountInfo;

    private MambuErrorResponse[] errors;

    @JsonIgnore
    private HttpStatusCode statusCode;

    public ClientCreateResponseDTO getClientInfo() {
        return clientInfo;
    }

    public void setClientInfo(ClientCreateResponseDTO clientInfo) {
        this.clientInfo = clientInfo;
    }

    public CreateDepositAccountResponse getAccountInfo() {
        return accountInfo;
    }

    public void setAccountInfo(CreateDepositAccountResponse accountInfo) {
        this.accountInfo = accountInfo;
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
