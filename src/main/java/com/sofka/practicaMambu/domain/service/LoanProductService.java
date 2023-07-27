package com.sofka.practicaMambu.domain.service;

import com.sofka.practicaMambu.domain.activeProducts.dto.LoanProductResponse;

public interface LoanProductService {
    LoanProductResponse getLoanProductById(String productId);
}
