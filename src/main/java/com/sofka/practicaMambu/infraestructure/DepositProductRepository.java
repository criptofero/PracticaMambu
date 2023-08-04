package com.sofka.practicaMambu.infraestructure;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sofka.practicaMambu.domain.dto.*;
import com.sofka.practicaMambu.domain.model.DepositAccount;
import com.sofka.practicaMambu.domain.model.DepositTransaction;
import com.sofka.practicaMambu.domain.model.TransactionDetail;
import com.sofka.practicaMambu.domain.model.TransactionFilterInfo;
import com.sofka.practicaMambu.domain.model.query.MambuQueryFilter;
import com.sofka.practicaMambu.domain.model.query.MambuSortingCriteria;
import com.sofka.practicaMambu.domain.seedWork.MambuAPIHelper;
import com.sofka.practicaMambu.domain.service.DepositProductService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

public class DepositProductRepository implements DepositProductService {

    public static final String DEPOSIT_ACCOUNT_PRODUCT_ID = "1099003";
    public static final String DEPOSIT_ACCOUNT_DEFAULT_HOLDER_TYPE = "CLIENT";
    public static final String DEPOSIT_ACCOUNT_DEFAULT_STATE = "APPROVED";
    public static final String DEPOSIT_ACCOUNT_DEFAULT_CURRENCY = "COP";
    public static final String DEPOSIT_ACCOUNT_DEFAULT_TRAN_CHANNEL = "OnlineChannelLocales";
    public static final String DEPOSIT_ACCOUNT_SEIZURE_DEFAULT_TRAN_CHANNEL = "cash";
    public static final int ROWS_LIMIT = 20;
    public static final String LOCK_ACTION = "LOCK";

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
        ResponseEntity<DepositProductResponse> responseResult = null;
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
            createResponse = new CreateDepositAccountResponse();
            MambuAPIHelper.setResponseErrorInfo(createResponse, e);
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
            createDepositResponse = (CreateDepositTransactionResponse) MambuAPIHelper.handleErrorResponse(e, CreateDepositTransactionResponse.class);
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
            createWithdrawalResponse = (CreateDepositTransactionResponse) MambuAPIHelper.handleErrorResponse(e, CreateDepositTransactionResponse.class);
            ;
        } catch (Exception e) {
            System.err.println(e.toString());
            throw new RuntimeException(e);
        }
        return createWithdrawalResponse;
    }

    @Override
    public TransactionsQueryResponse getAccountTransactions(TransactionFilterInfo transactionFilterInfo) {
        TransactionsQueryResponse queryResponse = null;
        String operationUrl = mambuAPIRootUrl.concat("/deposits/transactions:search?limit=%d".formatted(ROWS_LIMIT));
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
            queryResponse = (TransactionsQueryResponse) MambuAPIHelper.handleErrorResponse(e, TransactionsQueryResponse.class);
        } catch (Exception e) {
            System.err.println(e.toString());
            throw new RuntimeException(e);
        }
        return queryResponse;
    }

    @Override
    public DepositAccount getAccountById(String accountKey) {
        DepositAccount account = null;
        String operationUrl = mambuAPIRootUrl.concat("/deposits:search");
        TransactionFilterInfo filterInfo = new TransactionFilterInfo();
        MambuQueryFilter queryFilter = new MambuQueryFilter();
        queryFilter.setField("encodedKey");
        queryFilter.setOperator("EQUALS");
        queryFilter.setValue(accountKey);
        MambuSortingCriteria sortingCriteria = new MambuSortingCriteria();
        sortingCriteria.setField("encodedKey");
        sortingCriteria.setOrder("ASC");
        filterInfo.setFilterCriteria(new MambuQueryFilter[]{queryFilter});
        filterInfo.setSortingCriteria(sortingCriteria);
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setBasicAuth(mambuAPIUserName, mambuAPIPassword);
        MambuAPIHelper.addAcceptHeader(requestHeaders);
        HttpEntity<TransactionFilterInfo> httpEntity = new HttpEntity<>(filterInfo, requestHeaders);
        RestTemplate restTemplate = new RestTemplate();
        var accountQueryResponse = restTemplate.exchange(operationUrl, HttpMethod.POST, httpEntity, DepositAccount[].class);
        if (accountQueryResponse.getBody() != null && accountQueryResponse.getBody().length > 0) {
            account = accountQueryResponse.getBody()[0];
        }
        return account;
    }

    @Override
    public CreateBalanceBlockResponse blockAccountBalance(DepositBalanceBlockCommand blockCommand, String accountKey) {
        CreateBalanceBlockResponse blockResponse = null;
        String operationUrl = mambuAPIRootUrl.concat("/deposits/{accountKey}/blocks");
        String jsonBody;
        ObjectMapper mapper = new ObjectMapper();
        ResponseEntity<CreateBalanceBlockResponse> responseResult = null;
        try {
            jsonBody = mapper.writeValueAsString(blockCommand);
            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.setBasicAuth(mambuAPIUserName, mambuAPIPassword);
            MambuAPIHelper.addAcceptHeader(requestHeaders);
            MambuAPIHelper.addContentHeader(requestHeaders);
            MambuAPIHelper.addIdempotencyHeader(requestHeaders, jsonBody);
            HttpEntity<DepositBalanceBlockCommand> httpEntity = new HttpEntity<>(blockCommand, requestHeaders);
            RestTemplate restTemplate = new RestTemplate();
            responseResult = restTemplate.postForEntity(operationUrl, httpEntity, CreateBalanceBlockResponse.class, accountKey);
            blockResponse = responseResult.getBody();
        } catch (RestClientException e) {
            blockResponse = (CreateBalanceBlockResponse) MambuAPIHelper.handleErrorResponse(e, CreateBalanceBlockResponse.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            System.err.println(e.toString());
            throw new RuntimeException(e);
        }
        return blockResponse;
    }

    @Override
    public ApplySeizureResponse applyAccountBalanceBlockAndSeizure(DepositBalanceBlockCommand blockCommand, String accountKey) {
        ApplySeizureResponse seizureResponse = null;
        var blockResponse = blockAccountBalance(blockCommand, accountKey);
        if (blockResponse != null) {
            if (blockResponse.getErrors() != null) {
                seizureResponse = new ApplySeizureResponse();
                seizureResponse.setErrors(blockResponse.getErrors());
                seizureResponse.setStatusCode(blockResponse.getStatusCode());
            } else {
                seizureResponse = applyAccountSeizure(blockCommand, accountKey, blockResponse.getExternalReferenceId());
            }
        }
        return seizureResponse;
    }

    public ApplySeizureResponse applyAccountSeizure(DepositBalanceBlockCommand blockCommand, String accountKey, String externalReferenceId) {
        ApplySeizureResponse seizureResponse;
        String operationUrl = mambuAPIRootUrl.concat("/deposits/{accountKey}/seizure-transactions");
        var accountInfo = getAccountById(accountKey);
        if (accountInfo != null && accountInfo.getAccountState().equalsIgnoreCase("LOCKED")) {
            seizureResponse = new ApplySeizureResponse();
            var errorResponse = new MambuErrorResponse();
            errorResponse.setErrorCode(1103);
            errorResponse.setErrorReason("No es posible aplicar embargo porque la cuenta se encuentra bloqueada");
            seizureResponse.setErrors(new MambuErrorResponse[]{errorResponse});
            seizureResponse.setStatusCode(HttpStatus.FORBIDDEN);
        } else {
            CreateSeizureCommand createSeizureCommand = new CreateSeizureCommand();
            createSeizureCommand.setAmount(blockCommand.getAmount());
            createSeizureCommand.setBlockId(externalReferenceId);
            createSeizureCommand.setExternalId(UUID.randomUUID().toString());
            createSeizureCommand.setNotes("Prueba Embargo Cuenta %s desde backend Sofka".formatted(accountKey));
            createSeizureCommand.setTransactionChannelId(DEPOSIT_ACCOUNT_SEIZURE_DEFAULT_TRAN_CHANNEL);
            String jsonBody;
            ObjectMapper mapper = new ObjectMapper();
            ResponseEntity<ApplySeizureResponse> responseResult = null;
            try {
                jsonBody = mapper.writeValueAsString(blockCommand);
                HttpHeaders requestHeaders = new HttpHeaders();
                requestHeaders.setBasicAuth(mambuAPIUserName, mambuAPIPassword);
                MambuAPIHelper.addAcceptHeader(requestHeaders);
                MambuAPIHelper.addContentHeader(requestHeaders);
                MambuAPIHelper.addIdempotencyHeader(requestHeaders, jsonBody);
                HttpEntity<CreateSeizureCommand> httpEntity = new HttpEntity<>(createSeizureCommand, requestHeaders);
                RestTemplate restTemplate = new RestTemplate();
                responseResult = restTemplate.postForEntity(operationUrl, httpEntity, ApplySeizureResponse.class, accountKey);
                seizureResponse = responseResult.getBody();
                seizureResponse.setStatusCode(responseResult.getStatusCode());
            } catch (RestClientException e) {
                seizureResponse = (ApplySeizureResponse) MambuAPIHelper.handleErrorResponse(e, ApplySeizureResponse.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            } catch (Exception e) {
                System.err.println(e.toString());
                throw new RuntimeException(e);
            }
        }
        return seizureResponse;
    }

    @Override
    public LockAccountResponse lockAccount(String lockNotes, String accountKey) {
        LockAccountResponse lockAccountResponse = null;
        String operationUrl = mambuAPIRootUrl.concat("/deposits/{accountKey}:changeState");
        var lockAccountCommand = new LockAccountCommand();
        lockAccountCommand.setAction(LOCK_ACTION);
        lockAccountCommand.setNotes(lockNotes);
        String jsonBody;
        ObjectMapper mapper = new ObjectMapper();
        ResponseEntity<LockAccountResponse> responseResult = null;
        try {
            jsonBody = mapper.writeValueAsString(lockAccountCommand);
            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.setBasicAuth(mambuAPIUserName, mambuAPIPassword);
            MambuAPIHelper.addAcceptHeader(requestHeaders);
            MambuAPIHelper.addIdempotencyHeader(requestHeaders, jsonBody);
            HttpEntity<LockAccountCommand> httpEntity = new HttpEntity<>(lockAccountCommand, requestHeaders);
            RestTemplate restTemplate = new RestTemplate();
            responseResult = restTemplate.postForEntity(operationUrl, httpEntity, LockAccountResponse.class, accountKey);
            lockAccountResponse = responseResult.getBody();
            lockAccountResponse.setStatusCode(responseResult.getStatusCode());
        } catch (RestClientException e) {
            lockAccountResponse = (LockAccountResponse) MambuAPIHelper.handleErrorResponse(e, LockAccountResponse.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            System.err.println(e.toString());
            throw new RuntimeException(e);
        }
        return lockAccountResponse;
    }

    @Override
    public ApplyInterestResponse applyInterest(ApplyInterestCommand applyInterestCommand, String accountKey) {
        ApplyInterestResponse applyInterestResponse = null;
        String operationUrl = mambuAPIRootUrl.concat("/deposits/{accountKey}:applyInterest");
        String jsonBody;
        ObjectMapper mapper = new ObjectMapper();
        ResponseEntity<ApplyInterestResponse> responseResult = null;
        try {
            jsonBody = mapper.writeValueAsString(applyInterestCommand);
            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.setBasicAuth(mambuAPIUserName, mambuAPIPassword);
            MambuAPIHelper.addAcceptHeader(requestHeaders);
            MambuAPIHelper.addIdempotencyHeader(requestHeaders, jsonBody);
            HttpEntity<ApplyInterestCommand> httpEntity = new HttpEntity<>(applyInterestCommand, requestHeaders);
            RestTemplate restTemplate = new RestTemplate();
            responseResult = restTemplate.postForEntity(operationUrl, httpEntity, ApplyInterestResponse.class, accountKey);
            applyInterestResponse = responseResult.getBody();
        } catch (RestClientException e) {
            applyInterestResponse = (ApplyInterestResponse) MambuAPIHelper.handleErrorResponse(e, ApplyInterestResponse.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            System.err.println(e.toString());
            throw new RuntimeException(e);
        }
        return applyInterestResponse;
    }
}
