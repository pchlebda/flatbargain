package com.flat.bargain;

import com.flat.bargain.model.Flat;
import com.flat.bargain.model.FlatStatistics;
import com.flat.bargain.parser.GratkaPageNumberParser;
import com.flat.bargain.parser.GratkaPageParser;
import com.flat.bargain.parser.PageNumberParser;
import com.flat.bargain.runner.BasicOfferRunner;
import com.flat.bargain.runner.OfferRunner;
import com.flat.bargain.runner.RunConfiguration;
import com.flat.bargain.runner.Website;
import com.flat.bargain.service.FileSaver;
import com.flat.bargain.service.FlatBargainService;
import com.flat.bargain.service.FlatBargainServiceImp;
import com.flat.bargain.task.WebCrawlerTask;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class Main {

    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
        int PAGES = 598;


        List<String> urls = new ArrayList<>();
        ExecutorService executorService = Executors.newCachedThreadPool();
        for (int i = 1; i <= PAGES; ++i) {
            // String url = "http://dom.gratka.pl/mieszkania-sprzedam/lista/,katowice,150000,40," + i + ",2000,cd,li,s,cmo.html";
            //String url = "http://dom.gratka.pl/mieszkania-sprzedam/lista/,krakow,200000,40," + i + ",3500,cd,li,s,cmo.html";
            //  String url = "http://dom.gratka.pl/mieszkania-sprzedam/lista/,krakow,nowa_huta,200000,40" + i + ",3500,dz,cd,li,s,cmo.html";
            String url = "http://dom.gratka.pl/mieszkania-sprzedam/lista/malopolskie,krakow,40,"+i+",3500,li,s,cmo.html";

            urls.add(url);
        }

        List<Future<List<Flat>>> futures = new ArrayList<>();

        for (int i = 0; i < PAGES; ++i) {
            Callable<List<Flat>> callable = new WebCrawlerTask(urls.get(i));
            Future<List<Flat>> future = executorService.submit(callable);
            futures.add(future);
        }

        List<Flat> allResult = new ArrayList<>();
        for (Future<List<Flat>> future : futures) {
            allResult.addAll(future.get());
        }
        executorService.shutdown();

        FlatBargainService flatBargainService = new FlatBargainServiceImp();
        FlatStatistics flatStatistic = flatBargainService.getFlatStatistic(allResult);
        System.out.println(flatStatistic);
        List<Flat> bestOffer = flatBargainService.getBestOffer(allResult, 10);
        FileSaver fileSaver = new FileSaver();
        fileSaver.saveToFile(bestOffer, "moje_oferty.txt");


/*        OfferRunner offerRunner = new BasicOfferRunner();
        RunConfiguration runConfiguration = new RunConfiguration();

        runConfiguration.setSquareMeter(56);
        runConfiguration.setTotalPrice(new BigDecimal(500000));
        runConfiguration.setSquareMeterPrice(new BigDecimal(3500));
        runConfiguration.setWebsite(Website.GRATKA);

        FlatStatistics flatStatistics = offerRunner.execute(runConfiguration);

        System.out.println(flatStatistics);*/
    }

}
