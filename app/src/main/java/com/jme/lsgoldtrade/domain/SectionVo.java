package com.jme.lsgoldtrade.domain;

import java.io.Serializable;

public class SectionVo implements Serializable {
    /**
     *  "contractId": "au9999",
     *  "sectionId": 1,
     *  "startTime": "2018-12-18 19:50:00",
     *  "endTime": "2018-12-18 20:00:00",
     *  "sectionType": 1
     */

    private String contractId;

    private int sectionId;

    private String startTime;

    private String endTime;

    private int sectionType;

    public String getContractId() {
        return contractId;
    }

    public void setContractId(String contractId) {
        this.contractId = contractId;
    }

    public int getSectionId() {
        return sectionId;
    }

    public void setSectionId(int sectionId) {
        this.sectionId = sectionId;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getSectionType() {
        return sectionType;
    }

    public void setSectionType(int sectionType) {
        this.sectionType = sectionType;
    }
}
