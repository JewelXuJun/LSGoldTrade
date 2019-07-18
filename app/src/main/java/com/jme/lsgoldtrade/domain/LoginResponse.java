package com.jme.lsgoldtrade.domain;

import com.google.gson.Gson;

import java.io.Serializable;

public class LoginResponse implements Serializable {

    private String code;

    private String msg;

    private UserInfoVo value;

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

    public UserInfoVo getValue() {
        return value;
    }

    public void setValue(UserInfoVo value) {
        this.value = value;
    }

    public String getBodyToString(){
        return new Gson().toJson(value);
    }
}
