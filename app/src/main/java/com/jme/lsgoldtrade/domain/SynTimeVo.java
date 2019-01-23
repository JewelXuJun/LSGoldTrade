package com.jme.lsgoldtrade.domain;

import java.io.Serializable;
import java.util.List;

public class SynTimeVo implements Serializable {

    /**
     * "tradeDate": "2019-01-16",
     * "sysTime": 1547618592921,
     * "lastNoticeId": 17,
     * "maxMatchNo": "1901160200000040",
     * "sysStatus": 5,
     * "tradeList": [
     * {
     * "matchNo": "1901160200000040",
     * "bsFlag": 0,
     * "contractId": "Au(T+D)",
     * "matchDate": "2019-01-16",
     * "matchTime": "10.45.09",
     * "matchPrice": 27012,
     * "matchQuantity": 1000000000,
     * "matchHand": 1,
     * "amount": 27012000,
     * "orderNo": 0,
     * "orderType": 0,
     * "ocFlag": 0,
     * "tradingType": 0,
     * "bankCloseFlag": 0,
     * "exOrderNo": "02000277",
     * "transactionFee": 0,
     * "basicMarginRate": 0
     * }
     * ]
     */

    private String tradeDate;

    private long sysTime;

    private long lastNoticeId;

    private String maxMatchNo;

    private int sysStatus;

    private List<TradeBean> tradeList;

    public String getTradeDate() {
        return tradeDate;
    }

    public void setTradeDate(String tradeDate) {
        this.tradeDate = tradeDate;
    }

    public long getSysTime() {
        return sysTime;
    }

    public void setSysTime(long sysTime) {
        this.sysTime = sysTime;
    }

    public long getLastNoticeId() {
        return lastNoticeId;
    }

    public void setLastNoticeId(long lastNoticeId) {
        this.lastNoticeId = lastNoticeId;
    }

    public String getMaxMatchNo() {
        return maxMatchNo;
    }

    public void setMaxMatchNo(String maxMatchNo) {
        this.maxMatchNo = maxMatchNo;
    }

    public int getSysStatus() {
        return sysStatus;
    }

    public void setSysStatus(int sysStatus) {
        this.sysStatus = sysStatus;
    }

    public List<TradeBean> getTradeList() {
        return tradeList;
    }

    public void setTradeList(List<TradeBean> tradeList) {
        this.tradeList = tradeList;
    }

    public static class TradeBean {
        /**
         * "matchNo": "1901160200000040",
         * "bsFlag": 0,
         * "contractId": "Au(T+D)",
         * "matchDate": "2019-01-16",
         * "matchTime": "10.45.09",
         * "matchPrice": 27012,
         * "matchQuantity": 1000000000,
         * "matchHand": 1,
         * "amount": 27012000,
         * "orderNo": 0,
         * "orderType": 0,
         * "ocFlag": 0,
         * "tradingType": 0,
         * "bankCloseFlag": 0,
         * "exOrderNo": "02000277",
         * "transactionFee": 0,
         * "basicMarginRate": 0
         */

        private String matchNo;

        private int bsFlag;

        private String contractId;

        private String matchDate;

        private String matchTime;

        private long matchPrice;

        private long matchQuantity;

        private long matchHand;

        private long amount;

        private long orderNo;

        private int orderType;

        private int ocFlag;

        private int tradingType;

        private int bankCloseFlag;

        private String exOrderNo;

        private long transactionFee;

        private long basicMarginRate;

        public String getMatchNo() {
            return matchNo;
        }

        public void setMatchNo(String matchNo) {
            this.matchNo = matchNo;
        }

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

        public String getMatchDate() {
            return matchDate;
        }

        public void setMatchDate(String matchDate) {
            this.matchDate = matchDate;
        }

        public String getMatchTime() {
            return matchTime;
        }

        public void setMatchTime(String matchTime) {
            this.matchTime = matchTime;
        }

        public long getMatchPrice() {
            return matchPrice;
        }

        public void setMatchPrice(long matchPrice) {
            this.matchPrice = matchPrice;
        }

        public long getMatchQuantity() {
            return matchQuantity;
        }

        public void setMatchQuantity(long matchQuantity) {
            this.matchQuantity = matchQuantity;
        }

        public long getMatchHand() {
            return matchHand;
        }

        public void setMatchHand(long matchHand) {
            this.matchHand = matchHand;
        }

        public long getAmount() {
            return amount;
        }

        public void setAmount(long amount) {
            this.amount = amount;
        }

        public long getOrderNo() {
            return orderNo;
        }

        public void setOrderNo(long orderNo) {
            this.orderNo = orderNo;
        }

        public int getOrderType() {
            return orderType;
        }

        public void setOrderType(int orderType) {
            this.orderType = orderType;
        }

        public int getOcFlag() {
            return ocFlag;
        }

        public void setOcFlag(int ocFlag) {
            this.ocFlag = ocFlag;
        }

        public int getTradingType() {
            return tradingType;
        }

        public void setTradingType(int tradingType) {
            this.tradingType = tradingType;
        }

        public int getBankCloseFlag() {
            return bankCloseFlag;
        }

        public void setBankCloseFlag(int bankCloseFlag) {
            this.bankCloseFlag = bankCloseFlag;
        }

        public String getExOrderNo() {
            return exOrderNo;
        }

        public void setExOrderNo(String exOrderNo) {
            this.exOrderNo = exOrderNo;
        }

        public long getTransactionFee() {
            return transactionFee;
        }

        public void setTransactionFee(long transactionFee) {
            this.transactionFee = transactionFee;
        }

        public long getBasicMarginRate() {
            return basicMarginRate;
        }

        public void setBasicMarginRate(long basicMarginRate) {
            this.basicMarginRate = basicMarginRate;
        }
    }

}
