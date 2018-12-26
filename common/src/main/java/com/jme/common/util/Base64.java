package com.jme.common.util;

/**
 * Created by Android Studio.
 * Author : zhangzhongqiang
 * Email  : zhangzhongqiang@jsdttec.com
 * Time   : 2017/03/15 下午 4:56
 * Desc   : description
 */

public final class Base64 {
    private static char[] alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=".toCharArray();
    private static byte[] codes = new byte[256];

    static {
        int i;
        for(i = 0; i < 256; ++i) {
            codes[i] = -1;
        }

        for(i = 65; i <= 90; ++i) {
            codes[i] = (byte)(i - 65);
        }

        for(i = 97; i <= 122; ++i) {
            codes[i] = (byte)(26 + i - 97);
        }

        for(i = 48; i <= 57; ++i) {
            codes[i] = (byte)(52 + i - 48);
        }

        codes[43] = 62;
        codes[47] = 63;
    }

    public Base64() {
    }

    public static byte[] decode(byte[] data) {
        int len = (data.length + 3) / 4 * 3;
        if(data.length > 0 && data[data.length - 1] == 61) {
            --len;
        }

        if(data.length > 1 && data[data.length - 2] == 61) {
            --len;
        }

        byte[] out = new byte[len];
        int shift = 0;
        int accum = 0;
        int index = 0;

        for(int ix = 0; ix < data.length; ++ix) {
            byte value = codes[data[ix] & 255];
            if(value >= 0) {
                accum <<= 6;
                shift += 6;
                accum |= value;
                if(shift >= 8) {
                    shift -= 8;
                    out[index++] = (byte)(accum >> shift & 255);
                }
            }
        }

        if(index != out.length) {
            throw new RuntimeException("miscalculated data length!");
        } else {
            return out;
        }
    }

    public static char[] encode(byte[] data) {
        char[] out = new char[(data.length + 2) / 3 * 4];
        int i = 0;

        for(int index = 0; i < data.length; index += 4) {
            boolean quad = false;
            boolean trip = false;
            int val = 255 & data[i];
            val <<= 8;
            if(i + 1 < data.length) {
                val |= 255 & data[i + 1];
                trip = true;
            }

            val <<= 8;
            if(i + 2 < data.length) {
                val |= 255 & data[i + 2];
                quad = true;
            }

            out[index + 3] = alphabet[quad?val & 63:64];
            val >>= 6;
            out[index + 2] = alphabet[trip?val & 63:64];
            val >>= 6;
            out[index + 1] = alphabet[val & 63];
            val >>= 6;
            out[index + 0] = alphabet[val & 63];
            i += 3;
        }

        return out;
    }

    public static byte[] encodebyte(byte[] data) {
        byte[] out = new byte[(data.length + 2) / 3 * 4];
        int i = 0;

        for(int index = 0; i < data.length; index += 4) {
            boolean quad = false;
            boolean trip = false;
            int val = 255 & data[i];
            val <<= 8;
            if(i + 1 < data.length) {
                val |= 255 & data[i + 1];
                trip = true;
            }

            val <<= 8;
            if(i + 2 < data.length) {
                val |= 255 & data[i + 2];
                quad = true;
            }

            out[index + 3] = (byte)alphabet[quad?val & 63:64];
            val >>= 6;
            out[index + 2] = (byte)alphabet[trip?val & 63:64];
            val >>= 6;
            out[index + 1] = (byte)alphabet[val & 63];
            val >>= 6;
            out[index + 0] = (byte)alphabet[val & 63];
            i += 3;
        }

        return out;
    }
}