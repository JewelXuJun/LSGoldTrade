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

    public void setmAccount(String account) {
        this.mAccount = account;
    }

    public String getAccount() {
        return mAccount;
    }

    public void setmAccountID(String accountID) {
        this.mAccountID = accountID;
    }

    public String getAccountID() {
        return mAccountID;
    }

}
