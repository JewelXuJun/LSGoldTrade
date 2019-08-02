package com.jme.lsgoldtrade.config;

import com.jme.lsgoldtrade.domain.UserInfoVo;

/**
 * Created by XuJun on 2018/11/8.
 */

public class User {

    private static User mUser;

    private UserInfoVo mUserInfoVo;

    private String mToken = "";
    private String mAccount = "";
    private String mAccountID = "";
    private String mTraderId = "";

    public static User getInstance() {
        if (null == mUser)
            mUser = new User();

        return mUser;
    }

    public void login(UserInfoVo userInfoVo) {
        if (null == userInfoVo)
            return;

        mUserInfoVo = userInfoVo;

        mToken = userInfoVo.getToken();
        mAccount = userInfoVo.getAccount();
        mAccountID = userInfoVo.getAccountId();
        mTraderId = userInfoVo.getTraderId();
    }

    public void logout() {
        mUserInfoVo = null;
        mToken = "";
    }

    public boolean isLogin() {
        return null != mUserInfoVo;
    }

    public UserInfoVo getCurrentUser() {
        return mUserInfoVo;
    }

    public String getToken() {
        return mToken;
    }

    public String getAccount() {
        return mAccount;
    }

    public String getAccountID() {
        return mAccountID;
    }

    public String getTraderId() {
        return mTraderId;
    }

    public void setAccountID(String accountID) {
        mAccountID = accountID;
    }

    public void setAccount(String account) {
        mAccount = account;
    }

    public void setTraderId(String traderId) {
        mTraderId = traderId;
    }
}
