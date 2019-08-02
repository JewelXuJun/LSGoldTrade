package com.datai.common.charts.tchart;

import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.google.gson.JsonArray;
import com.jme.common.util.DateUtil;
import com.jme.common.util.TChartVo;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by XuJun on 2016/1/21.
 */
public class TChartData {
    public static final int STABLE = 0;
    public static final int INCREASING = 1;
    public static final int DECREASING = 2;

    private SimpleDateFormat mSdf = new SimpleDateFormat("HH:mm");

    private List<long[]> mTradeTimeList;
    private long mTimeInterval;
    private boolean bIsNeedSupplement = false;//是否需要补充余数，用于平均分后的数量不能整除
    private boolean bIsStartFromBeginning = false;//是否第一条数据需要从起点开始展示
    private boolean bIsSingleVol = false;
    private String mPreclose;

    private HashMap<String, List> mValsMap = new HashMap<String, List>();
    private List<String> mTimeVals;
    private List<Entry> mCurrentVals;
    private List<Entry> mRiseRangeVals;
    private List<Entry> mAverageVals;
    private List<BarEntry> mVolVals;
    private List<Integer> mRiseVals;
    private List<Entry> mEmptyVals;

    public void setPreclose(String preclose) {
        mPreclose = preclose;
    }

    public String getPreclose() {
        return mPreclose;
    }

    public void setIsNeedSupplement(boolean enable) {                //是否需要补充余数，用于平均分后的数量不能整除
        bIsNeedSupplement = enable;
    }

    public void setIsStartFromBeginning(boolean enable) {           //是否第一条数据需要从起点开始展示
        bIsStartFromBeginning = enable;
    }

    public void setIsSingleVol(boolean enable) {
        bIsSingleVol = enable;
    }

    public void setTradeTime(List<long[]> tradeTimeList, long timeInterval) {
        mTradeTimeList = tradeTimeList;
        mTimeInterval = timeInterval;
    }

    public void loadData(JsonArray jsonArray) {
        int mArrayCount = jsonArray.size();

        if (mArrayCount <= 0)
            return;

        mTimeVals = new ArrayList<>();
        mCurrentVals = new ArrayList<>();
        mRiseRangeVals = new ArrayList<>();
        mAverageVals = new ArrayList<>();
        mVolVals = new ArrayList<>();
        mRiseVals = new ArrayList<>();
        mEmptyVals = new ArrayList<>();

        long tradeTimeAverage = 0;

        if (mTradeTimeList == null)
            return;

        for (int i = 0; i < mTradeTimeList.size(); i++) {
            long[] time = mTradeTimeList.get(i);

            tradeTimeAverage = tradeTimeAverage + (time[1] - time[0]);
        }

        long allCount = tradeTimeAverage / (mTimeInterval);
        long xAverage = mTimeInterval;

        if (bIsNeedSupplement) {
            long supplement = allCount % 4;

            if (supplement == 0) {
                allCount = allCount + 4;
            } else {
                allCount = allCount + (4 - supplement);
            }
        } else {
            allCount = allCount + 1;                //不需要补充余数时，总数量+1(开盘时间的第一条数据)
        }

        for (int i = 0; i < allCount; i++) {
            long time;
            if (i < mArrayCount) {
                time = jsonArray.get(i).getAsJsonArray().get(0).getAsLong();
            } else {
                time = jsonArray.get(mArrayCount - 1).getAsJsonArray().get(0).getAsLong() + xAverage * (i - (mArrayCount - 1));
            }
            mTimeVals.add(mSdf.format(time));
        }

        float last_vol = 0f;
        float last_current = 0f;
        float preclose = Float.parseFloat(mPreclose);
        for (int i = 0; i < mArrayCount; i++) {
            JsonArray tempArray = jsonArray.get(i).getAsJsonArray();

            float current = jsonArray.get(i).getAsJsonArray().get(1).getAsFloat();
            float average = jsonArray.get(i).getAsJsonArray().get(2).getAsFloat();
            float vol = 0f;
            if (tempArray.size() > 3) {
                vol = jsonArray.get(i).getAsJsonArray().get(3).getAsFloat();
            }

            int index = i;

//            if (bIsStartFromBeginning) {
//                index = i;
//            } else {
//                index = i + 1;
//            }

            if (index < allCount) {
                mCurrentVals.add(new Entry(current, index));
                mRiseRangeVals.add(new Entry((current - preclose) / preclose * 100, index));
                mAverageVals.add(new Entry(average, index));
                mRiseVals.add(isRise(current - last_current));
                mVolVals.add(new BarEntry(vol, index));//服务器传过来的成交量是增量，所以需要减去前一个周期值
            }
            last_vol = vol;
            last_current = current;
        }

        for (int i = 0; i < allCount; i++) {
            mEmptyVals.add(new Entry(jsonArray.get(0).getAsJsonArray().get(2).getAsFloat(), i));//i + 1
        }

    }

