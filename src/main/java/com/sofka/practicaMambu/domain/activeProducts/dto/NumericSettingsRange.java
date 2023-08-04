package com.sofka.practicaMambu.domain.activeProducts.dto;

public class NumericSettingsRange {
    private Long minValue;

    public Long getMinValue() {
        return minValue;
    }

    public void setMinValue(Long minValue) {
        this.minValue = minValue;
    }

    public Long getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(Long maxValue) {
        this.maxValue = maxValue;
    }

    private Long maxValue;
}
