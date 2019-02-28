package com.jme.lsgoldtrade.domain;

import java.io.Serializable;

public class ChannelVo implements Serializable {

    /**
     * "code": "string",
     * "createTime": "2019-02-27T08:10:52.404Z",
     * "id": "string",
     * "isShow": "string",
     * "name": "string",
     * "siteId": 0,
     * "type": "string",
     * "updateTime": "2019-02-27T08:10:52.404Z",
     * "weight": 0
     */

    private String code;

    private String createTime;

    private long id;

    private String isShow;

    private String name;

    private long siteId;

    private String type;

    private String updateTime;

    private long weight;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getIsShow() {
        return isShow;
    }

    public void setIsShow(String isShow) {
        this.isShow = isShow;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getSiteId() {
        return siteId;
    }

    public void setSiteId(long siteId) {
        this.siteId = siteId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public long getWeight() {
        return weight;
    }

    public void setWeight(long weight) {
        this.weight = weight;
    }
}
