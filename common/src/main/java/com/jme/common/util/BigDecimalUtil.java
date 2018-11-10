package com.jme.common.util;

import android.text.TextUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class BigDecimalUtil {

    /**
     * 将数字进行格式化.
     * <br>
     * 去掉数字小数点右边多余的0。
     * <br>
     * 例如：
     * <br>
     * 123.9800 -> 123.98
     * <br>
     * 12300 -> 12300
     * <br>
     * 123.9080 -> 123.908
     * <br>
     *
     * @param val 待格式化数字
     * @return 格式化后的数字字符串
     */
    public static String formatNumber(BigDecimal val) {
        String valStr = val.toString();
        if (0 < valStr.indexOf(".")) {
            // 去掉多余的0
            valStr = valStr.replaceAll("0+?$", "");
            // 如最后一位是.则去掉
            valStr = valStr.replaceAll("[.]$", "");
        }
        return valStr;
    }

    public static String formatStrNumber(String val) {
        if (0 < val.indexOf(".")) {
            // 去掉多余的0
            val = val.replaceAll("0+?$", "");
            // 如最后一位是.则去掉
            val = val.replaceAll("[.]$", "");
        }
        return val;
    }

    /**
     * 保留金额小数点后2位
     *
     * @param money 待格式化金额
     * @return 格式化后的金额字符串
     */
    public static String formatMoney(String money) {
        return formatMoneyNum(money, 2);
    }

    public static String formatRate(String value) {
        return formatMoneyNum(value, 2) + "%";
    }

    public static String decimalFormatMoney(String money) {
        boolean flag;

        String value = formatMoneyNum(money, 2);

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

    /**
     * 将数字进行格式化.
     * <br>
     * 11239080-> 11,239,080
     * <br>
     *
     * @param money 待格式化金额
     * @return 格式化后的金额字符串
     */
    public static String formatNumber(String money) {
        if (Double.parseDouble(money) == 0.0) {
            return "0";
        }

        DecimalFormat format = new DecimalFormat();
        format.applyPattern("#######");

        return format.format(new BigDecimal(money));
    }

    /**
     * 将银行卡号进行格式化.
     * <br>
     * 622584 8945 125847 -> 622584 **** 125847
     * <br>
     *
     * @param cardNumber 待格式化银行卡号
     * @return 格式化后的银行卡号字符串
     */
    public static String formatCardNumber(String cardNumber) {
        StringBuffer sb = new StringBuffer();
        String part_start = cardNumber.substring(0, 6);
        String part_end = cardNumber.substring(cardNumber.length() - 6);
        sb.append(part_start);
        sb.append(" **** ");
        sb.append(part_end);
        return sb.toString();
    }

    /**
     * Float 相加
     *
     * @param original 初始值
     * @param addition 加数
     * @return 计算结果
     */
    public static String valueAdd(float original, float addition) {
        BigDecimal bigDecimal = new BigDecimal(Float.toString(original)).add(new BigDecimal(Float.toString(addition)));

        DecimalFormat format = new DecimalFormat();
        format.applyPattern("0.00");

        return format.format(bigDecimal);
    }

    public static String valueAdd(BigDecimal original, float addition) {
        BigDecimal bigDecimal = original.add(new BigDecimal(Float.toString(addition)));

        DecimalFormat format = new DecimalFormat();
        format.applyPattern("0.00");

        return format.format(bigDecimal);
    }

    public static String valueAdd(String original, String addition) {
        BigDecimal bigDecimal = new BigDecimal(original).add(new BigDecimal(addition));

        DecimalFormat format = new DecimalFormat();
        format.applyPattern("0.00");

        return format.format(bigDecimal);
    }

    /**
     * Float 相减
     *
     * @param original 初始值
     * @param meiosis  减数
     * @return 计算结果
     */
    public static String valueSubtract(float original, float meiosis) {
        BigDecimal bigDecimal = new BigDecimal(Float.toString(original)).subtract(new BigDecimal(Float.toString(meiosis)));

        DecimalFormat format = new DecimalFormat();
        format.applyPattern("0.00");

        return format.format(bigDecimal);
    }

    public static String valueSubtract(BigDecimal original, float meiosis) {
        BigDecimal bigDecimal = original.subtract(new BigDecimal(Float.toString(meiosis)));

        DecimalFormat format = new DecimalFormat();
        format.applyPattern("0.00");

        return format.format(bigDecimal);
    }

    /**
     * BigDecimal 相乘
     *
     * @param original   初始值
     * @param multiplier 减数
     * @return 计算结果
     */
    public static BigDecimal valueMultiplier(BigDecimal original, BigDecimal multiplier) {
        BigDecimal bigDecimal = new BigDecimal(original.toString()).multiply(multiplier);

        return bigDecimal;
    }

    /**
     * BigDecimal 相除
     *
     * @param original 初始值
     * @param divisor  除数
     * @return 计算结果
     */
    public static BigDecimal valueDivisor(BigDecimal original, BigDecimal divisor) {
        if (valueCompare(divisor, new BigDecimal("0")) == 0)
            return new BigDecimal("0");
        else
            return new BigDecimal(original.toString()).divide(divisor);

    }

    /**
     * int 金额比较大小
     *
     * @param price          初始值
     * @param availablePrice 比较值
     * @return 比较结果 0 相等 1 大于 -1 小于
     */
    public static int valueCompare(BigDecimal price, BigDecimal availablePrice) {
        return price.compareTo(availablePrice);
    }

    /***
     * 为了兼容已经调用的地方
     *
     * @param value
     * @param currency
     * @return
     */
    public static String ScreenValue(String value, boolean currency) {
        return ScreenValue(value, 2, currency);
    }

    /**
     * 格式化数据位数较长的数据
     * value 金额
     * num 保留几位小数
     */
    public static String format2Number(String value, int num) {
        return formatValue(value, num);
    }

    public static String ScreenValue(String value, int num, boolean currency) {
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
            result = formatMoneyNum(m, num);
        } else if ((valueCompare(valueDecimal.abs(), new BigDecimal(10000.0)) == 1
                || valueCompare(valueDecimal.abs(), new BigDecimal(10000.0)) == 0)
                && valueCompare(valueDecimal.abs(), new BigDecimal(100000000.0)) == -1)
            result = formatMoneyNum(valueDivisor(valueDecimal, new BigDecimal(10000.0)).toString(), 2) + "万";
        else
            result = formatMoneyNum(valueDivisor(valueDecimal, new BigDecimal(100000000.0)).toString(), 2) + "亿";

        if (currency)
            return "￥" + result;
        else
            return result;

    }

    public static String formatMoneyNum(String money, int num) {
        boolean flag = false;

        if (TextUtils.isEmpty(money))
            return "";

        if (money.contains("E")) {
            if (money.contains("-")) {
                money = new BigDecimal(money).negate().toPlainString();

                if (money.contains("-")) {
                    money = money.substring(1, money.length());
                    flag = true;
                }
            } else {
                money = new BigDecimal(money).toPlainString();
            }
        } else {
            if (money.contains("-")) {
                money = money.substring(1, money.length());
                flag = true;
            }
        }

        money = formatValue(money, num);

        if (new BigDecimal(money).compareTo(new BigDecimal("0")) == 0)
            return money;

        if (flag)
            return "-" + money;
        else
            return money;
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
}