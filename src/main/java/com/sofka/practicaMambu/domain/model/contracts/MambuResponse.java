package com.sofka.practicaMambu.domain.model.contracts;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sofka.practicaMambu.domain.dto.MambuErrorResponse;
import org.springframework.http.HttpStatusCode;

public interface MambuResponse {
    MambuErrorResponse[] getErrors();
    void setErrors(MambuErrorResponse[] errors);
    HttpStatusCode getStatusCode();
    void setStatusCode(HttpStatusCode statusCode);
}
