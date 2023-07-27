package com.sofka.practicaMambu.application.controllers;

import com.sofka.practicaMambu.domain.activeProducts.dto.CreateLoanAccountCommand;
import com.sofka.practicaMambu.domain.activeProducts.dto.LoanAccountResponse;
import com.sofka.practicaMambu.domain.activeProducts.dto.LoanProductResponse;
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
    public ResponseEntity<LoanAccountResponse> createDepositAccount(@RequestBody CreateLoanAccountCommand loanAccountCommand) {
        LoanAccountResponse responseDTO = productService.createLoanAccount(loanAccountCommand);
        ResponseEntity responseEntity = new ResponseEntity(responseDTO, responseDTO.getStatusCode());
        return responseEntity;
    }
}
