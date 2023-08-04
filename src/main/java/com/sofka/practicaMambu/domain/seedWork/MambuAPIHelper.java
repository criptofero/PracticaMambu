package com.sofka.practicaMambu.domain.seedWork;

import com.sofka.practicaMambu.domain.dto.LockAccountResponse;
import com.sofka.practicaMambu.domain.dto.MambuErrorResponse;
import com.sofka.practicaMambu.domain.model.contracts.MambuResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;

import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

public class MambuAPIHelper {
    public static void addAcceptHeader(HttpHeaders requestHeaders) {
        if (requestHeaders != null) {
            requestHeaders.set("Accept", "application/vnd.mambu.v2+json");
        }
    }

    public static void addContentHeader(HttpHeaders requestHeaders) {
        if (requestHeaders != null) {
            requestHeaders.set("Content-Type", "application/json");
        }
    }

    public static void addIdempotencyHeader(HttpHeaders requestHeaders, String requestPayload) {
        if (requestHeaders != null) {
            UUID requestKey = CommonUtils.generateNamedUUID(requestPayload);
            requestHeaders.set("Idempotency-Key", requestKey.toString());
            System.out.println("Payload:\n%s".formatted(requestPayload));
            System.out.println("Generated Idempotency-Key: %s".formatted(requestKey.toString()));
        }
    }

    public static void setResponseErrorInfo(MambuResponse mambuResponse, RestClientException e) {
        if (mambuResponse != null) {
            HttpStatusCode errorCode = MambuAPIHelper.getHttpStatusCode(e);
            MambuErrorResponse[] errorResponse = MambuAPIHelper.getMambuErrorResponses(e);
            mambuResponse.setStatusCode(errorCode);
            mambuResponse.setErrors(errorResponse);
        }
    }

    public static HttpStatusCode getHttpStatusCode(RestClientException e) {
        var errorCode = ((HttpStatusCodeException) e).getStatusCode();
        System.err.println(e);
        System.err.println("errorCode: %s".formatted(errorCode.toString()));
        System.err.println("value: %s".formatted(String.valueOf(errorCode.value())));
        System.err.println("isError: %s".formatted(String.valueOf(errorCode.isError())));
        System.err.println("is4xxClientError: %s".formatted(String.valueOf(errorCode.is4xxClientError())));
        System.err.println("is2xxSuccessful: %s".formatted(String.valueOf(errorCode.is2xxSuccessful())));
        return errorCode;
    }

    public static MambuErrorResponse[] getMambuErrorResponses(RestClientException e) {
        MambuErrorResponse[] errorResponse = null;
        String jsonError = e instanceof HttpStatusCodeException ?
                ((HttpStatusCodeException) e).getResponseBodyAsString()
                : "";
        if (!jsonError.isEmpty()) {
            jsonError = jsonError.substring(jsonError.indexOf("["), jsonError.indexOf("]") + 1);
            errorResponse = MambuErrorResponse.fromJson(jsonError);
        }
        return errorResponse;
    }

    public static MambuResponse handleErrorResponse(RestClientException e, Class responseType){
        MambuResponse responseInstance = null;
        try {
            responseInstance = (MambuResponse) responseType.getDeclaredConstructor().newInstance();
            HttpStatusCode errorCode = MambuAPIHelper.getHttpStatusCode(e);
            MambuErrorResponse[] errorResponse = MambuAPIHelper.getMambuErrorResponses(e);
            responseInstance.setStatusCode(errorCode);
            responseInstance.setErrors(errorResponse);
        } catch (Exception ex) {
            responseInstance = null;
        }
        return responseInstance;
    }
}
