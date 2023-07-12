package com.sofka.practicaMambu.domain.service;

import com.sofka.practicaMambu.domain.dto.*;
import com.sofka.practicaMambu.domain.model.DepositAccount;
import com.sofka.practicaMambu.domain.model.DepositTransaction;

public interface DepositProductService {
    DepositProductResponse GetDepositProductById(String productId);

    CreateDepositAccountResponse CreateAccount(DepositAccount account);

    CreateDepositTransactionResponse MakeDeposit(DepositTransaction deposit, String parentAccountKey);

    CreateDepositTransactionResponse MakeWithdrawal(DepositTransaction withdrawal, String parentAccountKey);
}
