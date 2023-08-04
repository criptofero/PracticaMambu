package com.sofka.practicaMambu.domain.activeProducts.dto;

public class LoanDetailQueryResponse {
    private LoanAccountQueryResponse loanAccountInfo;
    private LoanTransactionQueryResponse disbursementDetails;
    private LoanTransactionQueryResponse repayments;
    private LoanScheduleQueryResponse schedule;

    public LoanAccountQueryResponse getLoanAccountInfo() {
        return loanAccountInfo;
    }

    public void setLoanAccountInfo(LoanAccountQueryResponse loanAccountInfo) {
        this.loanAccountInfo = loanAccountInfo;
    }

    public LoanTransactionQueryResponse getDisbursementDetails() {
        return disbursementDetails;
    }

    public void setDisbursementDetails(LoanTransactionQueryResponse disbursementDetails) {
        this.disbursementDetails = disbursementDetails;
    }

    public LoanTransactionQueryResponse getRepayments() {
        return repayments;
    }

    public void setRepayments(LoanTransactionQueryResponse repayments) {
        this.repayments = repayments;
    }

    public LoanScheduleQueryResponse getSchedule() {
        return schedule;
    }

    public void setSchedule(LoanScheduleQueryResponse schedule) {
        this.schedule = schedule;
    }
}
