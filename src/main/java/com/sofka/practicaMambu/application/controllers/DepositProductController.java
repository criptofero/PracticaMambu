package com.sofka.practicaMambu.application.controllers;

import com.sofka.practicaMambu.domain.dto.CreateDepositAccountResponse;
import com.sofka.practicaMambu.domain.dto.CreateDepositTransactionResponse;
import com.sofka.practicaMambu.domain.dto.DepositProductResponse;
import com.sofka.practicaMambu.domain.dto.TransactionsQueryResponse;
import com.sofka.practicaMambu.domain.model.DepositAccount;
import com.sofka.practicaMambu.domain.model.DepositTransaction;
import com.sofka.practicaMambu.domain.model.TransactionFilterInfo;
import com.sofka.practicaMambu.domain.service.DepositProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<TransactionsQueryResponse> getAccountTransactions(@RequestBody TransactionFilterInfo filterInfo){
        TransactionsQueryResponse queryResponse = productService.getAccountTransactions(filterInfo);
        ResponseEntity responseEntity = new ResponseEntity(queryResponse, HttpStatus.OK);
        return  responseEntity;
    }
}
