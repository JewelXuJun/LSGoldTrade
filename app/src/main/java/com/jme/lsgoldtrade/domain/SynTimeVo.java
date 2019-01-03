package com.jme.lsgoldtrade.domain;

import java.io.Serializable;
import java.util.List;

public class SynTimeVo implements Serializable {

    /**
     * "sysTime":12321321816060000000, // 毫秒数 long
     * "lastNoticeID":3322,
     * "tradeTotalCount":1,
     * "tradeDate":"20180707",
     * "sysStatus":5,
     * "tradelst":[
     * {
     * orderNo : 21232323
     * contractId : "au9999";
     * name: "黄金(T+D)",
     * quantity : 1,
     * orderType : 1, // 1-现货，2-递延业务，3-中立仓，4-交割
     * bsFlag : 1, // 1买，2卖
     * ocFlag : 0, // 开平标志，0-开仓 1-平仓 9-不适用
     * tradingType : 1, // 0-普通限价委托，1-限价FAK，2-限价FOK，3-市价FAK，4-市价FOK，5-市价转限价
     * closeFlag : 1 // 强平标志0-否，1-工行强平
     * }
     * ]
     */

    private long sysTime;

    private String lastNoticeID;

    private int tradeTotalCount;

    private String tradeDate;

    private int sysStatus;

    private List<TradeBean> tradelst;

    public long getSysTime() {
        return sysTime;
    }

    public void setSysTime(long sysTime) {
        this.sysTime = sysTime;
    }

    public String getLastNoticeID() {
        return lastNoticeID;
    }

    public void setLastNoticeID(String lastNoticeID) {
        this.lastNoticeID = lastNoticeID;
    }

    public int getTradeTotalCount() {
        return tradeTotalCount;
    }

    public void setTradeTotalCount(int tradeTotalCount) {
        this.tradeTotalCount = tradeTotalCount;
    }

    public String getTradeDate() {
        return tradeDate;
    }

    public void setTradeDate(String tradeDate) {
        this.tradeDate = tradeDate;
    }

    public int getSysStatus() {
        return sysStatus;
    }

    public void setSysStatus(int sysStatus) {
        this.sysStatus = sysStatus;
    }

    public List<TradeBean> getTradelst() {
        return tradelst;
    }

    public void setTradelst(List<TradeBean> tradelst) {
        this.tradelst = tradelst;
    }

    public static class TradeBean {
        /**
         * orderNo : 21232323
         * contractId : "au9999";
         * name: "黄金(T+D)",
         * quantity : 1,
         * orderType : 1, // 1-现货，2-递延业务，3-中立仓，4-交割
         * bsFlag : 1, // 1买，2卖
         * ocFlag : 0, // 开平标志，0-开仓 1-平仓 9-不适用
         * tradingType : 1, // 0-普通限价委托，1-限价FAK，2-限价FOK，3-市价FAK，4-市价FOK，5-市价转限价
         * closeFlag : 1 // 强平标志0-否，1-工行强平
         */

        private String orderNo;

        private String contractId;

        private String name;

        private int quantity;

        private int orderType;

        private int bsFlag;

        private int ocFlag;

        private int tradingType;

        private int closeFlag;

        public String getOrderNo() {
            return orderNo;
        }

        public void setOrderNo(String orderNo) {
            this.orderNo = orderNo;
        }

        public String getContractId() {
            return contractId;
        }

        public void setContractId(String contractId) {
            this.contractId = contractId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
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
    }

}
