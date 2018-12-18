package com.jme.lsgoldtrade.domain;

import java.io.Serializable;

public class DetailVo implements Serializable {

    /**
     *  "id": 53411,
     *  "quoteTime": "2018-12-11 11:21:43",
     *  "latestPrice": 27405,
     *  "turnVolume": 2
     */

    private int id;

    private String quoteTime;

    private long latestPrice;

    private int turnVolume;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuoteTime() {
        return quoteTime;
    }

    public void setQuoteTime(String quoteTime) {
        this.quoteTime = quoteTime;
    }

    public long getLatestPrice() {
        return latestPrice;
    }

    public void setLatestPrice(long latestPrice) {
        this.latestPrice = latestPrice;
    }

    public int getTurnVolume() {
        return turnVolume;
    }

    public void setTurnVolume(int turnVolume) {
        this.turnVolume = turnVolume;
    }
}
