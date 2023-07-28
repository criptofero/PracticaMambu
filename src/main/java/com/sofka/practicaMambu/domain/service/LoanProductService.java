package com.sofka.practicaMambu.domain.service;

import com.sofka.practicaMambu.domain.activeProducts.dto.*;
import com.sofka.practicaMambu.domain.dto.LockAccountResponse;
import com.sofka.practicaMambu.domain.model.activeProducts.LoanAccount;

public interface LoanProductService {
    LoanProductResponse getLoanProductById(String productId);
    LoanAccountQueryResponse getLoanAccountById(String accountKey);
    LoanAccountResponse createLoanAccount(CreateLoanAccountCommand createAccountCommand);
    LoanAccountResponse approveLoanAccount(String accountKey, String approveNotes);
    LoanDisbursementResponse disburseLoan(String accountKey, String disburseNotes);

    LoanAccountQueryResponse lockLoanAccount(String accountKey, LockLoanCommand lockLoanCommand);
}
