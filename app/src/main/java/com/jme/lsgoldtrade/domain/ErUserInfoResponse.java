package com.jme.lsgoldtrade.domain;

import com.google.gson.Gson;

import java.io.Serializable;

public class ErUserInfoResponse implements Serializable {

    private String code;

    private String msg;

    private ErUserInfoBean erUserInfo;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public ErUserInfoBean getErUserInfo() {
        return erUserInfo;
    }

    public void setErUserInfo(ErUserInfoBean erUserInfo) {
        this.erUserInfo = erUserInfo;
    }

    public static class ErUserInfoBean implements Serializable {

        private String ectronicAccounts;

        private String evanceId;

        public String getEctronicAccounts() {
            return ectronicAccounts;
        }

        public void setEctronicAccounts(String ectronicAccounts) {
            this.ectronicAccounts = ectronicAccounts;
        }

        public String getEvanceId() {
            return evanceId;
        }

        public void setEvanceId(String evanceId) {
            this.evanceId = evanceId;
        }
    }

    public String getBodyToString(){
        return new Gson().toJson(erUserInfo);
    }
}
