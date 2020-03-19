package com.jme.lsgoldtrade.domain;

import java.io.Serializable;

public class PasswordInfoVo implements Serializable {

    /**
     *  "hasTimeout": "Y",
     *  "customerNo": "0001000200004",
     *  "hasSettingDigital": "Y",
     *  "hasSettingGestures": "N",
     *  "hasSettingFingerPrint": "N",
     *  "hasOpenGestures": "N",
     *  "hasOpenFingerPrint": "N"
     */

    private String hasTimeout;

    private String customerNo;

    private String hasSettingDigital;

    private String hasSettingGestures;

    private String hasSettingFingerPrint;

    private String hasOpenGestures;

    private String hasOpenFingerPrint;

    public String getHasTimeout() {
        return hasTimeout;
    }

    public void setHasTimeout(String hasTimeout) {
        this.hasTimeout = hasTimeout;
    }

    public String getCustomerNo() {
        return customerNo;
    }

    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
    }

    public String getHasSettingDigital() {
        return hasSettingDigital;
    }

    public void setHasSettingDigital(String hasSettingDigital) {
        this.hasSettingDigital = hasSettingDigital;
    }

    public String getHasSettingGestures() {
        return hasSettingGestures;
    }

    public void setHasSettingGestures(String hasSettingGestures) {
        this.hasSettingGestures = hasSettingGestures;
    }

    public String getHasSettingFingerPrint() {
        return hasSettingFingerPrint;
    }

    public void setHasSettingFingerPrint(String hasSettingFingerPrint) {
        this.hasSettingFingerPrint = hasSettingFingerPrint;
    }

    public String getHasOpenGestures() {
        return hasOpenGestures;
    }

    public void setHasOpenGestures(String hasOpenGestures) {
        this.hasOpenGestures = hasOpenGestures;
    }

    public String getHasOpenFingerPrint() {
        return hasOpenFingerPrint;
    }

    public void setHasOpenFingerPrint(String hasOpenFingerPrint) {
        this.hasOpenFingerPrint = hasOpenFingerPrint;
    }
}
