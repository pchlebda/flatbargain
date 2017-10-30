package com.flat.bargain.service;

import com.flat.bargain.comparators.SquareMeterComparator;
import com.flat.bargain.comparators.TotalPriceComparator;
import com.flat.bargain.model.Flat;
import com.flat.bargain.model.FlatStatistics;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.*;
import java.util.stream.Collectors;

public class FlatBargainServiceImp implements FlatBargainService {
    @Override
    public FlatStatistics getFlatStatistic(List<Flat> flats) {

        flats = flats.stream().filter(flat -> Objects.nonNull(flat.getSquareMeterPrice())).collect(Collectors.toList());
        Comparator<Flat> cmp = new SquareMeterComparator();

        Flat min = Collections.min(flats, cmp);
        Flat max = Collections.max(flats, cmp);
        double avg = flats.stream().map(Flat::getSquareMeterPrice).filter(Objects::nonNull).mapToDouble(BigDecimal::doubleValue).average().getAsDouble();
        List<BigDecimal> collect = flats.stream().sorted(cmp).map(Flat::getSquareMeterPrice).filter(Objects::nonNull).collect(Collectors.toList());
        int size = collect.size();
        BigDecimal mean = null;
        if (size % 2 == 1) {
            mean = collect.get(size / 2);
        } else {
            BigDecimal first = collect.get(size / 2);
            BigDecimal second = collect.get((size / 2) - 1);
            mean = first.add(second).divide(new BigDecimal(2), new MathContext(2));
        }


        return new FlatStatistics(new BigDecimal(avg), min, max, mean);
    }

    @Override
    public List<Flat> getBestOffer(List<Flat> flats, int number) {
       Comparator<Flat> cmp = new SquareMeterComparator();
       //Comparator<Flat> cmp = new TotalPriceComparator();

        List<Flat> list = flats.stream().distinct().collect(Collectors.toList());
        Collections.sort(list, cmp);
        return list.subList(0, number);
    }


}
