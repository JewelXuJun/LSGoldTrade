package com.jme.lsgoldtrade.domain;

import java.io.Serializable;
import java.util.List;

public class InOutTurnOverVo implements Serializable {

    private String searchKey;

    private List<TurnOverBean> turnover;

    public String getSearchKey() {
        return searchKey;
    }

    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
    }

    public List<TurnOverBean> getTurnover() {
        return turnover;
    }

    public void setTurnover(List<TurnOverBean> turnover) {
        this.turnover = turnover;
    }

    public static class TurnOverBean implements Serializable {
        /**
         * "serialNo":"02005000095",
         * "direction":0,
         * "tradeDate":"2017-01-01",
         * "tradeTime":"10:10:10",
         * "depositFlag":0,
         * "amount":10000,
         * "summary":"客户出金"
         */

        private String serialNo;

        private String direction;

        private String tradeDate;

        private String tradeTime;

        private String depositFlag;

        private long amount;

        private String summary;

        public String getSerialNo() {
            return serialNo;
        }

        public void setSerialNo(String serialNo) {
            this.serialNo = serialNo;
        }

        public String getDirection() {
            return direction;
        }

        public void setDirection(String direction) {
            this.direction = direction;
        }

        public String getTradeDate() {
            return tradeDate;
        }

        public void setTradeDate(String tradeDate) {
            this.tradeDate = tradeDate;
        }

        public String getTradeTime() {
            return tradeTime;
        }

        public void setTradeTime(String tradeTime) {
            this.tradeTime = tradeTime;
        }

        public String getDepositFlag() {
            return depositFlag;
        }

        public void setDepositFlag(String depositFlag) {
            this.depositFlag = depositFlag;
        }

        public long getAmount() {
            return amount;
        }

        public void setAmount(long amount) {
            this.amount = amount;
        }

        public String getSummary() {
            return summary;
        }

        public void setSummary(String summary) {
            this.summary = summary;
        }
    }


}
