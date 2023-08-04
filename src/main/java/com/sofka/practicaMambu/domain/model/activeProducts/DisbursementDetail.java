package com.sofka.practicaMambu.domain.model.activeProducts;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class DisbursementDetail {
    private String expectedDisbursementDate;
    private String firstRepaymentDate;

    public String getExpectedDisbursementDate() {
        return expectedDisbursementDate;
    }

    public void setExpectedDisbursementDate(String expectedDisbursementDate) {
        this.expectedDisbursementDate = expectedDisbursementDate;
    }

    public String getFirstRepaymentDate() {
        return firstRepaymentDate;
    }

    public void setFirstRepaymentDate(String firstRepaymentDate) {
        this.firstRepaymentDate = firstRepaymentDate;
    }
}
