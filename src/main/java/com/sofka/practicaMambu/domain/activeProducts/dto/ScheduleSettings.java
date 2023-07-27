package com.sofka.practicaMambu.domain.activeProducts.dto;

public class ScheduleSettings {
    public NumericSettingsRange getNumInstallments() {
        return numInstallments;
    }

    public void setNumInstallments(NumericSettingsRange numInstallments) {
        this.numInstallments = numInstallments;
    }

    private NumericSettingsRange numInstallments;
}
