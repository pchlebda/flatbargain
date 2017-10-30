package com.flat.bargain.parser;

import org.jsoup.nodes.Document;

public interface PageNumberParser {

    Integer getPageNumber(Document document);
}
