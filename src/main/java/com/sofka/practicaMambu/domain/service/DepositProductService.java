package com.sofka.practicaMambu.domain.service;

import com.sofka.practicaMambu.domain.dto.DepositProductResponse;

public interface DepositProductService {
    DepositProductResponse GetDepositProductById(String productId);
}
