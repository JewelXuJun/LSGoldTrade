package com.jme.lsgoldtrade.domain;

import java.util.List;

public class TradingBoxDetailsVo {

    /**
     * analystOpinion : string
     * chance : string
     * closePositionsTimeBegin : string
     * closePositionsTimeEnd : string
     * closeTime : 0
     * closeTimeStr : string
     * direction : string
     * directionDownNum : 0
     * directionDownRate : string
     * directionUpNum : 0
     * directionUpRate : string
     * fundamentalAnalysis : string
     * id : string
     * openPositionsTimeBegin : string
     * openPositionsTimeEnd : string
     * periodId : string
     * periodName : string
     * relevantInfoListVos : [{"content":"string","id":"string"}]
     * variety : string
     */

    private String analystOpinion;

    private String chance;

    private String closePositionsTimeBegin;

    private String closePositionsTimeEnd;

    private long closeTime;

    private String closeTimeStr;

    private String direction;

    private int directionDownNum;

    private String directionDownRate;

    private int directionUpNum;

    private String directionUpRate;

    private String fundamentalAnalysis;

    private String id;

    private String openPositionsTimeBegin;

    private String openPositionsTimeEnd;

    private String periodId;

    private String periodName;

    private String variety;

    private String earningsLine;

    private String lossLine;

    private List<RelevantInfoListVosBean> relevantInfoListVos;

    public String getLossLine() {
        return lossLine;
    }

    public void setLossLine(String lossLine) {
        this.lossLine = lossLine;
    }

    public String getEarningsLine() {
        return earningsLine;
    }

    public void setEarningsLine(String earningsLine) {
        this.earningsLine = earningsLine;
    }

    public String getAnalystOpinion() {
        return analystOpinion;
    }

    public void setAnalystOpinion(String analystOpinion) {
        this.analystOpinion = analystOpinion;
    }

    public String getChance() {
        return chance;
    }

    public void setChance(String chance) {
        this.chance = chance;
    }

    public String getClosePositionsTimeBegin() {
        return closePositionsTimeBegin;
    }

    public void setClosePositionsTimeBegin(String closePositionsTimeBegin) {
        this.closePositionsTimeBegin = closePositionsTimeBegin;
    }

    public String getClosePositionsTimeEnd() {
        return closePositionsTimeEnd;
    }

    public void setClosePositionsTimeEnd(String closePositionsTimeEnd) {
        this.closePositionsTimeEnd = closePositionsTimeEnd;
    }

    public long getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(long closeTime) {
        this.closeTime = closeTime;
    }

    public String getCloseTimeStr() {
        return closeTimeStr;
    }

    public void setCloseTimeStr(String closeTimeStr) {
        this.closeTimeStr = closeTimeStr;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public int getDirectionDownNum() {
        return directionDownNum;
    }

    public void setDirectionDownNum(int directionDownNum) {
        this.directionDownNum = directionDownNum;
    }

    public String getDirectionDownRate() {
        return directionDownRate;
    }

    public void setDirectionDownRate(String directionDownRate) {
        this.directionDownRate = directionDownRate;
    }

    public int getDirectionUpNum() {
        return directionUpNum;
    }

    public void setDirectionUpNum(int directionUpNum) {
        this.directionUpNum = directionUpNum;
    }

    public String getDirectionUpRate() {
        return directionUpRate;
    }

    public void setDirectionUpRate(String directionUpRate) {
        this.directionUpRate = directionUpRate;
    }

    public String getFundamentalAnalysis() {
        return fundamentalAnalysis;
    }

    public void setFundamentalAnalysis(String fundamentalAnalysis) {
        this.fundamentalAnalysis = fundamentalAnalysis;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOpenPositionsTimeBegin() {
        return openPositionsTimeBegin;
    }

    public void setOpenPositionsTimeBegin(String openPositionsTimeBegin) {
        this.openPositionsTimeBegin = openPositionsTimeBegin;
    }

    public String getOpenPositionsTimeEnd() {
        return openPositionsTimeEnd;
    }

    public void setOpenPositionsTimeEnd(String openPositionsTimeEnd) {
        this.openPositionsTimeEnd = openPositionsTimeEnd;
    }

    public String getPeriodId() {
        return periodId;
    }

    public void setPeriodId(String periodId) {
        this.periodId = periodId;
    }

    public String getPeriodName() {
        return periodName;
    }

    public void setPeriodName(String periodName) {
        this.periodName = periodName;
    }

    public String getVariety() {
        return variety;
    }

    public void setVariety(String variety) {
        this.variety = variety;
    }

    public List<RelevantInfoListVosBean> getRelevantInfoListVos() {
        return relevantInfoListVos;
    }

    public void setRelevantInfoListVos(List<RelevantInfoListVosBean> relevantInfoListVos) {
        this.relevantInfoListVos = relevantInfoListVos;
    }

    public static class RelevantInfoListVosBean {
        /**
         * content : string
         * id : string
         */

        private String content;

        private String id;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }
}
