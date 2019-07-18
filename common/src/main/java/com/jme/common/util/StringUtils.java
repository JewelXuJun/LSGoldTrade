package com.jme.common.util;

import android.text.TextUtils;

import java.text.DecimalFormat;
import java.util.regex.Pattern;

/**
 * Created by XuJun on 2018/11/7.
 */

public class StringUtils {

    /**
     * 手机号中间四位替换为*
     *
     * @param phone
     * @return 136****7423
     */
    public static String phoneInvisibleMiddle(String phone) {
        if (!TextUtils.isEmpty(phone))
            return phone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");

        return "";
    }

    /**
     * 身份证中间替换为*
     *
     * @param cardID
     * @return 3****************6
     */
    public static String cardIDInvisibleMiddle(String cardID) {
        int length = cardID.length();

        if (!TextUtils.isEmpty(cardID))
            return cardID.substring(0, 1) + "****************" + cardID.substring(length - 1, length);

        return "";
    }

    /**
     * 银行卡卡号替换为*
     *
     * @param bankCard
     * @return **** **** **** 2021
     */
    public static String bankCardInvisible(String bankCard) {
        if (TextUtils.isEmpty(bankCard))
            return "";

        int length = bankCard.length();

        if (length < 4)
            return "";

        return "****  ****  ****  " + bankCard.substring(length - 4, length);
    }

    /**
     * 银行卡卡号替换为*
     *
     * @param bankCard
     * @return 6214 **** **** 2021
     */
    public static String bankCardInvisibleMiddle(String bankCard) {
        if (TextUtils.isEmpty(bankCard))
            return "";

        int length = bankCard.length();

        if (length < 8)
            return "";

        return bankCard.substring(0, 4) + getBankCardStars(bankCard) + bankCard.substring(length - 4, length);
    }

    public static String bankCardInvisibleMiddleUnified(String bankCard) {
        if (TextUtils.isEmpty(bankCard))
            return "";

        int length = bankCard.length();

        if (length < 8)
            return "";

        return bankCard.substring(0, 4) + " **** **** " + bankCard.substring(length - 4, length);
    }

    private static String getBankCardStars(String bankcard) {
        int length = bankcard.length();
        String value;

        if (length == 16)
            value = " **** **** ";
        else if (length == 17)
            value = " **** **** * ";
        else if (length == 18)
            value = " **** **** ** ";
        else if (length == 19)
            value = " **** **** *** ";
        else
            value = " **** **** ";

        return value;
    }

    public static String getBankCardLastDigitsNumber(String bankcard) {
        if (TextUtils.isEmpty(bankcard))
            return "";

        int length = bankcard.length();

        if (length < 4)
            return bankcard;

        return bankcard.substring(length - 4, length);
    }

    /**
     * 只显示名字的第一个字
     *
     * @param name
     * @return 王*
     */
    public static String nameInvisible(String name) {
        if (!TextUtils.isEmpty(name))
            return name.substring(0, 1) + "*";
        else
            return "";
    }

    public static boolean checkBankCardLength(String bankCard) {
        int length = bankCard.length();

        if (length < 16 || length > 19)
            return false;

        return true;
    }

    /**
     * 校验银行卡卡号
     * 校验过程：
     * 1、从卡号最后一位数字开始，逆向将奇数位(1、3、5等等)相加。
     * 2、从卡号最后一位数字开始，逆向将偶数位数字，先乘以2（如果乘积为两位数，将个位十位数字相加，即将其减去9），再求和。
     * 3、将奇数位总和加上偶数位总和，结果应该可以被10整除。
     */
    public static boolean checkBankCard(String bankCard) {
        int length = bankCard.length();

        if (length < 16 || length > 19)
            return false;

        char bit = getBankCardCheckCode(bankCard.substring(0, bankCard.length() - 1));

        if (bit == 'N')
            return false;

        return bankCard.charAt(length - 1) == bit;
    }

