package com.jme.lsgoldtrade.domain;

import java.io.Serializable;
import java.util.List;

public class HistoryBoxVo implements Serializable {

    /**
     * periodId : 1143698348910444545
     * periodName : 20190626-2
     * historyListVoList : [{"tradeId":"1143698595044786178","chance":"123","variety":"Ag(T+D)","direction":"0","pushTime":"2019-06-26 00:00:00"}]
     */

    private String periodId;
    private String periodName;
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

    public List<HistoryListVoListBean> getHistoryListVoList() {
        return historyListVoList;
    }

    public void setHistoryListVoList(List<HistoryListVoListBean> historyListVoList) {
        this.historyListVoList = historyListVoList;
    }

    public static class HistoryListVoListBean {
        /**
         * tradeId : 1143698595044786178
         * chance : 123
         * variety : Ag(T+D)
         * direction : 0
         * pushTime : 2019-06-26 00:00:00
         */

        private String tradeId;
        private String chance;
        private String variety;
        private String direction;
        private String pushTime;

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
}
