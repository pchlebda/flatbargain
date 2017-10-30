package com.flat.bargain.runner;

import com.flat.bargain.model.Flat;
import com.flat.bargain.model.FlatStatistics;
import com.flat.bargain.parser.GratkaPageNumberParser;
import com.flat.bargain.parser.PageNumberParser;
import com.flat.bargain.service.FileSaver;
import com.flat.bargain.service.FlatBargainService;
import com.flat.bargain.service.FlatBargainServiceImp;
import com.flat.bargain.task.WebCrawlerTask;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class GratkaRunner implements OfferRunner {

    private static final String URL_TEMPLATE = "http://dom.gratka.pl/mieszkania-sprzedam/lista/,krakow,%s,40,%s,%s,%s,cd,li,s,cmo,mo.html";
    private ExecutorService executorService;

    public GratkaRunner() {
        executorService = Executors.newCachedThreadPool();
    }

    @Override
    public FlatStatistics execute(RunConfiguration runConfiguration) {

        BigDecimal totalPrice = runConfiguration.getTotalPrice();
        BigDecimal squareMeterPrice = runConfiguration.getSquareMeterPrice();
        Integer squareMeter = runConfiguration.getSquareMeter();
        Integer websitePages = 0;
        String template = String.format(URL_TEMPLATE, totalPrice, 1, squareMeterPrice, squareMeter);


        PageNumberParser pageNumberParser = new GratkaPageNumberParser();
        try {
            System.out.println(template);
            websitePages = pageNumberParser.getPageNumber(Jsoup.connect(template).get());
            System.out.println(websitePages);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        List<String> urls = new ArrayList<>();

        for (int i = 1; i < websitePages; ++i) {
            template = String.format(URL_TEMPLATE, totalPrice, i, squareMeterPrice, squareMeter);
            urls.add(template);
        }
        List<Future<List<Flat>>> futures = new ArrayList<>();


        for (int i = 0; i < websitePages - 1; ++i) {
            Callable<List<Flat>> callable = new WebCrawlerTask(urls.get(i));
            Future<List<Flat>> future = executorService.submit(callable);
            futures.add(future);
        }


        List<Flat> allResult = new ArrayList<>();
        for (Future<List<Flat>> future : futures) {
            try {
                allResult.addAll(future.get());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        executorService.shutdown();

        FlatBargainService flatBargainService = new FlatBargainServiceImp();
        FlatStatistics flatStatistic = flatBargainService.getFlatStatistic(allResult);
        System.out.println(flatStatistic);
        List<Flat> bestOffer = flatBargainService.getBestOffer(allResult, 10);
        FileSaver fileSaver = new FileSaver();
        try {
            fileSaver.saveToFile(bestOffer, "moje_oferty.txt");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return flatStatistic;
    }
}
