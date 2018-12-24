package com.jme.lsgoldtrade.domain;

public class UserInfoVo {

    /**
     * "token":"xxxxxxxxxxxxxx",
     * "firmId":"xxxxxxxxxxxxxx",
     * "lastIP":"192.168.1.167",
     * "lastTime":"2014-09-17 10:00:00",
     * "forceChangePwd":1, // 2-无需更改密码 1-需要更改密码
     * "tradeName": "张三"
     */

    private String token;

    private String firmId;

    private String lastIP;

    private String lastTime;

    private boolean forceChangePwd;

    private String tradeName;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getFirmId() {
        return firmId;
    }

    public void setFirmId(String firmId) {
        this.firmId = firmId;
    }

    public String getLastIP() {
        return lastIP;
    }

    public void setLastIP(String lastIP) {
        this.lastIP = lastIP;
    }

    public String getLastTime() {
        return lastTime;
    }

    public void setLastTime(String lastTime) {
        this.lastTime = lastTime;
    }

    public boolean isForceChangePwd() {
        return forceChangePwd;
    }

    public void setForceChangePwd(boolean forceChangePwd) {
        this.forceChangePwd = forceChangePwd;
    }

    public String getTradeName() {
        return tradeName;
    }

    public void setTradeName(String tradeName) {
        this.tradeName = tradeName;
    }
}
