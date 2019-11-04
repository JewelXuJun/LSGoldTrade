package com.jme.lsgoldtrade.domain;

import com.jme.lsgoldtrade.util.MarketUtil;

import java.io.Serializable;

public class PositionVo implements Serializable {

    private String contractId;

    private String type;

    private String exists;

    private long Position;

    private long lastPosition;

    private long todayPosition;

    private long offsetPosition;

    private long turnOver;

    private long deliveryApplyFrozen;

    private long deliveryApply;

    private long limitFrozen;

    private long offsetFrozen;

    private long positionAverage;

    private long positionMargin;

    private long positionLimit;

    private long floatProfit;

    private long unliquidatedProfit;

    private String stopOrderFlag;

    private int closeFrozenOrderNum;

    public String getExists() {
        return exists;
    }

    public void setExists(String exists) {
        this.exists = exists;
    }


    public String getContractId() {
        return contractId;
    }

    public void setContractId(String contractId) {
        this.contractId = contractId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getPosition() {
        return Position;
    }

    public void setPosition(long position) {
        Position = position;
    }

    public long getLastPosition() {
        return lastPosition;
    }

    public void setLastPosition(long lastPosition) {
        this.lastPosition = lastPosition;
    }

    public long getTodayPosition() {
        return todayPosition;
    }

    public void setTodayPosition(long todayPosition) {
        this.todayPosition = todayPosition;
    }

    public long getOffsetPosition() {
        return offsetPosition;
    }

    public void setOffsetPosition(long offsetPosition) {
        this.offsetPosition = offsetPosition;
    }

    public long getTurnOver() {
        return turnOver;
    }

    public void setTurnOver(long turnOver) {
        this.turnOver = turnOver;
    }

    public long getDeliveryApplyFrozen() {
        return deliveryApplyFrozen;
    }

    public void setDeliveryApplyFrozen(long deliveryApplyFrozen) {
        this.deliveryApplyFrozen = deliveryApplyFrozen;
    }

    public long getDeliveryApply() {
        return deliveryApply;
    }

    public void setDeliveryApply(long deliveryApply) {
        this.deliveryApply = deliveryApply;
    }

    public long getLimitFrozen() {
        return limitFrozen;
    }

    public void setLimitFrozen(long limitFrozen) {
        this.limitFrozen = limitFrozen;
    }

    public long getOffsetFrozen() {
        return offsetFrozen;
    }

    public void setOffsetFrozen(long offsetFrozen) {
        this.offsetFrozen = offsetFrozen;
    }

    public long getPositionAverage() {
        return positionAverage;
    }

    public void setPositionAverage(long positionAverage) {
        this.positionAverage = positionAverage;
    }

    public String getPositionMargin() {
        return MarketUtil.getPriceValue(positionMargin);
    }

    public void setPositionMargin(long positionMargin) {
        this.positionMargin = positionMargin;
    }

    public long getPositionLimit() {
        return positionLimit;
    }

    public void setPositionLimit(long positionLimit) {
        this.positionLimit = positionLimit;
    }

    public long getFloatProfit() {
        return floatProfit;
    }

    public void setFloatProfit(long floatProfit) {
        this.floatProfit = floatProfit;
    }

    public long getUnliquidatedProfit() {
        return unliquidatedProfit;
    }

    public String getUnliquidatedProfitStr() {
        return MarketUtil.getPriceValue(unliquidatedProfit);
    }

    public void setUnliquidatedProfit(long unliquidatedProfit) {
        this.unliquidatedProfit = unliquidatedProfit;
    }

    public String getPositionAverageStr() {
        return MarketUtil.getPriceValue(positionAverage);
    }

    public String getStopOrderFlag() {
        return stopOrderFlag;
    }

    public void setStopOrderFlag(String stopOrderFlag) {
        this.stopOrderFlag = stopOrderFlag;
    }

    public int getCloseFrozenOrderNum() {
        return closeFrozenOrderNum;
    }

    public void setCloseFrozenOrderNum(int closeFrozenOrderNum) {
        this.closeFrozenOrderNum = closeFrozenOrderNum;
    }
}
