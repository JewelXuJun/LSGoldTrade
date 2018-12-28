package com.jme.lsgoldtrade.util;

import android.text.TextUtils;

import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.config.AppConfig;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class MarketUtil {

    private static final String DEFAULT = "- - -";

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
                contractName = DEFAULT;

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
                contractName = DEFAULT;

                break;
        }

        return contractName;
    }

    public static int getMarketStateColor(int type) {
        int color;

        switch (type) {
            case -1:
                color = R.color.common_font_decrease;

                break;
            case 0:
                color = R.color.common_font_stable;

                break;
            case 1:
                color = R.color.common_font_increase;

                break;
            default:
                color = R.color.common_font_stable;

                break;
        }

        return color;
    }

    public static int getMarketStateBackgroundColor(int type) {
        int drawable;

        switch (type) {
            case -1:
                drawable = R.drawable.bg_decrease;

                break;
            case 0:
                drawable = R.drawable.bg_stable;

                break;
            case 1:
                drawable = R.drawable.bg_increase;

                break;
            default:
                drawable = R.drawable.bg_stable;

                break;
        }

        return drawable;
    }

    public static String getValue(String value) {
        return TextUtils.isEmpty(value) ? "- - -" : value;
    }

    public static String getMarketRangeValue(int type, String range) {
        if (TextUtils.isEmpty(range))
            return DEFAULT;

        String value;

        switch (type) {
            case -1:
                value = range;

                break;
            case 0:
                value = "0.00";

                break;
            case 1:
                value = "+" + range;

                break;
            default:
                value = "";

                break;
        }

        return value;
    }

    public static String getMarketRateValue(int type, String rate) {
        if (TextUtils.isEmpty(rate))
            return DEFAULT;

        String value;

        switch (type) {
            case -1:
                value = rate + "%";

                break;
            case 0:
                value = "0.00%";

                break;
            case 1:
                value = "+" + rate + "%";

                break;
            default:
                value = "";

                break;
        }

        return value;
    }

    public static String getVolumeValue(String value, boolean currency) {
        if (TextUtils.isEmpty(value))
            return DEFAULT;

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

        if (valueDecimal.abs().compareTo(new BigDecimal(10000.0)) == -1)
            result = formatValueNum(valueStr, 2);
        else if (valueDecimal.abs().compareTo(new BigDecimal(10000.0)) != -1
                && valueDecimal.abs().compareTo(new BigDecimal(100000000.0)) == -1)
            result = formatValueNum(valueDivisor(valueDecimal, new BigDecimal(10000.0)).toString(), 2) + "万";
        else
            result = formatValueNum(valueDivisor(valueDecimal, new BigDecimal(100000000.0)).toString(), 2) + "亿";

        if (currency)
            return "￥" + result;
        else
            return result;
    }

    public static String formatValueNum(String value, int num) {
        if (TextUtils.isEmpty(value))
            return "";

        boolean flag = false;

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
        if (TextUtils.isEmpty(value))
            return "";

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

    public static String decimalFormatMoney(String money) {
        boolean flag;

        String value = formatValueNum(money, 2);

        if (TextUtils.isEmpty(value))
            return "";

        DecimalFormat format = new DecimalFormat();

        if (money.startsWith("-")) {
            flag = true;

            value = money.substring(1);
        } else {
            flag = false;
        }

        if (Double.parseDouble(value) < 1.0)
            format.applyPattern("0.00");
        else
            format.applyPattern("#,###,###.00");

        return format.format(new BigDecimal(flag ? ("-" + value) : value));
    }

    public static BigDecimal valueDivisor(BigDecimal original, BigDecimal divisor) {
        if (divisor.compareTo(new BigDecimal("0")) == 0)
            return new BigDecimal("0");
        else
            return new BigDecimal(original.toString()).divide(divisor);

    }

    public static String getPriceValue(long price) {
        return formatValue(new BigDecimal(price).divide(new BigDecimal(AppConfig.Price_Divisor)).toString(), 2);
    }

    public static String getRateValue(long rate) {
        return formatValue(new BigDecimal(rate).divide(new BigDecimal(AppConfig.Rate_Divisor)).multiply(new BigDecimal(100)).toString(), 2);
    }

}
