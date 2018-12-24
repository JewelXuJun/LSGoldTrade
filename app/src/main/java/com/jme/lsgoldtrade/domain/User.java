package com.jme.lsgoldtrade.domain;

/**
 * Created by XuJun on 2018/11/8.
 */

public class User {

    private static User mUser;

    private UserInfoVo mUserInfoVo;

    private String mToken = "";

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
    }

    public void logout() {
        mUserInfoVo = null;
        mToken = "";
    }

    public boolean isLogin() {
        return mUserInfoVo != null;
    }

    public UserInfoVo getCurrentUser() {
        return mUserInfoVo;
    }

    public String getToken() {
        return mToken;
    }
}
