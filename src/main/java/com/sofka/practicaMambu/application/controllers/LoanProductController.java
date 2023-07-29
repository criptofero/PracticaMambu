package com.sofka.practicaMambu.application.controllers;

import com.sofka.practicaMambu.domain.activeProducts.dto.*;
import com.sofka.practicaMambu.domain.service.LoanProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/loanproduct")
public class LoanProductController {
    @Autowired
    private LoanProductService productService;

    @GetMapping("/{productId}")
    public ResponseEntity<LoanProductResponse> getProductById(@PathVariable String productId) {
        var response = productService.getLoanProductById(productId);
        ResponseEntity responseEntity = new ResponseEntity(response, HttpStatus.OK);
        return responseEntity;
    }

    @PostMapping("/loans")
    public ResponseEntity<LoanAccountResponse> createLoanAccount(@RequestBody CreateLoanAccountCommand loanAccountCommand) {
        LoanAccountResponse responseDTO = productService.createLoanAccount(loanAccountCommand);
        ResponseEntity responseEntity = new ResponseEntity(responseDTO, responseDTO.getStatusCode());
        return responseEntity;
    }

    @GetMapping("/loans/{accountKey}")
    public ResponseEntity<LoanAccountQueryResponse> getLoanAccountById(@PathVariable String accountKey) {
        var response = productService.getLoanAccountById(accountKey);
        ResponseEntity responseEntity = new ResponseEntity(response, HttpStatus.OK);
        return responseEntity;
    }

    @PostMapping("/loans/{accountKey}:approve")
    public ResponseEntity<LoanAccountResponse> approveLoanAccount (@PathVariable String accountKey, @RequestBody LoanActionCommand approveCommand) {
        LoanAccountResponse loanAccountResponse = productService.approveLoanAccount(accountKey, approveCommand);
        ResponseEntity responseEntity = new ResponseEntity(loanAccountResponse, loanAccountResponse.getStatusCode());
        return responseEntity;
    }

    @PostMapping("/loans/{accountKey}:disburse")
    public ResponseEntity<LoanDisbursementResponse> disburseLoan (@PathVariable String accountKey, @RequestBody LoanActionCommand disburseCommand) {
        LoanDisbursementResponse loanDisbursementResponse = productService.disburseLoan(accountKey, disburseCommand);
        ResponseEntity responseEntity = new ResponseEntity(loanDisbursementResponse, loanDisbursementResponse.getStatusCode());
        return responseEntity;
    }

    @PostMapping("/loans/{accountKey}:lock")
    public ResponseEntity<LoanAccountQueryResponse> lockLoanAccount(@PathVariable String accountKey, @RequestBody LoanActionCommand lockLoanCommand) {
        var response = productService.lockLoanAccount(accountKey, lockLoanCommand);
        ResponseEntity responseEntity = new ResponseEntity(response, response.getStatusCode());
        return responseEntity;
    }

    @PostMapping("/loans/{accountKey}:payOff")
    public ResponseEntity<LoanAccountQueryResponse> payoffLoanAccount(@PathVariable String accountKey, @RequestBody LoanActionCommand payoffLoanCommand) {
        var response = productService.payoffLoanAccount(accountKey, payoffLoanCommand);
        ResponseEntity responseEntity = new ResponseEntity(response, response.getStatusCode());
        return responseEntity;
    }
}
