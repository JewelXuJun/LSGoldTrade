package com.datai.common.charts.common;

import android.text.TextUtils;

import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.formatter.YAxisValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.math.BigDecimal;

public class MyValueFormatter implements ValueFormatter, YAxisValueFormatter {

    private int digits;
    private String suffix;

    public MyValueFormatter(int _digits) {
        this(_digits, "");
    }

    public MyValueFormatter(int _digits, String _suffix) {
        this.digits = _digits;
        this.suffix = _suffix;
    }

    @Override
    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
        return floatValue(value, digits);
    }

    @Override
    public String getFormattedValue(float value, YAxis yAxis) {
        return floatValue(value, digits) + suffix;
    }

    /*private String format(float value, int digits) {
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

        value = new BigDecimal(value).setScale(num, BigDecimal.ROUND_DOWN).toString();

        if (num == 0) {
            if (flag)
                return "-" + value;
            else
                return value;
        } else {
            if (Double.parseDouble(value) == 0) {
                return "0.00";
            }

            DecimalFormat format = new DecimalFormat();
            if (Double.parseDouble(value) < 0)
                format.applyPattern("#######.00");
            else if (Double.parseDouble(value) > 0 && Double.parseDouble(value) < 1.0)
                format.applyPattern("0.00");
            else
                format.applyPattern("#######.00");

            if (flag)
                return "-" + format.format(new BigDecimal(value));
            else
                return format.format(new BigDecimal(value));
        }
    }*/

    private String floatValue(float m, int digits) {
       /* String result = "";
        if (m < 10000f)
            result = format(m, digits);
        else if (m >= 10000f && m < 100000000f)
            result = format(m / 10000f, 2) + "万";
        else
            result = format(m / 100000000f, 2) + "亿";*/

//        return result;
        Float f = new Float(m);
        if (f.isNaN()) {
            return "";
        }
        return ScreenValue(String.valueOf(m), digits, false);
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
            result = formatMoney(valueDivisor(valueDecimal, new BigDecimal(10000.0)).toString(), num) + "万";
        else
            result = formatMoney(valueDivisor(valueDecimal, new BigDecimal(100000000.0)).toString(), num) + "亿";

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