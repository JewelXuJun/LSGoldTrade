package com.jme.lsgoldtrade.domain;

import java.io.Serializable;

public class ContractInfoVo implements Serializable {

    /**
     * "contractId":"au9999",
     * "name":"测试黄金",
     * "marketId":1,
     * "tradingLimit":0,
     * "lowestQuantity":10,
     * "highestQuantity":100,
     * "unitHand": 1000,
     * "priceUnit": 0,
     * "tickSize": 1,
     * "bankFeeRate": 100,
     * "exchangeFeeRate": 80,
     * "deliveryFreezeRate": 80,
     * "midFreezeRate": 80,
     * "minimumDeliveryNumber":1,
     * "banklongMarginRate":5000,
     * "bankshortMarginRate":5000,
     * "exlongMarginRate":5000,
     * "exshortMarginRate":5000
     */

    private String contractId;

    private String name;

    private int marketId;

    private int tradingLimit;

    private long lowestQuantity;

    private long highestQuantity;

    private long unitHand;

    private int priceUnit;

    private int tickSize;

    private float bankFeeRate;

    private float exchangeFeeRate;

    private float deliveryFreezeRate;

    private float midFreezeRate;

    private long minimumDeliveryNumber;

    private float banklongMarginRate;

    private float bankshortMarginRate;

    private float exlongMarginRate;

    private long exshortMarginRate;

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

    public int getMarketId() {
        return marketId;
    }

    public void setMarketId(int marketId) {
        this.marketId = marketId;
    }

    public int getTradingLimit() {
        return tradingLimit;
    }

    public void setTradingLimit(int tradingLimit) {
        this.tradingLimit = tradingLimit;
    }

    public long getLowestQuantity() {
        return lowestQuantity;
    }

    public void setLowestQuantity(long lowestQuantity) {
        this.lowestQuantity = lowestQuantity;
    }

    public long getHighestQuantity() {
        return highestQuantity;
    }

    public void setHighestQuantity(long highestQuantity) {
        this.highestQuantity = highestQuantity;
    }

    public long getUnitHand() {
        return unitHand;
    }

    public void setUnitHand(long unitHand) {
        this.unitHand = unitHand;
    }

    public int getPriceUnit() {
        return priceUnit;
    }

    public void setPriceUnit(int priceUnit) {
        this.priceUnit = priceUnit;
    }

    public int getTickSize() {
        return tickSize;
    }

    public void setTickSize(int tickSize) {
        this.tickSize = tickSize;
    }

    public float getBankFeeRate() {
        return bankFeeRate;
    }

    public void setBankFeeRate(float bankFeeRate) {
        this.bankFeeRate = bankFeeRate;
    }

    public float getExchangeFeeRate() {
        return exchangeFeeRate;
    }

    public void setExchangeFeeRate(float exchangeFeeRate) {
        this.exchangeFeeRate = exchangeFeeRate;
    }

    public float getDeliveryFreezeRate() {
        return deliveryFreezeRate;
    }

    public void setDeliveryFreezeRate(float deliveryFreezeRate) {
        this.deliveryFreezeRate = deliveryFreezeRate;
    }

    public float getMidFreezeRate() {
        return midFreezeRate;
    }

    public void setMidFreezeRate(float midFreezeRate) {
        this.midFreezeRate = midFreezeRate;
    }

    public long getMinimumDeliveryNumber() {
        return minimumDeliveryNumber;
    }

    public void setMinimumDeliveryNumber(long minimumDeliveryNumber) {
        this.minimumDeliveryNumber = minimumDeliveryNumber;
    }

    public float getBanklongMarginRate() {
        return banklongMarginRate;
    }

    public void setBanklongMarginRate(float banklongMarginRate) {
        this.banklongMarginRate = banklongMarginRate;
    }

    public float getBankshortMarginRate() {
        return bankshortMarginRate;
    }

    public void setBankshortMarginRate(float bankshortMarginRate) {
        this.bankshortMarginRate = bankshortMarginRate;
    }

    public float getExlongMarginRate() {
        return exlongMarginRate;
    }

    public void setExlongMarginRate(float exlongMarginRate) {
        this.exlongMarginRate = exlongMarginRate;
    }

    public long getExshortMarginRate() {
        return exshortMarginRate;
    }

    public void setExshortMarginRate(long exshortMarginRate) {
        this.exshortMarginRate = exshortMarginRate;
    }
}
