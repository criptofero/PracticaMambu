package com.sofka.practicaMambu.infraestructure;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sofka.practicaMambu.domain.dto.ClientCreateResponseDTO;
import com.sofka.practicaMambu.domain.dto.ClientOnboardingCommand;
import com.sofka.practicaMambu.domain.dto.ClientOnboardingResponse;
import com.sofka.practicaMambu.domain.dto.MambuErrorResponse;
import com.sofka.practicaMambu.domain.model.Client;
import com.sofka.practicaMambu.domain.seedWork.MambuAPIHelper;
import com.sofka.practicaMambu.domain.service.ClientService;
import com.sofka.practicaMambu.domain.service.DepositProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.net.InetAddress;

@Service
public class ClientRepository implements ClientService {

    @Autowired
    private DepositProductService productService;
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
        ClientCreateResponseDTO createResponse = new ClientCreateResponseDTO();
        ResponseEntity<ClientCreateResponseDTO> responseResult = null;
        String jsonBody;
        try {
            if (client.getNotes() == null) {
                client.setNotes("Cliente creado desde backend, account: [%s], instanceName: %s".formatted(mambuAPIUserName, InetAddress.getLocalHost().getHostName()));
            }
            jsonBody = mapper.writeValueAsString(client);
            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.setBasicAuth(mambuAPIUserName, mambuAPIPassword);
            MambuAPIHelper.addAcceptHeader(requestHeaders);
            MambuAPIHelper.addContentHeader(requestHeaders);
            MambuAPIHelper.addIdempotencyHeader(requestHeaders, jsonBody);
            HttpEntity<String> httpEntity = new HttpEntity<>(jsonBody, requestHeaders);
            restTemplate = new RestTemplate();
            responseResult = restTemplate.postForEntity(operationUrl, httpEntity, ClientCreateResponseDTO.class);
            createResponse = responseResult.getBody();
            createResponse.setStatusCode(responseResult.getStatusCode());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (RestClientException e) {
            createResponse = handleErrorResponse(e);
        } catch (Exception e) {
            System.err.println(e.toString());
            throw new RuntimeException(e);
        }
        return createResponse;
    }

    private static ClientCreateResponseDTO handleErrorResponse(RestClientException e) {
        ClientCreateResponseDTO createResponse;
        System.err.println(e.toString());
        createResponse = new ClientCreateResponseDTO();
        String jsonError = e instanceof HttpStatusCodeException ?
                ((HttpStatusCodeException) e).getResponseBodyAsString()
                : "";
        var errorCode = ((HttpStatusCodeException) e).getStatusCode();
        createResponse.setStatusCode(errorCode);
        System.err.println("errorCode: %s".formatted(errorCode.toString()));
        System.err.println("value: %s".formatted(String.valueOf(errorCode.value())));
        System.err.println("isError: %s".formatted(String.valueOf(errorCode.isError())));
        System.err.println("is4xxClientError: %s".formatted(String.valueOf(errorCode.is4xxClientError())));
        System.err.println("is2xxSuccessful: %s".formatted(String.valueOf(errorCode.is2xxSuccessful())));
        if (!jsonError.isEmpty()) {
            try {
                jsonError = jsonError.substring(jsonError.indexOf("["), jsonError.indexOf("]") + 1);
                MambuErrorResponse[] errorResponse = new ObjectMapper().readValue(jsonError, MambuErrorResponse[].class);
                createResponse.setErrors(errorResponse);
            } catch (JsonProcessingException ex) {

            }
        }
        return createResponse;
    }

    @Override
    public ClientOnboardingResponse activateClient(ClientOnboardingCommand command) {
        ClientOnboardingResponse onboardingResponse = null;
        if (command.getClientInfo() != null) {
            var clientCreateResponse = createClient(command.getClientInfo());
            if (clientCreateResponse.getStatusCode().isError()){
                throw new ResponseStatusException(clientCreateResponse.getStatusCode(), "Error al intentar crear cliente");
            }
            var accountInfo = command.getAccountInfo();
            accountInfo.setAccountHolderKey(clientCreateResponse.getEncodedKey());
            var accountCreateResponse = productService.CreateAccount(accountInfo);
            if (accountCreateResponse.getStatusCode().isError()){
                throw new ResponseStatusException(accountCreateResponse.getStatusCode(), "Error al intentar crear cuenta");
            }
            onboardingResponse = new ClientOnboardingResponse();
            onboardingResponse.setClientInfo(clientCreateResponse);
            onboardingResponse.setAccountInfo(accountCreateResponse);
        }
        return onboardingResponse;
    }
}
