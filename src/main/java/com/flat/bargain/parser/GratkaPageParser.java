package com.flat.bargain.parser;

import com.flat.bargain.model.Flat;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

public class GratkaPageParser implements PageParser {

    private static final String ADVERT_LI_CLASS = "ogloszenie";

    public List<Flat> getAdvertUrls(Document document) {
        Elements advertElements = document.getElementsByClass(ADVERT_LI_CLASS);
        return Objects.isNull(advertElements) ? null :
                advertElements
                        .stream()
                        .map(this::parse)
                        .filter(Objects::nonNull)
                        .collect(toList());
    }

    private Flat parse(Element flatElement) {
        final String url = flatElement.select("a").attr("abs:href");
        Element priceElement = flatElement.getElementsByClass("price").first();
        Elements spanElements = getSpanDetails(flatElement);
        if (shouldSkip(spanElements)) {
            return null;
        }

        FlatType flatType = selectFlatType(spanElements);

        BigDecimal totalPrice = getTotalPrice(priceElement);
        BigDecimal squarePerMeter = getPricePerSquareMeter(spanElements, flatType);
        Double area = getArea(spanElements, flatType);
        Integer year = getYear(spanElements, flatType);
        Integer roomsNumber = getRoomsNumber(spanElements);

        return new Flat.Builder()
                .withUrl(url)
                .withTotalPrice(totalPrice)
                .withSquareMeterPrice(squarePerMeter)
                .withArea(area)
                .withYear(year)
                .withRoomsNumber(roomsNumber)
                .build();
    }

    private BigDecimal getTotalPrice(Element priceElement) {
        try {
            return Optional.ofNullable(priceElement).map(element -> element.getElementsByTag("b"))
                    .map(Elements::first)
                    .map(Element::text)
                    .map(price -> price.replaceAll("\\s+", ""))
                    .map(BigDecimal::new)
                    .orElse(null);

        } catch (NumberFormatException e) {
            return null;
        }
    }

    private BigDecimal getPricePerSquareMeter(Elements spanElements, FlatType flatElement) {
        int indexForPricePerSquareMeter = getIndexForPricePerSquareMeter(flatElement);
        String firstPart = Optional.ofNullable(spanElements).filter(elements -> elements.size() > indexForPricePerSquareMeter)
                .map(elements -> elements.get(indexForPricePerSquareMeter - 1))
                .map(Element::text)
                .orElse(null);

        String secondPart = Optional.ofNullable(spanElements).filter(elements -> elements.size() > indexForPricePerSquareMeter)
                .map(elements -> elements.get(indexForPricePerSquareMeter))
                .map(Element::text)
                .orElse(null);
        if (Objects.nonNull(firstPart) && Objects.nonNull(secondPart)) {
            String squarePerMeterAsString = firstPart + secondPart;
            try {
                return new BigDecimal(squarePerMeterAsString);
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }

    private int getIndexForPricePerSquareMeter(FlatType flatType) {
        return 4 - flatType.getValue();
    }

    private Double getArea(Elements spanElements, FlatType flatType) {
        int areaIndex = getAreaIndex(flatType);
        try {
            return Optional.ofNullable(spanElements).filter(elements -> elements.size() > areaIndex)
                    .map(elements -> elements.get(areaIndex))
                    .map(element -> element.getElementsByTag("b"))
                    .filter(elements -> elements.size() > 0)
                    .map(Elements::first)
                    .map(Element::text)
                    .map(area -> area.replace(",", "."))
                    .map(Double::valueOf)
                    .orElse(null);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private int getAreaIndex(FlatType flatType) {
        return 5 - flatType.getValue();
    }

    private Integer getYear(Elements spanElements, FlatType flatType) {
        int yearIndex = getYearIndex(flatType);
        return Optional.ofNullable(spanElements).filter(elements -> elements.size() > yearIndex)
                .map(elements -> elements.get(yearIndex))
                .map(Element::text)
                .map(Integer::valueOf)
                .orElse(null);
    }

    private int getYearIndex(FlatType flatType) {
        return 2 - flatType.getValue();
    }

    private Integer getRoomsNumber(Elements spanElements) {
        return Optional.ofNullable(spanElements).filter(elements -> elements.size() > 0)
                .map(elements -> elements.get(0))
                .map(Element::text)
                .map(Integer::valueOf)
                .orElse(null);
    }

    private Elements getSpanDetails(Element flatElement) {
        Element infoData = flatElement.getElementsByClass("infoDane").first();
        return Optional.ofNullable(infoData)
                .map(element -> element.getElementsByTag("span"))
                .orElse(null);
    }

    private FlatType selectFlatType(Elements spanElements) {
        if (spanElements.size() == 6) {
            return FlatType.NORMAL;
        } else if (spanElements.size() == 5) {
            return FlatType.GROUND;
        } else {
            throw new RuntimeException("Invalid span elements - " + spanElements);
        }
    }

    private boolean shouldSkip(Elements spanElement) {
        int size = spanElement.size();
        return size < 5 || size > 6;
    }

}
