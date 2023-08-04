package com.sofka.practicaMambu.domain.model;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransactionDetail {
    private String transactionChannelId;

    public String getTransactionChannelKey() {
        return transactionChannelKey;
    }

    public void setTransactionChannelKey(String transactionChannelKey) {
        this.transactionChannelKey = transactionChannelKey;
    }

    private String transactionChannelKey;

    public String getTransactionChannelId() {
        return transactionChannelId;
    }

    public void setTransactionChannelId(String transactionChannelId) {
        this.transactionChannelId = transactionChannelId;
    }
}
