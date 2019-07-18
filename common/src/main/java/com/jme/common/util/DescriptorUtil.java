package com.jme.common.util;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;

import com.jme.common.R;

/**
 * Created by XuJun on 2016/3/15.
 */
public class DescriptorUtil {

    public static final String S_INTERVAL = "-";
    public static final String S_NOBLANK = "";
    public static final String S_ZERO = "0";
    public static final String S_COLON = ":";
    public static final String S_SPACE = "  ";
    public static final String S_COMMA = ", ";

    public static final String S_HAND = "手";
    public static final String S_KCHART_PERCENTAGE = "%";

    private Context mContext;

    public DescriptorUtil(Context context) {
        mContext = context;
    }

    public String getTimeContainInterval(int year, int month, int day) {
        StringBuffer sb = new StringBuffer();
        sb.append(year);

        sb.append(S_INTERVAL);
        if (month < 9) {
            sb.append(S_ZERO);
        }
        sb.append(month + 1);

        sb.append(S_INTERVAL);
        if (day < 10) {
            sb.append(S_ZERO);
        }
        sb.append(day);

        return sb.toString();
    }

    public String replaceString(String s, String original, String replace) {
        return s.replace(original, replace);
    }

    public SpannableString setHighLight(String src, String key, int color) {
        SpannableString dst = new SpannableString(src);
        if (TextUtils.isEmpty(key))
            return dst;

        int index = src.indexOf(key);
        if (index == -1)
            return dst;

        dst.setSpan(new ForegroundColorSpan(color), index, index + key.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return dst;
    }

    public SpannableString setPrice(String price, int type) {
        StringBuffer sb = new StringBuffer();
        sb.append(BigDecimalUtil.formatMoney(price));

        SpannableString ss = new SpannableString(sb.toString());
        ss.setSpan(new ForegroundColorSpan(getTextColor(type)), 0, sb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return ss;
    }

    public SpannableString setPriceInt(String price, int type) {
        StringBuffer sb = new StringBuffer();
        sb.append(Math.round(Double.parseDouble(price)));

        SpannableString ss = new SpannableString(sb.toString());
        ss.setSpan(new ForegroundColorSpan(getTextColor(type)), 0, sb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return ss;
    }

    public SpannableString setRange(String range, int type) {
        StringBuffer sb = new StringBuffer();
        sb.append(BigDecimalUtil.formatMoney(range));
        sb.append(S_KCHART_PERCENTAGE);

        SpannableString ss = new SpannableString(sb.toString());
        ss.setSpan(new ForegroundColorSpan(getTextColor(type)), 0, sb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return ss;
    }

    public SpannableString setBalance(String price, int type) {
        StringBuffer sb = new StringBuffer();
        sb.append(price);

        SpannableString ss = new SpannableString(sb.toString());
        ss.setSpan(new ForegroundColorSpan(getTextColor(type)), 0, sb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return ss;
    }

    public SpannableString setAmount(String price, int type) {
        StringBuffer sb = new StringBuffer();
        sb.append(price);
        sb.append(S_HAND);

        SpannableString ss = new SpannableString(sb.toString());
        ss.setSpan(new ForegroundColorSpan(getTextColor(type)), 0, sb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return ss;
    }

    public SpannableString setStatus(String status, int color) {
        StringBuffer sb = new StringBuffer();
        sb.append(status);

        SpannableString ss = new SpannableString(sb.toString());
        ss.setSpan(new ForegroundColorSpan(color), 0, sb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return ss;
    }

    public String setTimeFormat(String time) {
        StringBuffer sb = new StringBuffer();

        if (TextUtils.isEmpty(time))
            return "";

        int count = time.length();

        if (count < 5)
            return "";

        sb.append(time.substring(0, count - 4));
        sb.append(S_COLON);
        sb.append(time.substring(count - 4, count - 2));
        sb.append(S_COLON);
        sb.append(time.substring(count - 2, count));

        return sb.toString();
    }

    public String setDateTimeFormat(String date, String time) {
        StringBuffer sb = new StringBuffer();

        if (!TextUtils.isEmpty(date)) {
            int countDate = date.length();

            if (countDate != 8)
                return "";

            sb.append(date.substring(0, 4));
            sb.append(S_INTERVAL);
            sb.append(date.substring(4, 6));
            sb.append(S_INTERVAL);
            sb.append(date.substring(6, 8));
            sb.append(S_SPACE);
        }

        if (!TextUtils.isEmpty(time)) {
            int countTime = time.length();

            if (countTime < 6) {
                for (int i = countTime; i < 6; i++) {
                    sb.append(S_ZERO);
                }
            }

            sb.append(time.substring(0, countTime - 4));
            sb.append(S_COLON);
            sb.append(time.substring(countTime - 4, countTime - 2));
            sb.append(S_COLON);
            sb.append(time.substring(countTime - 2, countTime));
        }

        return sb.toString().trim();
    }

    public SpannableString setValue(String value, int color) {
        StringBuffer sb = new StringBuffer();
        sb.append(value);

        SpannableString ss = new SpannableString(sb.toString());
        ss.setSpan(new ForegroundColorSpan(color), 0, sb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return ss;
    }

    public int getTextColor(int type) {
        int color = ContextCompat.getColor(mContext, R.color.common_font_stable);

        switch (type) {
            case 0:
                color = ContextCompat.getColor(mContext, R.color.common_font_stable);
                break;

            case 1:
                color = ContextCompat.getColor(mContext, R.color.common_font_increase);
                break;

            case 2:
                color = ContextCompat.getColor(mContext, R.color.common_font_decrease);
                break;

            case 3:
                color = ContextCompat.getColor(mContext, R.color.white);
                break;

            case 4:
                color = ContextCompat.getColor(mContext, R.color.gray);
                break;
            case 5:
                color = ContextCompat.getColor(mContext, R.color.gray);
                break;
        }

        return color;
    }

    public SpannableString combine(String part1, String part2) {
        int count = 0;

        if (!TextUtils.isEmpty(part1))
            count = count + 1;

        if (!TextUtils.isEmpty(part2))
            count = count + 1;

        if (count == 0) {
            return new SpannableString("");
        } else {
            int strNum = 0;
            int[] endIdx = new int[count + 1];
            int[] colors = new int[count];
            endIdx[0] = 0;

            StringBuffer sb = new StringBuffer();

            if (!TextUtils.isEmpty(part1)) {
                sb.append(part1);
                sb.append(S_SPACE);

                endIdx[strNum + 1] = endIdx[strNum] + part1.length() + S_SPACE.length();
                colors[strNum] = Color.rgb(34, 34, 34);

                strNum++;
            }

            if (!TextUtils.isEmpty(part2)) {
                sb.append(part2);

                endIdx[strNum + 1] = endIdx[strNum] + part2.length();
                colors[strNum] = Color.rgb(153, 153, 153);

                strNum++;
            }

            SpannableString ss = new SpannableString(sb.toString());

            for (int i = 0; i < strNum; i++) {
                int color = colors[i];
                ss.setSpan(new ForegroundColorSpan(color), endIdx[i], endIdx[i + 1], Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            return ss;
        }
    }

    public SpannableString combine(String part1, String part2, int color1, int color2) {
        int count = 0;

        if (!TextUtils.isEmpty(part1))
            count = count + 1;

        if (!TextUtils.isEmpty(part2))
            count = count + 1;

        if (count == 0) {
            return new SpannableString("");
        } else {
            int strNum = 0;
            int[] endIdx = new int[count + 1];
            int[] colors = new int[count];
            endIdx[0] = 0;

            StringBuffer sb = new StringBuffer();

            if (!TextUtils.isEmpty(part1)) {
                sb.append(part1);
                sb.append(S_SPACE);

                endIdx[strNum + 1] = endIdx[strNum] + part1.length() + S_SPACE.length();
                colors[strNum] = color1;

                strNum++;
            }

            if (!TextUtils.isEmpty(part2)) {
                sb.append(part2);

                endIdx[strNum + 1] = endIdx[strNum] + part2.length();
                colors[strNum] = color2;

                strNum++;
            }

            SpannableString ss = new SpannableString(sb.toString());

            for (int i = 0; i < strNum; i++) {
                int color = colors[i];
                ss.setSpan(new ForegroundColorSpan(color), endIdx[i], endIdx[i + 1], Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            return ss;
        }
    }

    public String getHiddingPhoneNumber(String phoneNumber) {
        if (TextUtils.isEmpty(phoneNumber))
            return "";

        if (phoneNumber.length() <= 6)
            return phoneNumber;

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < phoneNumber.length(); i++) {
            char c = phoneNumber.charAt(i);
            if (i >= 3 && i <= 6) {
                sb.append('*');
            } else {
                sb.append(c);
            }
        }

        return sb.toString();
    }

    public SpannableString getKeyBoardPackageMessage(String part1, String part2) {
        int count = 0;

        if (!TextUtils.isEmpty(part1))
            count = count + 1;

        if (!TextUtils.isEmpty(part2))
            count = count + 1;

        if (count == 0) {
            return new SpannableString("");
        } else {
            int strNum = 0;
            int[] endIdx = new int[count + 1];
            int[] colors = new int[count];
            endIdx[0] = 0;

            StringBuffer sb = new StringBuffer();

            if (!TextUtils.isEmpty(part1)) {
                sb.append(part1);
                sb.append(S_SPACE);

                endIdx[strNum + 1] = endIdx[strNum] + part1.length() + S_SPACE.length();
                colors[strNum] = getTextColor(5);

                strNum++;
            }

            if (!TextUtils.isEmpty(part2)) {
                sb.append(part2);

                endIdx[strNum + 1] = endIdx[strNum] + part2.length();
                colors[strNum] = getTextColor(4);

                strNum++;
            }

            SpannableString ss = new SpannableString(sb.toString());

            for (int i = 0; i < strNum; i++) {
                int color = colors[i];
                ss.setSpan(new ForegroundColorSpan(color), endIdx[i], endIdx[i + 1], Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            return ss;
        }
    }

    public SpannableString getKeyBoardPackageMessage(String part1, String part2, String part3) {
        int count = 0;

        if (!TextUtils.isEmpty(part1))
            count = count + 1;

        if (!TextUtils.isEmpty(part2))
            count = count + 1;

        if (!TextUtils.isEmpty(part3))
            count = count + 1;

        if (count == 0) {
            return new SpannableString("");
        } else {
            int strNum = 0;
            int[] endIdx = new int[count + 1];
            int[] colors = new int[count];
            endIdx[0] = 0;

            StringBuffer sb = new StringBuffer();

            if (!TextUtils.isEmpty(part1)) {
                sb.append(part1);
                sb.append(S_COMMA);

                endIdx[strNum + 1] = endIdx[strNum] + part1.length() + S_COMMA.length();
                colors[strNum] = getTextColor(1);

                strNum++;
            }

            if (!TextUtils.isEmpty(part2)) {
                sb.append(part2);
                sb.append(S_COMMA);

                endIdx[strNum + 1] = endIdx[strNum] + part2.length() + S_COMMA.length();
                colors[strNum] = getTextColor(2);

                strNum++;
            }

            if (!TextUtils.isEmpty(part3)) {
                sb.append(part3);

                endIdx[strNum + 1] = endIdx[strNum] + part3.length();
                colors[strNum] = getTextColor(4);

                strNum++;
            }

            SpannableString ss = new SpannableString(sb.toString());

            for (int i = 0; i < strNum; i++) {
                int color = colors[i];
                ss.setSpan(new ForegroundColorSpan(color), endIdx[i], endIdx[i + 1], Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            return ss;
        }
    }
}
