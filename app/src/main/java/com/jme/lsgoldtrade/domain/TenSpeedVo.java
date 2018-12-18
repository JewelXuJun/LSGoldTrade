package com.jme.lsgoldtrade.domain;

import com.jme.lsgoldtrade.util.MarketUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TenSpeedVo implements Serializable {

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
     * "bidPrice6":32000,
     * "bidVolume6":100,
     * "bidPrice7":32000,
     * "bidVolume7":100,
     * "bidPrice8":32000,
     * "bidVolume8":100,
     * "bidPrice9":32000,
     * "bidVolume9":100,
     * "bidPrice10":32000,
     * "bidVolume10":100,
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
     * "askingPrice6":32000,
     * "askingVolume6":100,
     * "askingPrice7":32000,
     * "askingVolume7":100,
     * "askingPrice8":32000,
     * "askingVolume8":100,
     * "askingPrice9":32000,
     * "askingVolume9":100,
     * "askingPrice10":32000,
     * "askingVolume10":100,
     * "turnVolume":32000,
     * "tradeWeight":100,
     * "highLimitPrice":100,
     * "lowerLimitPrice":100,
     * "position":100,
     * "upDown":100,
     * "upDownRate":30000,
     * "turnover":100,
     * "averagePrice":35000,
     * "quoteTime":”2018-12-18 00:25:29”
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

    private long bidPrice6;

    private long bidVolume6;

    private long bidPrice7;

    private long bidVolume7;

    private long bidPrice8;

    private long bidVolume8;

    private long bidPrice9;

    private long bidVolume9;

    private long bidPrice10;

    private long bidVolume10;

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

    private long askPrice6;

    private long askVolume6;

    private long askPrice7;

    private long askVolume7;

    private long askPrice8;

    private long askVolume8;

    private long askPrice9;

    private long askVolume9;

    private long askPrice10;

    private long askVolume10;

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

    public String getLastClosePrice() {
        return MarketUtil.getPriceValue(lastClosePrice);
    }

    public void setLastClosePrice(long lastClosePrice) {
        this.lastClosePrice = lastClosePrice;
    }

    public String getLastSettlePrice() {
        return MarketUtil.getPriceValue(lastSettlePrice);
    }

    public void setLastSettlePrice(long lastSettlePrice) {
        this.lastSettlePrice = lastSettlePrice;
    }

    public String getOpenPrice() {
        return MarketUtil.getPriceValue(openPrice);
    }

    public void setOpenPrice(long openPrice) {
        this.openPrice = openPrice;
    }

    public String getHighestPrice() {
        return MarketUtil.getPriceValue(highestPrice);
    }

    public void setHighestPrice(long highestPrice) {
        this.highestPrice = highestPrice;
    }

    public String getLowestPrice() {
        return MarketUtil.getPriceValue(lowestPrice);
    }

    public void setLowestPrice(long lowestPrice) {
        this.lowestPrice = lowestPrice;
    }

    public String getLatestPrice() {
        return MarketUtil.getPriceValue(latestPrice);
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

    public long getBidPrice6() {
        return bidPrice6;
    }

    public void setBidPrice6(long bidPrice6) {
        this.bidPrice6 = bidPrice6;
    }

    public long getBidVolume6() {
        return bidVolume6;
    }

    public void setBidVolume6(long bidVolume6) {
        this.bidVolume6 = bidVolume6;
    }

    public long getBidPrice7() {
        return bidPrice7;
    }

    public void setBidPrice7(long bidPrice7) {
        this.bidPrice7 = bidPrice7;
    }

    public long getBidVolume7() {
        return bidVolume7;
    }

    public void setBidVolume7(long bidVolume7) {
        this.bidVolume7 = bidVolume7;
    }

    public long getBidPrice8() {
        return bidPrice8;
    }

    public void setBidPrice8(long bidPrice8) {
        this.bidPrice8 = bidPrice8;
    }

    public long getBidVolume8() {
        return bidVolume8;
    }

    public void setBidVolume8(long bidVolume8) {
        this.bidVolume8 = bidVolume8;
    }

    public long getBidPrice9() {
        return bidPrice9;
    }

    public void setBidPrice9(long bidPrice9) {
        this.bidPrice9 = bidPrice9;
    }

    public long getBidVolume9() {
        return bidVolume9;
    }

    public void setBidVolume9(long bidVolume9) {
        this.bidVolume9 = bidVolume9;
    }

    public long getBidPrice10() {
        return bidPrice10;
    }

    public void setBidPrice10(long bidPrice10) {
        this.bidPrice10 = bidPrice10;
    }

    public long getBidVolume10() {
        return bidVolume10;
    }

    public void setBidVolume10(long bidVolume10) {
        this.bidVolume10 = bidVolume10;
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

    public long getAskPrice6() {
        return askPrice6;
    }

    public void setAskPrice6(long askPrice6) {
        this.askPrice6 = askPrice6;
    }

    public long getAskVolume6() {
        return askVolume6;
    }

    public void setAskVolume6(long askVolume6) {
        this.askVolume6 = askVolume6;
    }

    public long getAskPrice7() {
        return askPrice7;
    }

    public void setAskPrice7(long askPrice7) {
        this.askPrice7 = askPrice7;
    }

    public long getAskVolume7() {
        return askVolume7;
    }

    public void setAskVolume7(long askVolume7) {
        this.askVolume7 = askVolume7;
    }

    public long getAskPrice8() {
        return askPrice8;
    }

    public void setAskPrice8(long askPrice8) {
        this.askPrice8 = askPrice8;
    }

    public long getAskVolume8() {
        return askVolume8;
    }

    public void setAskVolume8(long askVolume8) {
        this.askVolume8 = askVolume8;
    }

    public long getAskPrice9() {
        return askPrice9;
    }

    public void setAskPrice9(long askPrice9) {
        this.askPrice9 = askPrice9;
    }

    public long getAskVolume9() {
        return askVolume9;
    }

    public void setAskVolume9(long askVolume9) {
        this.askVolume9 = askVolume9;
    }

    public long getAskPrice10() {
        return askPrice10;
    }

    public void setAskPrice10(long askPrice10) {
        this.askPrice10 = askPrice10;
    }

    public long getAskVolume10() {
        return askVolume10;
    }

    public void setAskVolume10(long askVolume10) {
        this.askVolume10 = askVolume10;
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

    public String getUpDown() {
        return MarketUtil.getPriceValue(upDown);
    }

    public void setUpDown(long upDown) {
        this.upDown = upDown;
    }

    public String getUpDownRate() {
        return MarketUtil.getRateValue(upDownRate);
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

    public List<String[]> getAskLists() {
        List<String[]> list = new ArrayList<>();

        String[] askValue10 = new String[3];
        askValue10[0] = "卖10";
        askValue10[1] = MarketUtil.getPriceValue(askPrice10);
        askValue10[2] = String.valueOf(askVolume10);
        list.add(askValue10);

        String[] askValue9 = new String[3];
        askValue9[0] = "卖9";
        askValue9[1] = MarketUtil.getPriceValue(askPrice9);
        askValue9[2] = String.valueOf(askVolume9);
        list.add(askValue9);

        String[] askValue8 = new String[3];
        askValue8[0] = "卖8";
        askValue8[1] = MarketUtil.getPriceValue(askPrice8);
        askValue8[2] = String.valueOf(askVolume8);
        list.add(askValue8);

        String[] askValue7 = new String[3];
        askValue7[0] = "卖7";
        askValue7[1] = MarketUtil.getPriceValue(askPrice7);
        askValue7[2] = String.valueOf(askVolume7);
        list.add(askValue7);

        String[] askValue6 = new String[3];
        askValue6[0] = "卖6";
        askValue6[1] = MarketUtil.getPriceValue(askPrice6);
        askValue6[2] = String.valueOf(askVolume6);
        list.add(askValue6);

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

    public List<String[]> getBidLists() {
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

        String[] bidValue6 = new String[3];
        bidValue6[0] = "买6";
        bidValue6[1] = MarketUtil.getPriceValue(bidPrice6);
        bidValue6[2] = String.valueOf(bidVolume6);
        list.add(bidValue6);

        String[] bidValue7 = new String[3];
        bidValue7[0] = "买7";
        bidValue7[1] = MarketUtil.getPriceValue(bidPrice7);
        bidValue7[2] = String.valueOf(bidVolume7);
        list.add(bidValue7);

        String[] bidValue8 = new String[3];
        bidValue8[0] = "买8";
        bidValue8[1] = MarketUtil.getPriceValue(bidPrice8);
        bidValue8[2] = String.valueOf(bidVolume8);
        list.add(bidValue8);

        String[] bidValue9 = new String[3];
        bidValue9[0] = "买9";
        bidValue9[1] = MarketUtil.getPriceValue(bidPrice9);
        bidValue9[2] = String.valueOf(bidVolume9);
        list.add(bidValue9);

        String[] bidValue10 = new String[3];
        bidValue10[0] = "买10";
        bidValue10[1] = MarketUtil.getPriceValue(bidPrice10);
        bidValue10[2] = String.valueOf(bidVolume10);
        list.add(bidValue10);

        return list;
    }

}