    public void loadData(List<TChartVo> list) {
        if (null == list)
            return;

        int mArrayCount = list.size();

        if (mArrayCount <= 0)
            return;

        mTimeVals = new ArrayList<>();
        mCurrentVals = new ArrayList<>();
        mRiseRangeVals = new ArrayList<>();
        mAverageVals = new ArrayList<>();
        mVolVals = new ArrayList<>();
        mRiseVals = new ArrayList<>();
        mEmptyVals = new ArrayList<>();

        long tradeTimeAverage = 0;

        if (null == mTradeTimeList)
            return;

        for (int i = 0; i < mTradeTimeList.size(); i++) {
            long[] time = mTradeTimeList.get(i);

            tradeTimeAverage = tradeTimeAverage + (time[1] - time[0]);
        }

        long allCount = tradeTimeAverage / mTimeInterval + 1;
        long xAverage = mTimeInterval;

        for (int i = 0; i < allCount; i++) {
            long time;

            if (i < mArrayCount)
                time = DateUtil.dateToLong(list.get(i).getQuoteTime()).longValue();
             else
                time = DateUtil.dateToLong(list.get(mArrayCount - 1).getQuoteTime()).longValue() + xAverage * (i - (mArrayCount - 1));

            mTimeVals.add(mSdf.format(time));
        }

        float last_vol = 0f;
        float last_current = 0f;
        float preclose = Float.parseFloat(mPreclose);

        for (int i = 0; i < mArrayCount; i++) {
            TChartVo tChartVo = list.get(i);

            if (null != tChartVo) {
                float current = new BigDecimal(tChartVo.getClosePrice()).divide(new BigDecimal(100)).floatValue();
                float average = new BigDecimal(tChartVo.getAveragePrice()).divide(new BigDecimal(100)).floatValue();
                float vol = tChartVo.getTurnVolume();

                if (i < allCount) {
                    mCurrentVals.add(new Entry(current, i));
                    mRiseRangeVals.add(new Entry((new BigDecimal(current).subtract(new BigDecimal(preclose))).
                            divide(new BigDecimal(preclose), 4, BigDecimal.ROUND_UP).multiply(new BigDecimal(100)).floatValue(), i));
                    mAverageVals.add(new Entry(average, i));
                    mRiseVals.add(isRise(new BigDecimal(current).subtract(new BigDecimal(last_current)).floatValue()));
                    mVolVals.add(new BarEntry(vol, i));//服务器传过来的成交量是增量，所以需要减去前一个周期值
                }

                last_vol = vol;
                last_current = current;
            }
        }

        for (int i = 0; i < allCount; i++) {
            mEmptyVals.add(new Entry(new BigDecimal(list.get(0).getAveragePrice()).divide(new BigDecimal(100)).floatValue(), i));//i + 1
        }
    }

    public boolean hasData() {
        if (mCurrentVals != null && mCurrentVals.size() > 0) {
            return true;
        }
        return false;
    }

    public List<String> getXVals() {
        return mTimeVals;
    }

    public List<Entry> getCurrentVals() {
        return mCurrentVals;
    }

    public List<Entry> getRiseRangeVals() {
        return mRiseRangeVals;
    }

    public List<Entry> getAverageVals() {
        return mAverageVals;
    }

    public List<BarEntry> getVolVals() {
        return mVolVals;
    }

    public List<Integer> getRiseVals() {
        return mRiseVals;
    }

    public List<Entry> getEmptyVals() {
        return mEmptyVals;
    }

    public List<Object> compriseData(int position) {
        if (position < mCurrentVals.size() && position < mAverageVals.size()) {
            String timeData = mTimeVals.get(position);
            Entry currentData = mCurrentVals.get(position);
            Entry averageData = mAverageVals.get(position);

            float preclose = Float.parseFloat(mPreclose);

            List<Object> list = new ArrayList<>();
            list.add(timeData);
            list.add(averageData.getVal());
            list.add(currentData.getVal());
            list.add(currentData.getVal() - preclose);
            list.add((new BigDecimal(currentData.getVal()).subtract(new BigDecimal(preclose)))
                    .divide(new BigDecimal(preclose), 4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)).floatValue());

            return list;
        } else {
            return null;
        }
    }

    public int isRise(float riseData) {
        if (riseData == 0f) {
            return STABLE;
        } else if (riseData > 0f) {
            return INCREASING;
        } else if (riseData < 0f) {
            return DECREASING;
        } else {
            return STABLE;
        }
    }
}
