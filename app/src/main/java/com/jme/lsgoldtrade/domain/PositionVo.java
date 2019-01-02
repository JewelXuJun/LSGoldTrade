package com.jme.lsgoldtrade.domain;

import java.io.Serializable;

public class PositionVo implements Serializable {

    /**
     * "contractId":"Au(T+D)",
     * "longPosition":100,
     * "shortPosition":100,
     * "lastLongPosition":100,
     * "lastShortPosition":100,
     * "todayLongPosition":100,
     * "todayShortPosition":300,
     * "longOffsetPosition":300,
     * "shortOffsetPosition":300,
     * "longTurnover":30000,
     * "shortTurnover":30000,
     * "deliveryApplyLongFrozen":1000,
     * "deliveryApplyShortFrozen":1000,
     * "deliveryApplyLong":1000,
     * "deliveryApplyShort":1000,
     * "longLimitFrozen":10000,
     * "shortLimitFrozen":10000,
     * "offsetLongFrozen":20000,
     * "offsetShortFrozen":20000,
     * "longPositionAverage":30000,
     * "shortPositionAverage":30000,
     * "longPositionMargin":1000000,
     * "shortPositionMargin":1000000,
     * "longPositionLimit":100,
     * "shortPositionLimit":100,
     * "longFloatProfit":200,
     * "shortFloatProfit":100,
     * "longUnliquidatedProfit":0,
     * "shortUnliquidatedProfit":0
     */

    private String contractId;

    private long longPosition;

    private long shortPosition;

    private long lastLongPosition;

    private long lastShortPosition;

    private long todayLongPosition;

    private long todayShortPosition;

    private long longOffsetPosition;

    private long shortOffsetPosition;

    private long longTurnover;

    private long shortTurnover;

    private long deliveryApplyLongFrozen;

    private long deliveryApplyShortFrozen;

    private long deliveryApplyLong;

    private long deliveryApplyShort;

    private long longLimitFrozen;

    private long shortLimitFrozen;

    private long offsetLongFrozen;

    private long offsetShortFrozen;

    private long longPositionAverage;

    private long shortPositionAverage;

    private long longPositionMargin;

    private long shortPositionMargin;

    private long longPositionLimit;

    private long shortPositionLimit;

    private long longFloatProfit;

    private long shortFloatProfit;

    private long longUnliquidatedProfit;

    private long shortUnliquidatedProfit;

    public String getContractId() {
        return contractId;
    }

    public void setContractId(String contractId) {
        this.contractId = contractId;
    }

    public long getLongPosition() {
        return longPosition;
    }

    public void setLongPosition(long longPosition) {
        this.longPosition = longPosition;
    }

    public long getShortPosition() {
        return shortPosition;
    }

    public void setShortPosition(long shortPosition) {
        this.shortPosition = shortPosition;
    }

    public long getLastLongPosition() {
        return lastLongPosition;
    }

    public void setLastLongPosition(long lastLongPosition) {
        this.lastLongPosition = lastLongPosition;
    }

    public long getLastShortPosition() {
        return lastShortPosition;
    }

    public void setLastShortPosition(long lastShortPosition) {
        this.lastShortPosition = lastShortPosition;
    }

    public long getTodayLongPosition() {
        return todayLongPosition;
    }

    public void setTodayLongPosition(long todayLongPosition) {
        this.todayLongPosition = todayLongPosition;
    }

    public long getTodayShortPosition() {
        return todayShortPosition;
    }

    public void setTodayShortPosition(long todayShortPosition) {
        this.todayShortPosition = todayShortPosition;
    }

    public long getLongOffsetPosition() {
        return longOffsetPosition;
    }

    public void setLongOffsetPosition(long longOffsetPosition) {
        this.longOffsetPosition = longOffsetPosition;
    }

    public long getShortOffsetPosition() {
        return shortOffsetPosition;
    }

    public void setShortOffsetPosition(long shortOffsetPosition) {
        this.shortOffsetPosition = shortOffsetPosition;
    }

    public long getLongTurnover() {
        return longTurnover;
    }

    public void setLongTurnover(long longTurnover) {
        this.longTurnover = longTurnover;
    }

