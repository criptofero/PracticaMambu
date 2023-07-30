package com.sofka.practicaMambu.infraestructure;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sofka.practicaMambu.domain.activeProducts.dto.*;
import com.sofka.practicaMambu.domain.dto.MambuErrorResponse;
import com.sofka.practicaMambu.domain.model.TransactionFilterInfo;
import com.sofka.practicaMambu.domain.model.activeProducts.LoanAccount;
import com.sofka.practicaMambu.domain.model.activeProducts.LoanTransaction;
import com.sofka.practicaMambu.domain.model.query.MambuQueryFilter;
import com.sofka.practicaMambu.domain.model.query.MambuSortingCriteria;
import com.sofka.practicaMambu.domain.seedWork.CommonUtils;
import com.sofka.practicaMambu.domain.seedWork.MambuAPIHelper;
import com.sofka.practicaMambu.domain.seedWork.mappers.LoanAccountMapper;
import com.sofka.practicaMambu.domain.service.LoanProductService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

public class LoanProductRepository implements LoanProductService {
    public static final String LOAN_ACCOUNT_PRODUCT_ID = "microcredcol";
    public static final String LOAN_ACCOUNT_DEFAULT_HOLDER_TYPE = "CLIENT";
    public static final String APPROVE_ACTION = "APPROVE";

    public static final int MAX_ROWS_LIMIT = 20;

    @Value("${mambuAPI.rootUrl}")
    private String mambuAPIRootUrl;

    @Value("${mambuAPI.userName}")
    private String mambuAPIUserName;

    @Value("${mambuAPI.password}")
    private String mambuAPIPassword;

    @Override
    public LoanProductResponse getLoanProductById(String productId) {
        LoanProductResponse productResponse;
        String operationUrl = mambuAPIRootUrl.concat("/loanproducts/{productId}?offset=0&limit=1&paginationDetails=OFF&detailsLevel=FULL");
        ResponseEntity<LoanProductResponse> responseResult = null;
        try {
            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.setBasicAuth(mambuAPIUserName, mambuAPIPassword);
            MambuAPIHelper.addAcceptHeader(requestHeaders);
            HttpEntity<String> httpEntity = new HttpEntity<>(null, requestHeaders);
            RestTemplate restTemplate = new RestTemplate();
            responseResult = restTemplate.exchange(operationUrl, HttpMethod.GET, httpEntity, LoanProductResponse.class, productId);
            productResponse = responseResult.getBody();
        } catch (Exception e) {
            System.err.println(e.toString());
            throw new RuntimeException(e);
        }
        return productResponse;
    }

    @Override
    public LoanAccountQueryResponse getLoanAccountById(String accountKey) {
        LoanAccountQueryResponse accountQueryResponse = null;
        String operationUrl = mambuAPIRootUrl.concat("/loans/{accountKey}");
        ResponseEntity<LoanAccountQueryResponse> responseResult = null;
        try {
            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.setBasicAuth(mambuAPIUserName, mambuAPIPassword);
            MambuAPIHelper.addAcceptHeader(requestHeaders);
            HttpEntity<?> httpEntity = new HttpEntity<>(null, requestHeaders);
            RestTemplate restTemplate = new RestTemplate();
            responseResult = restTemplate.exchange(operationUrl, HttpMethod.GET, httpEntity, LoanAccountQueryResponse.class, accountKey);
            accountQueryResponse = responseResult.getBody();
        } catch (Exception e) {
            System.err.println(e);
            throw new RuntimeException(e);
        }
        return accountQueryResponse;
    }

