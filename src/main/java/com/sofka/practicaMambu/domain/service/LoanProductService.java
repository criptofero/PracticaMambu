package com.sofka.practicaMambu.domain.service;

import com.sofka.practicaMambu.domain.activeProducts.dto.*;

public interface LoanProductService {
    LoanProductResponse getLoanProductById(String productId);
    LoanAccountResponse createLoanAccount(CreateLoanAccountCommand createAccountCommand);
    LoanAccountResponse approveLoanAccount(String accountKey, String approveNotes);
    LoanDisbursementResponse disburseLoan(String accountKey, String disburseNotes);
}
