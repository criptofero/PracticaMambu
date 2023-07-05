package com.sofka.practicaMambu.infraestructure;

import com.sofka.practicaMambu.domain.dto.ClientCreateResponseDTO;
import com.sofka.practicaMambu.domain.model.Client;
import com.sofka.practicaMambu.domain.service.ClientService;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ClientRepository implements ClientService {
    private RestTemplate restTemplate;
    @Override
    public ClientCreateResponseDTO createClient(Client client) {

        ClientCreateResponseDTO prueba = new ClientCreateResponseDTO();
        prueba.setFirstName("Bart");
        prueba.setLastName("Simpson");

        return prueba;
    }
}
