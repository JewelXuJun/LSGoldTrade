package com.jme.lsgoldtrade.util;

public class NormalUtils {

    /**
     * 身份证号校验
     *
     * @param idCard
     * @return
     */
    public static boolean isIdCardNum(String idCard) {
        String reg = "^\\d{15}$|^\\d{17}[0-9Xx]$";
        if (!idCard.matches(reg)) {
            System.out.println("Format Error!");
            return false;
        }
        return true;
    }

}
