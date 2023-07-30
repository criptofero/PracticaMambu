package com.sofka.practicaMambu.domain.model.activeProducts;

import java.math.BigDecimal;

public class AmountDetail {
    private BigDecimal expected;
    private BigDecimal paid;
    private BigDecimal due;

    public BigDecimal getExpected() {
        return expected;
    }

    public void setExpected(BigDecimal expected) {
        this.expected = expected;
    }

    public BigDecimal getPaid() {
        return paid;
    }

    public void setPaid(BigDecimal paid) {
        this.paid = paid;
    }

    public BigDecimal getDue() {
        return due;
    }

    public void setDue(BigDecimal due) {
        this.due = due;
    }
}
