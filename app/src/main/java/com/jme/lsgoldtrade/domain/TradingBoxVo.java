package com.jme.lsgoldtrade.domain;

import java.io.Serializable;
import java.util.List;

public class TradingBoxVo implements Serializable {
    /**
     *  "periodId": "1173038066567331842",
     *  "periodName": "徐军测试-2",
     *  "historyListVoList": [
     *     {
     *       "tradeId": "1173038348902711298",
     *       "chance": "银看空",
     *       "variety": "Ag(T+D)",
     *       "direction": "1",
     *       "pushTime": "2019-09-15 00:00:00",
     *       "analystOpinion": null,
     *       "moodUrl": null,
     *       "etfUrl": null,
     *       "moodUrlShow": null,
     *       "etfUrlShow": null,
     *       "closeTimeStr": "2019-09-30 00:00:00",
     *       "closeTime": 1260793,
     *       "mainTitle": "中秋节测试",
     *       "mainContent": "中秋节测试"
     *      }
     *   ],
     *   "subscriberCount": null,
     *   "historyListVos": null
     */

    private String periodId;

    private String periodName;

    private List<TradingBoxListVoBean> historyListVoList;

    private String subscriberCount;

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

    public List<TradingBoxListVoBean> getHistoryListVoList() {
        return historyListVoList;
    }

    public void setHistoryListVoList(List<TradingBoxListVoBean> historyListVoList) {
        this.historyListVoList = historyListVoList;
    }

    public String getSubscriberCount() {
        return subscriberCount;
    }

    public void setSubscriberCount(String subscriberCount) {
        this.subscriberCount = subscriberCount;
    }

    public static class TradingBoxListVoBean implements Serializable{

        private String tradeId;

        private String chance;

        private String variety;

        private String direction;

        private String pushTime;

        private String analystOpinion;

        private String moodUrl;

        private String etfUrl;

        private String moodUrlShow;

        private String etfUrlShow;

        private String closeTimeStr;

        private long closeTime;

        private String mainTitle;

        private String mainContent;

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

        public String getAnalystOpinion() {
            return analystOpinion;
        }

        public void setAnalystOpinion(String analystOpinion) {
            this.analystOpinion = analystOpinion;
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
    }

}
