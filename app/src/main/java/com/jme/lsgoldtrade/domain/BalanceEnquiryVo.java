package com.jme.lsgoldtrade.domain;

import com.jme.lsgoldtrade.util.MarketUtil;

import java.io.Serializable;

public class BalanceEnquiryVo implements Serializable {

    /**
     * "returnCode": "0",
     * "returnMsg": "",
     * "accountBalance": "1000",
     * "holdBalance": "0",
     * "mediumStatus": "1",
     * "userName": "丁绍成",
     * "userId": "0001000200003",
     * "electronicAccounts": "6214764301600690498",
     * "relevanceId": "6212264301011846589",
     * "bankId": "6212264301011846589",
     * "mobile": "18066072352"
     */

    private String returnCode;

    private String returnMsg;

    private String accountBalance;

    private String holdBalance;

    private String mediumStatus;

    private String userName;

    private String userId;

    private String electronicAccounts;

    private String relevanceId;

    private String bankId;

    private String mobile;

    public String getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(String returnCode) {
        this.returnCode = returnCode;
    }

    public String getReturnMsg() {
        return returnMsg;
    }

    public void setReturnMsg(String returnMsg) {
        this.returnMsg = returnMsg;
    }

    public String getAccountBalance() {
        return MarketUtil.getPriceValue(accountBalance);
    }

    public void setAccountBalance(String accountBalance) {
        this.accountBalance = accountBalance;
    }

    public String getHoldBalance() {
        return holdBalance;
    }

    public void setHoldBalance(String holdBalance) {
        this.holdBalance = holdBalance;
    }

    public String getMediumStatus() {
        return mediumStatus;
    }

    public void setMediumStatus(String mediumStatus) {
        this.mediumStatus = mediumStatus;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getElectronicAccounts() {
        return electronicAccounts;
    }

    public void setElectronicAccounts(String electronicAccounts) {
        this.electronicAccounts = electronicAccounts;
    }

    public String getRelevanceId() {
        return relevanceId;
    }

    public void setRelevanceId(String relevanceId) {
        this.relevanceId = relevanceId;
    }

    public String getBankId() {
        return bankId;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
