package com.sofka.practicaMambu.infraestructure;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sofka.practicaMambu.domain.dto.*;
import com.sofka.practicaMambu.domain.model.DepositAccount;
import com.sofka.practicaMambu.domain.model.DepositTransaction;
import com.sofka.practicaMambu.domain.model.TransactionDetail;
import com.sofka.practicaMambu.domain.model.TransactionFilterInfo;
import com.sofka.practicaMambu.domain.seedWork.MambuAPIHelper;
import com.sofka.practicaMambu.domain.service.DepositProductService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

public class DepositProductRepository implements DepositProductService {

    public static final String DEPOSIT_ACCOUNT_PRODUCT_ID = "1099003";
    public static final String DEPOSIT_ACCOUNT_DEFAULT_HOLDER_TYPE = "CLIENT";
    public static final String DEPOSIT_ACCOUNT_DEFAULT_STATE = "APPROVED";
    public static final String DEPOSIT_ACCOUNT_DEFAULT_CURRENCY = "COP";
    public static final String DEPOSIT_ACCOUNT_DEFAULT_TRAN_CHANNEL = "OnlineChannelLocales";

    @Value("${mambuAPI.rootUrl}")
    private String mambuAPIRootUrl;

    @Value("${mambuAPI.userName}")
    private String mambuAPIUserName;

    @Value("${mambuAPI.password}")
    private String mambuAPIPassword;

    @Override
    public DepositProductResponse getDepositProductById(String productId) {
        DepositProductResponse productResponse = new DepositProductResponse();
        String operationUrl = mambuAPIRootUrl.concat("/depositproducts/{productId}?offset=0&limit=1&paginationDetails=OFF&detailsLevel=FULL");
        ObjectMapper mapper = new ObjectMapper();
        ResponseEntity<DepositProductResponse> responseResult = null;
        String jsonBody;
        try {
            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.setBasicAuth(mambuAPIUserName, mambuAPIPassword);
            MambuAPIHelper.addAcceptHeader(requestHeaders);
            HttpEntity<String> httpEntity = new HttpEntity<>(null, requestHeaders);
            RestTemplate restTemplate = new RestTemplate();
            responseResult = restTemplate.exchange(operationUrl, HttpMethod.GET, httpEntity, DepositProductResponse.class, productId);
            productResponse = responseResult.getBody();
        } catch (Exception e) {
            System.err.println(e.toString());
            throw new RuntimeException(e);
        }
        return productResponse;
    }

