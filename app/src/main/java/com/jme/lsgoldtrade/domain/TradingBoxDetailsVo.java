package com.jme.lsgoldtrade.domain;

import java.io.Serializable;
import java.util.List;

public class TradingBoxDetailsVo implements Serializable {

    /**
     * "id": "1173037356840763394",
     * "periodId": "1173036745260908545",
     * "periodName": "徐军测试",
     * "chance": "银",
     * "fundamentalAnalysis": "银多",
     * "analystOpinion": "银多",
     * "variety": "Ag(T+D)",
     * "direction": "0",
     * "directionUpNum": 1,
     * "directionDownNum": 0,
     * "directionUpRate": "100.00",
     * "directionDownRate": "0",
     * "relevantInfoListVos": [
     * {
     * "id": "1173037357868367874",
     * "content": "银多"
     * }
     * ],
     * "openPositionsTimeBegin": "2019-09-15 00:00:00",
     * "openPositionsTimeEnd": "2019-09-20 00:00:00",
     * "closePositionsTimeBegin": "2019-09-21 00:00:00",
     * "closePositionsTimeEnd": "2019-09-24 00:00:00",
     * "closeTimeStr": "2019-09-26 00:00:00",
     * "closeTime": 890139,
     * "mainTitle": "安卓测试",
     * "mainContent": "安卓测试",
     * "moodUrl": null,
     * "etfUrl": null,
     * "moodUrlShow": "0",
     * "etfUrlShow": "0"
     */

    private String id;

    private String periodId;

    private String periodName;

    private String chance;

    private String fundamentalAnalysis;

    private String analystOpinion;

    private String variety;

    private String direction;

    private int directionUpNum;

    private int directionDownNum;

    private String directionUpRate;

    private String directionDownRate;

    private List<RelevantInfoListVosBean> relevantInfoListVos;

    private String openPositionsTimeBegin;

    private String openPositionsTimeEnd;

    private String closePositionsTimeBegin;

    private String closePositionsTimeEnd;

    private String closeTimeStr;

    private long closeTime;

    private String mainTitle;

    private String mainContent;

    private String moodUrl;

    private String etfUrl;

    private String moodUrlShow;

    private String etfUrlShow;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getChance() {
        return chance;
    }

    public void setChance(String chance) {
        this.chance = chance;
    }

    public String getFundamentalAnalysis() {
        return fundamentalAnalysis;
    }

    public void setFundamentalAnalysis(String fundamentalAnalysis) {
        this.fundamentalAnalysis = fundamentalAnalysis;
    }

    public String getAnalystOpinion() {
        return analystOpinion;
    }

    public void setAnalystOpinion(String analystOpinion) {
        this.analystOpinion = analystOpinion;
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

    public int getDirectionUpNum() {
        return directionUpNum;
    }

    public void setDirectionUpNum(int directionUpNum) {
        this.directionUpNum = directionUpNum;
    }

    public int getDirectionDownNum() {
        return directionDownNum;
    }

    public void setDirectionDownNum(int directionDownNum) {
        this.directionDownNum = directionDownNum;
    }

    public String getDirectionUpRate() {
        return directionUpRate;
    }

    public void setDirectionUpRate(String directionUpRate) {
        this.directionUpRate = directionUpRate;
    }

    public String getDirectionDownRate() {
        return directionDownRate;
    }

    public void setDirectionDownRate(String directionDownRate) {
        this.directionDownRate = directionDownRate;
    }

    public List<RelevantInfoListVosBean> getRelevantInfoListVos() {
        return relevantInfoListVos;
    }

    public void setRelevantInfoListVos(List<RelevantInfoListVosBean> relevantInfoListVos) {
        this.relevantInfoListVos = relevantInfoListVos;
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

    public String getCloseTimeStr() {
        return closeTimeStr;
    }

    public void setCloseTimeStr(String closeTimeStr) {
        this.closeTimeStr = closeTimeStr;
    }

    public long getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(long closeTime) {
        this.closeTime = closeTime;
    }

    public String getMainTitle() {
        return mainTitle;
    }

    public void setMainTitle(String mainTitle) {
        this.mainTitle = mainTitle;
    }

    public String getMainContent() {
        return mainContent;
    }

    public void setMainContent(String mainContent) {
        this.mainContent = mainContent;
    }

    public String getMoodUrl() {
        return moodUrl;
    }

    public void setMoodUrl(String moodUrl) {
        this.moodUrl = moodUrl;
    }

    public String getEtfUrl() {
        return etfUrl;
    }

    public void setEtfUrl(String etfUrl) {
        this.etfUrl = etfUrl;
    }

    public String getMoodUrlShow() {
        return moodUrlShow;
    }

    public void setMoodUrlShow(String moodUrlShow) {
        this.moodUrlShow = moodUrlShow;
    }

    public String getEtfUrlShow() {
        return etfUrlShow;
    }

    public void setEtfUrlShow(String etfUrlShow) {
        this.etfUrlShow = etfUrlShow;
    }

    public static class RelevantInfoListVosBean implements Serializable{
        /**
         * content : string
         * id : string
         */

        private String id;

        private String content;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}
