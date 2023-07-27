package com.sofka.practicaMambu.domain.model.activeProducts;

public class LoanScheduleSettings {
    private int gracePeriod;
    private int repaymentInstallments;

    public int getGracePeriod() {
        return gracePeriod;
    }

    public void setGracePeriod(int gracePeriod) {
        this.gracePeriod = gracePeriod;
    }

    public int getRepaymentInstallments() {
        return repaymentInstallments;
    }

    public void setRepaymentInstallments(int repaymentInstallments) {
        this.repaymentInstallments = repaymentInstallments;
    }
}
