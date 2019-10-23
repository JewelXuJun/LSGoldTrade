package com.jme.lsgoldtrade.ui.security;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.jme.common.ui.view.VerificationCodeView;
import com.jme.common.util.RxBus;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseActivity;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.ActivityTradingPasswordSettingBinding;

import rx.Subscription;

@Route(path = Constants.ARouterUriConst.TRADINGPASSWORDSETTING)
public class TradingPasswordSettingActivity extends JMEBaseActivity {

    private ActivityTradingPasswordSettingBinding mBinding;

    private TextView tvClose;

    private Subscription mRxbus;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_trading_password_setting;
    }

    @Override
    protected void initView() {
        super.initView();

        initToolbar(R.string.security_trading_password_setting, false);

        tvClose = findViewById(R.id.tv_close);
        tvClose.setText(R.string.text_cancel);
        tvClose.setTextColor(ContextCompat.getColor(this, R.color.color_blue_deep));
        tvClose.setVisibility(View.VISIBLE);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
    }

    @Override
    protected void initListener() {
        super.initListener();

        initRxBus();

        tvClose.setOnClickListener((view) -> finish());

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

    private void initRxBus() {
        mRxbus = RxBus.getInstance().toObserverable(RxBus.Message.class).subscribe(message -> {
            String callType = message.getObject().toString();

            if (TextUtils.isEmpty(callType))
                return;

            switch (callType) {
                case Constants.RxBusConst.RXBUS_TRADE:
                case Constants.RxBusConst.RXBUS_TRADING_PASSWORD_SETTING_SUCCESS:
                    finish();

                    break;
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (!mRxbus.isUnsubscribed())
            mRxbus.unsubscribe();
    }

}
