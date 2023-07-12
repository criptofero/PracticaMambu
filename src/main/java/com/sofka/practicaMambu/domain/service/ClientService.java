package com.sofka.practicaMambu.domain.service;

import com.sofka.practicaMambu.domain.dto.ClientCreateResponseDTO;
import com.sofka.practicaMambu.domain.dto.ClientOnboardingCommand;
import com.sofka.practicaMambu.domain.dto.ClientOnboardingResponse;
import com.sofka.practicaMambu.domain.model.Client;
import org.springframework.stereotype.Repository;


public interface ClientService {

    ClientCreateResponseDTO createClient(Client client);

    ClientOnboardingResponse activateClient(ClientOnboardingCommand command);
}
