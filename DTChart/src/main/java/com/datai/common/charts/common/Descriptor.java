package com.datai.common.charts.common;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;

import com.datai.common.charts.fchart.FData;
import com.datai.common.charts.indicator.Indicator;
import com.datai.common.charts.indicator.VOL;
import com.datai.common.charts.kchart.KChartData;
import com.datai.common.charts.kchart.KData;
import com.datai.common.charts.tchart.TChartData;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Monking on 2016/1/21.
 */
public class Descriptor {
    public static final String S_COLON = ":";
    public static final String S_BLANK = "  ";
    public static final String S_COMMA = ",";
    public static final String S_LEFT_BRACKET = "(";
    public static final String S_RIGHT_BRACKET = ")";
    public static final String S_NA = "---";

    public static final String S_TIME = "时间";
    public static final String S_NOW = "现价";
    public static final String S_AVERAGE = "均价";
    public static final String S_POSITIVE = "+";
    public static final String S_PERCENTAGE = "%";

    public static final String S_KCHART_TIME = "时间";
    public static final String S_KCHART_OPEN = "今开";
    public static final String S_KCHART_CLOSE = "收盘";
    public static final String S_KCHART_HIGH = "最高";
    public static final String S_KCHART_LOW = "最低";
    public static final String S_KCHART_RISE_VALUE = "涨跌";
    public static final String S_KCHART_RISE_PERCENT = "涨幅";
    public static final String S_KCHART_COLON = ":";
    public static final String S_KCHART_POSITIVE = "+";
    public static final String S_KCHART_PERCENTAGE = "%";

    public Descriptor() {
    }

    private String[] getKeys() {
        return new String[]{
                S_TIME, S_AVERAGE, S_NOW
        };
    }

    public String getTitle(Indicator indicator) {
        String title = null;
        StringBuffer sb = new StringBuffer();
        sb.append(indicator.getType().toString());
        int[] params = indicator.getParams();
        if (params != null && params.length > 0) {
            sb.append(S_LEFT_BRACKET);
            for (int i = 0; i < params.length; i++) {
                if (i != 0)
                    sb.append(S_COMMA);
                sb.append(params[i]);
            }
            sb.append(S_RIGHT_BRACKET);
        }
        title = sb.toString();
        return title;
    }

    public SpannableString getDetail(Indicator indicator, HashMap<String, Object> entryData, boolean hasTitle, boolean hasVaule) {
        DecimalFormat df = new DecimalFormat("0.00");
        StringBuffer sb = new StringBuffer();

        String title = null;
        if (hasTitle) {
            title = getTitle(indicator);
        }

        String[] strArray = null;
        if (hasVaule) {
            String[] keys = indicator.getKeys();
            strArray = new String[keys.length];

            for (int i = 0; i < keys.length; i++) {
                String keyAlias = indicator.getKeyAlias(keys[i]);
                sb.setLength(0);
                if (!TextUtils.isEmpty(keyAlias)) {
                    sb.append(keyAlias);
                    sb.append(S_COLON);
                }
                Object value = null;
                if (entryData != null) value = entryData.get(keys[i]);
//                Object value = indicator.VALUE(keys[i], index);
                if (value == null || Float.isNaN((float) value))
                    sb.append(S_NA);
                else {
                    //sb.append(df.format((float) value));
                    if (keys[i].equals(VOL.K_VOL)) {
                        sb.append(floatValue((float) value, 0));
                    } else {
                        sb.append(floatValue((float) value, 2));
                    }
                }
                strArray[i] = sb.toString();
            }
        }

        return combine(title, strArray);
    }

