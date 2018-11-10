package com.datai.common.charts.kchart;

import android.os.Environment;

import com.datai.common.charts.common.Config;
import com.datai.common.charts.indicator.Indicator;
import com.google.gson.JsonArray;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Yanmin on 2016/6/25.
 */
public class KData {

    public enum Unit {
        TIME("time", "HHmm", "分时", "time"),
        DAY("day", "yyyyMMdd", "日线", "6"),
        WEEK("week", "yyyyMMdd", "周线", "7"),
        MONTH("month", "yyyyMMdd", "月线", "8"),
        MIN1("min", "yyyyMMddHHmm", "1分钟", "1"),
        MIN5("5min", "yyyyMMddHHmm", "5分钟", "2"),
        MIN15("15min", "yyyyMMddHHmm", "15分钟", "3"),
        MIN30("30min", "yyyyMMddHHmm", "30分钟", "4"),
        MIN60("60min", "yyyyMMddHHmm", "60分钟", "5"),
        HOUR2("2h", "yyyyMMddHH", "2小时", "2h"),
        HOUR4("4h", "yyyyMMddHH", "4小时", "4h");

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

    public void loadInitialData(JsonArray jsonArray) {
        mDataList.clear();
        HashMap<String, Object> entry;
        JsonArray elements;
        int j = 0;

        for (int i = 0; i < jsonArray.size(); i++) {
            j = 0;
            elements = jsonArray.get(i).getAsJsonArray();
            entry = new HashMap<>();

            entry.put(Indicator.K_TIME, elements.get(j++).getAsLong());
            entry.put(Indicator.K_OPEN, elements.get(j++).getAsFloat());
            entry.put(Indicator.K_HIGH, elements.get(j++).getAsFloat());
            entry.put(Indicator.K_LOW, elements.get(j++).getAsFloat());
            entry.put(Indicator.K_CLOSE, elements.get(j++).getAsFloat());
            if (bFromServerMAs) {
                entry.put(Config.MARanks[0].KEY(), elements.get(j++).getAsFloat());
                entry.put(Config.MARanks[1].KEY(), elements.get(j++).getAsFloat());
                entry.put(Config.MARanks[2].KEY(), elements.get(j++).getAsFloat());
            }
            if (bHasTradeVolume) {
                entry.put(Indicator.K_VOL, elements.get(j++).getAsFloat());
                entry.put(Indicator.K_VOL_MONEY, elements.get(j++).getAsFloat());
            }

            mDataList.add(entry);
        }
    }

    public void loadMoreData(JsonArray jsonArray) {
        HashMap<String, Object> entry;
        JsonArray elements;
        int j = 0;

        for (int i = 0; i < jsonArray.size(); i++) {
            j = 0;
            elements = jsonArray.get(i).getAsJsonArray();
            entry = new HashMap<>();

            entry.put(Indicator.K_TIME, elements.get(j++).getAsLong());
            entry.put(Indicator.K_OPEN, elements.get(j++).getAsFloat());
            entry.put(Indicator.K_HIGH, elements.get(j++).getAsFloat());
            entry.put(Indicator.K_LOW, elements.get(j++).getAsFloat());
            entry.put(Indicator.K_CLOSE, elements.get(j++).getAsFloat());
            if (bFromServerMAs) {
                entry.put(Config.MARanks[0].KEY(), elements.get(j++).getAsFloat());
                entry.put(Config.MARanks[1].KEY(), elements.get(j++).getAsFloat());
                entry.put(Config.MARanks[2].KEY(), elements.get(j++).getAsFloat());
            }
            if (bHasTradeVolume) {
                entry.put(Indicator.K_VOL, elements.get(j++).getAsFloat());
                entry.put(Indicator.K_VOL_MONEY, elements.get(j++).getAsFloat());
            }

            mDataList.add(i, entry);
        }
    }

    public int loadNewestData(JsonArray jsonArray) {
        if (jsonArray == null || jsonArray.size() == 0) {
            return -1;
        }

        long startTime = jsonArray.get(0).getAsJsonArray().get(0).getAsLong();
        HashMap<String, Object> entry;
        JsonArray elements;
        int j = 0;

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

        for (int i = 0; i < jsonArray.size(); i++) {
            elements = jsonArray.get(i).getAsJsonArray();
            j = 0;

            if (s_index >= mDataList.size()) {
                entry = new HashMap<>();
                mDataList.add(entry);
            } else {
                entry = mDataList.get(s_index);
                entry.clear();
            }

            entry.put(Indicator.K_TIME, elements.get(j++).getAsLong());
            entry.put(Indicator.K_OPEN, elements.get(j++).getAsFloat());
            entry.put(Indicator.K_HIGH, elements.get(j++).getAsFloat());
            entry.put(Indicator.K_LOW, elements.get(j++).getAsFloat());
            entry.put(Indicator.K_CLOSE, elements.get(j++).getAsFloat());
            if (bFromServerMAs) {
                entry.put(Config.MARanks[0].KEY(), elements.get(j++).getAsFloat());
                entry.put(Config.MARanks[1].KEY(), elements.get(j++).getAsFloat());
                entry.put(Config.MARanks[2].KEY(), elements.get(j++).getAsFloat());
            }
            if (bHasTradeVolume) {
                entry.put(Indicator.K_VOL, elements.get(j++).getAsFloat());
                entry.put(Indicator.K_VOL_MONEY, elements.get(j++).getAsFloat());
            }

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
