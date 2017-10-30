package com.flat.bargain.comparators;

import com.flat.bargain.model.Flat;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Objects;

public class TotalPriceComparator implements Comparator<Flat> {


    @Override
    public int compare(Flat o1, Flat o2) {
        BigDecimal firstTotalPrice = o1.getTotalPrice();
        BigDecimal secondTotalPrice = o2.getTotalPrice();
        if (Objects.isNull(firstTotalPrice) && Objects.isNull(secondTotalPrice)) {
            return 0;
        }
        if (Objects.isNull(firstTotalPrice)) {
            return 1;
        }
        if (Objects.isNull(secondTotalPrice)) {
            return -1;
        }
        return firstTotalPrice.compareTo(secondTotalPrice);
    }
}
