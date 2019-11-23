package com.jme.lsgoldtrade.domain;

import java.io.Serializable;

public class PayIconVo implements Serializable {

    /**
     * {
     *  "id":"1",
     *  "name":"微信",
     *  "descript":"推荐使用微信支付",
     *  "iconpath":"https://tjs-pro.oss-cn-shanghai.aliyuncs.com/prod/syscofig/app/icon_pay_wx%403x.png",
     *  "isshow":1
     * }
     */

    private String id;

    private String name;

    private String descript;

    private String iconpath;

    private int isshow;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescript() {
        return descript;
    }

    public void setDescript(String descript) {
        this.descript = descript;
    }

    public String getIconpath() {
        return iconpath;
    }

    public void setIconpath(String iconpath) {
        this.iconpath = iconpath;
    }

    public int getIsshow() {
        return isshow;
    }

    public void setIsshow(int isshow) {
        this.isshow = isshow;
    }
}
