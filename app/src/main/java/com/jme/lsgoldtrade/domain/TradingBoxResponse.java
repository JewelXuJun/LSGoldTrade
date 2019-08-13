package com.jme.lsgoldtrade.domain;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.List;

public class TradingBoxResponse implements Serializable {

    private String code;

    private String msg;

    private List<TradingBoxOrderVo> value;

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

    public List<TradingBoxOrderVo> getValue() {
        return value;
    }

    public void setValue(List<TradingBoxOrderVo> value) {
        this.value = value;
    }

    public String getBodyToString(){
        return new Gson().toJson(value);
    }
}
