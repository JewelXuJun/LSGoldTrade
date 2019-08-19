package com.jme.lsgoldtrade.domain;

import java.io.Serializable;

public class ProtocolVo implements Serializable {

    /**
     * {
     * "rowindex":0,
     * "protocolType":"泰金所用户使用协议",
     * "code":"YH",
     * "protocolVersion":null,
     * "createTime":null,
     * "updateTime":null,
     * "submitter":null,
     * "submitPeopleId":null,
     * "zprotocolTypeList":{
     *      "protocolType":"泰金所用户使用协议",
     *      "code":"YH",
     *      "protocolVersion":"YH20190814",
     *      "protocolUrl":"http://www.taijs.com/upload/yhxy.htm"
     *      }
     * }
     */

    private int rowindex;

    private String protocolType;

    private String code;

    private String protocolVersion;

    private String createTime;

    private String updateTime;

    private String submitter;

    private String submitPeopleId;

    private ZprotocolTypeBean zprotocolTypeList;

    public int getRowindex() {
        return rowindex;
    }

    public void setRowindex(int rowindex) {
        this.rowindex = rowindex;
    }

    public String getProtocolType() {
        return protocolType;
    }

    public void setProtocolType(String protocolType) {
        this.protocolType = protocolType;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getProtocolVersion() {
        return protocolVersion;
    }

    public void setProtocolVersion(String protocolVersion) {
        this.protocolVersion = protocolVersion;
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

    public String getSubmitter() {
        return submitter;
    }

    public void setSubmitter(String submitter) {
        this.submitter = submitter;
    }

    public String getSubmitPeopleId() {
        return submitPeopleId;
    }

    public void setSubmitPeopleId(String submitPeopleId) {
        this.submitPeopleId = submitPeopleId;
    }

    public ZprotocolTypeBean getZprotocolTypeList() {
        return zprotocolTypeList;
    }

    public void setZprotocolTypeList(ZprotocolTypeBean zprotocolTypeList) {
        this.zprotocolTypeList = zprotocolTypeList;
    }

    public static class ZprotocolTypeBean implements Serializable {

        private String protocolType;

        private String code;

        private String protocolVersion;

        private String protocolUrl;

        public String getProtocolType() {
            return protocolType;
        }

        public void setProtocolType(String protocolType) {
            this.protocolType = protocolType;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getProtocolVersion() {
            return protocolVersion;
        }

        public void setProtocolVersion(String protocolVersion) {
            this.protocolVersion = protocolVersion;
        }

        public String getProtocolUrl() {
            return protocolUrl;
        }

        public void setProtocolUrl(String protocolUrl) {
            this.protocolUrl = protocolUrl;
        }
    }

}
