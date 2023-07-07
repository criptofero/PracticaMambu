package com.sofka.practicaMambu.domain.seedWork;

import org.springframework.http.HttpHeaders;

import java.util.UUID;

public class MambuAPIHelper {
    public static void addAcceptHeader(HttpHeaders requestHeaders ){
        if (requestHeaders != null){
            requestHeaders.set("Accept", "application/vnd.mambu.v2+json");
        }
    }

    public static void addContentHeader(HttpHeaders requestHeaders ){
        if (requestHeaders != null){
            requestHeaders.set("Content-Type", "application/json");
        }
    }

    public static void addIdempotencyHeader(HttpHeaders requestHeaders, String requestPayload){
        if (requestHeaders != null){
            UUID requestKey = CommonUtils.generateNamedUUID(requestPayload);
            requestHeaders.set("Idempotency-Key", requestKey.toString());
        }
    }
}
