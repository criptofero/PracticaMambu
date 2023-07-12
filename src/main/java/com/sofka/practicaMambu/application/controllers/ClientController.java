package com.sofka.practicaMambu.application.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sofka.practicaMambu.domain.dto.ClientCreateResponseDTO;
import com.sofka.practicaMambu.domain.dto.ClientOnboardingCommand;
import com.sofka.practicaMambu.domain.dto.ClientOnboardingResponse;
import com.sofka.practicaMambu.domain.dto.MambuErrorResponse;
import com.sofka.practicaMambu.domain.model.Client;
import com.sofka.practicaMambu.domain.service.ClientService;
import com.sofka.practicaMambu.infraestructure.ClientRepository;
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

    @PostMapping("/onboarding")
    public ResponseEntity<ClientOnboardingResponse> activateClient(@RequestBody ClientOnboardingCommand command){
        ResponseEntity<ClientOnboardingResponse> result = null;
        ClientOnboardingResponse onboardingResponse = new ClientOnboardingResponse();
        try{
            onboardingResponse = clientService.activateClient(command);
            result = new ResponseEntity<>(onboardingResponse, onboardingResponse.getStatusCode());
        }catch(ResponseStatusException exc){
            var errorResponse = new ClientOnboardingResponse();
            var jsonError = exc.getMessage();
            jsonError = jsonError.substring(jsonError.indexOf("["), jsonError.indexOf("]") + 1);
            MambuErrorResponse[] onboardingErrors = MambuErrorResponse.fromJson(jsonError);
            errorResponse.setErrors(onboardingErrors);
            result = new ResponseEntity<>(errorResponse, exc.getStatusCode());
        }
        return result;
    }
}
