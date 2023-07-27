package com.sofka.practicaMambu.application.controllers;

import com.sofka.practicaMambu.domain.activeProducts.dto.LoanClientOnboardingCommand;
import com.sofka.practicaMambu.domain.activeProducts.dto.LoanClientOnboardingResponse;
import com.sofka.practicaMambu.domain.dto.ClientCreateResponseDTO;
import com.sofka.practicaMambu.domain.dto.DepositClientOnboardingCommand;
import com.sofka.practicaMambu.domain.dto.DepositClientOnboardingResponse;
import com.sofka.practicaMambu.domain.dto.MambuErrorResponse;
import com.sofka.practicaMambu.domain.model.Client;
import com.sofka.practicaMambu.domain.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/client")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ClientCreateResponseDTO> createClient(@RequestBody Client client){
        ClientCreateResponseDTO responseDTO = clientService.createClient(client);
        ResponseEntity responseEntity = new ResponseEntity(responseDTO, responseDTO.getStatusCode());
        return responseEntity;
    }

    @PostMapping("/deposit-onboarding")
    public ResponseEntity<DepositClientOnboardingResponse> activateDepositClient(@RequestBody DepositClientOnboardingCommand command){
        ResponseEntity<DepositClientOnboardingResponse> result = null;
        DepositClientOnboardingResponse onboardingResponse = new DepositClientOnboardingResponse();
        try{
            onboardingResponse = clientService.activateDepositClient(command);
            result = new ResponseEntity<>(onboardingResponse, onboardingResponse.getStatusCode());
        }catch(ResponseStatusException exc){
            var errorResponse = new DepositClientOnboardingResponse();
            var jsonError = exc.getMessage();
            jsonError = jsonError.substring(jsonError.indexOf("["), jsonError.indexOf("]") + 1);
            MambuErrorResponse[] onboardingErrors = MambuErrorResponse.fromJson(jsonError);
            errorResponse.setErrors(onboardingErrors);
            result = new ResponseEntity<>(errorResponse, exc.getStatusCode());
        }
        return result;
    }

    @PostMapping("/loan-onboarding")
    public ResponseEntity<LoanClientOnboardingResponse> activateLoanClient(@RequestBody LoanClientOnboardingCommand command){
        ResponseEntity<LoanClientOnboardingResponse> result = null;
        LoanClientOnboardingResponse onboardingResponse = new LoanClientOnboardingResponse();
        try{
            onboardingResponse = clientService.activateLoanClient(command);
            result = new ResponseEntity<>(onboardingResponse, onboardingResponse.getStatusCode());
        }catch(ResponseStatusException exc){
            var errorResponse = new LoanClientOnboardingResponse();
            var jsonError = exc.getMessage();
            jsonError = jsonError.substring(jsonError.indexOf("["), jsonError.indexOf("]") + 1);
            MambuErrorResponse[] onboardingErrors = MambuErrorResponse.fromJson(jsonError);
            errorResponse.setErrors(onboardingErrors);
            result = new ResponseEntity<>(errorResponse, exc.getStatusCode());
        }
        return result;
    }
}
