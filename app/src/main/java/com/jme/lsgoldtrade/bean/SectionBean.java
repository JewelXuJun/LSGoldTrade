package com.jme.lsgoldtrade.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2017/11/24.
 */

public class SectionBean {

    /**
     * newsid : 1120
     * tid : 0
     * title : 欢聚时代珠海办公楼 | 艾迪尔IDEAL
     * preview : http://small.justeasy.cn/edu/201705/20170515122216_59192cf83b03e.jpg
     * author :
     * addtime : 1496301243
     * style : 3
     * avatar :
     */

    @SerializedName("chance")
    private String chance;
    @SerializedName("direction")
    private String direction;
    @SerializedName("pushTime")
    private String pushTime;
    @SerializedName("tradeId")
    private String tradeId;
    @SerializedName("variety")
    private String variety;

    public SectionBean(String chance, String direction, String pushTime, String tradeId, String variety) {
        this.chance = chance;
        this.direction = direction;
        this.pushTime = pushTime;
        this.tradeId = tradeId;
        this.variety = variety;
    }

    public String getChance() {
        return chance;
    }

    public void setChance(String chance) {
        this.chance = chance;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getPushTime() {
        return pushTime;
    }

    public void setPushTime(String pushTime) {
        this.pushTime = pushTime;
    }

    public String getTradeId() {
        return tradeId;
    }

    public void setTradeId(String tradeId) {
        this.tradeId = tradeId;
    }

    public String getVariety() {
        return variety;
    }

    public void setVariety(String variety) {
        this.variety = variety;
    }
}
