package com.flat.bargain.service;

import com.flat.bargain.model.Flat;
import com.flat.bargain.model.FlatStatistics;

import java.util.List;

public interface FlatBargainService {

    FlatStatistics getFlatStatistic(List<Flat> flats);


    List<Flat> getBestOffer(List<Flat> flats, int number);
}
