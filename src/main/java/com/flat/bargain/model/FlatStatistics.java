package com.flat.bargain.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class FlatStatistics {

    private BigDecimal average;
    private Flat min;
    private Flat max;
    private BigDecimal mean;

    public FlatStatistics(BigDecimal average, Flat min,Flat max, BigDecimal mean) {
        this.average = average;
        this.min = min;
        this.mean = mean;
        this.max = max;
    }

    public BigDecimal getAverage() {
        return average;
    }

    public Flat getMin() {
        return min;
    }

    public BigDecimal getMean() {
        return mean;
    }

    @Override
    public String toString() {
        return "FlatStatistics{" +
                "srednia cena=" + average.setScale(2, RoundingMode.HALF_UP) +
                ", minimalna=" + min.getSquareMeterPrice() +
                ",maksymalna="+max.getSquareMeterPrice()+
                ", mediana=" + mean +
                '}';
    }
}
