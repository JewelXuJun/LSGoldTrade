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

    private String traderId;

    private String token;

    private String account;

    private String lastIP;

    private int forceChangePwd;

    private String tradeName;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTraderId() {
        return traderId;
    }

    public void setTraderId(String traderId) {
        this.traderId = traderId;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getLastIP() {
        return lastIP;
    }

    public void setLastIP(String lastIP) {
        this.lastIP = lastIP;
    }

    public int getForceChangePwd() {
        return forceChangePwd;
    }

    public void setForceChangePwd(int forceChangePwd) {
        this.forceChangePwd = forceChangePwd;
    }

    public String getTradeName() {
        return tradeName;
    }

    public void setTradeName(String tradeName) {
        this.tradeName = tradeName;
    }
}
