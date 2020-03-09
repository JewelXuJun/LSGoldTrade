package com.jme.lsgoldtrade.domain;

import java.io.Serializable;

public class HFBBindVo implements Serializable {

    private String accountId;

    private String account;

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
}
