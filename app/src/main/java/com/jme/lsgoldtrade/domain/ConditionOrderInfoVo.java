package com.jme.lsgoldtrade.domain;

import com.jme.lsgoldtrade.util.MarketUtil;

import java.io.Serializable;

public class ConditionOrderInfoVo implements Serializable {

    private int bsFlag;

    private String contractId;

    private int effectiveTimeFlag;

    private int entrustNumber;

    private String id;

    private int ocFlag;

    private String setDate;

    private int status;

    private String stime;

    private long stopLossPrice;

    private long stopProfitPrice;

    private long triggerPrice;

    public int getBsFlag() {
        return bsFlag;
    }

    public void setBsFlag(int bsFlag) {
        this.bsFlag = bsFlag;
    }

    public String getContractId() {
        return contractId;
    }

    public void setContractId(String contractId) {
        this.contractId = contractId;
    }

    public int getEffectiveTimeFlag() {
        return effectiveTimeFlag;
    }

    public void setEffectiveTimeFlag(int effectiveTimeFlag) {
        this.effectiveTimeFlag = effectiveTimeFlag;
    }

    public int getEntrustNumber() {
        return entrustNumber;
    }

    public void setEntrustNumber(int entrustNumber) {
        this.entrustNumber = entrustNumber;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getOcFlag() {
        return ocFlag;
    }

    public void setOcFlag(int ocFlag) {
        this.ocFlag = ocFlag;
    }

    public String getSetDate() {
        return setDate;
    }

    public void setSetDate(String setDate) {
        this.setDate = setDate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getStime() {
        return stime;
    }

    public void setStime(String stime) {
        this.stime = stime;
    }

    public long getStopLossPrice() {
        return stopLossPrice;
    }

    public void setStopLossPrice(long stopLossPrice) {
        this.stopLossPrice = stopLossPrice;
    }

    public long getStopProfitPrice() {
        return stopProfitPrice;
    }

    public void setStopProfitPrice(long stopProfitPrice) {
        this.stopProfitPrice = stopProfitPrice;
    }

    public long getTriggerPrice() {
        return triggerPrice;
    }

    public void setTriggerPrice(long triggerPrice) {
        this.triggerPrice = triggerPrice;
    }

    public String getTriggerPriceStr() {
        return MarketUtil.getPriceValue(triggerPrice);
    }
}
