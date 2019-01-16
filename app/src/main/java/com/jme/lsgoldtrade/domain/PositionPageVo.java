package com.jme.lsgoldtrade.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PositionPageVo implements Serializable {

    private boolean hasNext;

    private String pagingKey;

    private List<PositionBean> list;

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

    public List<PositionBean> getList() {
        return list;
    }

    public void setList(List<PositionBean> list) {
        this.list = list;
    }

    public static class PositionBean implements Serializable {
        /**
         * "contractId": "mAu(T+D)",
         * "longPosition": 5,
         * "shortPosition": 1,
         * "lastLongPosition": 0,
         * "lastShortPosition": 0,
         * "todayLongPosition": 5,
         * "todayShortPosition": 1,
         * "longOffsetPosition": 0,
         * "shortOffsetPosition": 0,
         * "longTurnOver": 10378500,
         * "shortTurnOver": 0,
         * "deliveryApplyLongFrozen": 0,
         * "deliveryApplyShortFrozen": 0,
         * "deliveryApplyLong": 0,
         * "deliveryApplyShort": 0,
         * "longLimitFrozen": 0,
         * "shortLimitFrozen": 0,
         * "offsetLongFrozen": 0,
         * "offsetShortFrozen": 0,
         * "longPositionAverage": 20757,
         * "shortPositionAverage": 21222,
         * "longPositionMargin": 934065,
         * "shortPositionMargin": 0,
         * "longPositionLimit": 2000,
         * "shortPositionLimit": 2000,
         * "longFloatProfit": 232500,
         * "shortFloatProfit": 0,
         * "longUnliquidatedProfit": 0,
         * "shortUnliquidatedProfit": 0
         */

        private String contractId;

        private long longPosition;

        private long shortPosition;

        private long lastLongPosition;

        private long lastShortPosition;

        private long todayLongPosition;

        private long todayShortPosition;

        private long longOffsetPosition;

        private long shortOffsetPosition;

        private long longTurnover;

        private long shortTurnover;

        private long deliveryApplyLongFrozen;

        private long deliveryApplyShortFrozen;

        private long deliveryApplyLong;

        private long deliveryApplyShort;

        private long longLimitFrozen;

        private long shortLimitFrozen;

        private long offsetLongFrozen;

        private long offsetShortFrozen;

        private long longPositionAverage;

        private long shortPositionAverage;

        private long longPositionMargin;

        private long shortPositionMargin;

        private long longPositionLimit;

        private long shortPositionLimit;

        private long longFloatProfit;

        private long shortFloatProfit;

        private long longUnliquidatedProfit;

        private long shortUnliquidatedProfit;

        public String getContractId() {
            return contractId;
        }

        public void setContractId(String contractId) {
            this.contractId = contractId;
        }

        public long getLongPosition() {
            return longPosition;
        }

        public void setLongPosition(long longPosition) {
            this.longPosition = longPosition;
        }

        public long getShortPosition() {
            return shortPosition;
        }

        public void setShortPosition(long shortPosition) {
            this.shortPosition = shortPosition;
        }

        public long getLastLongPosition() {
            return lastLongPosition;
        }

        public void setLastLongPosition(long lastLongPosition) {
            this.lastLongPosition = lastLongPosition;
        }

        public long getLastShortPosition() {
            return lastShortPosition;
        }

        public void setLastShortPosition(long lastShortPosition) {
            this.lastShortPosition = lastShortPosition;
        }

        public long getTodayLongPosition() {
            return todayLongPosition;
        }

        public void setTodayLongPosition(long todayLongPosition) {
            this.todayLongPosition = todayLongPosition;
        }

        public long getTodayShortPosition() {
            return todayShortPosition;
        }

        public void setTodayShortPosition(long todayShortPosition) {
            this.todayShortPosition = todayShortPosition;
        }

        public long getLongOffsetPosition() {
            return longOffsetPosition;
        }

        public void setLongOffsetPosition(long longOffsetPosition) {
            this.longOffsetPosition = longOffsetPosition;
        }

        public long getShortOffsetPosition() {
            return shortOffsetPosition;
        }

        public void setShortOffsetPosition(long shortOffsetPosition) {
            this.shortOffsetPosition = shortOffsetPosition;
        }

        public long getLongTurnover() {
            return longTurnover;
        }

        public void setLongTurnover(long longTurnover) {
            this.longTurnover = longTurnover;
        }

        public long getShortTurnover() {
            return shortTurnover;
        }

        public void setShortTurnover(long shortTurnover) {
            this.shortTurnover = shortTurnover;
        }

        public long getDeliveryApplyLongFrozen() {
            return deliveryApplyLongFrozen;
        }

        public void setDeliveryApplyLongFrozen(long deliveryApplyLongFrozen) {
            this.deliveryApplyLongFrozen = deliveryApplyLongFrozen;
        }

        public long getDeliveryApplyShortFrozen() {
            return deliveryApplyShortFrozen;
        }

        public void setDeliveryApplyShortFrozen(long deliveryApplyShortFrozen) {
            this.deliveryApplyShortFrozen = deliveryApplyShortFrozen;
        }

        public long getDeliveryApplyLong() {
            return deliveryApplyLong;
        }

        public void setDeliveryApplyLong(long deliveryApplyLong) {
            this.deliveryApplyLong = deliveryApplyLong;
        }

        public long getDeliveryApplyShort() {
            return deliveryApplyShort;
        }

        public void setDeliveryApplyShort(long deliveryApplyShort) {
            this.deliveryApplyShort = deliveryApplyShort;
        }

        public long getLongLimitFrozen() {
            return longLimitFrozen;
        }

        public void setLongLimitFrozen(long longLimitFrozen) {
            this.longLimitFrozen = longLimitFrozen;
        }

        public long getShortLimitFrozen() {
            return shortLimitFrozen;
        }

        public void setShortLimitFrozen(long shortLimitFrozen) {
            this.shortLimitFrozen = shortLimitFrozen;
        }

        public long getOffsetLongFrozen() {
            return offsetLongFrozen;
        }

        public void setOffsetLongFrozen(long offsetLongFrozen) {
            this.offsetLongFrozen = offsetLongFrozen;
        }

        public long getOffsetShortFrozen() {
            return offsetShortFrozen;
        }

        public void setOffsetShortFrozen(long offsetShortFrozen) {
            this.offsetShortFrozen = offsetShortFrozen;
        }

        public long getLongPositionAverage() {
            return longPositionAverage;
        }

        public void setLongPositionAverage(long longPositionAverage) {
            this.longPositionAverage = longPositionAverage;
        }

        public long getShortPositionAverage() {
            return shortPositionAverage;
        }

        public void setShortPositionAverage(long shortPositionAverage) {
            this.shortPositionAverage = shortPositionAverage;
        }

        public long getLongPositionMargin() {
            return longPositionMargin;
        }

        public void setLongPositionMargin(long longPositionMargin) {
            this.longPositionMargin = longPositionMargin;
        }

        public long getShortPositionMargin() {
            return shortPositionMargin;
        }

        public void setShortPositionMargin(long shortPositionMargin) {
            this.shortPositionMargin = shortPositionMargin;
        }

        public long getLongPositionLimit() {
            return longPositionLimit;
        }

        public void setLongPositionLimit(long longPositionLimit) {
            this.longPositionLimit = longPositionLimit;
        }

        public long getShortPositionLimit() {
            return shortPositionLimit;
        }

        public void setShortPositionLimit(long shortPositionLimit) {
            this.shortPositionLimit = shortPositionLimit;
        }

        public long getLongFloatProfit() {
            return longFloatProfit;
        }

        public void setLongFloatProfit(long longFloatProfit) {
            this.longFloatProfit = longFloatProfit;
        }

        public long getShortFloatProfit() {
            return shortFloatProfit;
        }

        public void setShortFloatProfit(long shortFloatProfit) {
            this.shortFloatProfit = shortFloatProfit;
        }

        public long getLongUnliquidatedProfit() {
            return longUnliquidatedProfit;
        }

        public void setLongUnliquidatedProfit(long longUnliquidatedProfit) {
            this.longUnliquidatedProfit = longUnliquidatedProfit;
        }

        public long getShortUnliquidatedProfit() {
            return shortUnliquidatedProfit;
        }

        public void setShortUnliquidatedProfit(long shortUnliquidatedProfit) {
            this.shortUnliquidatedProfit = shortUnliquidatedProfit;
        }
    }

    public List<PositionVo> getPositionList() {
        List<PositionVo> positionVoList = null;

        if (null != list && 0 != list.size()) {
            positionVoList = new ArrayList<>();

            for (PositionPageVo.PositionBean positionBean : list) {
                if (null != positionBean) {
                    PositionVo longPositionVo = new PositionVo();
                    longPositionVo.setContractId(positionBean.getContractId());
                    longPositionVo.setType("多");
                    longPositionVo.setPosition(positionBean.getLongPosition());
                    longPositionVo.setLastPosition(positionBean.getLastLongPosition());
                    longPositionVo.setTodayPosition(positionBean.getTodayLongPosition());
                    longPositionVo.setOffsetPosition(positionBean.getLongOffsetPosition());
                    longPositionVo.setTurnOver(positionBean.getLongTurnover());
                    longPositionVo.setDeliveryApplyFrozen(positionBean.getDeliveryApplyLongFrozen());
                    longPositionVo.setDeliveryApply(positionBean.getDeliveryApplyLong());
                    longPositionVo.setLimitFrozen(positionBean.getLongLimitFrozen());
                    longPositionVo.setOffsetFrozen(positionBean.getOffsetLongFrozen());
                    longPositionVo.setPositionAverage(positionBean.getLongPositionAverage());
                    longPositionVo.setPositionMargin(positionBean.getLongPositionMargin());
                    longPositionVo.setPositionLimit(positionBean.getLongPositionLimit());
                    longPositionVo.setFloatProfit(positionBean.getLongFloatProfit());
                    longPositionVo.setUnliquidatedProfit(positionBean.getLongUnliquidatedProfit());

                    positionVoList.add(longPositionVo);

                    PositionVo shortPositionVo = new PositionVo();
                    shortPositionVo.setContractId(positionBean.getContractId());
                    shortPositionVo.setType("空");
                    shortPositionVo.setPosition(positionBean.getShortPosition());
                    shortPositionVo.setLastPosition(positionBean.getLastShortPosition());
                    shortPositionVo.setTodayPosition(positionBean.getTodayShortPosition());
                    shortPositionVo.setOffsetPosition(positionBean.getShortOffsetPosition());
                    shortPositionVo.setTurnOver(positionBean.getShortTurnover());
                    shortPositionVo.setDeliveryApplyFrozen(positionBean.getDeliveryApplyShortFrozen());
                    shortPositionVo.setDeliveryApply(positionBean.getDeliveryApplyShort());
                    shortPositionVo.setLimitFrozen(positionBean.getShortLimitFrozen());
                    shortPositionVo.setOffsetFrozen(positionBean.getOffsetShortFrozen());
                    shortPositionVo.setPositionAverage(positionBean.getShortPositionAverage());
                    shortPositionVo.setPositionMargin(positionBean.getShortPositionMargin());
                    shortPositionVo.setPositionLimit(positionBean.getShortPositionLimit());
                    shortPositionVo.setFloatProfit(positionBean.getShortFloatProfit());
                    shortPositionVo.setUnliquidatedProfit(positionBean.getShortUnliquidatedProfit());

                    positionVoList.add(shortPositionVo);
                }
            }
        }

        return positionVoList;
    }

}
