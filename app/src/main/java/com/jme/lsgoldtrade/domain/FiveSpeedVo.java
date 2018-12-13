package com.jme.lsgoldtrade.domain;

import com.jme.lsgoldtrade.util.MarketUtil;

import java.io.Serializable;

public class FiveSpeedVo implements Serializable {

    /**
     * "contractId":"iAu100g",
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

    private long askingPrice1;

    private long askingVolume1;

    private long askingPrice2;

    private long askingVolume2;

    private long askingPrice3;

    private long askingVolume3;

    private long askingPrice4;

    private long askingVolume4;

    private long askingPrice5;

    private long askingVolume5;

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
        return askingPrice1;
    }

    public void setAskingPrice1(long askingPrice1) {
        this.askingPrice1 = askingPrice1;
    }

    public long getAskingVolume1() {
        return askingVolume1;
    }

    public void setAskingVolume1(long askingVolume1) {
        this.askingVolume1 = askingVolume1;
    }

    public long getAskingPrice2() {
        return askingPrice2;
    }

    public void setAskingPrice2(long askingPrice2) {
        this.askingPrice2 = askingPrice2;
    }

    public long getAskingVolume2() {
        return askingVolume2;
    }

    public void setAskingVolume2(long askingVolume2) {
        this.askingVolume2 = askingVolume2;
    }

    public long getAskingPrice3() {
        return askingPrice3;
    }

    public void setAskingPrice3(long askingPrice3) {
        this.askingPrice3 = askingPrice3;
    }

    public long getAskingVolume3() {
        return askingVolume3;
    }

    public void setAskingVolume3(long askingVolume3) {
        this.askingVolume3 = askingVolume3;
    }

    public long getAskingPrice4() {
        return askingPrice4;
    }

    public void setAskingPrice4(long askingPrice4) {
        this.askingPrice4 = askingPrice4;
    }

    public long getAskingVolume4() {
        return askingVolume4;
    }

    public void setAskingVolume4(long askingVolume4) {
        this.askingVolume4 = askingVolume4;
    }

    public long getAskingPrice5() {
        return askingPrice5;
    }

    public void setAskingPrice5(long askingPrice5) {
        this.askingPrice5 = askingPrice5;
    }

    public long getAskingVolume5() {
        return askingVolume5;
    }

    public void setAskingVolume5(long askingVolume5) {
        this.askingVolume5 = askingVolume5;
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

    public long getHighLimitPrice() {
        return highLimitPrice;
    }

    public void setHighLimitPrice(long highLimitPrice) {
        this.highLimitPrice = highLimitPrice;
    }

    public long getLowerLimitPrice() {
        return lowerLimitPrice;
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

    public String getUpDownRateValue() {
        return MarketUtil.getRateValue(upDownRate);
    }

}
