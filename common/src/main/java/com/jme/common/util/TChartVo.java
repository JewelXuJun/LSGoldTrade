package com.jme.common.util;

import java.io.Serializable;

public class TChartVo implements Serializable {

    /**
     * "quoteTime":"2018-12-21 22:41:00",
     * "closePrice":22405,
     * "turnVolume":46398,
     * "averagePrice":25686,
     * "turnover":1191811824
     */

    private String quoteTime;

    private long closePrice;

    private long turnVolume;

    private long averagePrice;

    private long turnover;

    public String getQuoteTime() {
        return quoteTime;
    }

    public void setQuoteTime(String quoteTime) {
        this.quoteTime = quoteTime;
    }

    public long getClosePrice() {
        return closePrice;
    }

    public void setClosePrice(long closePrice) {
        this.closePrice = closePrice;
    }

    public long getTurnVolume() {
        return turnVolume;
    }

    public void setTurnVolume(long turnVolume) {
        this.turnVolume = turnVolume;
    }

    public long getAveragePrice() {
        return averagePrice;
    }

    public void setAveragePrice(long averagePrice) {
        this.averagePrice = averagePrice;
    }

    public long getTurnover() {
        return turnover;
    }

    public void setTurnover(long turnover) {
        this.turnover = turnover;
    }

}
