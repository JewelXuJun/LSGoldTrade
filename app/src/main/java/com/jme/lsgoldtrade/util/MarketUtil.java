package com.jme.lsgoldtrade.util;

import android.text.TextUtils;

import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.config.AppConfig;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class MarketUtil {

    private static final String DEFAULT = "- - -";

    public static String getContractName(String contractName) {
        if (TextUtils.isEmpty(contractName))
            return DEFAULT;

        return contractName;
    }

    public static String getContractCode(String contractId) {
        if (TextUtils.isEmpty(contractId))
            return DEFAULT;

        return contractId;
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
                color = R.color.color_text_black;

                break;
        }

        return color;
    }

    public static int getPriceStateColor(int type) {
        int color;

        switch (type) {
            case -1:
                color = R.color.common_font_decrease;

                break;
            case 1:
                color = R.color.common_font_increase;

                break;
            default:
                color = R.color.color_text_black;

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
        return TextUtils.isEmpty(value) ? DEFAULT : value;
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
                value = "-" + rate + "%";

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

    public static int getInOutMoneyDirectionColor(int direction) {
        int color;

        if (direction == 0)
            color = R.color.common_font_increase;
        else if (direction == 1)
            color = R.color.common_font_decrease;
        else
            color = R.color.color_text_black;

        return color;
    }

    public static int getInOutMoneyStatusColor(String businessStatus) {
        int color;

        if (businessStatus.equals("recharge"))
            color = R.color.common_font_increase;
        else if (businessStatus.equals("withdraw"))
            color = R.color.common_font_decrease;
        else
            color = R.color.color_text_black;

        return color;
    }

    public static String getInOutMoneyState(int depositFlag) {
        String value;

        if (depositFlag == 0)
            value = "未入账";
        else if (depositFlag == 1)
            value = "已入账";
        else if (depositFlag == 2)
            value = "入账失败";
        else if (depositFlag == 3)
            value = "入账冲正";
        else
            value = "";

        return value;
    }

    public static String getTradeDirection(int direction) {
        String value;

        if (direction == 1)
            value = "买入";
        else if (direction == 2)
            value = "卖出";
        else
            value = "";

        return value;
    }

    public static String getOCState(int state) {
        String value;

        if (state == 0)
            value = "开仓";
        else if (state == 1)
            value = "平仓";
        else
            value = "";

        return value;
    }

    public static int getTradeDirectionColor(int direction) {
        int color;

        if (direction == 1)
            color = R.color.common_font_increase;
        else if (direction == 2)
            color = R.color.common_font_decrease;
        else
            color = R.color.color_text_black;

        return color;
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

        if (valueDecimal.abs().compareTo(new BigDecimal(0)) == 0)
            result = "0";
        else if (valueDecimal.abs().compareTo(new BigDecimal(10000.0)) == -1)
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

    public static String getVolumeValue2(String value, boolean currency) {
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

        if (valueDecimal.abs().compareTo(new BigDecimal(0)) == 0)
            result = "0";
        else if (valueDecimal.abs().compareTo(new BigDecimal(10000.0)) == -1)
            result = formatValueNum(valueStr, 2);
        else if (valueDecimal.abs().compareTo(new BigDecimal(10000.0)) != -1
                && valueDecimal.abs().compareTo(new BigDecimal(100000000.0)) == -1)
            result = formatValueNum(valueDivisor2(valueDecimal, new BigDecimal(10000.0)).toString(), 2) + "万";
        else
            result = formatValueNum(valueDivisor2(valueDecimal, new BigDecimal(100000000.0)).toString(), 2) + "亿";

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
                    value = value.substring(1);
                    flag = true;
                }
            } else {
                value = new BigDecimal(value).toPlainString();
            }
        } else {
            if (value.contains("-")) {
                value = value.substring(1);
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
            String elseValue = value.substring(index + 1);
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

        return format.format(new BigDecimal(flag ? "-" + value : value));
    }

    public static String decimalFormatFloating(String money) {
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

        if (new BigDecimal(value).compareTo(new BigDecimal(0)) == 0)
            return "0.00";
        else
            return (flag ? "-" : "+") + format.format(new BigDecimal(value));
    }

    public static String getEntrustState(int state) {
        String value;

        if (state == 1)
            value = "委托已报入";
        else if (state == 2)
            value = "委托失败";
        else if (state == 3)
            value = "未成交";
        else if (state == 4)
            value = "部分成交";
        else if (state == 5)
            value = "完全成交";
        else if (state == 6)
            value = "撤单已报入";
        else if (state == 7)
            value = "撤单失败";
        else if (state == 8)
            value = "已撤单";
        else if (state == 9)
            value = "已撤单";
        else if (state == 10)
            value = "委托应答";
        else if (state == 11)
            value = "撤单应答";
        else if (state == 12)
            value = "发送失败";
        else if (state == 15)
            value = "应急撤单";
        else
            value = "";

        return value;
    }

    public static String getOrderStatus(String status) {
        String value = "";

        if (status.equals("0"))
            value = "委托中";
        else if (status.equals("1"))
            value = "建仓";
        else if (status.equals("2"))
            value = "建仓中";
        else if (status.equals("3"))
            value = "平仓";
        else if (status.equals("4"))
            value = "平仓中";
        else if (status.equals("5"))
            value = "委托完成";
        else if (status.equals("6"))
            value = "撤销中";
        else if (status.equals("7"))
            value = "已撤销";
        else if (status.equals("8"))
            value = "建仓完成";
        else if (status.equals("9"))
            value = "委托失败";

        return value;
    }

    public static BigDecimal valueDivisor(BigDecimal original, BigDecimal divisor) {
        if (divisor.compareTo(new BigDecimal("0")) == 0)
            return new BigDecimal("0");
        else
            return new BigDecimal(original.toString()).divide(divisor);
    }

    public static BigDecimal valueDivisor2(BigDecimal original, BigDecimal divisor) {
        if (divisor.compareTo(new BigDecimal("0")) == 0)
            return new BigDecimal("0");
        else
            return new BigDecimal(original.toString()).divide(divisor, 2, BigDecimal.ROUND_HALF_UP);

    }

    public static String getPriceValue(long price) {
        return formatValue(new BigDecimal(price).divide(new BigDecimal(AppConfig.Price_Divisor)).toString(), 2);
    }

    public static String getPriceValue(String price) {
        if (TextUtils.isEmpty(price))
            return "";

        return formatValue(new BigDecimal(price).divide(new BigDecimal(AppConfig.Price_Divisor)).toString(), 2);
    }

    public static String getRateValue(long rate) {
        return formatValue(new BigDecimal(rate).divide(new BigDecimal(AppConfig.Rate_Divisor), 4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)).toString(), 2);
    }

    public static long getHandWeight(long handWeight) {
        return new BigDecimal(handWeight).divide(new BigDecimal(AppConfig.HandWeight_Divisor)).longValue();
    }

}
