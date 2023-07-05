package com.sofka.practicaMambu.domain.service;

import com.sofka.practicaMambu.domain.dto.ClientCreateResponseDTO;
import com.sofka.practicaMambu.domain.model.Client;
import org.springframework.stereotype.Repository;


public interface ClientService {

    ClientCreateResponseDTO createClient(Client client);
}
