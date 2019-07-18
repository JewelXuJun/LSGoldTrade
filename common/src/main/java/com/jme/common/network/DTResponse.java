package com.jme.common.network;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by zhangzhongqiang on 2015/7/29.
 */
public class DTResponse<V> implements Serializable {

    private String code;

    private String msg;

    private V value;

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

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }

    public String getBodyToString(){
        return new Gson().toJson(value);
    }
}
