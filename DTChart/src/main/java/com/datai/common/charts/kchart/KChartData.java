package com.datai.common.charts.kchart;

import android.os.Environment;

import com.datai.common.charts.common.Config;
import com.datai.common.charts.indicator.BIAS;
import com.datai.common.charts.indicator.BOLL;
import com.datai.common.charts.indicator.Indicator;
import com.datai.common.charts.indicator.KDJ;
import com.datai.common.charts.indicator.MA;
import com.datai.common.charts.indicator.MACD;
import com.datai.common.charts.indicator.RISE;
import com.datai.common.charts.indicator.RSI;
import com.datai.common.charts.indicator.VOL;
import com.datai.common.charts.indicator.WR;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.Entry;
import com.jme.common.util.KChartVo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Monking on 2016/1/15.
 */
public class KChartData {
    private final SimpleDateFormat dfmm = new SimpleDateFormat("yyyy-MM-dd HH:mm");//设置时间格式
    private final SimpleDateFormat dfdd = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
    private final SimpleDateFormat dfyy = new SimpleDateFormat("yyyy-MM");//设置日期格式

    public static final int STABLE = 0;
    public static final int INCREASING = 1;
    public static final int DECREASING = 2;

    private KData mKData;
    private MA.Rank mMARanks[];
    private List<Indicator> mIndicatorList = new ArrayList<Indicator>();
    private ArrayList<String> mXVals;

    public KChartData() {
        mIndicatorList.add(new RISE());
        mKData = new KData("");
    }

    public MA.Rank[] getMARanks() {
        return mMARanks;
    }

    public Indicator setMARanks(MA.Rank[] ranks) {
        if (ranks == null || ranks.length <= 0)
            return null;

        removeIndicator(Indicator.Type.MA);

        this.mMARanks = ranks;
        Indicator indicatorMA = new MA(mMARanks);
        mIndicatorList.add(indicatorMA);

        return indicatorMA;
    }

    public Indicator setBOLLRanks(MA.Rank[] ranks) {
        if (ranks == null || ranks.length <= 0)
            return null;

        removeIndicator(Indicator.Type.MA);

        this.mMARanks = ranks;
        Indicator indicatorMA = new MA(mMARanks);
        mIndicatorList.add(indicatorMA);

        return indicatorMA;
    }

    public void setUnit(KData.Unit unit) {
        mKData.setUnit(unit);
    }

    public KData.Unit getUnit() {
        return mKData.getUnit();
    }

    public void setIsFromServerMAs(boolean enable) {
        mKData.setIsFromServerMAs(enable);
    }

    public void setHasTradeVolume(boolean enable) {
        mKData.setHasTradeVolume(enable);
    }

    public boolean hasKDataInCurrentUnit() {
        KData.Unit unit = mKData.getUnit();
        return mKData.hasKDataByUnit(unit);
    }

    public void setIndicator(Indicator.Type[] types) {
        if (types == null || types.length <= 0)
            return;

        for (int i = 0; i < types.length; i++) {
            setIndicator(types[i]);
        }
    }

    public void removeIndicator(Indicator.Type type) {
        for (Indicator indicator : mIndicatorList) {
            if (indicator.getType() == type) {
                mIndicatorList.remove(indicator);
                break;
            }
        }
    }

    public void setIndicator(Indicator.Type type) {
        removeIndicator(type);

        if (type == Indicator.Type.MACD) {
            mIndicatorList.add(new MACD());
        } else if (type == Indicator.Type.KDJ) {
            mIndicatorList.add(new KDJ());
        } else if (type == Indicator.Type.BIAS) {
            mIndicatorList.add(new BIAS());
        } else if (type == Indicator.Type.RSI) {
            mIndicatorList.add(new RSI());
        } else if (type == Indicator.Type.WR) {
            mIndicatorList.add(new WR());
        } else if (type == Indicator.Type.BOLL) {
            mIndicatorList.add(new BOLL());
        } else if (type == Indicator.Type.VOL) {
            mIndicatorList.add(new VOL());
        }
    }

    public HashMap<String, Object> getEntryData(int index) {
        List list = mKData.getDataList();
        if (list.size() <= 0 || index >= list.size())
            return null;
        return mKData.getDataList().get(index);
    }

    public long getNewestTimeTick(int offset) {
        return mKData.getNewestTimeTick(offset);
    }

    public long getOldestTimeTick() {
        return mKData.getOldestTimeTick();
    }

    private List<HashMap<String, Object>> loadDataFromFile(String path) {

        File sdcard = Environment.getExternalStorageDirectory();

        // Get the text file
        File file = new File(sdcard, path);

        List<HashMap<String, Object>> entries = new ArrayList<HashMap<String, Object>>();

        try {
            @SuppressWarnings("resource")
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            int i = 0;
            while ((line = br.readLine()) != null) {
                String[] split = line.split("#");

                if (split.length > 4) {
                    HashMap<String, Object> entry = new HashMap<String, Object>();
                    entry.put(Indicator.K_TIME, Long.parseLong(split[0]));
                    entry.put(Indicator.K_OPEN, Float.parseFloat(split[1]));
                    entry.put(Indicator.K_HIGH, Float.parseFloat(split[2]));
                    entry.put(Indicator.K_LOW, Float.parseFloat(split[3]));
                    entry.put(Indicator.K_CLOSE, Float.parseFloat(split[4]));

//                    if (split.length > 7) {
//                        entry.put(MA.K_MA + 5, Float.parseFloat(split[5]));
//                        entry.put(MA.K_MA + 10, Float.parseFloat(split[6]));
//                        entry.put(MA.K_MA + 20, Float.parseFloat(split[7]));
////                        exData.setMa5(Float.parseFloat(split[5]));
////                        exData.setMa10(Float.parseFloat(split[6]));
////                        exData.setMa20(Float.parseFloat(split[7]));
//                    }
                    entries.add(entry);
                }
            }
        } catch (IOException e) {
            //Log.e(LOG, e.toString());
        }

        return entries;
    }

