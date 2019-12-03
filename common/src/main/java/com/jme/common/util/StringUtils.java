package com.jme.common.util;

import android.text.TextUtils;

public class StringUtils {

    public static boolean validatePassword(String pass) {
        String passRegex = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,18}$";

        return !TextUtils.isEmpty(pass) && pass.matches(passRegex);
    }

    public static String phoneInvisibleMiddle(String phone) {
        if (!TextUtils.isEmpty(phone))
            return phone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");

        return "";
    }

    public static String formatIDCardNumber(String IDCard) {
        if (TextUtils.isEmpty(IDCard))
            return "";

        int length = IDCard.length();

        return IDCard.substring(0, 3) + "*************" + IDCard.substring(length - 2, length);
    }

    public static String formatBankCard(String bankCard) {
        if (TextUtils.isEmpty(bankCard))
            return "";

        int length = bankCard.length();

        if (length < 4)
            return "";

        return "****" + bankCard.substring(length - 4, length);
    }

    public static String formatBankCardDefault(String bankCard) {
        if (TextUtils.isEmpty(bankCard))
            return "";

        int length = bankCard.length();

        if (length < 8)
            return "";

        return bankCard.substring(0, 4) + "****" + bankCard.substring(length - 4, length);
    }

    public static String formatName(String name) {
        if (TextUtils.isEmpty(name))
            return "";
        else
            return "**" + name.substring(name.length() - 1);
    }

    public static String formatUpperCase(String value) {
        if (TextUtils.isEmpty(value))
            return "";

        return value.toUpperCase();
    }

    public static String[] getStringArray(String value) {
        if (TextUtils.isEmpty(value))
            return null;

        if (value.endsWith(","))
            value = value.substring(0, value.length() - 1);

        return value.split(",");
    }

}
