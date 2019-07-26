package com.jme.lsgoldtrade.domain;

public class WarnVo {


    /**
     * id : 97
     * contractId : Ag(T+D)
     * customerNo : 000100020026
     * priceCeiling : 390000
     * priceFloor : null
     * cycle : 1
     * warnBegin : 2019-07-26 14:53:56
     * warnEnd : 2019-07-27 14:53:56
     * ceilingFlag : 1
     * floorFlag : 0
     * status : 1
     * createTime : 2019-07-26 14:53:56
     * updateTime : 2019-07-26 14:53:56
     */

    private int id;
    private String contractId;
    private String customerNo;
    private long priceCeiling;
    private long priceFloor;
    private String cycle;
    private String warnBegin;
    private String warnEnd;
    private String ceilingFlag;
    private String floorFlag;
    private String status;
    private String createTime;
    private String updateTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContractId() {
        return contractId;
    }

    public void setContractId(String contractId) {
        this.contractId = contractId;
    }

    public String getCustomerNo() {
        return customerNo;
    }

    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
    }

    public long getPriceCeiling() {
        return priceCeiling;
    }

    public void setPriceCeiling(long priceCeiling) {
        this.priceCeiling = priceCeiling;
    }

    public long getPriceFloor() {
        return priceFloor;
    }

    public void setPriceFloor(long priceFloor) {
        this.priceFloor = priceFloor;
    }

    public String getCycle() {
        return cycle;
    }

    public void setCycle(String cycle) {
        this.cycle = cycle;
    }

    public String getWarnBegin() {
        return warnBegin;
    }

    public void setWarnBegin(String warnBegin) {
        this.warnBegin = warnBegin;
    }

    public String getWarnEnd() {
        return warnEnd;
    }

    public void setWarnEnd(String warnEnd) {
        this.warnEnd = warnEnd;
    }

    public String getCeilingFlag() {
        return ceilingFlag;
    }

    public void setCeilingFlag(String ceilingFlag) {
        this.ceilingFlag = ceilingFlag;
    }

    public String getFloorFlag() {
        return floorFlag;
    }

    public void setFloorFlag(String floorFlag) {
        this.floorFlag = floorFlag;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}