    /**
     * 从不含校验位的银行卡卡号采用 Luhm 校验算法获得校验位
     *
     * @param nonCheckCodeBankCard
     * @return
     */
    private static char getBankCardCheckCode(String nonCheckCodeBankCard) {
        if (null == nonCheckCodeBankCard || nonCheckCodeBankCard.trim().length() == 0
                || !nonCheckCodeBankCard.matches("\\d+"))
            return 'N';

        char[] chs = nonCheckCodeBankCard.trim().toCharArray();

        int luhmSum = 0;

        for (int i = chs.length - 1, j = 0; i >= 0; i--, j++) {
            int k = chs[i] - '0';

            if (j % 2 == 0) {
                k *= 2;
                k = k / 10 + k % 10;
            }

            luhmSum += k;
        }

        return (luhmSum % 10 == 0) ? '0' : (char) ((10 - luhmSum % 10) + '0');
    }

    public static String formatPriceEachTree(double price) {
        DecimalFormat df = new DecimalFormat("#,##0.00");
        return df.format(price);
    }

    public static String formatPriceTwoPoint(double price) {
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(price);
    }

    public static String formatUpperCase(String value) {
        if (TextUtils.isEmpty(value))
            return "";

        return value.toUpperCase();
    }

    public static String formatLowerCase(String value) {
        if (TextUtils.isEmpty(value))
            return "";

        return value.toLowerCase();
    }

    public static String getArrayValue(String[] arrayValue) {
        if (null == arrayValue)
            return "";

        StringBuffer stringBuffer = new StringBuffer();
        int size = arrayValue.length;

        for (int i = 0; i < size; i++) {
            if (i < size - 1)
                stringBuffer.append("" + (i + 1) + ". " + arrayValue[i] + "\n");
            else
                stringBuffer.append("" + (i + 1) + ". " + arrayValue[i]);
        }

        return stringBuffer.toString();
    }

    //邮箱验证
    public static boolean isEmail(String strEmail) {
        String strPattern = "^[a-zA-Z0-9][\\w\\.-]*[a-zA-Z0-9]@[a-zA-Z0-9][\\w\\.-]*[a-zA-Z0-9]\\.[a-zA-Z][a-zA-Z\\.]*[a-zA-Z]$";
        if (TextUtils.isEmpty(strPattern)) {
            return false;
        } else {
            return strEmail.matches(strPattern);
        }
    }

    public static String getStringValue(String[] value) {
        if (null == value || 0 == value.length)
            return "";

        StringBuffer stringBuffer = new StringBuffer();
        int size = value.length;

        for (int i = 0; i < size; i++) {
            if (i < size - 1)
                stringBuffer.append(value[i] + ",");
            else
                stringBuffer.append(value[i]);
        }

        return stringBuffer.toString();
    }

    public static String[] getStringArray(String value) {
        if (TextUtils.isEmpty(value))
            return null;

        return value.split(",");
    }

    public static String getValueStr(String value) {
        String valueStr;

        if (TextUtils.isEmpty(value)) {
            valueStr = "";
        } else {
            if (value.startsWith("."))
                valueStr = "";
            else if (value.endsWith("."))
                valueStr = value.substring(0, value.length() - 1);
            else
                valueStr = value;
        }

        return valueStr;
    }

    public static String getEmail(String value) {
        String email;

        if (TextUtils.isEmpty(value)) {
            email = "";
        } else {
            String emailHead;

            if (value.contains("@")) {
                int index = value.indexOf("@");

                emailHead = value.substring(0, index);
                String emailEnd = value.substring(index, value.length());

                if (TextUtils.isEmpty(emailHead)) {
                    email = "";
                } else {
                    int length = emailHead.length();

                    if (length > 4)
                        email = emailHead.substring(0, 2) + "***" + emailHead.substring(length - 2, length) + emailEnd;
                    else
                        email = "***" + emailEnd;
                }
            } else {
                emailHead = value;

                int length = emailHead.length();

                if (length > 4)
                    email = emailHead.substring(0, 2) + "***" + emailHead.substring(length - 2, length);
                else
                    email = "***";
            }
        }

        return email;
    }

}
