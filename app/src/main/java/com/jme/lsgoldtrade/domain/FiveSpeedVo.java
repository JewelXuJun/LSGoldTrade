package com.jme.lsgoldtrade.domain;

import com.jme.lsgoldtrade.util.MarketUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FiveSpeedVo implements Serializable {

    /**
     * "contractId":"iAu100g",
     * "name":"黄金延期"
     * "lastClosePrice":30000,
     * "lastSettlePrice":30000,
     * "openPrice":30000,
     * "highestPrice":30000,
     * "lowestPrice":30000,
     * "latestPrice":30000,
     * "closePrice":30000,
     * "settlePrice":30000,
     * "bidPrice1":32000,
     * "bidVolume1":100,
     * "bidPrice2":32000,
     * "bidVolume2":100,
     * "bidPrice3":32000,
     * "bidVolume3":100,
     * "bidPrice4":32000,
     * "bidVolume4":100,
     * "bidPrice5":32000,
     * "bidVolume5":100,
     * "askingPrice1":32000,
     * "askingVolume1":100,
     * "askingPrice2":32000,
     * "askingVolume2":100,
     * "askingPrice3":32000,
     * "askingVolume3":100,
     * "askingPrice4":32000,
     * "askingVolume4":100,
     * "askingPrice5":32000,
     * "askingVolume5":100,
     * "turnVolume":32000,
     * "tradeWeight":100,
     * "highLimitPrice":100,
     * "lowerLimitPrice":100,
     * "position":100,
     * "upDown":100,
     * "upDownRate":30000,
     * "turnover":100,
     * "averagePrice":35000,
     * "quoteTime":”20180103162858”
     */

    private String contractId;

    private String name;

    private long lastClosePrice;

    private long lastSettlePrice;

    private long openPrice;

    private long highestPrice;

    private long lowestPrice;

    private long latestPrice;

    private long closePrice;

    private long settlePrice;

    private long bidPrice1;

    private long bidVolume1;

    private long bidPrice2;

    private long bidVolume2;

    private long bidPrice3;

    private long bidVolume3;

    private long bidPrice4;

    private long bidVolume4;

    private long bidPrice5;

    private long bidVolume5;

    private long askPrice1;

    private long askVolume1;

    private long askPrice2;

    private long askVolume2;

    private long askPrice3;

    private long askVolume3;

    private long askPrice4;

    private long askVolume4;

    private long askPrice5;

    private long askVolume5;

    private long turnVolume;

    private long tradeWeight;

    private long highLimitPrice;

    private long lowerLimitPrice;

    private long position;

    private long upDown;

    private long upDownRate;

    private long turnover;

    private long averagePrice;

    private String quoteTime;

    public String getContractId() {
        return contractId;
    }

    public void setContractId(String contractId) {
        this.contractId = contractId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getLastClosePrice() {
        return lastClosePrice;
    }

    public void setLastClosePrice(long lastClosePrice) {
        this.lastClosePrice = lastClosePrice;
    }

    public long getLastSettlePrice() {
        return lastSettlePrice;
    }

    public void setLastSettlePrice(long lastSettlePrice) {
        this.lastSettlePrice = lastSettlePrice;
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

    public long getLatestPrice() {
        return latestPrice;
    }

    public void setLatestPrice(long latestPrice) {
        this.latestPrice = latestPrice;
    }

    public long getClosePrice() {
        return closePrice;
    }

    public void setClosePrice(long closePrice) {
        this.closePrice = closePrice;
    }

    public long getSettlePrice() {
        return settlePrice;
    }

    public void setSettlePrice(long settlePrice) {
        this.settlePrice = settlePrice;
    }

    public long getBidPrice1() {
        return bidPrice1;
    }

    public void setBidPrice1(long bidPrice1) {
        this.bidPrice1 = bidPrice1;
    }

    public long getBidVolume1() {
        return bidVolume1;
    }

    public void setBidVolume1(long bidVolume1) {
        this.bidVolume1 = bidVolume1;
    }

    public long getBidPrice2() {
        return bidPrice2;
    }

    public void setBidPrice2(long bidPrice2) {
        this.bidPrice2 = bidPrice2;
    }

    public long getBidVolume2() {
        return bidVolume2;
    }

    public void setBidVolume2(long bidVolume2) {
        this.bidVolume2 = bidVolume2;
    }

    public long getBidPrice3() {
        return bidPrice3;
    }

    public void setBidPrice3(long bidPrice3) {
        this.bidPrice3 = bidPrice3;
    }

    public long getBidVolume3() {
        return bidVolume3;
    }

    public void setBidVolume3(long bidVolume3) {
        this.bidVolume3 = bidVolume3;
    }

    public long getBidPrice4() {
        return bidPrice4;
    }

    public void setBidPrice4(long bidPrice4) {
        this.bidPrice4 = bidPrice4;
    }

    public long getBidVolume4() {
        return bidVolume4;
    }

    public void setBidVolume4(long bidVolume4) {
        this.bidVolume4 = bidVolume4;
    }

    public long getBidPrice5() {
        return bidPrice5;
    }

    public void setBidPrice5(long bidPrice5) {
        this.bidPrice5 = bidPrice5;
    }

    public long getBidVolume5() {
        return bidVolume5;
    }

    public void setBidVolume5(long bidVolume5) {
        this.bidVolume5 = bidVolume5;
    }

    public long getAskingPrice1() {
        return askPrice1;
    }

    public long getAskPrice1() {
        return askPrice1;
    }

    public void setAskPrice1(long askPrice1) {
        this.askPrice1 = askPrice1;
    }

    public long getAskVolume1() {
        return askVolume1;
    }

    public void setAskVolume1(long askVolume1) {
        this.askVolume1 = askVolume1;
    }

    public long getAskPrice2() {
        return askPrice2;
    }

    public void setAskPrice2(long askPrice2) {
        this.askPrice2 = askPrice2;
    }

    public long getAskVolume2() {
        return askVolume2;
    }

    public void setAskVolume2(long askVolume2) {
        this.askVolume2 = askVolume2;
    }

    public long getAskPrice3() {
        return askPrice3;
    }

    public void setAskPrice3(long askPrice3) {
        this.askPrice3 = askPrice3;
    }

    public long getAskVolume3() {
        return askVolume3;
    }

    public void setAskVolume3(long askVolume3) {
        this.askVolume3 = askVolume3;
    }

    public long getAskPrice4() {
        return askPrice4;
    }

    public void setAskPrice4(long askPrice4) {
        this.askPrice4 = askPrice4;
    }

    public long getAskVolume4() {
        return askVolume4;
    }

    public void setAskVolume4(long askVolume4) {
        this.askVolume4 = askVolume4;
    }

    public long getAskPrice5() {
        return askPrice5;
    }

    public void setAskPrice5(long askPrice5) {
        this.askPrice5 = askPrice5;
    }

    public long getAskVolume5() {
        return askVolume5;
    }

    public void setAskVolume5(long askVolume5) {
        this.askVolume5 = askVolume5;
    }

    public long getTurnVolume() {
        return turnVolume;
    }

    public void setTurnVolume(long turnVolume) {
        this.turnVolume = turnVolume;
    }

    public long getTradeWeight() {
        return tradeWeight;
    }

    public void setTradeWeight(long tradeWeight) {
        this.tradeWeight = tradeWeight;
    }

    public String getHighLimitPrice() {
        return MarketUtil.getPriceValue(highLimitPrice);
    }

    public void setHighLimitPrice(long highLimitPrice) {
        this.highLimitPrice = highLimitPrice;
    }

    public String getLowerLimitPrice() {
        return MarketUtil.getPriceValue(lowerLimitPrice);
    }

    public void setLowerLimitPrice(long lowerLimitPrice) {
        this.lowerLimitPrice = lowerLimitPrice;
    }

    public long getPosition() {
        return position;
    }

    public void setPosition(long position) {
        this.position = position;
    }

    public long getUpDown() {
        return upDown;
    }

    public void setUpDown(long upDown) {
        this.upDown = upDown;
    }

    public long getUpDownRate() {
        return upDownRate;
    }

    public void setUpDownRate(long upDownRate) {
        this.upDownRate = upDownRate;
    }

    public long getTurnover() {
        return turnover;
    }

    public void setTurnover(long turnover) {
        this.turnover = turnover;
    }

    public long getAveragePrice() {
        return averagePrice;
    }

    public void setAveragePrice(long averagePrice) {
        this.averagePrice = averagePrice;
    }

    public String getQuoteTime() {
        return quoteTime;
    }

    public void setQuoteTime(String quoteTime) {
        this.quoteTime = quoteTime;
    }

    public String getLatestPriceValue() {
        return MarketUtil.getPriceValue(latestPrice);
    }

    public String getUpDownValue() {
        return MarketUtil.getPriceValue(upDown);
    }

    public String getUpDownRateValue() {
        return MarketUtil.getRateValue(upDownRate);
    }

    public List<String[]> getFiveAskLists() {
        List<String[]> list = new ArrayList<>();

        String[] askValue5 = new String[3];
        askValue5[0] = "卖5";
        askValue5[1] = MarketUtil.getPriceValue(askPrice5);
        askValue5[2] = String.valueOf(askVolume5);
        list.add(askValue5);

        String[] askValue4 = new String[3];
        askValue4[0] = "卖4";
        askValue4[1] = MarketUtil.getPriceValue(askPrice4);
        askValue4[2] = String.valueOf(askVolume4);
        list.add(askValue4);

        String[] askValue3 = new String[3];
        askValue3[0] = "卖3";
        askValue3[1] = MarketUtil.getPriceValue(askPrice3);
        askValue3[2] = String.valueOf(askVolume3);
        list.add(askValue3);

        String[] askValue2 = new String[3];
        askValue2[0] = "卖2";
        askValue2[1] = MarketUtil.getPriceValue(askPrice2);
        askValue2[2] = String.valueOf(askVolume2);
        list.add(askValue2);

        String[] askValue1 = new String[3];
        askValue1[0] = "卖1";
        askValue1[1] = MarketUtil.getPriceValue(askPrice1);
        askValue1[2] = String.valueOf(askVolume1);
        list.add(askValue1);

        return list;
    }

    public List<String[]> getFiveBidLists() {
        List<String[]> list = new ArrayList<>();

        String[] bidValue1 = new String[3];
        bidValue1[0] = "买1";
        bidValue1[1] = MarketUtil.getPriceValue(bidPrice1);
        bidValue1[2] = String.valueOf(bidVolume1);
        list.add(bidValue1);

        String[] bidValue2 = new String[3];
        bidValue2[0] = "买2";
        bidValue2[1] = MarketUtil.getPriceValue(bidPrice2);
        bidValue2[2] = String.valueOf(bidVolume2);
        list.add(bidValue2);

        String[] bidValue3 = new String[3];
        bidValue3[0] = "买3";
        bidValue3[1] = MarketUtil.getPriceValue(bidPrice3);
        bidValue3[2] = String.valueOf(bidVolume3);
        list.add(bidValue3);

        String[] bidValue4 = new String[3];
        bidValue4[0] = "买4";
        bidValue4[1] = MarketUtil.getPriceValue(bidPrice4);
        bidValue4[2] = String.valueOf(bidVolume4);
        list.add(bidValue4);

        String[] bidValue5 = new String[3];
        bidValue5[0] = "买5";
        bidValue5[1] = MarketUtil.getPriceValue(bidPrice5);
        bidValue5[2] = String.valueOf(bidVolume5);
        list.add(bidValue5);

        return list;
    }

}
