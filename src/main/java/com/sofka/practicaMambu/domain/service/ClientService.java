package com.sofka.practicaMambu.domain.service;

import com.sofka.practicaMambu.domain.activeProducts.dto.LoanClientOnboardingCommand;
import com.sofka.practicaMambu.domain.activeProducts.dto.LoanClientOnboardingResponse;
import com.sofka.practicaMambu.domain.dto.ClientCreateResponseDTO;
import com.sofka.practicaMambu.domain.dto.DepositClientOnboardingCommand;
import com.sofka.practicaMambu.domain.dto.DepositClientOnboardingResponse;
import com.sofka.practicaMambu.domain.model.Client;


public interface ClientService {

    ClientCreateResponseDTO createClient(Client client);

    DepositClientOnboardingResponse activateDepositClient(DepositClientOnboardingCommand command);

    LoanClientOnboardingResponse activateLoanClient(LoanClientOnboardingCommand command);
}
