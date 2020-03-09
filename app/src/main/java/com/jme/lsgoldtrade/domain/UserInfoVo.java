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

    private String isOpen;

    private String bankId;       // 代理银行编号

    private String openStatus;   //"N"-未开户，"O"-已开户，"B"-已绑定

    private String noMaskMoble;  // 无掩码手机号

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

    public String getIsOpen() {
        return isOpen;
    }

    public void setIsOpen(String isOpen) {
        this.isOpen = isOpen;
    }

    public String getBankId() {
        return bankId;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    public String getOpenStatus() {
        return openStatus;
    }

    public void setOpenStatus(String openStatus) {
        this.openStatus = openStatus;
    }

    public String getNoMaskMoble() {
        return noMaskMoble;
    }

    public void setNoMaskMoble(String noMaskMoble) {
        this.noMaskMoble = noMaskMoble;
    }
}
