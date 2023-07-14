package com.sofka.practicaMambu.domain.service;

import com.sofka.practicaMambu.domain.dto.*;
import com.sofka.practicaMambu.domain.model.DepositAccount;
import com.sofka.practicaMambu.domain.model.DepositTransaction;
import com.sofka.practicaMambu.domain.model.TransactionFilterInfo;

public interface DepositProductService {
    DepositProductResponse getDepositProductById(String productId);

    CreateDepositAccountResponse createAccount(DepositAccount account);

    CreateDepositTransactionResponse makeDeposit(DepositTransaction deposit, String parentAccountKey);

    CreateDepositTransactionResponse makeWithdrawal(DepositTransaction withdrawal, String parentAccountKey);

    TransactionsQueryResponse getAccountTransactions(TransactionFilterInfo transactionFilterInfo);

    DepositAccount getAccountById(String accountKey);

    ApplySeizureResponse applyAccountBalanceBlockAndSeizure(DepositBalanceBlockCommand blockCommand);
}
