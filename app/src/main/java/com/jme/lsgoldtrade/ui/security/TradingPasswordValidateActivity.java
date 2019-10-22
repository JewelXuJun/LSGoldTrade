package com.jme.lsgoldtrade.ui.security;

import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseActivity;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.ActivityTradingPasswordValidateBinding;

@Route(path = Constants.ARouterUriConst.TRADINGPASSWORDVALIDATE)
public class TradingPasswordValidateActivity extends JMEBaseActivity {

    private ActivityTradingPasswordValidateBinding mBinding;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_trading_password_validate;
    }

    @Override
    protected void initView() {
        super.initView();

        initToolbar(R.string.security_trading_password_setting, true);
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

        mBinding = (ActivityTradingPasswordValidateBinding) mBindingUtil;
        mBinding.setHandlers(new ClickHandlers());
    }

    @Override
    protected void DataReturn(DTRequest request, Head head, Object response) {
        super.DataReturn(request, head, response);
    }

    public class ClickHandlers {

        public void onClickGetVerificationCode() {

        }

        public void onClickNext() {
            ARouter.getInstance().build(Constants.ARouterUriConst.TRADINGPASSWORDSETTING).navigation();
        }

    }
}