    @Override
    public LoanAccountResponse createLoanAccount(CreateLoanAccountCommand createAccountCommand) {
        LoanAccountResponse loanAccountResponse;
        String operationUrl = mambuAPIRootUrl.concat("/loans");
        ObjectMapper mapper = new ObjectMapper();
        ResponseEntity<LoanAccountResponse> responseResult = null;
        String jsonBody;
        try {
            var productInfo = getLoanProductById(LOAN_ACCOUNT_PRODUCT_ID);
            LoanAccount loanAccount = LoanAccountMapper.INSTANCE.toLoanAccount(createAccountCommand);
            loanAccount.setProductTypeKey(productInfo.getEncodedKey());
            loanAccount.setAccountHolderType(LOAN_ACCOUNT_DEFAULT_HOLDER_TYPE);
            jsonBody = mapper.writeValueAsString(loanAccount);
            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.setBasicAuth(mambuAPIUserName, mambuAPIPassword);
            MambuAPIHelper.addAcceptHeader(requestHeaders);
            MambuAPIHelper.addContentHeader(requestHeaders);
            MambuAPIHelper.addIdempotencyHeader(requestHeaders, jsonBody);
            HttpEntity<String> httpEntity = new HttpEntity<>(jsonBody, requestHeaders);
            RestTemplate restTemplate = new RestTemplate();
            responseResult = restTemplate.postForEntity(operationUrl, httpEntity, LoanAccountResponse.class);
            loanAccountResponse = responseResult.getBody();
            loanAccountResponse.setStatusCode(responseResult.getStatusCode());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (RestClientException e) {
            loanAccountResponse = new LoanAccountResponse();
            MambuAPIHelper.setResponseErrorInfo(loanAccountResponse, e);
        } catch (Exception e) {
            System.err.println(e);
            throw new RuntimeException(e);
        }
        return loanAccountResponse;
    }

    @Override
    public LoanAccountResponse approveLoanAccount(String accountKey, LoanActionCommand approveCommand) {
        LoanAccountResponse loanAccountResponse = null;
        String operationUrl = mambuAPIRootUrl.concat("/loans/{accountKey}:changeState");
        var approveLoanAccountCommand = new ApproveLoanAccountCommand();
        approveLoanAccountCommand.setAction(APPROVE_ACTION);
        approveLoanAccountCommand.setNotes(approveCommand.getNotes());
        String jsonBody;
        ObjectMapper mapper = new ObjectMapper();
        ResponseEntity<LoanAccountResponse> responseResult = null;
        try {
            jsonBody = mapper.writeValueAsString(approveLoanAccountCommand);
            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.setBasicAuth(mambuAPIUserName, mambuAPIPassword);
            MambuAPIHelper.addAcceptHeader(requestHeaders);
            MambuAPIHelper.addIdempotencyHeader(requestHeaders, jsonBody);
            HttpEntity<ApproveLoanAccountCommand> httpEntity = new HttpEntity<>(approveLoanAccountCommand, requestHeaders);
            RestTemplate restTemplate = new RestTemplate();
            responseResult = restTemplate.exchange(operationUrl, HttpMethod.POST, httpEntity, LoanAccountResponse.class, accountKey);
            loanAccountResponse = responseResult.getBody();
            loanAccountResponse.setStatusCode(responseResult.getStatusCode());
        } catch (RestClientException e) {
            loanAccountResponse = handleLoanAccountErrorResponse(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            System.err.println(e.toString());
            throw new RuntimeException(e);
        }
        return loanAccountResponse;
    }

    @Override
    public LoanDisbursementResponse disburseLoan(String accountKey, LoanActionCommand disburseCommand) {
        LoanDisbursementResponse disbursementResponse = null;
        String operationUrl = mambuAPIRootUrl.concat("/loans/{accountKey}/disbursement-transactions");
        var disbursementCommand = new LoanDisbursementCommand();
        disbursementCommand.setNotes(disburseCommand.getNotes());
        String jsonBody;
        ObjectMapper mapper = new ObjectMapper();
        ResponseEntity<LoanDisbursementResponse> responseResult = null;
        try {
            jsonBody = mapper.writeValueAsString(disbursementCommand);
            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.setBasicAuth(mambuAPIUserName, mambuAPIPassword);
            MambuAPIHelper.addAcceptHeader(requestHeaders);
            MambuAPIHelper.addIdempotencyHeader(requestHeaders, jsonBody);
            HttpEntity<LoanDisbursementCommand> httpEntity = new HttpEntity<>(disbursementCommand, requestHeaders);
            RestTemplate restTemplate = new RestTemplate();
            responseResult = restTemplate.exchange(operationUrl, HttpMethod.POST, httpEntity, LoanDisbursementResponse.class, accountKey);
            disbursementResponse = responseResult.getBody();
            disbursementResponse.setStatusCode(responseResult.getStatusCode());
        } catch (RestClientException e) {
            disbursementResponse = handleLoanDisbursementResponse(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            System.err.println(e.toString());
            throw new RuntimeException(e);
        }
        return disbursementResponse;
    }

    @Override
    public LoanAccountQueryResponse lockLoanAccount(String accountKey, LoanActionCommand lockLoanCommand) {
        LoanAccountQueryResponse lockAccountResponse = null;
        String operationUrl = mambuAPIRootUrl.concat("/loans/{accountKey}/lock-transactions");
        String jsonBody;
        ObjectMapper mapper = new ObjectMapper();
        ResponseEntity<LoanAccountQueryResponse> responseResult = null;
        try {
            jsonBody = mapper.writeValueAsString(lockLoanCommand);
            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.setBasicAuth(mambuAPIUserName, mambuAPIPassword);
            MambuAPIHelper.addAcceptHeader(requestHeaders);
            MambuAPIHelper.addIdempotencyHeader(requestHeaders, jsonBody);
            HttpEntity<LoanActionCommand> httpEntity = new HttpEntity<>(lockLoanCommand, requestHeaders);
            RestTemplate restTemplate = new RestTemplate();
            responseResult = restTemplate.exchange(operationUrl, HttpMethod.POST, httpEntity, LoanAccountQueryResponse.class, accountKey);
            lockAccountResponse = getLoanAccountById(accountKey);
            lockAccountResponse.setStatusCode(responseResult.getStatusCode());
        } catch (RestClientException e) {
            lockAccountResponse = handleLoanActionResponse(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            System.err.println(e.toString());
            throw new RuntimeException(e);
        }
        return lockAccountResponse;
    }

    @Override
    public LoanAccountQueryResponse payoffLoanAccount(String accountKey, LoanActionCommand payoffLoanCommand) {
        LoanAccountQueryResponse payoffAccountResponse = null;
        String operationUrl = mambuAPIRootUrl.concat("/loans/{accountKey}:payOff");
        String jsonBody;
        ObjectMapper mapper = new ObjectMapper();
        ResponseEntity<LoanAccountQueryResponse> responseResult = null;
        try {
            jsonBody = mapper.writeValueAsString(payoffLoanCommand);
            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.setBasicAuth(mambuAPIUserName, mambuAPIPassword);
            MambuAPIHelper.addAcceptHeader(requestHeaders);
            MambuAPIHelper.addIdempotencyHeader(requestHeaders, jsonBody);
            HttpEntity<LoanActionCommand> httpEntity = new HttpEntity<>(payoffLoanCommand, requestHeaders);
            RestTemplate restTemplate = new RestTemplate();
            responseResult = restTemplate.exchange(operationUrl, HttpMethod.POST, httpEntity, LoanAccountQueryResponse.class, accountKey);
            if (responseResult.getStatusCode() != null && !responseResult.getStatusCode().isError()) {
                payoffAccountResponse = getLoanAccountById(accountKey);
                payoffAccountResponse.setStatusCode(HttpStatus.OK);
            }
        } catch (RestClientException e) {
            payoffAccountResponse = handleLoanActionResponse(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            System.err.println(e.toString());
            throw new RuntimeException(e);
        }
        return payoffAccountResponse;
    }

    @Override
    public MakeRepaymentResponse makeLoanRepayment(String accountKey, RepaymentCommand repaymentCommand) {
        MakeRepaymentResponse repaymentResponse = null;
        String operationUrl = mambuAPIRootUrl.concat("/loans/{accountKey}/repayment-transactions");
        String jsonBody;
        ObjectMapper mapper = new ObjectMapper();
        ResponseEntity<MakeRepaymentResponse> responseResult = null;
        try {
            jsonBody = mapper.writeValueAsString(repaymentCommand);
            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.setBasicAuth(mambuAPIUserName, mambuAPIPassword);
            MambuAPIHelper.addAcceptHeader(requestHeaders);
            MambuAPIHelper.addIdempotencyHeader(requestHeaders, jsonBody);
            HttpEntity<RepaymentCommand> httpEntity = new HttpEntity<>(repaymentCommand, requestHeaders);
            RestTemplate restTemplate = new RestTemplate();
            responseResult = restTemplate.exchange(operationUrl, HttpMethod.POST, httpEntity, MakeRepaymentResponse.class, accountKey);
            repaymentResponse = responseResult.getBody();
            repaymentResponse.setStatusCode(responseResult.getStatusCode());
        } catch (RestClientException e) {
            repaymentResponse = handleRepaymentResponse(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            System.err.println(e.toString());
            throw new RuntimeException(e);
        }
        return repaymentResponse;
    }

    @Override
    public LoanTransactionQueryResponse getLoanTransactions(TransactionFilterInfo transactionFilterInfo, int rowsLimit) {
        LoanTransactionQueryResponse queryResponse = null;
        if (rowsLimit < 1 || rowsLimit > MAX_ROWS_LIMIT) {
            rowsLimit = MAX_ROWS_LIMIT;
        }
        String operationUrl = mambuAPIRootUrl.concat("/loans/transactions:search?limit=%d".formatted(rowsLimit));
        ResponseEntity<LoanTransaction[]> responseResult = null;
        try {
            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.setBasicAuth(mambuAPIUserName, mambuAPIPassword);
            MambuAPIHelper.addAcceptHeader(requestHeaders);
            HttpEntity<TransactionFilterInfo> httpEntity = new HttpEntity<>(transactionFilterInfo, requestHeaders);
            RestTemplate restTemplate = new RestTemplate();
            responseResult = restTemplate.exchange(operationUrl, HttpMethod.POST, httpEntity, LoanTransaction[].class);
            queryResponse = new LoanTransactionQueryResponse();
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

    @Override
    public LoanTransactionQueryResponse getLoanDisbursement(String accountKey) {
        LoanTransactionQueryResponse queryResponse = null;
        var loanIdFilter = new MambuQueryFilter();
        loanIdFilter.setField("parentAccountKey");
        loanIdFilter.setOperator("EQUALS");
        loanIdFilter.setValue(accountKey);
        var typeFilter = new MambuQueryFilter();
        typeFilter.setField("type");
        typeFilter.setOperator("IN");
        typeFilter.setValues(new String[]{"DISBURSEMENT"});
        var nonReversedFilter = new MambuQueryFilter();
        nonReversedFilter.setField("adjustmentTransactionKey");
        nonReversedFilter.setOperator("EMPTY");
        MambuQueryFilter[] queryFilters = new MambuQueryFilter[]{loanIdFilter, typeFilter, nonReversedFilter};
        var sortingCriteria = new MambuSortingCriteria();
        sortingCriteria.setField("creationDate");
        sortingCriteria.setOrder("DESC");
        TransactionFilterInfo filterInfo = new TransactionFilterInfo();
        filterInfo.setFilterCriteria(queryFilters);
        filterInfo.setSortingCriteria(sortingCriteria);
        queryResponse = getLoanTransactions(filterInfo, 1);
        return queryResponse;
    }

    @Override
    public LoanTransactionQueryResponse getLoanRepayments(String accountKey) {
        LoanTransactionQueryResponse queryResponse = null;
        var loanIdFilter = new MambuQueryFilter();
        loanIdFilter.setField("parentAccountKey");
        loanIdFilter.setOperator("EQUALS");
        loanIdFilter.setValue(accountKey);
        var typeFilter = new MambuQueryFilter();
        typeFilter.setField("type");
        typeFilter.setOperator("IN");
        typeFilter.setValues(new String[]{"REPAYMENT"});
        var nonReversedFilter = new MambuQueryFilter();
        nonReversedFilter.setField("adjustmentTransactionKey");
        nonReversedFilter.setOperator("EMPTY");
        MambuQueryFilter[] queryFilters = new MambuQueryFilter[]{loanIdFilter, typeFilter ,nonReversedFilter};
        var sortingCriteria = new MambuSortingCriteria();
        sortingCriteria.setField("creationDate");
        sortingCriteria.setOrder("DESC");
        TransactionFilterInfo filterInfo = new TransactionFilterInfo();
        filterInfo.setFilterCriteria(queryFilters);
        filterInfo.setSortingCriteria(sortingCriteria);
        queryResponse = getLoanTransactions(filterInfo, 0);
        return queryResponse;
    }

    private static LoanAccountResponse handleLoanAccountErrorResponse(RestClientException e) {
        LoanAccountResponse loanAccountResponse = new LoanAccountResponse();
        HttpStatusCode errorCode = MambuAPIHelper.getHttpStatusCode(e);
        MambuErrorResponse[] errorResponse = MambuAPIHelper.getMambuErrorResponses(e);
        loanAccountResponse.setStatusCode(errorCode);
        loanAccountResponse.setErrors(errorResponse);
        return loanAccountResponse;
    }

    private static LoanDisbursementResponse handleLoanDisbursementResponse(RestClientException e) {
        LoanDisbursementResponse loanDisbursementResponse = new LoanDisbursementResponse();
        HttpStatusCode errorCode = MambuAPIHelper.getHttpStatusCode(e);
        MambuErrorResponse[] errorResponse = MambuAPIHelper.getMambuErrorResponses(e);
        loanDisbursementResponse.setStatusCode(errorCode);
        loanDisbursementResponse.setErrors(errorResponse);
        return loanDisbursementResponse;
    }

    private static LoanAccountQueryResponse handleLoanActionResponse(RestClientException e) {
        LoanAccountQueryResponse loanLockResponse = new LoanAccountQueryResponse();
        HttpStatusCode errorCode = MambuAPIHelper.getHttpStatusCode(e);
        MambuErrorResponse[] errorResponse = MambuAPIHelper.getMambuErrorResponses(e);
        loanLockResponse.setStatusCode(errorCode);
        loanLockResponse.setErrors(errorResponse);
        return loanLockResponse;
    }

    private static MakeRepaymentResponse handleRepaymentResponse(RestClientException e) {
        MakeRepaymentResponse loanLockResponse = new MakeRepaymentResponse();
        HttpStatusCode errorCode = MambuAPIHelper.getHttpStatusCode(e);
        MambuErrorResponse[] errorResponse = MambuAPIHelper.getMambuErrorResponses(e);
        loanLockResponse.setStatusCode(errorCode);
        loanLockResponse.setErrors(errorResponse);
        return loanLockResponse;
    }

    private static LoanTransactionQueryResponse handleQueryErrorResponse(RestClientException e) {
        LoanTransactionQueryResponse transactionQueryResponse = new LoanTransactionQueryResponse();
        HttpStatusCode errorCode = MambuAPIHelper.getHttpStatusCode(e);
        MambuErrorResponse[] errorResponse = MambuAPIHelper.getMambuErrorResponses(e);
        transactionQueryResponse.setStatusCode(errorCode);
        transactionQueryResponse.setErrors(errorResponse);
        return transactionQueryResponse;
    }
}
