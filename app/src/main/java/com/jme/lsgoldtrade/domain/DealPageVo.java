package com.jme.lsgoldtrade.domain;

import com.jme.lsgoldtrade.util.MarketUtil;

import java.io.Serializable;
import java.util.List;

public class DealPageVo implements Serializable {

    private boolean hasNext;

    private String pagingKey;

    private List<DealBean> list;

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

    public List<DealBean> getList() {
        return list;
    }

    public void setList(List<DealBean> list) {
        this.list = list;
    }

    public static class DealBean {
        /**
         * "matchNo":"1704190000009857",
         * "bsFlag":2,
         * "contractId":"Au(T+N2)",
         * "matchDate":"2017-01-01",
         * "matchTime":"10:10:10",
         * "matchPrice":30000,
         * "matchQuantity":1000000000,
         * "matchHand":1000,
         * "amount":1000000,
         * "orderNo":02005000095,
         * "orderType":1,
         * "ocFlag":0,
         * "tradingType":4,
         * "bankCloseFlag": 0,
         * "exOrderNo": "02000473",
         * "transactionFee": 0,
         * "basicMarginRate": 900
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

        public String getMatchPriceStr() {
            return MarketUtil.getPriceValue(matchPrice);
        }

        public String getAmountStr() {
            return MarketUtil.getPriceValue(amount);
        }
    }

}
