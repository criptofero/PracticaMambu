package com.sofka.practicaMambu.domain.activeProducts.dto;

import com.sofka.practicaMambu.domain.model.activeProducts.DisbursementDetail;
import com.sofka.practicaMambu.domain.model.activeProducts.InterestSettings;
import com.sofka.practicaMambu.domain.model.activeProducts.LoanScheduleSettings;

public class LoanRefinanceDetail {
    private String productTypeKey;
    private DisbursementDetail disbursementDetails;
    private InterestSettings interestSettings;
    private LoanScheduleSettings scheduleSettings;

    public String getProductTypeKey() {
        return productTypeKey;
    }

    public void setProductTypeKey(String productTypeKey) {
        this.productTypeKey = productTypeKey;
    }

    public DisbursementDetail getDisbursementDetails() {
        return disbursementDetails;
    }

    public void setDisbursementDetails(DisbursementDetail disbursementDetails) {
        this.disbursementDetails = disbursementDetails;
    }

    public InterestSettings getInterestSettings() {
        return interestSettings;
    }

    public void setInterestSettings(InterestSettings interestSettings) {
        this.interestSettings = interestSettings;
    }

    public LoanScheduleSettings getScheduleSettings() {
        return scheduleSettings;
    }

    public void setScheduleSettings(LoanScheduleSettings scheduleSettings) {
        this.scheduleSettings = scheduleSettings;
    }
}
