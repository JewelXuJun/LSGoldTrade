package com.jme.lsgoldtrade.ui.security;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.common.ui.view.VerificationCodeView;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseActivity;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.ActivityTradingPasswordSettingConfirmBinding;
import com.jme.lsgoldtrade.view.ConfirmSimplePopupwindow;

@Route(path = Constants.ARouterUriConst.TRADINGPASSWORDSETTINGCONFIRM)
public class TradingPasswordSettingConfirmActivity extends JMEBaseActivity {

    private ActivityTradingPasswordSettingConfirmBinding mBinding;

    private ConfirmSimplePopupwindow mConfirmSimplePopupwindow;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_trading_password_setting_confirm;
    }

    @Override
    protected void initView() {
        super.initView();

        initToolbar(R.string.security_trading_password_setting, true);

        mConfirmSimplePopupwindow = new ConfirmSimplePopupwindow(this);
        mConfirmSimplePopupwindow.setOutsideTouchable(true);
        mConfirmSimplePopupwindow.setFocusable(true);
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

                if (!TextUtils.isEmpty(content) && content.length() == 6 && null != mConfirmSimplePopupwindow && !mConfirmSimplePopupwindow.isShowing()) {
                    mConfirmSimplePopupwindow.setData(getResources().getString(R.string.security_password_setting_success),
                            (view) -> mConfirmSimplePopupwindow.dismiss());
                    mConfirmSimplePopupwindow.showAtLocation(mBinding.verificationCodeView, Gravity.CENTER, 0, 0);
                }
            }

            @Override
            public void deleteContent() {

            }
        });
    }

    @Override
    protected void initBinding() {
        super.initBinding();

        mBinding = (ActivityTradingPasswordSettingConfirmBinding) mBindingUtil;
    }

    @Override
    protected void DataReturn(DTRequest request, Head head, Object response) {
        super.DataReturn(request, head, response);
    }

}
