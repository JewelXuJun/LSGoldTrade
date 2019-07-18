package com.jme.lsgoldtrade.domain;

public class UpdateInfoVo {

    /**
     * id : 2
     * name : v1.0.1
     * downloadUrl : http://www.taijs.com
     * force : 2
     * updateContent : 更新内容
     */

    private String id;
    private String name;
    private String downloadUrl;
    private String force;
    private String updateContent;

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

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getForce() {
        return force;
    }

    public void setForce(String force) {
        this.force = force;
    }

    public String getUpdateContent() {
        return updateContent;
    }

    public void setUpdateContent(String updateContent) {
        this.updateContent = updateContent;
    }
}
