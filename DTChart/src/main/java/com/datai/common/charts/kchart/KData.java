package com.datai.common.charts.kchart;

import android.os.Environment;

import com.datai.common.charts.indicator.Indicator;
import com.jme.common.util.DateUtil;
import com.jme.common.util.KChartVo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Yanmin on 2016/6/25.
 */
public class KData {

    public enum Unit {
        TIME("time", "HHmm", "分时", "time1"),
        DAY("day", "yyyyMMdd", "日线", "dayK"),
        WEEK("week", "yyyyMMdd", "周线", "weekK"),
        MONTH("month", "yyyyMMdd", "月线", "monthK"),
        MIN1("min", "yyyyMMddHHmm", "1分钟", "1minK"),
        MIN5("5min", "yyyyMMddHHmm", "5分钟", "5minK"),
        MIN15("15min", "yyyyMMddHHmm", "15分钟", "15minK"),
        MIN30("30min", "yyyyMMddHHmm", "30分钟", "30minK"),
        MIN60("60min", "yyyyMMddHHmm", "60分钟", "60minK");

        private String describe;
        private String format;
        private String ch;
        private String code;

        Unit(String _desc, String _format, String _ch, String _code) {
            describe = _desc;
            format = _format;
            ch = _ch;
            code = _code;
        }

        public String toString() {
            return describe;
        }

        public String getFormat() {
            return format;
        }

        public String getCHDescribe() {
            return ch;
        }

        public String getCode() {
            return code;
        }
    }

    private String mCommodity;
    private boolean bFromServerMAs = false;
    private boolean bHasTradeVolume = true;
    private Unit mUnit;
    private HashMap<Unit, List<HashMap<String, Object>>> mDataUnitGroup = new HashMap<Unit, List<HashMap<String, Object>>>();
    private List<HashMap<String, Object>> mDataList = new ArrayList<>();
    private Unit mDataUnit;

    public KData(String commodity) {
        mCommodity = commodity;
    }

    public void setUnit(Unit unit) {
        mUnit = unit;
    }

    public Unit getUnit() {
        return mUnit;
    }

    public void setIsFromServerMAs(boolean enable) {
        bFromServerMAs = enable;
    }

    public void setHasTradeVolume(boolean enable) {
        bHasTradeVolume = enable;
    }

    public boolean getIsFromServerMAs() {
        return bFromServerMAs;
    }

    public boolean hasKDataByUnit(Unit unit) {
        List<HashMap<String, Object>> dataList = mDataUnitGroup.get(unit);
        if (dataList == null || dataList.size() == 0) {
            return false;
        }
        return true;
    }

    public void switchKData(Unit unit) {
        List<HashMap<String, Object>> dataList = mDataUnitGroup.get(unit);
        if (dataList == null) {
            dataList = new ArrayList<>();
            mDataUnitGroup.put(unit, dataList);
        }

        mDataList = dataList;
        mDataUnit = unit;
    }

    public void loadInitialData(List<KChartVo> list) {
        mDataList.clear();
        HashMap<String, Object> entry;

        int size = list.size();

        for (int i = 0; i < size; i++) {
            KChartVo kChartVo = list.get(size -1 -i);

            if (null != kChartVo) {
                entry = new HashMap<>();

                entry.put(Indicator.K_TIME, DateUtil.dateToLong(kChartVo.getQuoteTime(), "yyyy-MM-dd HH:mm:ss").longValue());
                entry.put(Indicator.K_OPEN, new BigDecimal(kChartVo.getOpenPrice()).divide(new BigDecimal(100)).floatValue());
                entry.put(Indicator.K_HIGH, new BigDecimal(kChartVo.getHighestPrice()).divide(new BigDecimal(100)).floatValue());
                entry.put(Indicator.K_LOW, new BigDecimal(kChartVo.getLowestPrice()).divide(new BigDecimal(100)).floatValue());
                entry.put(Indicator.K_CLOSE, new BigDecimal(kChartVo.getClosePrice()).divide(new BigDecimal(100)).floatValue());
                entry.put(Indicator.K_VOL, Float.parseFloat(String.valueOf(kChartVo.getTurnVolumn())));

                mDataList.add(entry);
            }
        }
    }

