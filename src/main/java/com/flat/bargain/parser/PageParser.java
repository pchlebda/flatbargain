package com.flat.bargain.parser;

import com.flat.bargain.model.Flat;
import org.jsoup.nodes.Document;

import java.util.List;

public interface PageParser {


    List<Flat> getAdvertUrls(final Document document);
}
