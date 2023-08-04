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
            var validationErrors = checkLoanInfo(loanAccount, productInfo);
            if (validationErrors != null) {
                loanAccountResponse = validationErrors;
            } else {
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
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (RestClientException e) {
            loanAccountResponse = (LoanAccountResponse) MambuAPIHelper.handleErrorResponse(e, LoanAccountResponse.class);
        } catch (Exception e) {
            System.err.println(e);
            throw new RuntimeException(e);
        }
        return loanAccountResponse;
    }

    private LoanAccountResponse checkLoanInfo(LoanAccount loanAccount, LoanProductResponse loanProductInfo) {
        LoanAccountResponse loanValidationResponse = null;
        if (loanAccount != null && loanProductInfo != null) {
            MambuErrorResponse errorResponse = null;
            var loanAmount = loanAccount.getLoanAmount();
            var loanInstallments = loanAccount.getScheduleSettings().getRepaymentInstallments();
            var productAmountSettings = loanProductInfo.getLoanAmountSettings();
            var productInstallmentsSettings = loanProductInfo.getScheduleSettings().getNumInstallments();
            if (loanAmount <= 0) {
                loanValidationResponse = new LoanAccountResponse();
                errorResponse = new MambuErrorResponse();
                errorResponse.setErrorCode(1201);
                errorResponse.setErrorReason("Monto de préstamo %,d es inválido, debe ser un valor mayor a cero.".formatted(loanAmount));
                errorResponse.setErrorSource(this.getClass().getName());
                loanValidationResponse.setErrors(new MambuErrorResponse[]{errorResponse});
                loanValidationResponse.setStatusCode(HttpStatus.BAD_REQUEST);
            } else if (productAmountSettings != null && loanAmount < productAmountSettings.getLoanAmount().getMinValue() || loanAmount > productAmountSettings.getLoanAmount().getMaxValue()) {
                loanValidationResponse = new LoanAccountResponse();
                errorResponse = new MambuErrorResponse();
                errorResponse.setErrorCode(1202);
                errorResponse.setErrorReason("Monto de préstamo %,d esta fuera del rango permitido [Mínimo: %,d; Máximo: %,d]).".formatted(loanAmount, productAmountSettings.getLoanAmount().getMinValue(), productAmountSettings.getLoanAmount().getMaxValue()));
                errorResponse.setErrorSource(this.getClass().getName());
                loanValidationResponse.setErrors(new MambuErrorResponse[]{errorResponse});
                loanValidationResponse.setStatusCode(HttpStatus.BAD_REQUEST);
            }else if (productInstallmentsSettings != null &&  loanInstallments < productInstallmentsSettings.getMinValue() || loanInstallments > productInstallmentsSettings.getMaxValue()){
                loanValidationResponse = new LoanAccountResponse();
                errorResponse = new MambuErrorResponse();
                errorResponse.setErrorCode(1203);
                errorResponse.setErrorReason("Número de cuotas de préstamo esta fuera del rango permitido [Mínimo: %d; Máximo: %d]).".formatted(productInstallmentsSettings.getMinValue(), productInstallmentsSettings.getMaxValue()));
                errorResponse.setErrorSource(this.getClass().getName());
                loanValidationResponse.setErrors(new MambuErrorResponse[]{errorResponse});
                loanValidationResponse.setStatusCode(HttpStatus.BAD_REQUEST);
            }
        }
        return loanValidationResponse;
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
            loanAccountResponse = (LoanAccountResponse) MambuAPIHelper.handleErrorResponse(e, LoanAccountResponse.class);
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
            disbursementResponse = (LoanDisbursementResponse) MambuAPIHelper.handleErrorResponse(e, LoanDisbursementResponse.class);
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
            lockAccountResponse = (LoanAccountQueryResponse) MambuAPIHelper.handleErrorResponse(e, LoanAccountQueryResponse.class);
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
            payoffAccountResponse = (LoanAccountQueryResponse) MambuAPIHelper.handleErrorResponse(e, LoanAccountQueryResponse.class);
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
            repaymentResponse = (MakeRepaymentResponse) MambuAPIHelper.handleErrorResponse(e, MakeRepaymentResponse.class);
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
            queryResponse = (LoanTransactionQueryResponse) MambuAPIHelper.handleErrorResponse(e, LoanTransactionQueryResponse.class);
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
        MambuQueryFilter[] queryFilters = new MambuQueryFilter[]{loanIdFilter, typeFilter, nonReversedFilter};
        var sortingCriteria = new MambuSortingCriteria();
        sortingCriteria.setField("creationDate");
        sortingCriteria.setOrder("DESC");
        TransactionFilterInfo filterInfo = new TransactionFilterInfo();
        filterInfo.setFilterCriteria(queryFilters);
        filterInfo.setSortingCriteria(sortingCriteria);
        queryResponse = getLoanTransactions(filterInfo, 0);
        return queryResponse;
    }

    @Override
    public LoanScheduleQueryResponse getLoanRepaymentsSchedule(String accountKey) {
        LoanScheduleQueryResponse loanScheduleQueryResponse = null;
        String operationUrl = mambuAPIRootUrl.concat("/loans/{accountKey}/schedule");
        ResponseEntity<LoanScheduleQueryResponse> responseResult = null;
        try {
            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.setBasicAuth(mambuAPIUserName, mambuAPIPassword);
            MambuAPIHelper.addAcceptHeader(requestHeaders);
            HttpEntity<?> httpEntity = new HttpEntity<>(null, requestHeaders);
            RestTemplate restTemplate = new RestTemplate();
            responseResult = restTemplate.exchange(operationUrl, HttpMethod.GET, httpEntity, LoanScheduleQueryResponse.class, accountKey);
            loanScheduleQueryResponse = responseResult.getBody();
            loanScheduleQueryResponse.setStatusCode(responseResult.getStatusCode());
        } catch (Exception e) {
            System.err.println(e);
            throw new RuntimeException(e);
        }
        return loanScheduleQueryResponse;
    }

    @Override
    public LoanDetailQueryResponse getLoanDetails(String accountKey) {
        LoanDetailQueryResponse queryResponse = new LoanDetailQueryResponse();
        var loanInfo = getLoanAccountById(accountKey);
        var disbursementInfo = getLoanDisbursement(accountKey);
        var repaymentsInfo = getLoanRepayments(accountKey);
        var loanScheduleInfo = getLoanRepaymentsSchedule(accountKey);
        queryResponse.setLoanAccountInfo(loanInfo);
        queryResponse.setDisbursementDetails(disbursementInfo);
        queryResponse.setRepayments(repaymentsInfo);
        queryResponse.setSchedule(loanScheduleInfo);
        return queryResponse;
    }

    @Override
    public LoanAccountQueryResponse refinanceLoan(String accountKey, LoanRefinanceCommand refinanceCommand) {
        LoanAccountQueryResponse refinanceResponse = null;
        String operationUrl = mambuAPIRootUrl.concat("/loans/{accountKey}:refinance");
        String jsonBody;
        ObjectMapper mapper = new ObjectMapper();
        ResponseEntity<LoanAccountQueryResponse> responseResult = null;
        try {
            jsonBody = mapper.writeValueAsString(refinanceCommand);
            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.setBasicAuth(mambuAPIUserName, mambuAPIPassword);
            MambuAPIHelper.addAcceptHeader(requestHeaders);
            MambuAPIHelper.addIdempotencyHeader(requestHeaders, jsonBody);
            HttpEntity<LoanRefinanceCommand> httpEntity = new HttpEntity<>(refinanceCommand, requestHeaders);
            RestTemplate restTemplate = new RestTemplate();
            responseResult = restTemplate.exchange(operationUrl, HttpMethod.POST, httpEntity, LoanAccountQueryResponse.class, accountKey);
            var refinanceBody = responseResult.getBody();
            if (responseResult.getStatusCode() != null && !responseResult.getStatusCode().isError()) {
                refinanceResponse = getLoanAccountById(refinanceBody.getEncodedKey());
                refinanceResponse.setStatusCode(HttpStatus.OK);
            }
        } catch (RestClientException e) {
            refinanceResponse = (LoanAccountQueryResponse) MambuAPIHelper.handleErrorResponse(e, LoanAccountQueryResponse.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            System.err.println(e.toString());
            throw new RuntimeException(e);
        }
        return refinanceResponse;
    }

    @Override
    public LoanAccountQueryResponse rescheduleLoan(String accountKey, LoanRefinanceCommand refinanceCommand) {
        LoanAccountQueryResponse rescheduleResponse = null;
        String operationUrl = mambuAPIRootUrl.concat("/loans/{accountKey}:reschedule");
        String jsonBody;
        ObjectMapper mapper = new ObjectMapper();
        ResponseEntity<LoanAccountQueryResponse> responseResult = null;
        try {
            jsonBody = mapper.writeValueAsString(refinanceCommand);
            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.setBasicAuth(mambuAPIUserName, mambuAPIPassword);
            MambuAPIHelper.addAcceptHeader(requestHeaders);
            MambuAPIHelper.addIdempotencyHeader(requestHeaders, jsonBody);
            HttpEntity<LoanRefinanceCommand> httpEntity = new HttpEntity<>(refinanceCommand, requestHeaders);
            RestTemplate restTemplate = new RestTemplate();
            responseResult = restTemplate.exchange(operationUrl, HttpMethod.POST, httpEntity, LoanAccountQueryResponse.class, accountKey);
            var rescheduleBody = responseResult.getBody();
            if (responseResult.getStatusCode() != null && !responseResult.getStatusCode().isError()) {
                rescheduleResponse = getLoanAccountById(rescheduleBody.getEncodedKey());
                rescheduleResponse.setStatusCode(HttpStatus.OK);
            }
        } catch (RestClientException e) {
            rescheduleResponse = (LoanAccountQueryResponse) MambuAPIHelper.handleErrorResponse(e, LoanAccountQueryResponse.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            System.err.println(e.toString());
            throw new RuntimeException(e);
        }
        return rescheduleResponse;
    }
}
