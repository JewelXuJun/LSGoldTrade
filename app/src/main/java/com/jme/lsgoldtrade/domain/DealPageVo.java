package com.jme.lsgoldtrade.domain;

import java.io.Serializable;
import java.util.List;

public class DealPageVo implements Serializable {

    private boolean hasNext;

    private List<DealBean> list;

    public boolean isHasNext() {
        return hasNext;
    }

    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }

    public List<DealBean> getList() {
        return list;
    }

    public void setList(List<DealBean> list) {
        this.list = list;
    }

    public static class DealBean {
        /**
         *  "matchNo":"1704190000009857",
         *  "bsFlag":2,
         *  "contractId":"Au(T+N2)",
         *  "matchDate":"2017-01-01",
         *  "matchTime":"10:10:10",
         *  "matchPrice":30000,
         *  "matchQuantity":1000000000,
         *  "matchHand":1000,
         *  "amount":1000000,
         *  "orderNo":02005000095,
         *  "orderType":1,
         *  "ocFlag":0,
         *  "tradingType":4,
         *  "closeFlag":0,
         *  "transactionFee":100
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

        private String orderNo;

        private int orderType;

        private int ocFlag;

        private int tradingType;

        private int closeFlag;

        private long transactionFee;

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

        public String getOrderNo() {
            return orderNo;
        }

        public void setOrderNo(String orderNo) {
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

        public int getCloseFlag() {
            return closeFlag;
        }

        public void setCloseFlag(int closeFlag) {
            this.closeFlag = closeFlag;
        }

        public long getTransactionFee() {
            return transactionFee;
        }

        public void setTransactionFee(long transactionFee) {
            this.transactionFee = transactionFee;
        }
    }

}
