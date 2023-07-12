package com.sofka.practicaMambu.application.controllers;

import com.sofka.practicaMambu.domain.dto.ClientCreateResponseDTO;
import com.sofka.practicaMambu.domain.dto.ClientOnboardingCommand;
import com.sofka.practicaMambu.domain.dto.ClientOnboardingResponse;
import com.sofka.practicaMambu.domain.model.Client;
import com.sofka.practicaMambu.domain.service.ClientService;
import com.sofka.practicaMambu.infraestructure.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        var onboardingResponse = clientService.activateClient(command);
        return new ResponseEntity<>(onboardingResponse, HttpStatus.OK);
    }
}
