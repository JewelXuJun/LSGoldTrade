package com.datai.common.charts.fchart;

import java.util.List;

/**
 * Created by XuJun on 2016/5/4.
 */
public class FData {

    private List<String[]> mOfferGrpList;
    private List<String[]> mBidGrpList;
    private List<String[]> mTickData;

    public static final int STABLE = 0;             //持平
    public static final int INCREASING = 1;         //上涨
    public static final int DECREASING = 2;         //下跌

    public static final int TYPE_BUY = 0;           //买5档
    public static final int TYPE_SELL = 1;          //卖5档
    public static final int TYPE_TICK = 2;          //明细
    public static final int TYPE_TICK_PART1 = 3;    //明细1
    public static final int TYPE_TICK_PART2 = 4;    //明细2

    public static final String TICK_SELL = "0";     //卖出
    public static final String TICK_BUY = "1";      //买入

    public FData() {

    }

    public int getType(float data, float prev) {
        if (data == 0) {
            return STABLE;
        } else {
            if (data == prev) {
                return STABLE;
            } else if (data > prev) {
                return INCREASING;
            } else if (data < prev) {
                return DECREASING;
            } else {
                return STABLE;
            }
        }
    }

    public List<String[]> getOfferGrp() {
        return mOfferGrpList;
    }

    public List<String[]> getBidGrp() {
        return mBidGrpList;
    }

    public List<String[]> getTickData() {
        return mTickData;
    }

    public void setData(List<String[]> offerGrpList, List<String[]> bidGrpList,
                        List<String[]> tickData) {
        mOfferGrpList = offerGrpList;
        mBidGrpList = bidGrpList;
        mTickData = tickData;
    }

    public void setTradeData(List<String[]> offerGrpList, List<String[]> bidGrpList) {
        mOfferGrpList = offerGrpList;
        mBidGrpList = bidGrpList;
    }

    public void setDealData(List<String[]> tickData) {
        mTickData = tickData;
    }

}
