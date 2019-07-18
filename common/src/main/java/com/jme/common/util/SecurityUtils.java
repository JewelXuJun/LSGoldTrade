/*
 * 文件名：SecurityUtils.java
 * 版权：Copyright 2015 江苏大泰信息技术有限公司. All Rights Reserved. 
 * 描述： 加密解密工具类
 * 创建人：H.CAAHN
 * 创建时间：2012-08-06
 * 修改人：汪涛
 * 修改时间：2016-02-03
 */
package com.jme.common.util;

import android.text.TextUtils;

import java.security.MessageDigest;

/**
 * 加解密工具集。
 * @author H.CAAHN
 * @createtime 2012-8-6 上午12:15:38
 */
public final class SecurityUtils {

    /** 默认的字符串编码方式 */
    public static String DEFAULT_ENCODING = "utf-8";

    /*
     * MD5
     */
    /** 十六进制数字字符 大写. */
    private static char hexdigitsUpperCase[] = { '0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

    /** 十六进制数字字符 小写. */
    private static char hexdigitsLowerCase[] = { '0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

    /**
     * 获得MD5后字符串.
     * @param str 待MD5加密字符串
     * @param isUpperCase 加密后字符串是否是大写(true:大写 false:小写)
     * @return MD5加密后字符串，返回null则表示加密失败
     */
    public static String getMD5(String str, boolean isUpperCase) {
        // 如果_str为空，则返回null表示加密失败
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        try {
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest md = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            byte[] buffer = str.getBytes();
            md.update(buffer);
            byte[] md5Str = md.digest();
            // 返回MD5加密后16进制字符串
            return byteToHexString(md5Str, isUpperCase);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        // MD5加密失败
        return null;
    }

    /**
     * 把byte[]数组转换成十六进制字符串表示形式.
     * @param tmp 要转换的byte[]
     * @param isOpperCase 加密后字符串是否是大写(true:大写 false:小写)
     * @return 十六进制字符串表示形式
     */
    private static String byteToHexString(byte[] tmp, boolean isOpperCase) {
        // 采用的字符集
        char hexdigits[] = null;
        // 判断需要返回的是大写字符串还是小写字符串
        if (isOpperCase) {
            hexdigits = hexdigitsUpperCase;
        } else {
            hexdigits = hexdigitsLowerCase;
        }
        // 存储MD5加密后16进制字符串数组
        char str[] = new char[tmp.length * 2];
        // 把密文转换成十六进制的字符串形式
        int k = 0;
        for (int i = 0; i < 16; i++) {
            byte byte0 = tmp[i];
            str[k++] = hexdigits[byte0 >>> 4 & 0xf];
            str[k++] = hexdigits[byte0 & 0xf];
        }
        return new String(str);
    }

    /**
     * 根据MD5加密算法加密自己数组.
     * @param value 需要加密的数据
     * @return 返回加密后的byte数组，返回null表示加密失败
     */
    public static final byte[] encryptMD5(byte[] value) {
        byte[] result = null;
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            result = messageDigest.digest(value);
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
        return result;
    }

    /**
     * 私有构造方法.
     */
    private SecurityUtils() {

    }
}
