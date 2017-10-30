package com.flat.bargain.parser;

import com.flat.bargain.model.Flat;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.math.BigDecimal;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class OttoPageParser implements PageParser {

    private static final Pattern DIGIT_PATTERN = Pattern.compile("\\d+");

    @Override
    public List<Flat> getAdvertUrls(Document document) {
        Elements elements = document.getElementsByClass("offer-item-details");
        return elements.stream().map(this::parse).collect(Collectors.toList());
    }

    private Flat parse(Element offerElement) {
        Integer roomsNumber = getRoomsNumber(offerElement);
        BigDecimal totalPrice = getTotalPrice(offerElement);
        Double squareMeters = getSquareMeters(offerElement);
        BigDecimal pricePerSquareMeter = getPricePerSquareMeter(offerElement);
        String offerURL = getOfferURL(offerElement);
        return new Flat.Builder()
                .withRoomsNumber(roomsNumber)
                .withTotalPrice(totalPrice)
                .withUrl(offerURL)
                .withArea(squareMeters)
                .withSquareMeterPrice(pricePerSquareMeter)
                .build();
    }

    private String getOfferURL(Element offerElement) {
        return offerElement.select("a").attr("abs:href");
    }

    private Integer getRoomsNumber(Element offerElement) {
        Element first = offerElement.getElementsByClass("offer-item-rooms").first();
        String text = first.text();
        Matcher matcher = DIGIT_PATTERN.matcher(text);
        matcher.find();
        return Integer.valueOf(matcher.group());
    }

    private BigDecimal getTotalPrice(Element offerElement) {
        Element totalPriceElement = offerElement.getElementsByClass("offer-item-price").first();
        String priceWithCurrency = totalPriceElement.text().replaceAll("\\s+", "");
        Matcher matcher = DIGIT_PATTERN.matcher(priceWithCurrency);
        matcher.find();
        return new BigDecimal(matcher.group());
    }

    private Double getSquareMeters(Element offerElement) {
        Element squareMeters = offerElement.getElementsByClass("offer-item-area").first();
        String squareMetersFull = squareMeters.text().replaceAll("\\s+", "");
        Matcher matcher = DIGIT_PATTERN.matcher(squareMetersFull);
        matcher.find();
        return Double.valueOf(matcher.group());
    }

    private BigDecimal getPricePerSquareMeter(Element offerElement) {
        Element squareMetersPrice = offerElement.getElementsByClass("offer-item-price-per-m").first();
        String squareMetersPriceFull = squareMetersPrice.text().replaceAll("\\s+", "");
        Matcher matcher = DIGIT_PATTERN.matcher(squareMetersPriceFull);
        matcher.find();
        return new BigDecimal(matcher.group());
    }
}
