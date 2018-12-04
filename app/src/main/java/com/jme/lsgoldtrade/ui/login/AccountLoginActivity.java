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
import com.jme.lsgoldtrade.databinding.ActivityAccountLoginBinding;

@Route(path = Constants.ARouterUriConst.ACCOUNTLOGIN)
public class AccountLoginActivity extends JMEBaseActivity {

    private ActivityAccountLoginBinding mBinding;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_account_login;
    }

    @Override
    protected void initView() {
        super.initView();

        mBinding = (ActivityAccountLoginBinding) mBindingUtil;

        mBinding.tvLoginMobile.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
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

        public void onClickLogin() {

        }

        public void onClickLoginMobile() {
            ARouter.getInstance()
                    .build(Constants.ARouterUriConst.MOBILELOGIN)
                    .navigation();

            finish();
        }

        public void onClickRegister() {

        }

    }
}
