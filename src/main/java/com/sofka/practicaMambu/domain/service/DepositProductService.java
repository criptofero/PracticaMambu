package com.sofka.practicaMambu.domain.service;

import com.sofka.practicaMambu.domain.dto.CreateDepositAccountResponse;
import com.sofka.practicaMambu.domain.dto.CreateDepositTransactionResponse;
import com.sofka.practicaMambu.domain.dto.DepositProductResponse;
import com.sofka.practicaMambu.domain.model.DepositAccount;
import com.sofka.practicaMambu.domain.model.DepositTransaction;

public interface DepositProductService {
    DepositProductResponse GetDepositProductById(String productId);

    CreateDepositAccountResponse CreateAccount(DepositAccount account);

    CreateDepositTransactionResponse MakeDeposit(DepositTransaction deposit, String parentAccountKey);
}
