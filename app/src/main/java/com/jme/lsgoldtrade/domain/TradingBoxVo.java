package com.jme.lsgoldtrade.domain;

import java.io.Serializable;
import java.util.List;

public class TradingBoxVo implements Serializable {

    /**
     * periodId : 1126363586070560769
     * periodName : G
     * historyListVoList : [{"tradeId":"1126681565062557698","chance":"刚变换会议形式这家央行就降息至纪录新低，纽元闻讯暴走","variety":"mAu(T+D)","direction":"1","pushTime":"2019-05-16 00:00:00","analystOpinion":"风险方面，该央行认为关键风险是澳洲等地的经济放缓大于预期。此外，全球经济增速放缓，对新西兰商品与服务的需求有所减弱。","moodUrl":"https://tjshj.oss-cn-beijing.aliyuncs.com/20190510/15574654032242979443324998700823.jpg","etfUrl":"https://tjshj.oss-cn-beijing.aliyuncs.com/20190510/15574654057102165544659267099395.jpg"}]
     * subscriberCount : 3
     */

    private String periodId;
    private String periodName;
    private int subscriberCount;
    private int closeTime;
    private List<HistoryListVoListBean> historyListVoList;

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

    public int getSubscriberCount() {
        return subscriberCount;
    }

    public void setSubscriberCount(int subscriberCount) {
        this.subscriberCount = subscriberCount;
    }

    public int getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(int closeTime) {
        this.closeTime = closeTime;
    }

    public List<HistoryListVoListBean> getHistoryListVoList() {
        return historyListVoList;
    }

    public void setHistoryListVoList(List<HistoryListVoListBean> historyListVoList) {
        this.historyListVoList = historyListVoList;
    }

    public static class HistoryListVoListBean {
        /**
         * tradeId : 1126681565062557698
         * chance : 刚变换会议形式这家央行就降息至纪录新低，纽元闻讯暴走
         * variety : mAu(T+D)
         * direction : 1
         * pushTime : 2019-05-16 00:00:00
         * analystOpinion : 风险方面，该央行认为关键风险是澳洲等地的经济放缓大于预期。此外，全球经济增速放缓，对新西兰商品与服务的需求有所减弱。
         * moodUrl : https://tjshj.oss-cn-beijing.aliyuncs.com/20190510/15574654032242979443324998700823.jpg
         * etfUrl : https://tjshj.oss-cn-beijing.aliyuncs.com/20190510/15574654057102165544659267099395.jpg
         */

        private String tradeId;
        private String chance;
        private String variety;
        private String direction;
        private String pushTime;
        private String analystOpinion;
        private String moodUrl;
        private String etfUrl;
        private int closeTime;

        public int getCloseTime() {
            return closeTime;
        }

        public void setCloseTime(int closeTime) {
            this.closeTime = closeTime;
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
    }
}
