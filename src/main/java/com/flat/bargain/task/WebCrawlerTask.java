package com.flat.bargain.task;

import com.flat.bargain.model.Flat;
import com.flat.bargain.parser.GratkaPageParser;
import com.flat.bargain.parser.PageParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.List;
import java.util.concurrent.Callable;

public class WebCrawlerTask implements Callable<List<Flat>> {

    private String url;
    private PageParser pageParser;

    public WebCrawlerTask(String url) {
        this.url = url;
        pageParser = new GratkaPageParser();
    }


    public WebCrawlerTask(String url, PageParser pageParser) {
        this.url = url;
        this.pageParser = pageParser;
    }

    public List<Flat> call() throws Exception {
        Document doc = Jsoup.connect(url).get();
        return pageParser.getAdvertUrls(doc);
    }
}