    private SpannableString combine(String title, String[] dataStr) {
        int dataNum;
        if (dataStr == null) {
            dataNum = 0;
        } else {
            dataNum = dataStr.length;
        }

        int strNum = 0;
        int[] endIdx = new int[dataNum + 2];
        int[] colors = new int[dataNum + 1];
        StringBuffer sb = new StringBuffer();
        endIdx[0] = 0;

        if (!TextUtils.isEmpty(title)) {
            sb.append(title);
            sb.append(S_BLANK);
            sb.append(S_BLANK);

            endIdx[strNum + 1] = endIdx[strNum] + title.length() + S_BLANK.length() * 2;
            colors[strNum] = Config.LineColorGroup[1];
            strNum++;
        }

        for (int i = 0; i < dataNum; i++) {
            sb.append(dataStr[i]);
            sb.append(S_BLANK);
            endIdx[strNum + 1] = endIdx[strNum] + dataStr[i].length() + S_BLANK.length();
            colors[strNum] = Config.LineColorGroup[i];
            strNum++;
        }

        SpannableString ss = new SpannableString(sb.toString());
        for (int i = 0; i < strNum; i++) {
            int color = colors[i];
            ss.setSpan(new ForegroundColorSpan(color), endIdx[i], endIdx[i + 1], Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return ss;
    }

    public SpannableString getTchartDetail(List<Object> list, int type, int digits) {
        DecimalFormat df = new DecimalFormat("0.00");
        StringBuffer sb = new StringBuffer();

        String[] keys = getKeys();

        if (list != null && list.size() != 0) {
            String[] strArray = new String[keys.length];

            for (int i = 0; i < keys.length; i++) {
                String key = keys[i];
                sb.setLength(0);
                sb.append(key);
                sb.append(S_COLON);

                if (i == 0) {
                    sb.append(list.get(0));
                } else if (i == keys.length - 1) {
                    sb.append(format((float) list.get(i), digits));//df.format(list.get(i))
                    sb.append(S_COMMA);
                    sb.append(S_BLANK);

                    String value = format((float) list.get(i + 1), digits);//df.format(list.get(i + 1));

                    if (type == TChartData.INCREASING && Float.parseFloat(value) > 0f) {
                        sb.append(S_POSITIVE);
                    }

                    sb.append(value);
                    sb.append(S_COMMA);
                    sb.append(S_BLANK);

                    if (type == TChartData.INCREASING && Float.parseFloat(value) > 0f) {
                        sb.append(S_POSITIVE);
                    }

                    sb.append(format((float) list.get(i + 2), 2));//df.format(list.get(i + 2))
                    sb.append(S_PERCENTAGE);
                } else {
                    sb.append(df.format(list.get(i)));
                }

                strArray[i] = sb.toString();
            }

            return combineTchart(strArray, type);
        }

        return null;
    }

    private SpannableString combineTchart(String[] dataStr, int type) {
        int dataNum;
        if (dataStr == null) {
            dataNum = 0;
        } else {
            dataNum = dataStr.length;
        }

        int strNum = 0;
        int[] endIdx = new int[dataNum + 2];
        int[] colors = new int[dataNum];
        StringBuffer sb = new StringBuffer();
        endIdx[0] = 0;

        for (int i = 0; i < dataNum; i++) {
            sb.append(dataStr[i]);
            sb.append(S_BLANK);
            endIdx[strNum + 1] = endIdx[strNum] + dataStr[i].length() + S_BLANK.length();

            if (i == 0) {
                colors[strNum] = Config.Current_Color;
            } else if (i == 1) {
                colors[strNum] = Config.Average_Color;
            } else {
                colors[strNum] = getTChartTextColor(type);
            }

            strNum++;
        }

        SpannableString ss = new SpannableString(sb.toString());
        for (int i = 0; i < strNum; i++) {
            int color = colors[i];
            ss.setSpan(new ForegroundColorSpan(color), endIdx[i], endIdx[i + 1], Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return ss;
    }

    public StringBuffer setTime(long time, KData.Unit unit) {
        StringBuffer sb = new StringBuffer();
        sb.append(Descriptor.S_KCHART_TIME);
        String formatStr = "HH:mm";
        if (unit == KData.Unit.DAY
                || unit == KData.Unit.WEEK
                || unit == KData.Unit.MONTH) {
            return sb;
        }

        SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
        sb.append(Descriptor.S_KCHART_COLON);
        sb.append(sdf.format(time));

        return sb;
    }

    public StringBuffer setDate(long time, KData.Unit unit) {
        StringBuffer sb = new StringBuffer();
        String formatStr;
        if (unit == KData.Unit.MONTH) {
            formatStr = "yyyy-MM";
        } else {
            formatStr = "yyyy-MM-dd";
        }

        SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
//        sb.append(Descriptor.S_KCHART_TIME);
//        sb.append(Descriptor.S_KCHART_COLON);
        sb.append(sdf.format(time));

        return sb;
    }

    public SpannableString setOpen(float open, int type) {
        StringBuffer sb = new StringBuffer();
        sb.append(Descriptor.S_KCHART_OPEN);
        sb.append(Descriptor.S_KCHART_COLON);
        sb.append(open);

        SpannableString ss = new SpannableString(sb.toString());
        ss.setSpan(new ForegroundColorSpan(getKChartTextColor(type)), 0, sb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return ss;
    }

    public SpannableString setClose(float close, int type) {
        StringBuffer sb = new StringBuffer();
        sb.append(Descriptor.S_KCHART_CLOSE);
        sb.append(Descriptor.S_KCHART_COLON);
        sb.append(close);

        SpannableString ss = new SpannableString(sb.toString());
        ss.setSpan(new ForegroundColorSpan(getKChartTextColor(type)), 0, sb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return ss;
    }

    public SpannableString setHigh(float high, int type) {
        StringBuffer sb = new StringBuffer();
        sb.append(Descriptor.S_KCHART_HIGH);
        sb.append(Descriptor.S_KCHART_COLON);
        sb.append(high);

        SpannableString ss = new SpannableString(sb.toString());
        ss.setSpan(new ForegroundColorSpan(getKChartTextColor(type)), 0, sb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return ss;
    }

    public SpannableString setLow(float low, int type) {
        StringBuffer sb = new StringBuffer();
        sb.append(Descriptor.S_KCHART_LOW);
        sb.append(Descriptor.S_KCHART_COLON);
        sb.append(low);

        SpannableString ss = new SpannableString(sb.toString());
        ss.setSpan(new ForegroundColorSpan(getKChartTextColor(type)), 0, sb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return ss;
    }

    public SpannableString setRiseValue(float riseValue) {
        DecimalFormat df = new DecimalFormat("0.00");

        int color;

        StringBuffer sb = new StringBuffer();
        sb.append(Descriptor.S_KCHART_RISE_VALUE);
        sb.append(Descriptor.S_KCHART_COLON);

        if (riseValue > 0f) {
            sb.append(Descriptor.S_KCHART_POSITIVE);
            color = Config.Increasing_Color;
        } else if (riseValue == 0f) {
            color = Config.Default_KChart_Text_Color;
        } else {
            color = Config.Decreasing_Color;
        }

        sb.append(df.format(riseValue));

        SpannableString ss = new SpannableString(sb.toString());
        ss.setSpan(new ForegroundColorSpan(color), 0, sb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return ss;
    }

    public SpannableString setRisePercent(float risePercent) {
        DecimalFormat df = new DecimalFormat("0.00");

        StringBuffer sb = new StringBuffer();
        sb.append(Descriptor.S_KCHART_RISE_PERCENT);
        sb.append(Descriptor.S_KCHART_COLON);

        int color;

        if (risePercent > 0f) {
            sb.append(Descriptor.S_KCHART_POSITIVE);
            color = Config.Increasing_Color;
        } else if (risePercent == 0f) {
            color = Config.Default_KChart_Text_Color;
        } else {
            color = Config.Decreasing_Color;
        }

        sb.append(df.format(risePercent));
        sb.append(Descriptor.S_KCHART_PERCENTAGE);

        SpannableString ss = new SpannableString(sb.toString());
        ss.setSpan(new ForegroundColorSpan(color), 0, sb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return ss;
    }

    public String getTime(Long time) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");

        return format.format(time);
    }

    public SpannableString setPrice(String price, int type) {
        StringBuffer sb = new StringBuffer();
        sb.append(formatNumberValue(price));

        SpannableString ss = new SpannableString(sb.toString());
        ss.setSpan(new ForegroundColorSpan(getFChartTextColor(type)), 0, sb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return ss;
    }

    private String format(float value, int digits) {
        return formatNumber(String.valueOf(value), digits);
    }

    private String formatNumber(String value, int num) {
        boolean flag = false;

        if (TextUtils.isEmpty(value))
            return "";

        if (value.contains("-")) {
            value = value.substring(1, value.length());
            flag = true;
        }

        value = new BigDecimal(value).setScale(num, BigDecimal.ROUND_HALF_UP).toString();

        if (flag)
            return "-" + value;
        else
            return value;
    }

    public String floatValue(float m, int digits) {
        return ScreenValue(String.valueOf(m), digits, false);
    }

    public String floatValue(String m, int digits) {
        return floatValue(Float.valueOf(m), digits);
    }

    public int getFChartTypeColor(String type) {
        int color = Config.Default_Color;

        switch (type) {
            case FData.TICK_SELL:
                color = Config.Decreasing_Color;

                break;
            case FData.TICK_BUY:
                color = Config.Increasing_Color;

                break;
        }

        return color;
    }

    private int getKChartTextColor(int type) {
        int color = Config.Default_KChart_Text_Color;

        switch (type) {
            case KChartData.STABLE:
                color = Config.Default_KChart_Text_Color;
                break;

            case KChartData.INCREASING:
                color = Config.Increasing_Color;
                break;

            case KChartData.DECREASING:
                color = Config.Decreasing_Color;
                break;
        }

        return color;
    }

    private int getTChartTextColor(int type) {
        int color = Config.Default_KChart_Text_Color;

        switch (type) {
            case TChartData.STABLE:
                color = Config.Default_Color;
                break;

            case TChartData.INCREASING:
                color = Config.Increasing_Color;
                break;

            case TChartData.DECREASING:
                color = Config.Decreasing_Color;
                break;
        }

        return color;
    }

    private static String formatNumberValue(String value) {
        if (Double.parseDouble(value) == 0) {
            return "0.00";
        }

        DecimalFormat format = new DecimalFormat();
        format.applyPattern("0.00");

        return format.format(new BigDecimal(value));
    }

    private int getFChartTextColor(int type) {
        int color = Config.Default_Color;

        switch (type) {
            case FData.STABLE:
                color = Config.Default_Color;
                break;

            case FData.INCREASING:
                color = Config.Increasing_Color;
                break;

            case FData.DECREASING:
                color = Config.Decreasing_Color;
                break;
        }

        return color;
    }

    private String ScreenValue(String value, int num, boolean currency) {
        String result;
        String m;
        BigDecimal valueDecimal;

        if (value.contains("E")) {
            if (value.contains("-")) {
                m = new BigDecimal(value).negate().toPlainString();
            } else {
                m = new BigDecimal(value).toPlainString();
            }
        } else {
            m = new BigDecimal(value).toString();
        }

        valueDecimal = new BigDecimal(m);

        if (valueCompare(valueDecimal.abs(), new BigDecimal(10000.0)) == -1) {
            result = formatMoney(m, num);
        } else if ((valueCompare(valueDecimal.abs(), new BigDecimal(10000.0)) == 1
                || valueCompare(valueDecimal.abs(), new BigDecimal(10000.0)) == 0)
                && valueCompare(valueDecimal.abs(), new BigDecimal(100000000.0)) == -1)
            result = formatMoney(valueDivisor(valueDecimal, new BigDecimal(10000.0)).toString(), 2) + "万";
        else
            result = formatMoney(valueDivisor(valueDecimal, new BigDecimal(100000000.0)).toString(), 2) + "亿";

        if (currency)
            return "￥" + result;
        else
            return result;

    }

    private int valueCompare(BigDecimal price, BigDecimal availablePrice) {
        return price.compareTo(availablePrice);
    }

    private BigDecimal valueDivisor(BigDecimal original, BigDecimal divisor) {
        if (valueCompare(divisor, new BigDecimal("0")) == 0)
            return new BigDecimal("0");
        else
            return new BigDecimal(original.toString()).divide(divisor);

    }

    private String formatMoney(String money, int num) {
        boolean flag = false;

        if (TextUtils.isEmpty(money))
            return "";

        if (money.contains("-")) {
            money = money.substring(1, money.length());
            flag = true;
        }

        money = formatValue(money, num);

        if (new BigDecimal(money).compareTo(new BigDecimal("0")) == 0)
            return money;

        if (flag)
            return "-" + money;
        else
            return money;
    }


    private String formatValue(String value, int num) {
        String result;

        if (value.contains(".")) {
            int index = value.indexOf(".");
            String elseValue = value.substring(index + 1, value.length());
            int elseLength = elseValue.length();

            if (num == 0) {
                result = value.substring(0, index);
            } else {
                if (elseLength == num) {
                    result = value;
                } else if (elseLength > num) {
                    result = value.substring(0, value.length() - (elseLength - num));
                } else {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(value);

                    for (int i = 0; i < (num - elseLength); i++) {
                        stringBuilder.append("0");
                    }

                    result = stringBuilder.toString();
                }
            }
        } else {
            if (num == 0) {
                result = value;
            } else {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(value).append(".");

                for (int i = 0; i < num; i++) {
                    stringBuilder.append("0");
                }

                result = stringBuilder.toString();
            }
        }

        return result;
    }

}