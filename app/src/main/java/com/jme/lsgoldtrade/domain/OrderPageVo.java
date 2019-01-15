package com.jme.lsgoldtrade.domain;

import com.jme.lsgoldtrade.util.MarketUtil;

import java.io.Serializable;
import java.util.List;

public class OrderPageVo implements Serializable {

    private boolean hasNext;

    private String pagingKey;

    private List<OrderBean> list;

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

    public List<OrderBean> getList() {
        return list;
    }

    public void setList(List<OrderBean> list) {
        this.list = list;
    }

    public static class OrderBean implements Serializable {
        /**
         * "tradingType": 0,
         * "orderNo": 2000980845,
         * "tradeDate": "2019-01-14",
         * "declareTime": "10.27.26",
         * "contractType": 1,
         * "status": 5,
         * "orderType": 2,
         * "bsFlag": 2,
         * "ocFlag": 0,
         * "contractId": "mAu(T+D)",
         * "matchPrice": 20760,
         * "entrustNumber": 1,
         * "entrustWeight": 100000000,
         * "remnantNumber": 0,
         * "remnantWeight": 0,
         * "revocationTime": ""
         */

        private int tradingType;

        private String orderNo;

        private String tradeDate;

        private String declarTime;

        private int contractType;

        private int status;

        private int orderType;

        private int bsFlag;

        private int ocFlag;

        private String contractId;

        private long matchPrice;

        private long entrustNumber;

        private long entrustWeight;

        private long remnantNumber;

        private long remnantWeight;

        private String revocationTime;

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

        public int getContractType() {
            return contractType;
        }

        public void setContractType(int contractType) {
            this.contractType = contractType;
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

        public String getContractId() {
            return contractId;
        }

        public void setContractId(String contractId) {
            this.contractId = contractId;
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
