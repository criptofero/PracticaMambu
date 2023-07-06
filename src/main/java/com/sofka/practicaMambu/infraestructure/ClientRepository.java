package com.sofka.practicaMambu.infraestructure;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sofka.practicaMambu.domain.dto.ClientCreateResponseDTO;
import com.sofka.practicaMambu.domain.dto.MambuErrorResponse;
import com.sofka.practicaMambu.domain.model.Client;
import com.sofka.practicaMambu.domain.service.ClientService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.InetAddress;
import java.util.UUID;

@Service
//@Configuration
public class ClientRepository implements ClientService {
    private RestTemplate restTemplate;

    @Value("${mambuAPI.rootUrl}")
    private String mambuAPIRootUrl;

    @Value("${mambuAPI.userName}")
    private String mambuAPIUserName;

    @Value("${mambuAPI.password}")
    private String mambuAPIPassword;

    @Override
    public ClientCreateResponseDTO createClient(Client client) {
        String operationUrl = mambuAPIRootUrl.concat("/clients/");
        ObjectMapper mapper = new ObjectMapper();
        ClientCreateResponseDTO createResponse;
        String jsonBody;
        try {
            if (client.getNotes() == null) {
                client.setNotes("Cliente creado desde backend, account: [%s], instanceName: %s".formatted(mambuAPIUserName, InetAddress.getLocalHost().getHostName()));
            }
            jsonBody = mapper.writeValueAsString(client);
            UUID requestKey = UUID.randomUUID();
            org.springframework.http.HttpHeaders requestHeaders = new org.springframework.http.HttpHeaders();
            requestHeaders.setBasicAuth(mambuAPIUserName, mambuAPIPassword);
            requestHeaders.set("Accept", "application/vnd.mambu.v2+json");
            requestHeaders.set("Content-Type", "application/json");
            requestHeaders.set("Idempotency-Key", requestKey.toString());
            HttpEntity<String> httpEntity = new HttpEntity<>(jsonBody, requestHeaders);
            restTemplate = new RestTemplate();
            var responseResult = restTemplate.postForEntity(operationUrl, httpEntity, ClientCreateResponseDTO.class);
            System.out.println("Status Code: " + responseResult.getStatusCode());
            boolean clientCreated = responseResult.getStatusCode().isSameCodeAs(HttpStatusCode.valueOf(HttpStatus.CREATED.value()));
            createResponse = responseResult.getBody();
            /*if (clientCreated){
                createResponse = responseResult.getBody();
            }*/
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (RestClientException e) {
            System.err.println(e.toString());
            createResponse = new ClientCreateResponseDTO();
            String jsonError = e instanceof HttpStatusCodeException ?
                    ((HttpStatusCodeException) e).getResponseBodyAsString()
                    : "";
            try {
                jsonError = jsonError.substring(jsonError.indexOf("["), jsonError.indexOf("]") + 1);
                MambuErrorResponse[] errorResponse = new ObjectMapper().readValue(jsonError, MambuErrorResponse[].class);
                createResponse.setErrors(errorResponse);
            } catch (JsonProcessingException ex) {

            }
        } catch (Exception e) {
            System.err.println(e.toString());
            throw new RuntimeException(e);
        }
        return createResponse;
    }
}
