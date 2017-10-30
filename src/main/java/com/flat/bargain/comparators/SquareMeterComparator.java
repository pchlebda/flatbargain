package com.flat.bargain.comparators;

import com.flat.bargain.model.Flat;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Objects;

public class SquareMeterComparator implements Comparator<Flat> {
    @Override
    public int compare(Flat o1, Flat o2) {
        BigDecimal firstSquareMeterPrice = o1.getSquareMeterPrice();
        BigDecimal secondSquareMeterPrice = o2.getSquareMeterPrice();
        if (Objects.isNull(firstSquareMeterPrice) && Objects.isNull(secondSquareMeterPrice)) {
            return 0;
        }
        if (Objects.isNull(firstSquareMeterPrice)) {
            return 1;
        }
        if (Objects.isNull(secondSquareMeterPrice)) {
            return -1;
        }
        return firstSquareMeterPrice.compareTo(secondSquareMeterPrice);
    }
}
