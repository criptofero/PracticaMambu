package com.sofka.practicaMambu.infraestructure;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sofka.practicaMambu.domain.dto.ClientCreateResponseDTO;
import com.sofka.practicaMambu.domain.model.Client;
import com.sofka.practicaMambu.domain.service.ClientService;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.InetAddress;
import java.net.http.HttpHeaders;
import java.util.UUID;

@Service
public class ClientRepository implements ClientService {
    private RestTemplate restTemplate;

    @Override
    public ClientCreateResponseDTO createClient(Client client) {
        final String mambuAPIRootUrl = "https://sofkamambudev.sandbox.mambu.com/api";
        final String MAMBU_API_USER = "andres.pinzon@sofka.com.co";
        final String MAMBU_API_PASSWORD = "M4a8vS0tK.auJ62";
        String operationUrl = mambuAPIRootUrl.concat("/clients/");
        ObjectMapper mapper = new ObjectMapper();
        ClientCreateResponseDTO createResponse = new ClientCreateResponseDTO();
        String jsonBody;
        try {
            if (client.getNotes() == null) {
                client.setNotes("Cliente creado desde backend, account: [%s], instanceName: %s".formatted(MAMBU_API_USER, InetAddress.getLocalHost().getHostName()));
            }
            jsonBody = mapper.writeValueAsString(client);
            UUID requestKey = UUID.randomUUID();
            org.springframework.http.HttpHeaders requestHeaders = new org.springframework.http.HttpHeaders();
            requestHeaders.setBasicAuth(MAMBU_API_USER, MAMBU_API_PASSWORD);
            requestHeaders.set("Accept", "application/vnd.mambu.v2+json");
            requestHeaders.set("Content-Type", "application/json");
            requestHeaders.set("Idempotency-Key", requestKey.toString());
            HttpEntity<String> httpEntity = new HttpEntity<>(jsonBody, requestHeaders);
            restTemplate = new RestTemplate();
            var responseResult = restTemplate.postForEntity(operationUrl, httpEntity, ClientCreateResponseDTO.class);
            System.out.println("Status Code: " + responseResult.getStatusCode());
            boolean clientCreated = responseResult.getStatusCode().isSameCodeAs(HttpStatusCode.valueOf(HttpStatus.CREATED.value()));
            if (clientCreated){
                createResponse = responseResult.getBody();
            }
            /*createResponse.setFirstName("Bart");
            createResponse.setLastName("Simpson");*/
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (RestClientException e) {
            System.err.println(e.toString());
            throw new RuntimeException(e);
        } catch (Exception e) {
            System.err.println(e.toString());
            throw new RuntimeException(e);
        }
        return createResponse;
    }
}
