package com.jme.lsgoldtrade.domain;

import java.io.Serializable;
import java.util.List;

public class InOutTurnOverVo implements Serializable {

    private boolean hasNext;

    private String pagingKey;

    private List<TurnOverBean> list;

    public boolean isHasNext() {
        return hasNext;
    }

    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }

    public String getPagingKey() {
        return pagingKey;
    }

    public void setPagingKey(String pagingKey) {
        this.pagingKey = pagingKey;
    }

    public List<TurnOverBean> getList() {
        return list;
    }

    public void setList(List<TurnOverBean> list) {
        this.list = list;
    }

    public static class TurnOverBean implements Serializable {
        /**
         * "bankSerialNo": "19030113344399928500454",
         * "direction": 1,
         * "tradeDate": "2019-03-01",
         * "tradeTime": "13.34.43",
         * "depositFlag": 1,
         * "amount": 100,
         * "summary": "出金"
         */

        private String bankSerialNo;

        private int direction;

        private String tradeDate;

        private String tradeTime;

        private int depositFlag;

        private long amount;

        private String summary;

        public String getBankSerialNo() {
            return bankSerialNo;
        }

        public void setBankSerialNo(String bankSerialNo) {
            this.bankSerialNo = bankSerialNo;
        }

        public int getDirection() {
            return direction;
        }

        public void setDirection(int direction) {
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

        public int getDepositFlag() {
            return depositFlag;
        }

        public void setDepositFlag(int depositFlag) {
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
