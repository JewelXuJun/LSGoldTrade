package com.jme.lsgoldtrade.domain;

public class AdvertisementVo {

    /**
     * id : 1141952996201193474
     * title : 关于系统升级维护的公告
     * channelId : 40.0
     * channelName : 公告
     * isTop : Y
     * createTime : 2019-06-21 14:16:25
     * txt : <p>请添加内容</p>
     * titleImg : https://tjshj.oss-cn-beijing.aliyuncs.com/20190625/15614262013546762112105902361596.jpg
     */

    private String id;
    private String title;
    private double channelId;
    private String channelName;
    private String isTop;
    private String createTime;
    private String txt;
    private String titleImg;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getChannelId() {
        return channelId;
    }

    public void setChannelId(double channelId) {
        this.channelId = channelId;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getIsTop() {
        return isTop;
    }

    public void setIsTop(String isTop) {
        this.isTop = isTop;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getTxt() {
        return txt;
    }

    public void setTxt(String txt) {
        this.txt = txt;
    }

    public String getTitleImg() {
        return titleImg;
    }

    public void setTitleImg(String titleImg) {
        this.titleImg = titleImg;
    }
}
