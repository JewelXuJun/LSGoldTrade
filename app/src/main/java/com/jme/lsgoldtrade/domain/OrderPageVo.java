package com.jme.lsgoldtrade.domain;

import com.jme.lsgoldtrade.util.MarketUtil;

import java.io.Serializable;
import java.util.List;

public class OrderPageVo implements Serializable {

    private boolean hasNext;

    private List<OrderBean> list;

    public boolean isHasNext() {
        return hasNext;
    }

    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }

    public List<OrderBean> getList() {
        return list;
    }

    public void setList(List<OrderBean> list) {
        this.list = list;
    }

    public static class OrderBean implements Serializable {
        /**
         * "contractId":"Au(T+N2)",
         * "tradingType":4,
         * "orderNo":02007700092,
         * "tradeDate":"2017-07-10",
         * "declarTime":"10:10:10",
         * "marketId":1,
         * "status":1,
         * "orderType":1,
         * "bsFlag":2,
         * "ocFlag":0,
         * "matchPrice":30000,
         * "entrustNumber":100,
         * "entrustWeight":1000000000,
         * "remnantNumber":100,
         * "remnantWeight":1000000000,
         * "revocationTime":"23.59.59"
         */

        private String contractId;

        private int tradingType;

        private String orderNo;

        private String tradeDate;

        private String declarTime;

        private int marketId;

        private int status;

        private int orderType;

        private int bsFlag;

        private int ocFlag;

        private long matchPrice;

        private long entrustNumber;

        private long entrustWeight;

        private long remnantNumber;

        private long remnantWeight;

        private String revocationTime;

        public String getContractId() {
            return contractId;
        }

        public void setContractId(String contractId) {
            this.contractId = contractId;
        }

        public int getTradingType() {
            return tradingType;
        }

        public void setTradingType(int tradingType) {
            this.tradingType = tradingType;
        }

        public String getOrderNo() {
            return orderNo;
        }

        public void setOrderNo(String orderNo) {
            this.orderNo = orderNo;
        }

        public String getTradeDate() {
            return tradeDate;
        }

        public void setTradeDate(String tradeDate) {
            this.tradeDate = tradeDate;
        }

        public String getDeclarTime() {
            return declarTime;
        }

        public void setDeclarTime(String declarTime) {
            this.declarTime = declarTime;
        }

        public int getMarketId() {
            return marketId;
        }

        public void setMarketId(int marketId) {
            this.marketId = marketId;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getOrderType() {
            return orderType;
        }

        public void setOrderType(int orderType) {
            this.orderType = orderType;
        }

        public int getBsFlag() {
            return bsFlag;
        }

        public void setBsFlag(int bsFlag) {
            this.bsFlag = bsFlag;
        }

        public int getOcFlag() {
            return ocFlag;
        }

        public void setOcFlag(int ocFlag) {
            this.ocFlag = ocFlag;
        }

        public long getMatchPrice() {
            return matchPrice;
        }

        public void setMatchPrice(long matchPrice) {
            this.matchPrice = matchPrice;
        }

        public long getEntrustNumber() {
            return entrustNumber;
        }

        public void setEntrustNumber(long entrustNumber) {
            this.entrustNumber = entrustNumber;
        }

        public long getEntrustWeight() {
            return entrustWeight;
        }

        public void setEntrustWeight(long entrustWeight) {
            this.entrustWeight = entrustWeight;
        }

        public long getRemnantNumber() {
            return remnantNumber;
        }

        public void setRemnantNumber(long remnantNumber) {
            this.remnantNumber = remnantNumber;
        }

        public long getRemnantWeight() {
            return remnantWeight;
        }

        public void setRemnantWeight(long remnantWeight) {
            this.remnantWeight = remnantWeight;
        }

        public String getRevocationTime() {
            return revocationTime;
        }

        public void setRevocationTime(String revocationTime) {
            this.revocationTime = revocationTime;
        }

        public String getMatchPriceStr() {
            return MarketUtil.getPriceValue(matchPrice);
        }
    }

}
