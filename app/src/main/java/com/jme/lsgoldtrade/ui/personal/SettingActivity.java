package com.jme.lsgoldtrade.ui.personal;

import android.os.Bundle;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.common.util.FileUtil;
import com.jme.common.util.RxBus;
import com.jme.common.util.SharedPreUtils;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseActivity;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.ActivitySettingBinding;
import com.jme.lsgoldtrade.service.UserService;

import java.util.HashMap;

/**
 * 设置
 */
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

        initToolbar(R.string.personal_setting, true);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);

        setCache();

        mBinding.btnLogout.setVisibility(null == mUser || !mUser.isLogin() ? View.GONE : View.VISIBLE);
    }

    @Override
    protected void initListener() {
        super.initListener();
    }

    @Override
    protected void initBinding() {
        super.initBinding();

        mBinding = (ActivitySettingBinding) mBindingUtil;
        mBinding.setHandlers(new ClickHandlers());
    }

    private void setCache() {
        String cache;

        try {
            cache = FileUtil.getTotalCacheSize(getApplicationContext());
        } catch (Exception e) {
            cache = null;

            e.printStackTrace();
        }

        mBinding.tvCache.setText(null != cache ? cache : "");
    }

    private void setLogoutLayout() {
        mUser.logout();

        SharedPreUtils.setString(this, SharedPreUtils.Token, "");

        RxBus.getInstance().post(Constants.RxBusConst.RXBUS_LOGOUT_SUCCESS, null);

        dismissLoginDialog();

        finish();
    }

    private void logout() {
        sendRequest(UserService.getInstance().logout, new HashMap<>(), true);
    }

    @Override
    protected void DataReturn(DTRequest request, Head head, Object response) {
        super.DataReturn(request, head, response);

        switch (request.getApi().getName()) {
            case "Logout":
                if (head.isSuccess())
                    setLogoutLayout();

                break;
        }
    }

    public class ClickHandlers {

        public void onClickRefreshSetting() {
            ARouter.getInstance().build(Constants.ARouterUriConst.MARKETREFRESH).navigation();
        }

        public void onClickClearCache() {
            if (FileUtil.cleanApplicationData(getApplicationContext()))
                showShortToast(R.string.personal_clear_cache_success);
            else
                showShortToast(R.string.personal_clear_cache_fail);

            setCache();
        }

        public void onClickLogout() {
            if (null == mUser) {
                setLogoutLayout();
            } else {
                if (mUser.isLogin())
                    logout();
                else
                    setLogoutLayout();
            }
        }
    }
}
