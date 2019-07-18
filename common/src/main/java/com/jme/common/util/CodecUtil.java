package com.jme.common.util;

import org.apaches.commons.codec.binary.Hex;
import java.io.UnsupportedEncodingException;

/**
 * Created by zhangzhongqiang on 2015/7/28.
 */
public class CodecUtil {

    private static final Hex hex = new Hex("UTF-8");

    /**
     * hex解密
     *
     * @param key
     * @return
     * @throws Exception
     */
    public static String decryptHexString(String key) {
        try {
            byte[] bytes = hex.decode(key.getBytes("UTF-8"));
            return new String(bytes, "UTF-8");
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * hex加密
     *
     * @param key
     * @return
     * @throws Exception
     */
    public static String encryptHex(String key) {
        try {
            return Hex.encodeHexString(key.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            return "";
        }

    }

    public static void main(String[] args) throws Exception {
        String data = CodecUtil.encryptHex("adfdaf哈a+b/c-d_e!.");
        System.out.println("加密后：" + data);
        String str = CodecUtil.decryptHexString(data);
        System.out.println("解密后：" + str);
        System.out.println(CodecUtil.encryptHex("111111"));
        System.out.println(CodecUtil.decryptHexString("3432323266373534346636663431376661303933653862386235636338666631"));
    }

}
