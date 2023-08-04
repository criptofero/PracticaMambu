package com.sofka.practicaMambu.infraestructure;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sofka.practicaMambu.domain.activeProducts.dto.LoanClientOnboardingCommand;
import com.sofka.practicaMambu.domain.activeProducts.dto.LoanClientOnboardingResponse;
import com.sofka.practicaMambu.domain.dto.ClientCreateResponseDTO;
import com.sofka.practicaMambu.domain.dto.DepositClientOnboardingCommand;
import com.sofka.practicaMambu.domain.dto.DepositClientOnboardingResponse;
import com.sofka.practicaMambu.domain.model.Client;
import com.sofka.practicaMambu.domain.seedWork.MambuAPIHelper;
import com.sofka.practicaMambu.domain.service.ClientService;
import com.sofka.practicaMambu.domain.service.DepositProductService;
import com.sofka.practicaMambu.domain.service.LoanProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.net.InetAddress;

@Service
public class ClientRepository implements ClientService {

    @Autowired
    private DepositProductService depositProductService;

    @Autowired
    private LoanProductService loanProductService;
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
            createResponse = new ClientCreateResponseDTO();
            MambuAPIHelper.setResponseErrorInfo(createResponse, e);
        } catch (Exception e) {
            System.err.println(e.toString());
            throw new RuntimeException(e);
        }
        return createResponse;
    }

    @Override
    public DepositClientOnboardingResponse activateDepositClient(DepositClientOnboardingCommand command) {
        DepositClientOnboardingResponse onboardingResponse = null;
        String jsonError;
        if (command.getClientInfo() != null) {
            var clientCreateResponse = createClient(command.getClientInfo());
            if (clientCreateResponse.getStatusCode().isError()) {
                try {
                    jsonError = new ObjectMapper().writeValueAsString(clientCreateResponse.getErrors());
                } catch (JsonProcessingException e) {
                    jsonError = "";
                }
                throw new ResponseStatusException(clientCreateResponse.getStatusCode(), jsonError);
            }
            var accountInfo = command.getAccountInfo();
            accountInfo.setAccountHolderKey(clientCreateResponse.getEncodedKey());
            var accountCreateResponse = depositProductService.createAccount(accountInfo);
            if (accountCreateResponse.getStatusCode().isError()) {
                try {
                    jsonError = new ObjectMapper().writeValueAsString(accountCreateResponse.getErrors());
                } catch (JsonProcessingException e) {
                    jsonError = "";
                }
                throw new ResponseStatusException(accountCreateResponse.getStatusCode(), jsonError);
            }
            onboardingResponse = new DepositClientOnboardingResponse();
            onboardingResponse.setClientInfo(clientCreateResponse);
            onboardingResponse.setAccountInfo(accountCreateResponse);
            onboardingResponse.setStatusCode(accountCreateResponse.getStatusCode());
        }
        return onboardingResponse;
    }

    @Override
    public LoanClientOnboardingResponse activateLoanClient(LoanClientOnboardingCommand command) {
        LoanClientOnboardingResponse onboardingResponse = null;
        String jsonError;
        if (command.getClientInfo() != null) {
            var clientCreateResponse = createClient(command.getClientInfo());
            if (clientCreateResponse.getStatusCode().isError()) {
                try {
                    jsonError = new ObjectMapper().writeValueAsString(clientCreateResponse.getErrors());
                } catch (JsonProcessingException e) {
                    jsonError = "";
                }
                throw new ResponseStatusException(clientCreateResponse.getStatusCode(), jsonError);
            }
            var accountInfo = command.getLoanAccountInfo();
            accountInfo.setAccountHolderKey(clientCreateResponse.getEncodedKey());
            var accountCreateResponse = loanProductService.createLoanAccount(accountInfo);
            if (accountCreateResponse.getStatusCode().isError()) {
                try {
                    jsonError = new ObjectMapper().writeValueAsString(accountCreateResponse.getErrors());
                } catch (JsonProcessingException e) {
                    jsonError = "";
                }
                throw new ResponseStatusException(accountCreateResponse.getStatusCode(), jsonError);
            }
            onboardingResponse = new LoanClientOnboardingResponse();
            onboardingResponse.setClientInfo(clientCreateResponse);
            onboardingResponse.setLoanAccountInfo(accountCreateResponse);
            onboardingResponse.setStatusCode(accountCreateResponse.getStatusCode());
        }
        return onboardingResponse;
    }
}
