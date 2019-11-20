package com.jme.lsgoldtrade.domain;

public class UserInfoVo {

    /**
     * "token":"xxxxxxxxxxxxxx",
     * "firmId":"xxxxxxxxxxxxxx",
     * "lastIP":"192.168.1.167",
     * "lastTime":"2014-09-17 10:00:00",
     * "forceChangePwd":1, // 2-无需更改密码 1-需要更改密码
     * "tradeName": "张三"
     * "mobile": "138****5915"
     * "accountId": 2,
     * "account": "131****930"
     * "cardType": “2”,
     * "reserveFlag": “Y”
     * "isSign":null
     */

    private String traderId;

    private String token;

    private String lastIP;

    private String lastLoginTime;

    private int forceChangePwd;

    private String tradeName;

    private String mobile;

    private String accountId;

    private String account;

    private String cardType;

    private String reserveFlag;

    private String isSign;

    public String getTraderId() {
        return traderId;
    }

    public void setTraderId(String traderId) {
        this.traderId = traderId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getLastIP() {
        return lastIP;
    }

    public void setLastIP(String lastIP) {
        this.lastIP = lastIP;
    }

    public String getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(String lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
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

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getReserveFlag() {
        return reserveFlag;
    }

    public void setReserveFlag(String reserveFlag) {
        this.reserveFlag = reserveFlag;
    }

    public String getIsSign() {
        return isSign;
    }

    public void setIsSign(String isSign) {
        this.isSign = isSign;
    }
}
