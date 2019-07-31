package com.jme.lsgoldtrade.ui.personal;

import android.os.Bundle;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseActivity;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.ActivityCheckUserInfoBinding;

/**
 * Created by Android Studio.
 * Author : zhangzhongqiang
 * Email  : betterzhang.dev@gmail.com
 * Time   : 2019/07/31 16:57
 * Desc   : 信息验证页面
 */
@Route(path = Constants.ARouterUriConst.CHECKUSERINFO)
public class CheckUserInfoActivity extends JMEBaseActivity {

    private ActivityCheckUserInfoBinding mBinding;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_check_user_info;
    }

    @Override
    protected void initView() {
        super.initView();
        initToolbar("信息验证", true);
        mBinding = (ActivityCheckUserInfoBinding) mBindingUtil;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
    }

    @Override
    protected void initBinding() {
        super.initBinding();
        mBinding.setHandlers(new ClickHandlers());
    }

    public class ClickHandlers {

        public void onClickSendSmsCode() {

        }

        public void onClickVerifyCode() {

        }

    }
}
