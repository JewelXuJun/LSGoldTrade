package com.jme.lsgoldtrade.ui.login;

import android.graphics.Paint;
import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseActivity;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.ActivityMobileLoginBinding;

@Route(path = Constants.ARouterUriConst.MOBILELOGIN)
public class MobileLoginActivity extends JMEBaseActivity {

    private ActivityMobileLoginBinding mBinding;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_mobile_login;
    }

    @Override
    protected void initView() {
        super.initView();

        mBinding = (ActivityMobileLoginBinding) mBindingUtil;

        mBinding.tvLoginAccount.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
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

    @Override
    protected void DataReturn(DTRequest request, Head head, Object response) {
        super.DataReturn(request, head, response);
    }

    public class ClickHandlers {

        public void onClickCancel() {
            finish();
        }

        public void onClickNews() {

        }

        public void onClickGetVerificationCode() {

        }

        public void onClickLogin() {

        }

        public void onClickLgoinAccount() {
            ARouter.getInstance()
                    .build(Constants.ARouterUriConst.ACCOUNTLOGIN)
                    .navigation();

            finish();
        }

        public void onClickRegister() {

        }

    }
}
