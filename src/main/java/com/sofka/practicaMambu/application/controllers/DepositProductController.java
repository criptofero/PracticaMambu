package com.sofka.practicaMambu.application.controllers;

import com.sofka.practicaMambu.domain.dto.CreateDepositAccountResponse;
import com.sofka.practicaMambu.domain.dto.DepositProductResponse;
import com.sofka.practicaMambu.domain.model.DepositAccount;
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
        var response = productService.GetDepositProductById(productId);
        ResponseEntity responseEntity = new ResponseEntity(response, HttpStatus.OK);
        return responseEntity;
    }

    @PostMapping
    public ResponseEntity<CreateDepositAccountResponse> createDepositAccount(@RequestBody DepositAccount account) {
        CreateDepositAccountResponse responseDTO = productService.CreateAccount(account);
        ResponseEntity responseEntity = new ResponseEntity(responseDTO, responseDTO.getStatusCode());
        return responseEntity;
    }
}
