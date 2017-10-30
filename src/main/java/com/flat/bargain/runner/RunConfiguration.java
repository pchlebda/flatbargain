package com.flat.bargain.runner;

import java.math.BigDecimal;

public class RunConfiguration {

    private Website website;
    private BigDecimal totalPrice;
    private BigDecimal squareMeterPrice;
    private Integer squareMeter;
    private Integer websitePages;

    public RunConfiguration() {
    }

    public Website getWebsite() {
        return website;
    }

    public void setWebsite(Website website) {
        this.website = website;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public BigDecimal getSquareMeterPrice() {
        return squareMeterPrice;
    }

    public void setSquareMeterPrice(BigDecimal squareMeterPrice) {
        this.squareMeterPrice = squareMeterPrice;
    }

    public Integer getSquareMeter() {
        return squareMeter;
    }

    public void setSquareMeter(Integer squareMeter) {
        this.squareMeter = squareMeter;
    }

    public Integer getWebsitePages() {
        return websitePages;
    }

    public void setWebsitePages(Integer websitePages) {
        this.websitePages = websitePages;
    }
}
