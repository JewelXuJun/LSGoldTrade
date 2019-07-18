package com.jme.lsgoldtrade.bean;

public class UserDetailsVo {

    /**
     * type : 人工入金
     * status : 已完成
     * amount : 100.0
     * createTime : 2019-07-16T02:31:48.000+0000
     */

    private String type;
    private String status;
    private String amount;
    private String createTime;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
