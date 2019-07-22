package com.jme.lsgoldtrade.domain;

import java.io.Serializable;
import java.util.List;

public class TradingBoxDataInfoVo implements Serializable {

    /**
     * "periodId":"1150995582228111362",
     * "periodName":"20190716",
     * "historyListVoList":[
     * {
     * "tradeId":"1150996933553168386",
     * "chance":"测试",
     * "variety":"Ag(T+D)",
     * "direction":"0",
     * "pushTime":"2019-07-16 13:00:00",
     * "analystOpinion":"美指在之前筹码密集区域横盘调整，今日继续关注此支撑位，看是否能形成有效支撑。晚间20:30恐怖数据重磅来袭，最终是企稳反弹还是击穿筹码密集区域继续下跌，此数据将起到决定性作用。",
     * "moodUrl":"https://tjshj.oss-cn-beijing.aliyuncs.com/20190716/15632540160042945381397191407287.png",
     * "etfUrl":"https://tjshj.oss-cn-beijing.aliyuncs.com/20190716/15632540200053733879753093860527.png",
     * "moodUrlShow":"0",
     * "etfUrlShow":"0",
     * "closeTimeStr":"2019-07-16 14:00:00",
     * "closeTime":0
     * }
     * ],
     * "subscriberCount":13}
     */

    private String periodId;

    private String periodName;

    private int subscriberCount;

    private List<HistoryVoBean> historyListVoList;

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

    public List<HistoryVoBean> getHistoryListVoList() {
        return historyListVoList;
    }

    public void setHistoryListVoList(List<HistoryVoBean> historyListVoList) {
        this.historyListVoList = historyListVoList;
    }

    public static class HistoryVoBean {
        /**
         * "tradeId":"1150996933553168386",
         * "chance":"测试",
         * "variety":"Ag(T+D)",
         * "direction":"0",
         * "pushTime":"2019-07-16 13:00:00",
         * "analystOpinion":"美指在之前筹码密集区域横盘调整，今日继续关注此支撑位，看是否能形成有效支撑。晚间20:30恐怖数据重磅来袭，最终是企稳反弹还是击穿筹码密集区域继续下跌，此数据将起到决定性作用。",
         * "moodUrl":"https://tjshj.oss-cn-beijing.aliyuncs.com/20190716/15632540160042945381397191407287.png",
         * "etfUrl":"https://tjshj.oss-cn-beijing.aliyuncs.com/20190716/15632540200053733879753093860527.png",
         * "moodUrlShow":"0",
         * "etfUrlShow":"0",
         * "closeTimeStr":"2019-07-16 14:00:00",
         * "closeTime":0
         */

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
    }
}
