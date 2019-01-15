package com.jme.lsgoldtrade.domain;

import java.io.Serializable;

public class ContractInfoVo implements Serializable {

    /**
     * "contractId": "Au(T+D)",
     * "contractType": 1,
     * "name": "黄金延期",
     * "minOrderQty": 1,
     * "maxOrderQty": 200,
     * "handWeight": 1000000000,
     * "priceUnit": 0,
     * "minPriceMove": 1,
     * "bankFeeRate": 20000,
     * "exchangeFeeRate": 60000,
     * "deliveryFreezeRate": 300,
     * "midFreezeRate": 200,
     * "minDeliveryQty": 1,
     * "displayOrder": 1,
     * "bankLongMarginRate": 900,
     * "exLongMarginRate": 600,
     * "bankShortMarginRate": 900,
     * "exShortMarginRate": 600
     */

    private String contractId;

    private int contractType;

    private String name;

    private long minOrderQty;

    private long maxOrderQty;

    private long handWeight;

    private int priceUnit;

    private long minPriceMove;

    private long bankFeeRate;

    private long exchangeFeeRate;

    private long deliveryFreezeRate;

    private int midFreezeRate;

    private int minDeliveryQty;

    private int displayOrder;

    private int bankLongMarginRate;

    private int exLongMarginRate;

    private int bankShortMarginRate;

    private int exShortMarginRate;

    public String getContractId() {
        return contractId;
    }

    public void setContractId(String contractId) {
        this.contractId = contractId;
    }

    public int getContractType() {
        return contractType;
    }

    public void setContractType(int contractType) {
        this.contractType = contractType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getMinOrderQty() {
        return minOrderQty;
    }

    public void setMinOrderQty(long minOrderQty) {
        this.minOrderQty = minOrderQty;
    }

    public long getMaxOrderQty() {
        return maxOrderQty;
    }

    public void setMaxOrderQty(long maxOrderQty) {
        this.maxOrderQty = maxOrderQty;
    }

    public long getHandWeight() {
        return handWeight;
    }

    public void setHandWeight(long handWeight) {
        this.handWeight = handWeight;
    }

    public int getPriceUnit() {
        return priceUnit;
    }

    public void setPriceUnit(int priceUnit) {
        this.priceUnit = priceUnit;
    }

    public long getMinPriceMove() {
        return minPriceMove;
    }

    public void setMinPriceMove(long minPriceMove) {
        this.minPriceMove = minPriceMove;
    }

    public long getBankFeeRate() {
        return bankFeeRate;
    }

    public void setBankFeeRate(long bankFeeRate) {
        this.bankFeeRate = bankFeeRate;
    }

    public long getExchangeFeeRate() {
        return exchangeFeeRate;
    }

    public void setExchangeFeeRate(long exchangeFeeRate) {
        this.exchangeFeeRate = exchangeFeeRate;
    }

    public long getDeliveryFreezeRate() {
        return deliveryFreezeRate;
    }

    public void setDeliveryFreezeRate(long deliveryFreezeRate) {
        this.deliveryFreezeRate = deliveryFreezeRate;
    }

    public int getMidFreezeRate() {
        return midFreezeRate;
    }

    public void setMidFreezeRate(int midFreezeRate) {
        this.midFreezeRate = midFreezeRate;
    }

    public int getMinDeliveryQty() {
        return minDeliveryQty;
    }

    public void setMinDeliveryQty(int minDeliveryQty) {
        this.minDeliveryQty = minDeliveryQty;
    }

    public int getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(int displayOrder) {
        this.displayOrder = displayOrder;
    }

    public int getBankLongMarginRate() {
        return bankLongMarginRate;
    }

    public void setBankLongMarginRate(int bankLongMarginRate) {
        this.bankLongMarginRate = bankLongMarginRate;
    }

    public int getExLongMarginRate() {
        return exLongMarginRate;
    }

    public void setExLongMarginRate(int exLongMarginRate) {
        this.exLongMarginRate = exLongMarginRate;
    }

    public int getBankShortMarginRate() {
        return bankShortMarginRate;
    }

    public void setBankShortMarginRate(int bankShortMarginRate) {
        this.bankShortMarginRate = bankShortMarginRate;
    }

    public int getExShortMarginRate() {
        return exShortMarginRate;
    }

    public void setExShortMarginRate(int exShortMarginRate) {
        this.exShortMarginRate = exShortMarginRate;
    }

}
