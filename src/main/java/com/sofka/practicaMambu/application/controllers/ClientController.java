package com.sofka.practicaMambu.application.controllers;

import com.sofka.practicaMambu.domain.dto.ClientCreateResponseDTO;
import com.sofka.practicaMambu.domain.model.Client;
import com.sofka.practicaMambu.domain.service.ClientService;
import com.sofka.practicaMambu.infraestructure.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/client")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @PostMapping
    public ResponseEntity<ClientCreateResponseDTO> createClient(@RequestBody Client client){
        ClientCreateResponseDTO responseDTO = clientService.createClient(client);
        ResponseEntity responseEntity = new ResponseEntity(responseDTO, HttpStatus.OK);
        System.out.println("%s %s".formatted(responseDTO.getFirstName(),responseDTO.getLastName()));
        return responseEntity;
    }
}
