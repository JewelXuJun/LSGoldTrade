package com.jme.common.util;

import android.text.InputFilter;
import android.text.Spanned;

import java.util.regex.Pattern;

/**
 * Created by XuJun on 2016/9/8.
 */
public class DecimalDigitsInputFilterUtil implements InputFilter {

    private int digitsBeforeZero;
    private int digitsAfterZero;

    Pattern mPattern;

    public DecimalDigitsInputFilterUtil(int digitsBeforeZero, int digitsAfterZero) {
        this.digitsBeforeZero = digitsBeforeZero;
        this.digitsAfterZero = digitsAfterZero;
        mPattern = Pattern.compile("[0-9]{0," + (digitsBeforeZero - 1) + "}+((\\.[0-9]{0," + (digitsAfterZero - 1) + "})?)||(\\.)?");
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        int index = dest.toString().indexOf(".");
        String d = dest.toString();
        String[] splitArray = d.split("\\.");
        if (splitArray.length > 1) {
            String _decima = splitArray[1]; //小数
            String _integer = splitArray[0]; //整数
            int _decimaLenth = _decima.length(); //小数长度
            int _integerLength = _integer.length(); //整数长度
            if (_decimaLenth >= digitsAfterZero && dstart >= index)  // 小数长度大于小数位数并且输入位置在小数点之后
                return "";
            else if (_integerLength >= digitsBeforeZero && dstart < index) // 整数部分长度大于整数位数 并且输入位置在小数点之前
                return "";
            else
                return null;
        } else {
            if (d.length() >= digitsBeforeZero) {
                if (index == d.length() - 1 || source.toString().equals("."))  // 最后输入一位为.
                    return null;
                else
                    return "";
            } else
                return null;
        }
    }
}
