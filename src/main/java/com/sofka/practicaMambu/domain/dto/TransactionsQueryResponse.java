package com.sofka.practicaMambu.domain.dto;

import com.sofka.practicaMambu.domain.model.DepositTransaction;

public class TransactionsQueryResponse {
    public DepositTransaction[] getTransactions() {
        return transactions;
    }

    public void setTransactions(DepositTransaction[] transactions) {
        this.transactions = transactions;
    }

    private DepositTransaction[] transactions;
}
