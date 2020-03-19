package com.jme.lsgoldtrade.domain;

import com.google.gson.Gson;

import java.io.Serializable;

public class ConditionSheetResponse implements Serializable {

    private String code;

    private String msg;

    private ConditionPageVo value;

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

    public ConditionPageVo getValue() {
        return value;
    }

    public void setValue(ConditionPageVo value) {
        this.value = value;
    }

    public String getBodyToString(){
        return new Gson().toJson(value);
    }
}
