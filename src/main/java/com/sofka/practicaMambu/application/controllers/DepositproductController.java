package com.sofka.practicaMambu.application.controllers;

import com.sofka.practicaMambu.domain.dto.DepositProductResponse;
import com.sofka.practicaMambu.domain.service.DepositProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/depositproduct")
public class DepositproductController{
    @Autowired
    private DepositProductService productService;

    @GetMapping("/{productId}")
    public ResponseEntity<DepositProductResponse> getProductById(@PathVariable String productId){
        var response = productService.GetDepositProductById(productId);
        ResponseEntity responseEntity = new ResponseEntity(response, HttpStatus.OK);
        return responseEntity;
    }
}
