package com.sofka.practicaMambu.domain.service;

import com.sofka.practicaMambu.domain.activeProducts.dto.CreateLoanAccountCommand;
import com.sofka.practicaMambu.domain.activeProducts.dto.LoanAccountResponse;
import com.sofka.practicaMambu.domain.activeProducts.dto.LoanProductResponse;

public interface LoanProductService {
    LoanProductResponse getLoanProductById(String productId);
    LoanAccountResponse createLoanAccount(CreateLoanAccountCommand createAccountCommand);
    LoanAccountResponse approveLoanAccount(String accountKey, String createNotes);
}
