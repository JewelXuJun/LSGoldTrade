package com.jme.lsgoldtrade.util;

import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Enumeration;
import java.util.regex.Pattern;

public class ValueUtils {

    public static boolean isPhoneNumber(String mobile) {
        if (TextUtils.isEmpty(mobile))
            return false;

        String REGEX_MOBILE = "^[1][3,4,5,7,8,9][0-9]{9}$";

        return Pattern.matches(REGEX_MOBILE, mobile);
    }

    public static boolean isPasswordRight(String password) {
        String REGEX_PASSWORD = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z_]{8,16}$";

        return Pattern.matches(REGEX_PASSWORD, password);
    }

    @NonNull
    public static String MD5(String string) {
        if (TextUtils.isEmpty(string))
            return "";

        MessageDigest md5;

        try {
            md5 = MessageDigest.getInstance("MD5");
            byte[] bytes = md5.digest(string.getBytes());
            StringBuilder result = new StringBuilder();

            for (byte b : bytes) {
                String temp = Integer.toHexString(b & 0xff);

                if (temp.length() == 1)
                    temp = "0" + temp;

                result.append(temp);
            }

            return result.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return "";
    }

    public static String getLocalIPAddress() {
        try {
            for (Enumeration<NetworkInterface> mEnumeration = NetworkInterface
                    .getNetworkInterfaces(); mEnumeration.hasMoreElements(); ) {
                NetworkInterface intf = mEnumeration.nextElement();
                for (Enumeration<InetAddress> enumIPAddr = intf
                        .getInetAddresses(); enumIPAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIPAddr.nextElement();
                    // 如果不是回环地址
                    // 判断是否是IPV4地址的方法
                    // 1. inetAddress instanceof Inet4Address
                    // 2. InetAddressUtils.isIPv4Address(inetAddress.getHostAddress())
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        // 直接返回本地IP地址
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {
            Log.e("NetWorkUtils", "获取手机内网IP地址失败！(SocketException)");
        } catch (Exception e) {
            Log.e("NetWorkUtils", "获取手机内网IP地址失败！(Exception)");
        }
        return null;
    }

}
