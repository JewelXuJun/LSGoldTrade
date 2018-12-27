package com.jme.lsgoldtrade.ui.personal;

import android.os.Bundle;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseActivity;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.ActivitySettingBinding;
import com.jme.lsgoldtrade.service.UserService;

import java.util.HashMap;

@Route(path = Constants.ARouterUriConst.SETTING)
public class SettingActivity extends JMEBaseActivity {

    private ActivitySettingBinding mBinding;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initView() {
        super.initView();

        mBinding = (ActivitySettingBinding) mBindingUtil;

        initToolbar(R.string.personal_setting, true);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);

        mBinding.btnLogout.setVisibility(null == mUser || !mUser.isLogin() ? View.GONE : View.VISIBLE);
    }

    @Override
    protected void initListener() {
        super.initListener();
    }

    @Override
    protected void initBinding() {
        super.initBinding();

        mBinding.setHandlers(new ClickHandlers());
    }

    private void logout() {
        sendRequest(UserService.getInstance().logout, new HashMap<>(), true);
    }

    @Override
    protected void DataReturn(DTRequest request, Head head, Object response) {
        super.DataReturn(request, head, response);

        switch (request.getApi().getName()) {
            case "Logout":
                if (head.isSuccess()) {
                    mBinding.btnLogout.setVisibility(View.GONE);

                    mUser.logout();

                    showShortToast(R.string.setting_logout_success);
                }

                break;
        }
    }

    public class ClickHandlers {

        public void onClickRefreshSetting() {
            ARouter.getInstance()
                    .build(Constants.ARouterUriConst.MARKETREFRESH)
                    .navigation();
        }

        public void onClickClearCache() {

        }

        public void onClickWelcome() {
            ARouter.getInstance()
                    .build(Constants.ARouterUriConst.SPLASH)
                    .navigation();
        }

        public void onClickAbout() {
            ARouter.getInstance()
                    .build(Constants.ARouterUriConst.ABOUT)
                    .navigation();
        }

        public void onClickLogout() {
            if (null != mUser && mUser.isLogin())
                logout();
        }
    }
}
