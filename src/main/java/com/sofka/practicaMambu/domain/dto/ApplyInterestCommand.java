package com.sofka.practicaMambu.domain.dto;

public class ApplyInterestCommand {
    private String interestApplicationDate;
    private Boolean isPaymentHolidaysInterest;
    private String notes;
    private Long paymentHolidaysInterestAmount;

    public String getInterestApplicationDate() {
        return interestApplicationDate;
    }

    public void setInterestApplicationDate(String interestApplicationDate) {
        this.interestApplicationDate = interestApplicationDate;
    }

    public Boolean isPaymentHolidaysInterest() {
        return isPaymentHolidaysInterest;
    }

    public void setPaymentHolidaysInterest(Boolean paymentHolidaysInterest) {
        isPaymentHolidaysInterest = paymentHolidaysInterest;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Long getPaymentHolidaysInterestAmount() {
        return paymentHolidaysInterestAmount;
    }

    public void setPaymentHolidaysInterestAmount(Long paymentHolidaysInterestAmount) {
        this.paymentHolidaysInterestAmount = paymentHolidaysInterestAmount;
    }
}
