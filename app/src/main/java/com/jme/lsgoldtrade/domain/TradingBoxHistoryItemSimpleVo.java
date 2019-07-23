package com.jme.lsgoldtrade.domain;

public class TradingBoxHistoryItemSimpleVo {

    /**
     * tradeId : 1145564307464060930
     * chance : AU看多
     * variety : Au(T+D)
     * direction : 0
     * pushTime : 2019-07-01 00:00:00
     */

    private String tradeId;

    private String chance;

    private String variety;

    private String direction;

    private String pushTime;

    private String periodName;

    public String getPeriodName() {
        return periodName;
    }

    public void setPeriodName(String periodName) {
        this.periodName = periodName;
    }

    public String getTradeId() {
        return tradeId;
    }

    public void setTradeId(String tradeId) {
        this.tradeId = tradeId;
    }

    public String getChance() {
        return chance;
    }

    public void setChance(String chance) {
        this.chance = chance;
    }

    public String getVariety() {
        return variety;
    }

    public void setVariety(String variety) {
        this.variety = variety;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getPushTime() {
        return pushTime;
    }

    public void setPushTime(String pushTime) {
        this.pushTime = pushTime;
    }
}
