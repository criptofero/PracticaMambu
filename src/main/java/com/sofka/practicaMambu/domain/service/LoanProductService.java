package com.sofka.practicaMambu.domain.service;

import com.sofka.practicaMambu.domain.activeProducts.dto.*;
import com.sofka.practicaMambu.domain.model.TransactionFilterInfo;

public interface LoanProductService {
    LoanProductResponse getLoanProductById(String productId);
    LoanAccountQueryResponse getLoanAccountById(String accountKey);
    LoanAccountResponse createLoanAccount(CreateLoanAccountCommand createAccountCommand);
    LoanAccountResponse approveLoanAccount(String accountKey, LoanActionCommand approveCommand);
    LoanDisbursementResponse disburseLoan(String accountKey, LoanActionCommand disburseCommand);
    LoanAccountQueryResponse lockLoanAccount(String accountKey, LoanActionCommand lockLoanCommand);
    LoanAccountQueryResponse payoffLoanAccount(String accountKey, LoanActionCommand payoffLoanCommand);
    MakeRepaymentResponse makeLoanRepayment(String accountKey, RepaymentCommand repaymentCommand);
    LoanTransactionQueryResponse getLoanTransactions(TransactionFilterInfo transactionFilterInfo, int rowsLimit);
    LoanTransactionQueryResponse getLoanDisbursement(String accountKey);
    LoanTransactionQueryResponse getLoanRepayments(String accountKey);
    LoanScheduleQueryResponse getLoanRepaymentsSchedule(String accountKey);
}
