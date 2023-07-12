package com.sofka.practicaMambu.domain.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MambuErrorResponse {
    private int errorCode;

    private String errorReason;

    private String errorSource;


    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorReason() {
        return errorReason;
    }

    public void setErrorReason(String errorReason) {
        this.errorReason = errorReason;
    }

    public String getErrorSource() {
        return errorSource;
    }

    public void setErrorSource(String errorSource) {
        this.errorSource = errorSource;
    }

    public static MambuErrorResponse[] fromJson(String jsonErrors){
        MambuErrorResponse[] errorResponse = null;
        if (jsonErrors != null && !jsonErrors.isEmpty()) {
            try {
                errorResponse = new ObjectMapper().readValue(jsonErrors, MambuErrorResponse[].class);
            } catch (JsonProcessingException e) {
                errorResponse = null;
            }
        }
        return errorResponse;
    }
}
