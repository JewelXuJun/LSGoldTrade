package com.jme.common.util;

import java.io.Serializable;

public class KChartVo implements Serializable {

    /**
     * "quoteTime ":"20181202",
     * "openPrice":30000,
     * "highestPrice ":30000,
     * "lowestPrice ":30000,
     * "closePrice":30000,
     * "turnover": 18870597
     * "turnVolume ":30000
     */

    private String quoteTime;

    private long openPrice;

    private long highestPrice;

    private long lowestPrice;

    private long closePrice;

    private long turnover;

    private long turnVolume;

    public String getQuoteTime() {
        return quoteTime;
    }

    public void setQuoteTime(String quoteTime) {
        this.quoteTime = quoteTime;
    }

    public long getOpenPrice() {
        return openPrice;
    }

    public void setOpenPrice(long openPrice) {
        this.openPrice = openPrice;
    }

    public long getHighestPrice() {
        return highestPrice;
    }

    public void setHighestPrice(long highestPrice) {
        this.highestPrice = highestPrice;
    }

    public long getLowestPrice() {
        return lowestPrice;
    }

    public void setLowestPrice(long lowestPrice) {
        this.lowestPrice = lowestPrice;
    }

    public long getClosePrice() {
        return closePrice;
    }

    public void setClosePrice(long closePrice) {
        this.closePrice = closePrice;
    }

    public long getTurnover() {
        return turnover;
    }

    public void setTurnover(long turnover) {
        this.turnover = turnover;
    }

    public long getTurnVolume() {
        return turnVolume;
    }

    public void setTurnVolume(long turnVolume) {
        this.turnVolume = turnVolume;
    }

}
