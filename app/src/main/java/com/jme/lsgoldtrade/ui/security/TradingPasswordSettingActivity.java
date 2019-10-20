package com.jme.lsgoldtrade.ui.security;

import android.os.Bundle;
import android.text.TextUtils;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.common.ui.view.VerificationCodeView;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseActivity;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.ActivityTradingPasswordSettingBinding;

@Route(path = Constants.ARouterUriConst.TRADINGPASSWORDSETTING)
public class TradingPasswordSettingActivity extends JMEBaseActivity {

    private ActivityTradingPasswordSettingBinding mBinding;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_trading_password_setting;
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

        mBinding.verificationCodeView.setInputCompleteListener(new VerificationCodeView.InputCompleteListener() {
            @Override
            public void inputComplete() {
                String content = mBinding.verificationCodeView.getInputContent();

                if (!TextUtils.isEmpty(content) && content.length() == 6)
                    ARouter.getInstance()
                            .build(Constants.ARouterUriConst.TRADINGPASSWORDSETTINGCONFIRM)
                            .withString("TradingPassword", content)
                            .navigation();
            }

            @Override
            public void deleteContent() {

            }
        });
    }

    @Override
    protected void initBinding() {
        super.initBinding();

        mBinding = (ActivityTradingPasswordSettingBinding) mBindingUtil;
    }

    @Override
    protected void DataReturn(DTRequest request, Head head, Object response) {
        super.DataReturn(request, head, response);
    }

}
