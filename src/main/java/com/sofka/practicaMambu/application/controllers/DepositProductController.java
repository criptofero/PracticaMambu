package com.sofka.practicaMambu.application.controllers;

import com.sofka.practicaMambu.domain.dto.*;
import com.sofka.practicaMambu.domain.model.DepositAccount;
import com.sofka.practicaMambu.domain.model.DepositTransaction;
import com.sofka.practicaMambu.domain.model.TransactionFilterInfo;
import com.sofka.practicaMambu.domain.seedWork.CommonUtils;
import com.sofka.practicaMambu.domain.service.DepositProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.temporal.ChronoUnit;

@RestController
@RequestMapping("/depositproduct")
public class DepositProductController {
    @Autowired
    private DepositProductService productService;

    @GetMapping("/{productId}")
    public ResponseEntity<DepositProductResponse> getProductById(@PathVariable String productId) {
        var response = productService.getDepositProductById(productId);
        ResponseEntity responseEntity = new ResponseEntity(response, HttpStatus.OK);
        return responseEntity;
    }

    @PostMapping("/accounts")
    public ResponseEntity<CreateDepositAccountResponse> createDepositAccount(@RequestBody DepositAccount account) {
        CreateDepositAccountResponse responseDTO = productService.createAccount(account);
        ResponseEntity responseEntity = new ResponseEntity(responseDTO, responseDTO.getStatusCode());
        return responseEntity;
    }

    @PostMapping("/accounts/{accountKey}/deposit-transacions")
    public ResponseEntity<CreateDepositTransactionResponse> createDepositTransaction(@RequestBody DepositTransaction transaction, @PathVariable String accountKey) {
        CreateDepositTransactionResponse transactionResponse = productService.makeDeposit(transaction, accountKey);
        ResponseEntity responseEntity = new ResponseEntity(transactionResponse, transactionResponse.getStatusCode());
        return responseEntity;
    }

    @PostMapping("/accounts/{accountKey}/withdrawal-transactions")
    public ResponseEntity<CreateDepositTransactionResponse> createWithdrawalTransaction(@RequestBody DepositTransaction transaction, @PathVariable String accountKey) {
        CreateDepositTransactionResponse transactionResponse = productService.makeWithdrawal(transaction, accountKey);
        ResponseEntity responseEntity = new ResponseEntity(transactionResponse, transactionResponse.getStatusCode());
        return responseEntity;
    }

    @GetMapping("/accounts/{accountKey}/transactions")
    public ResponseEntity<TransactionsQueryResponse> getAccountTransactions(@RequestBody TransactionFilterInfo filterInfo) {
        final String DATE_FORMAT = "yyyy-MM-dd";
        final int MAX_QUERY_RANGE_DAYS = 30;
        ResponseEntity responseEntity = null;
        TransactionsQueryResponse queryResponse = null;
        boolean isValidFilters = true;
        var dateFilter = CommonUtils.getFirstFilterByFieldAndOperator(filterInfo.getFilterCriteria(), "creationDate", "BETWEEN");
        if (dateFilter != null) {
            var startDate = CommonUtils.parseDateString(dateFilter.getValue(), DATE_FORMAT);
            var endDate = CommonUtils.parseDateString(dateFilter.getSecondValue(), DATE_FORMAT);
            long queryRangeDays = startDate.until(endDate, ChronoUnit.DAYS);
            MambuErrorResponse errorResponse = null;
            if (startDate.compareTo(endDate) > 0) {
                queryResponse = new TransactionsQueryResponse();
                errorResponse = new MambuErrorResponse();
                errorResponse.setErrorCode(1101);
                errorResponse.setErrorReason("Fecha inicial debe ser menor a fecha final de consulta");
                errorResponse.setErrorSource(dateFilter.getClass().getName());
                queryResponse.setErrors(new MambuErrorResponse[]{errorResponse});
                responseEntity = new ResponseEntity(queryResponse, HttpStatus.BAD_REQUEST);
                isValidFilters = false;
            }else if(queryRangeDays > MAX_QUERY_RANGE_DAYS){
                queryResponse = new TransactionsQueryResponse();
                errorResponse = new MambuErrorResponse();
                errorResponse.setErrorCode(1102);
                errorResponse.setErrorReason("Rango de consulta debe ser máximo de %d dias.".formatted(MAX_QUERY_RANGE_DAYS));
                errorResponse.setErrorSource(dateFilter.getClass().getName());
                queryResponse.setErrors(new MambuErrorResponse[]{errorResponse});
                responseEntity = new ResponseEntity(queryResponse, HttpStatus.BAD_REQUEST);
                isValidFilters = false;
            }
        }
        if (isValidFilters) {
            queryResponse = productService.getAccountTransactions(filterInfo);
            responseEntity = new ResponseEntity(queryResponse, queryResponse.getStatusCode());
        }
        return responseEntity;
    }

    @PostMapping("/accounts/{accountKey}/seizure-transactions")
    public ResponseEntity<ApplySeizureResponse> applyAccountBalanceBlockAndSeizure(@RequestBody DepositBalanceBlockCommand blockCommand, @PathVariable String accountKey) {
        ApplySeizureResponse transactionResponse = productService.applyAccountBalanceBlockAndSeizure(blockCommand, accountKey);
        ResponseEntity responseEntity = new ResponseEntity(transactionResponse, transactionResponse.getStatusCode());
        return responseEntity;
    }

    @PostMapping("/accounts/{accountKey}/locks")
    public ResponseEntity<LockAccountResponse> lockAccount(@RequestBody String notes, @PathVariable String accountKey) {
        LockAccountResponse lockAccountResponse = productService.lockAccount(notes, accountKey);
        ResponseEntity responseEntity = new ResponseEntity(lockAccountResponse, lockAccountResponse.getStatusCode());
        return responseEntity;
    }
}