    public void switchKDataToCurrentUnit() {
        KData.Unit unit = mKData.getUnit();
        mKData.switchKData(unit);
        prepareCandleEntry(0);
    }

    public void loadInitialData(List<KChartVo> list) {
        mKData.loadInitialData(list);
        prepareCandleEntry(0);
        calculate(0);
    }

    public void loadMoreData(List<KChartVo> list) {
        mKData.loadMoreData(list);
        prepareCandleEntry(0);
        calculate(0);
    }

    public void loadNewestData(List<KChartVo> list) {
//        int oriCount = mKData.getDataCount();
        int startIndex = mKData.loadNewestData(list);
//        int newCount = mKData.getDataCount();
        prepareCandleEntry(startIndex);
        calculate(startIndex);
    }

    private void prepareCandleEntry(int startIndex) {
        if (startIndex <= 0)
            startIndex = 0;

        List<HashMap<String, Object>> dataList = mKData.getDataList();
        SimpleDateFormat df;
        KData.Unit unit = mKData.getUnit();
        if (unit == KData.Unit.DAY
                || unit == KData.Unit.WEEK) {
            df = dfdd;
        } else if (unit == KData.Unit.MONTH) {
            df = dfyy;
        } else {
            df = dfmm;
        }

        mXVals = new ArrayList<String>();
        for (int i = 0; i < getEntryCount(); i++) {
            long time = (long) dataList.get(i).get(Indicator.K_TIME);
            mXVals.add(df.format(new Date(time)));
        }
//        calculate(startIndex);
    }

    public int getEntryCount() {
        return mKData.getDataCount();
    }

    public KData.Unit getEntryDataUnit() {
        return mKData.getDataUnit();
    }

    public void clearData() {
        mKData.clearData();
    }

    private ArrayList<Integer> mColorList = new ArrayList<Integer>();

    public List<CandleEntry> getCandleEntry() {
        List<CandleEntry> candleEntries = new ArrayList<CandleEntry>();
        mColorList.clear();

        float lastClose = -1f;
        int lastColor = Config.Candle_Shadow_Color;

        for (int index = 0; index < getEntryCount(); index++) {

            HashMap<String, Object> entry = mKData.getDataList().get(index);
            float high = (float) entry.get(Indicator.K_HIGH);
            float low = (float) entry.get(Indicator.K_LOW);
            float open = (float) entry.get(Indicator.K_OPEN);
            float close = (float) entry.get(Indicator.K_CLOSE);
            CandleEntry candle = new CandleEntry(index, high, low,
                    open, close);
            candleEntries.add(candle);

            //Set Color:
            int color;
            if (open < close) {
                color = Config.Increasing_Color;
            } else if (open > close) {
                color = Config.Decreasing_Color;
            } else {
                if (close > lastClose)
                    color = Config.Increasing_Color;
                else if (close < lastClose)
                    color = Config.Decreasing_Color;
                else {
                    color = lastColor;
                }
            }
            mColorList.add(color);
            lastColor = color;
            lastClose = close;
        }

        return candleEntries;
    }

    public ArrayList<Integer> getColorList() {
        return mColorList;
    }

    public ArrayList<String> getXVals() {
        return mXVals;
    }

    public Indicator getIndicator(Indicator.Type type) {
        for (Indicator indicator : mIndicatorList) {
            if (type == indicator.getType())
                return indicator;
        }
        return null;
    }

    public List<ArrayList> getEntryList(Indicator indicator) {
        String[] keys = indicator.getKeys();
        if (keys == null || keys.length == 0)
            return null;

        List<ArrayList> entryList = new ArrayList<ArrayList>();

        for (int j = 0; j < keys.length; j++) {
            if (keys[j].equals(MACD.K_STICK)
                    || keys[j].equals(MACD.K_VOL)) {
                entryList.add(new ArrayList<BarEntry>());
            } else {
                entryList.add(new ArrayList<Entry>());
            }
        }

        for (int index = 0; index < getEntryCount(); index++) {
            HashMap<String, Object> entry = mKData.getDataList().get(index);
            int i;
            for (i = 0; i < keys.length; i++) {
                Object o = entry.get(keys[i]);
                if (o != null) {
                    if (keys[i].equals(MACD.K_STICK)
                            || keys[i].equals(MACD.K_VOL)) {
                        ArrayList<BarEntry> entrylist_e = entryList.get(i);
                        entrylist_e.add(new BarEntry((float) o, index));
                    } else {
                        ArrayList<Entry> entrylist_e = entryList.get(i);
                        entrylist_e.add(new Entry((float) o, index));
                    }
                }
            }
        }

        return entryList;
    }

    private void calculate(int startIndex) {
        if (startIndex <= 0)
            startIndex = 0;

        for (Indicator indicator : mIndicatorList) {
            indicator.setData(mKData.getDataList());
        }

        for (int i = startIndex; i < mKData.getDataCount(); i++) {
            for (Indicator indicator : mIndicatorList) {
                if (indicator.getType() == Indicator.Type.MA && mKData.getIsFromServerMAs())
                    continue;

                indicator.calc(i);
            }
        }
    }

    public int getType(int index, float data, float prev) {
        if (index == 0) {
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
}