    @Override
    public CreateDepositAccountResponse createAccount(DepositAccount account) {
        CreateDepositAccountResponse createResponse = new CreateDepositAccountResponse();
        String operationUrl = mambuAPIRootUrl.concat("/deposits");
        ObjectMapper mapper = new ObjectMapper();
        ResponseEntity<CreateDepositAccountResponse> responseResult = null;
        String jsonBody;
        try {
            var productInfo = getDepositProductById(DEPOSIT_ACCOUNT_PRODUCT_ID);
            account.setProductTypeKey(productInfo.getEncodedKey());
            account.setAccountType(productInfo.getType());
            account.setAccountHolderType(DEPOSIT_ACCOUNT_DEFAULT_HOLDER_TYPE);
            account.setAccountState(DEPOSIT_ACCOUNT_DEFAULT_STATE);
            account.setCurrencyCode(DEPOSIT_ACCOUNT_DEFAULT_CURRENCY);
            jsonBody = mapper.writeValueAsString(account);
            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.setBasicAuth(mambuAPIUserName, mambuAPIPassword);
            MambuAPIHelper.addAcceptHeader(requestHeaders);
            MambuAPIHelper.addContentHeader(requestHeaders);
            MambuAPIHelper.addIdempotencyHeader(requestHeaders, jsonBody);
            HttpEntity<String> httpEntity = new HttpEntity<>(jsonBody, requestHeaders);
            RestTemplate restTemplate = new RestTemplate();
            responseResult = restTemplate.postForEntity(operationUrl, httpEntity, CreateDepositAccountResponse.class);
            createResponse = responseResult.getBody();
            createResponse.setStatusCode(responseResult.getStatusCode());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (RestClientException e) {
            System.err.println(e.toString());
            createResponse = new CreateDepositAccountResponse();
            String jsonError = e instanceof HttpStatusCodeException ?
                    ((HttpStatusCodeException) e).getResponseBodyAsString()
                    : "";
            var errorCode = ((HttpStatusCodeException) e).getStatusCode();
            createResponse.setStatusCode(errorCode);
            System.err.println("errorCode: %s".formatted(errorCode.toString()));
            System.err.println("value: %s".formatted(String.valueOf(errorCode.value())));
            System.err.println("isError: %s".formatted(String.valueOf(errorCode.isError())));
            System.err.println("is4xxClientError: %s".formatted(String.valueOf(errorCode.is4xxClientError())));
            System.err.println("is2xxSuccessful: %s".formatted(String.valueOf(errorCode.is2xxSuccessful())));
            if (!jsonError.isEmpty()) {
                try {
                    jsonError = jsonError.substring(jsonError.indexOf("["), jsonError.indexOf("]") + 1);
                    MambuErrorResponse[] errorResponse = new ObjectMapper().readValue(jsonError, MambuErrorResponse[].class);
                    createResponse.setErrors(errorResponse);
                } catch (JsonProcessingException ex) {

                }
            }
        } catch (Exception e) {
            System.err.println(e.toString());
            throw new RuntimeException(e);
        }
        return createResponse;
    }

    @Override
    public CreateDepositTransactionResponse makeDeposit(DepositTransaction deposit, String parentAccountKey) {
        CreateDepositTransactionResponse createDepositResponse = new CreateDepositTransactionResponse();
        String operationUrl = mambuAPIRootUrl.concat("/deposits/%s/deposit-transactions".formatted(parentAccountKey));
        ObjectMapper mapper = new ObjectMapper();
        ResponseEntity<CreateDepositTransactionResponse> responseResult = null;
        String jsonBody;
        try {
            var transactionDetails = new TransactionDetail();
            transactionDetails.setTransactionChannelId(DEPOSIT_ACCOUNT_DEFAULT_TRAN_CHANNEL);
            deposit.setTransactionDetails(transactionDetails);
            jsonBody = mapper.writeValueAsString(deposit);
            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.setBasicAuth(mambuAPIUserName, mambuAPIPassword);
            MambuAPIHelper.addAcceptHeader(requestHeaders);
            MambuAPIHelper.addContentHeader(requestHeaders);
            MambuAPIHelper.addIdempotencyHeader(requestHeaders, jsonBody);
            HttpEntity<String> httpEntity = new HttpEntity<>(jsonBody, requestHeaders);
            RestTemplate restTemplate = new RestTemplate();
            responseResult = restTemplate.postForEntity(operationUrl, httpEntity, CreateDepositTransactionResponse.class);
            createDepositResponse = responseResult.getBody();
            createDepositResponse.setStatusCode(responseResult.getStatusCode());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (RestClientException e) {
            createDepositResponse = handleErrorResponse(e);
        } catch (Exception e) {
            System.err.println(e.toString());
            throw new RuntimeException(e);
        }
        return createDepositResponse;
    }

    @Override
    public CreateDepositTransactionResponse makeWithdrawal(DepositTransaction withdrawal, String parentAccountKey) {
        CreateDepositTransactionResponse createWithdrawalResponse = new CreateDepositTransactionResponse();
        String operationUrl = mambuAPIRootUrl.concat("/deposits/%s/withdrawal-transactions".formatted(parentAccountKey));
        ObjectMapper mapper = new ObjectMapper();
        ResponseEntity<CreateDepositTransactionResponse> responseResult = null;
        String jsonBody;
        try {
            var transactionDetails = new TransactionDetail();
            transactionDetails.setTransactionChannelId(DEPOSIT_ACCOUNT_DEFAULT_TRAN_CHANNEL);
            withdrawal.setTransactionDetails(transactionDetails);
            jsonBody = mapper.writeValueAsString(withdrawal);
            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.setBasicAuth(mambuAPIUserName, mambuAPIPassword);
            MambuAPIHelper.addAcceptHeader(requestHeaders);
            MambuAPIHelper.addContentHeader(requestHeaders);
            MambuAPIHelper.addIdempotencyHeader(requestHeaders, jsonBody);
            HttpEntity<String> httpEntity = new HttpEntity<>(jsonBody, requestHeaders);
            RestTemplate restTemplate = new RestTemplate();
            responseResult = restTemplate.postForEntity(operationUrl, httpEntity, CreateDepositTransactionResponse.class);
            createWithdrawalResponse = responseResult.getBody();
            createWithdrawalResponse.setStatusCode(responseResult.getStatusCode());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (RestClientException e) {
            createWithdrawalResponse = handleErrorResponse(e);
        } catch (Exception e) {
            System.err.println(e.toString());
            throw new RuntimeException(e);
        }
        return createWithdrawalResponse;
    }

    private static CreateDepositTransactionResponse handleErrorResponse(RestClientException e) {
        CreateDepositTransactionResponse createWithdrawalResponse;
        System.err.println(e.toString());
        createWithdrawalResponse = new CreateDepositTransactionResponse();
        String jsonError = e instanceof HttpStatusCodeException ?
                ((HttpStatusCodeException) e).getResponseBodyAsString()
                : "";
        var errorCode = ((HttpStatusCodeException) e).getStatusCode();
        createWithdrawalResponse.setStatusCode(errorCode);
        System.err.println("errorCode: %s".formatted(errorCode.toString()));
        System.err.println("value: %s".formatted(String.valueOf(errorCode.value())));
        System.err.println("isError: %s".formatted(String.valueOf(errorCode.isError())));
        System.err.println("is4xxClientError: %s".formatted(String.valueOf(errorCode.is4xxClientError())));
        System.err.println("is2xxSuccessful: %s".formatted(String.valueOf(errorCode.is2xxSuccessful())));
        if (!jsonError.isEmpty()) {
            jsonError = jsonError.substring(jsonError.indexOf("["), jsonError.indexOf("]") + 1);
            MambuErrorResponse[] errorResponse = MambuErrorResponse.fromJson(jsonError);
            createWithdrawalResponse.setErrors(errorResponse);
        }
        return createWithdrawalResponse;
    }

    @Override
    public TransactionsQueryResponse getAccountTransactions(TransactionFilterInfo transactionFilterInfo) {
        TransactionsQueryResponse queryResponse = null;
        final int ROWS_LIMIT = 20;
        String operationUrl = mambuAPIRootUrl.concat("/deposits/transactions:search?limit=%d".formatted(ROWS_LIMIT));
        ObjectMapper mapper = new ObjectMapper();
        ResponseEntity<DepositTransaction[]> responseResult = null;
        try {
            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.setBasicAuth(mambuAPIUserName, mambuAPIPassword);
            MambuAPIHelper.addAcceptHeader(requestHeaders);
            HttpEntity<TransactionFilterInfo> httpEntity = new HttpEntity<>(transactionFilterInfo, requestHeaders);
            RestTemplate restTemplate = new RestTemplate();
            responseResult = restTemplate.exchange(operationUrl, HttpMethod.POST, httpEntity, DepositTransaction[].class);
            queryResponse = new TransactionsQueryResponse();
            queryResponse.setStatusCode(responseResult.getStatusCode());
            queryResponse.setTransactions(responseResult.getBody());
        } catch (RestClientException e) {
            queryResponse = handleQueryErrorResponse(e);
        } catch (Exception e) {
            System.err.println(e.toString());
            throw new RuntimeException(e);
        }
        return queryResponse;
    }

    private TransactionsQueryResponse handleQueryErrorResponse(RestClientException e) {
        TransactionsQueryResponse queryErrorResponse = null;
        System.err.println(e.toString());
        queryErrorResponse = new TransactionsQueryResponse();
        String jsonError = e instanceof HttpStatusCodeException ?
                ((HttpStatusCodeException) e).getResponseBodyAsString()
                : "";
        var errorCode = ((HttpStatusCodeException) e).getStatusCode();
        queryErrorResponse.setStatusCode(errorCode);
        System.err.println("errorCode: %s".formatted(errorCode.toString()));
        System.err.println("value: %s".formatted(String.valueOf(errorCode.value())));
        System.err.println("isError: %s".formatted(String.valueOf(errorCode.isError())));
        System.err.println("is4xxClientError: %s".formatted(String.valueOf(errorCode.is4xxClientError())));
        System.err.println("is2xxSuccessful: %s".formatted(String.valueOf(errorCode.is2xxSuccessful())));
        if (!jsonError.isEmpty()) {
            jsonError = jsonError.substring(jsonError.indexOf("["), jsonError.indexOf("]") + 1);
            MambuErrorResponse[] errorResponse = MambuErrorResponse.fromJson(jsonError);
            queryErrorResponse.setErrors(errorResponse);
        }
        return queryErrorResponse;
    }
}
