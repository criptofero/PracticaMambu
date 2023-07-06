package com.sofka.practicaMambu.domain.dto;

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
}
