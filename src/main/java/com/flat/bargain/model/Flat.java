package com.flat.bargain.model;

import java.math.BigDecimal;

public class Flat {

    private BigDecimal totalPrice;
    private BigDecimal squareMeterPrice;
    private Integer roomsNumber;
    private Double area;
    private Integer year;
    private String url;

    public Flat(Builder builder) {
        this.totalPrice = builder.totalPrice;
        this.squareMeterPrice = builder.squareMeterPrice;
        this.roomsNumber = builder.roomsNumber;
        this.area = builder.area;
        this.year = builder.year;
        this.url = builder.url;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public BigDecimal getSquareMeterPrice() {
        return squareMeterPrice;
    }

    public Integer getRoomsNumber() {
        return roomsNumber;
    }

    public Double getArea() {
        return area;
    }

    public String getUrl() {
        return url;
    }

    public Integer getYear() {
        return year;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Flat flat = (Flat) o;

        return url != null ? url.equals(flat.url) : flat.url == null;
    }

    @Override
    public int hashCode() {
        return url != null ? url.hashCode() : 0;
    }

    public static class Builder {

        private BigDecimal totalPrice;
        private BigDecimal squareMeterPrice;
        private Integer roomsNumber;
        private Double area;
        private Integer year;
        private String url;

        public Builder() {
        }

        public Builder withTotalPrice(BigDecimal totalPrice) {
            this.totalPrice = totalPrice;
            return this;
        }

        public Builder withSquareMeterPrice(BigDecimal squareMeterPrice) {
            this.squareMeterPrice = squareMeterPrice;
            return this;
        }

        public Builder withRoomsNumber(Integer roomsNumber) {
            this.roomsNumber = roomsNumber;
            return this;
        }

        public Builder withArea(Double area) {
            this.area = area;
            return this;
        }

        public Builder withUrl(String url) {
            this.url = url;
            return this;
        }

        public Builder withYear(Integer year) {
            this.year = year;
            return this;
        }

        public Flat build() {
            return new Flat(this);
        }

    }
}