    public void loadMoreData(List<KChartVo> list) {
        HashMap<String, Object> entry;

        int size = list.size();

        for (int i = 0; i < list.size(); i++) {
            KChartVo kChartVo = list.get(size - 1 - i);

            if (null != kChartVo) {
                entry = new HashMap<>();

                entry.put(Indicator.K_TIME, DateUtil.dateToLong(kChartVo.getQuoteTime(), "yyyy-MM-dd HH:mm:ss").longValue());
                entry.put(Indicator.K_OPEN, new BigDecimal(kChartVo.getOpenPrice()).divide(new BigDecimal(100)).floatValue());
                entry.put(Indicator.K_HIGH, new BigDecimal(kChartVo.getHighestPrice()).divide(new BigDecimal(100)).floatValue());
                entry.put(Indicator.K_LOW, new BigDecimal(kChartVo.getLowestPrice()).divide(new BigDecimal(100)).floatValue());
                entry.put(Indicator.K_CLOSE, new BigDecimal(kChartVo.getClosePrice()).divide(new BigDecimal(100)).floatValue());
                entry.put(Indicator.K_VOL, Float.parseFloat(String.valueOf(kChartVo.getTurnVolumn())));

                mDataList.add(i, entry);
            }
        }
    }

    public int loadNewestData(List<KChartVo> list) {
        if (list == null || list.size() == 0) {
            return -1;
        }

        KChartVo startKChartVo = list.get(0);

        if (null == startKChartVo)
            return -1;

        long startTime = DateUtil.dateToLong(startKChartVo.getQuoteTime(), "yyyy-MM-dd HH:mm:ss").longValue();
        HashMap<String, Object> entry;
        int startIndex = (mDataList == null) ? 0 : mDataList.size() - 1;

        while (startIndex >= 0) {
            long time = (long) mDataList.get(startIndex).get(Indicator.K_TIME);
            if (time < startTime) {
                startIndex++;
                break;
            } else if (time == startTime) {
                break;
            }
            startIndex--;
        }

        int s_index = startIndex;

        for (int i = 0; i < list.size(); i++) {
            KChartVo kChartVo = list.get(i);

            if (s_index >= mDataList.size()) {
                entry = new HashMap<>();
                mDataList.add(entry);
            } else {
                entry = mDataList.get(s_index);
                entry.clear();
            }

            entry.put(Indicator.K_TIME, DateUtil.dateToLong(kChartVo.getQuoteTime(), "yyyy-MM-dd HH:mm:ss").longValue());
            entry.put(Indicator.K_OPEN, new BigDecimal(kChartVo.getOpenPrice()).divide(new BigDecimal(100)).floatValue());
            entry.put(Indicator.K_HIGH, new BigDecimal(kChartVo.getHighestPrice()).divide(new BigDecimal(100)).floatValue());
            entry.put(Indicator.K_LOW, new BigDecimal(kChartVo.getLowestPrice()).divide(new BigDecimal(100)).floatValue());
            entry.put(Indicator.K_CLOSE, new BigDecimal(kChartVo.getClosePrice()).divide(new BigDecimal(100)).floatValue());
            entry.put(Indicator.K_VOL, Float.parseFloat(String.valueOf(kChartVo.getTurnVolumn())));

            s_index++;
        }

        return startIndex;
    }

    public void loadHistoryData() {
        //String filename = mCommodity + "_" + mUnit.toString();
        mDataList = loadDataFromFile("min_1452232501062.txt");
    }

    private List<HashMap<String, Object>> loadDataFromFile(String path) {

        File sdcard = Environment.getExternalStorageDirectory();

        // Get the text file
        File file = new File(sdcard, path);

        List<HashMap<String, Object>> entries = new ArrayList<>();

        try {
            @SuppressWarnings("resource")
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                String[] split = line.split("#");

                if (split.length > 4) {
                    HashMap<String, Object> entry = new HashMap<>();
                    entry.put(Indicator.K_TIME, Long.parseLong(split[0]));
                    entry.put(Indicator.K_OPEN, Float.parseFloat(split[1]));
                    entry.put(Indicator.K_HIGH, Float.parseFloat(split[2]));
                    entry.put(Indicator.K_LOW, Float.parseFloat(split[3]));
                    entry.put(Indicator.K_CLOSE, Float.parseFloat(split[4]));
                    entries.add(entry);
                }
            }
        } catch (IOException e) {
            //Log.e(LOG, e.toString());
        }

        return entries;
    }

    public List<HashMap<String, Object>> getDataList() {
        return mDataList;
    }

    public Unit getDataUnit() {
        return mDataUnit;
    }

    public int getDataCount() {
        return mDataList.size();
    }

    public long getNewestTimeTick(int offset) {
        if (mDataList.size() - 1 - offset < 0)
            return 0;
        return (long) mDataList.get(mDataList.size() - 1 - offset).get(Indicator.K_TIME);
    }

    public long getOldestTimeTick() {
        if (mDataList.size() == 0)
            return 0;
        return (long) mDataList.get(0).get(Indicator.K_TIME);
    }

}
