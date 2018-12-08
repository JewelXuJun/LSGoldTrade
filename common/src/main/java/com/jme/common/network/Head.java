package com.jme.common.network;

import java.io.Serializable;

/**
 * 响应数据头
 * Created by zhangzhongqiang on 2015/7/29.
 */
public class Head implements Serializable {

    private String code;

    private String msg;

    private boolean isSuccess;

    public boolean isSuccess() {
        return code.equals("0");
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

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
}
