package com.sofka.practicaMambu.infraestructure;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sofka.practicaMambu.domain.activeProducts.dto.ApproveLoanAccountCommand;
import com.sofka.practicaMambu.domain.activeProducts.dto.CreateLoanAccountCommand;
import com.sofka.practicaMambu.domain.activeProducts.dto.LoanAccountResponse;
import com.sofka.practicaMambu.domain.activeProducts.dto.LoanProductResponse;
import com.sofka.practicaMambu.domain.dto.MambuErrorResponse;
import com.sofka.practicaMambu.domain.model.activeProducts.LoanAccount;
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
    public LoanAccountResponse approveLoanAccount(String accountKey, String approveNotes) {
        LoanAccountResponse loanAccountResponse = null;
        String operationUrl = mambuAPIRootUrl.concat("/loans/{accountKey}:changeState");
        var approveLoanAccountCommand = new ApproveLoanAccountCommand();
        approveLoanAccountCommand.setAction(APPROVE_ACTION);
        approveLoanAccountCommand.setNotes(approveNotes);
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

    private static LoanAccountResponse handleLoanAccountErrorResponse(RestClientException e) {
        LoanAccountResponse loanAccountResponse = new LoanAccountResponse();
        HttpStatusCode errorCode = MambuAPIHelper.getHttpStatusCode(e);
        MambuErrorResponse[] errorResponse = MambuAPIHelper.getMambuErrorResponses(e);
        loanAccountResponse.setStatusCode(errorCode);
        loanAccountResponse.setErrors(errorResponse);
        return loanAccountResponse;
    }
}
