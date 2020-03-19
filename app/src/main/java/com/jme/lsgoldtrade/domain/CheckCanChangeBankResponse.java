package com.jme.lsgoldtrade.domain;

import java.io.Serializable;

public class CheckCanChangeBankResponse implements Serializable {
    private String code;

    private String msg;

    private String value;


    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
