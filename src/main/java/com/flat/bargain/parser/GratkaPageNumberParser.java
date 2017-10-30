package com.flat.bargain.parser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class GratkaPageNumberParser implements PageNumberParser {

    private static final String PAGE_CSS_ELEMENT = "stronicowanie";

    @Override
    public Integer getPageNumber(Document document) {
        Element first = document.getElementsByClass(PAGE_CSS_ELEMENT).first();
        Element ulElement = first.getElementsByTag("ul").first();
        Elements liElement = ulElement.getElementsByTag("li");
        Element element = liElement.get(liElement.size() - 2);
        Element aElement = element.getElementsByTag("a").first();
        return Integer.valueOf(aElement.text());
    }
}
