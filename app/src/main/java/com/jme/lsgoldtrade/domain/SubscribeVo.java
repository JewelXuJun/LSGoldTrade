package com.jme.lsgoldtrade.domain;

public class SubscribeVo {

    /**
     * createTime : 2019-05-22T00:50:09.505Z
     * customerNo : string
     * id : string
     * serviceId : 0
     * status : string
     * subscribeTime : string
     * updateTime : 2019-05-22T00:50:09.505Z
     */

    private String createTime;
    private String customerNo;
    private String id;
    private int serviceId;
    private String status;
    private String subscribeTime;
    private String updateTime;

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCustomerNo() {
        return customerNo;
    }

    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSubscribeTime() {
        return subscribeTime;
    }

    public void setSubscribeTime(String subscribeTime) {
        this.subscribeTime = subscribeTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}
