package com.jme.lsgoldtrade.util;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;

import com.jme.lsgoldtrade.R;

import java.math.BigDecimal;

public class MarketUtil {

    public static String getContractNameCN(String contractId) {
        String contractName;

        switch (contractId) {
            case "白银":
                contractName = "白银延期";

                break;
            case "au9999":
                contractName = "黄金延期";

                break;
            case "迷你黄金":
                contractName = "迷你黄金延期";

                break;
            default:
                contractName = "";

                break;
        }

        return contractName;
    }

    public static String getContractNameEN(String contractId) {
        String contractName;

        switch (contractId) {
            case "白银":
                contractName = "Ag(T+D)";

                break;
            case "au9999":
                contractName = "Au(T+D)";

                break;
            case "迷你黄金":
                contractName = "mAu(T+D)";

                break;
            default:
                contractName = "";

                break;
        }

        return contractName;
    }

    public static int getMarketStateColor(Context context, int type) {
        int color;

        switch (type) {
            case -1:
                color = ContextCompat.getColor(context, R.color.common_font_decrease);

                break;
            case 0:
                color = ContextCompat.getColor(context, R.color.color_text_black);

                break;
            case 1:
                color = ContextCompat.getColor(context, R.color.common_font_increase);

                break;
            default:
                color = ContextCompat.getColor(context, R.color.color_text_black);

                break;
        }

        return color;
    }

    public static String getMarketRateValue(Context context, int type, long rate) {
        String value;

        switch (type) {
            case -1:
                value = String.format(context.getResources().getString(R.string.text_rate_down), rate);

                break;
            case 0:
                value = "0.00%";

                break;
            case 1:
                value = String.format(context.getResources().getString(R.string.text_rate_up), rate);

                break;
            default:
                value = "";

                break;
        }

        return value;
    }

    public static String getVolumeValue(String value, boolean currency) {
        String result;
        String valueStr;
        BigDecimal valueDecimal;

        if (value.contains("E")) {
            if (value.contains("-"))
                valueStr = new BigDecimal(value).negate().toPlainString();
             else
                valueStr = new BigDecimal(value).toPlainString();
        } else {
            valueStr = new BigDecimal(value).toString();
        }

        valueDecimal = new BigDecimal(valueStr);

        if (valueCompare(valueDecimal.abs(), new BigDecimal(10000.0)) == -1) {
            result = formatValueNum(valueStr, 2);
        } else if ((valueCompare(valueDecimal.abs(), new BigDecimal(10000.0)) == 1
                || valueCompare(valueDecimal.abs(), new BigDecimal(10000.0)) == 0)
                && valueCompare(valueDecimal.abs(), new BigDecimal(100000000.0)) == -1)
            result = formatValueNum(valueDivisor(valueDecimal, new BigDecimal(10000.0)).toString(), 2) + "万";
        else
            result = formatValueNum(valueDivisor(valueDecimal, new BigDecimal(100000000.0)).toString(), 2) + "亿";

        if (currency)
            return "￥" + result;
        else
            return result;
    }

    public static int valueCompare(BigDecimal price, BigDecimal availablePrice) {
        return price.compareTo(availablePrice);
    }

    public static String formatValueNum(String value, int num) {
        boolean flag = false;

        if (TextUtils.isEmpty(value))
            return "";

        if (value.contains("E")) {
            if (value.contains("-")) {
                value = new BigDecimal(value).negate().toPlainString();

                if (value.contains("-")) {
                    value = value.substring(1, value.length());
                    flag = true;
                }
            } else {
                value = new BigDecimal(value).toPlainString();
            }
        } else {
            if (value.contains("-")) {
                value = value.substring(1, value.length());
                flag = true;
            }
        }

        value = formatValue(value, num);

        if (new BigDecimal(value).compareTo(new BigDecimal("0")) == 0)
            return value;

        if (flag)
            return "-" + value;
        else
            return value;
    }

    public static String formatValue(String value, int num) {
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

    public static BigDecimal valueDivisor(BigDecimal original, BigDecimal divisor) {
        if (valueCompare(divisor, new BigDecimal("0")) == 0)
            return new BigDecimal("0");
        else
            return new BigDecimal(original.toString()).divide(divisor);

    }

}
