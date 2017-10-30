package com.flat.bargain.parser;

import com.flat.bargain.model.Flat;
import com.flat.bargain.model.FlatStatistics;
import com.flat.bargain.service.FileSaver;
import com.flat.bargain.service.FlatBargainService;
import com.flat.bargain.service.FlatBargainServiceImp;
import com.flat.bargain.task.WebCrawlerTask;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {


    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {

        int PAGES = 323;


        List<String> urls = new ArrayList<>();
        ExecutorService executorService = Executors.newCachedThreadPool();
        for (int i = 1; i <= PAGES; ++i) {
            // String url = "http://dom.gratka.pl/mieszkania-sprzedam/lista/,katowice,150000,40," + i + ",2000,cd,li,s,cmo.html";
            String url = "https://www.otodom.pl/sprzedaz/mieszkanie/krakow/?filter_float_price%3Ato=160000&filter_float_price_per_m%3Afrom=3500&filter_float_m%3Afrom=22&description=1&dist=0&subregion_id=410&city_id=38&nrAdsPerPage=" + i;
            //  String url = "http://dom.gratka.pl/mieszkania-sprzedam/lista/,krakow,nowa_huta,200000,40" + i + ",3500,dz,cd,li,s,cmo.html";
            urls.add(url);
        }

        List<Future<List<Flat>>> futures = new ArrayList<>();

        for (int i = 0; i < PAGES; ++i) {
            Callable<List<Flat>> callable = new WebCrawlerTask(urls.get(i), new OttoPageParser());
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
        fileSaver.saveToFile(bestOffer, "otto.txt");

    }
}
