package com.jme.common.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.util.Log;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * Created by zhangzhongqiang on 2015/7/28.
 */
public class NetWorkUtils {

    /**
     * 获取本地内网IP
     * @return
     */
    public static String getLocalIPAddress() {
        try {
            for (Enumeration<NetworkInterface> mEnumeration = NetworkInterface
                    .getNetworkInterfaces(); mEnumeration.hasMoreElements();) {
                NetworkInterface intf = mEnumeration.nextElement();
                for (Enumeration<InetAddress> enumIPAddr = intf
                        .getInetAddresses(); enumIPAddr.hasMoreElements();) {
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


    /**
     * 获取当前网络类型
     *
     * @param context
     * @return 2G/3G/4G/WIFI/no/unknown
     */
    public static String getNetType(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo info = cm.getActiveNetworkInfo();
        if (info == null || !info.isAvailable()) {
            return "no";
        }
        if (info.getType() == ConnectivityManager.TYPE_WIFI) {
            return "WIFI";
        }
        if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
            int sub = info.getSubtype();
            switch (sub) {

                case TelephonyManager.NETWORK_TYPE_GPRS:
                case TelephonyManager.NETWORK_TYPE_EDGE:
                case TelephonyManager.NETWORK_TYPE_CDMA://电信的2G
                case TelephonyManager.NETWORK_TYPE_1xRTT:
                case TelephonyManager.NETWORK_TYPE_IDEN:
                    //以上的都是2G网络
                    return "2G";

                case TelephonyManager.NETWORK_TYPE_UMTS:
                case TelephonyManager.NETWORK_TYPE_EVDO_A:
                case TelephonyManager.NETWORK_TYPE_HSDPA:
                case TelephonyManager.NETWORK_TYPE_HSUPA:
                case TelephonyManager.NETWORK_TYPE_HSPA:
                case TelephonyManager.NETWORK_TYPE_EVDO_B:
                case TelephonyManager.NETWORK_TYPE_EHRPD:
                case TelephonyManager.NETWORK_TYPE_HSPAP:
                    //以上的都是3G网络
                    return "3G";

                case TelephonyManager.NETWORK_TYPE_LTE:

                    return "4G";

                case TelephonyManager.NETWORK_TYPE_UNKNOWN:

                    return "unknown";

                default:
                    return "unknown";
            }
        }
        return "unknown";
    }

    /**
     * 判断当前网络是否已连接
     *
     * @param context
     * @return
     */
    public static boolean isNetWorkConnected(Context context) {
        boolean result;
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();
        if (netinfo != null && netinfo.isConnected()) {
            result = true;
        } else {
            result = false;
        }
        return result;
    }


    /**
     * 判断当前的网络连接方式是否为WIFI
     *
     * @param context
     * @return
     */
    public static boolean isWifiConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiNetworkInfo = connectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiNetworkInfo.isConnected()) {
            return true;
        }
        return false;
    }

}