    public long getShortTurnover() {
        return shortTurnover;
    }

    public void setShortTurnover(long shortTurnover) {
        this.shortTurnover = shortTurnover;
    }

    public long getDeliveryApplyLongFrozen() {
        return deliveryApplyLongFrozen;
    }

    public void setDeliveryApplyLongFrozen(long deliveryApplyLongFrozen) {
        this.deliveryApplyLongFrozen = deliveryApplyLongFrozen;
    }

    public long getDeliveryApplyShortFrozen() {
        return deliveryApplyShortFrozen;
    }

    public void setDeliveryApplyShortFrozen(long deliveryApplyShortFrozen) {
        this.deliveryApplyShortFrozen = deliveryApplyShortFrozen;
    }

    public long getDeliveryApplyLong() {
        return deliveryApplyLong;
    }

    public void setDeliveryApplyLong(long deliveryApplyLong) {
        this.deliveryApplyLong = deliveryApplyLong;
    }

    public long getDeliveryApplyShort() {
        return deliveryApplyShort;
    }

    public void setDeliveryApplyShort(long deliveryApplyShort) {
        this.deliveryApplyShort = deliveryApplyShort;
    }

    public long getLongLimitFrozen() {
        return longLimitFrozen;
    }

    public void setLongLimitFrozen(long longLimitFrozen) {
        this.longLimitFrozen = longLimitFrozen;
    }

    public long getShortLimitFrozen() {
        return shortLimitFrozen;
    }

    public void setShortLimitFrozen(long shortLimitFrozen) {
        this.shortLimitFrozen = shortLimitFrozen;
    }

    public long getOffsetLongFrozen() {
        return offsetLongFrozen;
    }

    public void setOffsetLongFrozen(long offsetLongFrozen) {
        this.offsetLongFrozen = offsetLongFrozen;
    }

    public long getOffsetShortFrozen() {
        return offsetShortFrozen;
    }

    public void setOffsetShortFrozen(long offsetShortFrozen) {
        this.offsetShortFrozen = offsetShortFrozen;
    }

    public long getLongPositionAverage() {
        return longPositionAverage;
    }

    public void setLongPositionAverage(long longPositionAverage) {
        this.longPositionAverage = longPositionAverage;
    }

    public long getShortPositionAverage() {
        return shortPositionAverage;
    }

    public void setShortPositionAverage(long shortPositionAverage) {
        this.shortPositionAverage = shortPositionAverage;
    }

    public long getLongPositionMargin() {
        return longPositionMargin;
    }

    public void setLongPositionMargin(long longPositionMargin) {
        this.longPositionMargin = longPositionMargin;
    }

    public long getShortPositionMargin() {
        return shortPositionMargin;
    }

    public void setShortPositionMargin(long shortPositionMargin) {
        this.shortPositionMargin = shortPositionMargin;
    }

    public long getLongPositionLimit() {
        return longPositionLimit;
    }

    public void setLongPositionLimit(long longPositionLimit) {
        this.longPositionLimit = longPositionLimit;
    }

    public long getShortPositionLimit() {
        return shortPositionLimit;
    }

    public void setShortPositionLimit(long shortPositionLimit) {
        this.shortPositionLimit = shortPositionLimit;
    }

    public long getLongFloatProfit() {
        return longFloatProfit;
    }

    public void setLongFloatProfit(long longFloatProfit) {
        this.longFloatProfit = longFloatProfit;
    }

    public long getShortFloatProfit() {
        return shortFloatProfit;
    }

    public void setShortFloatProfit(long shortFloatProfit) {
        this.shortFloatProfit = shortFloatProfit;
    }

    public long getLongUnliquidatedProfit() {
        return longUnliquidatedProfit;
    }

    public void setLongUnliquidatedProfit(long longUnliquidatedProfit) {
        this.longUnliquidatedProfit = longUnliquidatedProfit;
    }

    public long getShortUnliquidatedProfit() {
        return shortUnliquidatedProfit;
    }

    public void setShortUnliquidatedProfit(long shortUnliquidatedProfit) {
        this.shortUnliquidatedProfit = shortUnliquidatedProfit;
    }

}
