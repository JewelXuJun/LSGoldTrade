package com.jme.lsgoldtrade.ui.security;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.common.ui.adapter.TextWatcherAdapter;
import com.jme.common.ui.base.JMECountDownTimer;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseActivity;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.ActivityTradingPasswordValidateBinding;
import com.jme.lsgoldtrade.domain.UserInfoVo;
import com.jme.lsgoldtrade.service.ManagementService;
import com.jme.lsgoldtrade.util.AESUtil;

import java.util.HashMap;

@Route(path = Constants.ARouterUriConst.TRADINGPASSWORDVALIDATE)
public class TradingPasswordValidateActivity extends JMEBaseActivity {

    private ActivityTradingPasswordValidateBinding mBinding;

    private boolean bFlag = false;

    private JMECountDownTimer mCountDownTimer;
    private TextWatcher mWatcher;

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

        mCountDownTimer = new JMECountDownTimer(60000, 1000,
                mBinding.btnVerificationCode, getString(R.string.trade_get_verification_code));

        if (null != mUser) {
            UserInfoVo userInfoVo = mUser.getCurrentUser();

            mBinding.tvMobileNumber.setText(null == userInfoVo ? "" : userInfoVo.getMobile());
        }
    }

    @Override
    protected void initListener() {
        super.initListener();

        mWatcher = validationTextWatcher();

        mBinding.etLoginPassword.addTextChangedListener(mWatcher);
        mBinding.etVerificationCode.addTextChangedListener(mWatcher);
    }

    @Override
    protected void initBinding() {
        super.initBinding();

        mBinding = (ActivityTradingPasswordValidateBinding) mBindingUtil;
        mBinding.setHandlers(new ClickHandlers());
    }

    private TextWatcher validationTextWatcher() {

        return new TextWatcherAdapter() {
            @Override
            public void afterTextChanged(final Editable s) {
                updateUIWithValidation();
            }
        };

    }

    private void updateUIWithValidation() {
        mBinding.btnNext.setEnabled(populated(mBinding.etLoginPassword) && populated(mBinding.etVerificationCode));
    }

    private boolean populated(final EditText editText) {
        return editText.length() > 0;
    }

    private void sendMessage() {
        bFlag = true;

        sendRequest(ManagementService.getInstance().sendMessage, new HashMap<>(), true);
    }

    private void validateLoginPassword(String password, String verificationCode) {
        HashMap<String, String> params = new HashMap<>();
        params.put("password", AESUtil.encryptString2Base64(password, "0J4S9B5C0J4S9B5C", "16-Bytes--String").trim());
        params.put("smsCode", verificationCode);

        sendRequest(ManagementService.getInstance().validateLoginPassword, params, true);
    }

    @Override
    protected void DataReturn(DTRequest request, Head head, Object response) {
        super.DataReturn(request, head, response);

        switch (request.getApi().getName()) {
            case "SendMessage":
                if (head.isSuccess()) {
                    showShortToast(R.string.login_verification_code_success);

                    if (null != mCountDownTimer)
                        mCountDownTimer.start();
                }

                break;
            case "ValidateLoginPassword":
                if (head.isSuccess())
                    ARouter.getInstance().build(Constants.ARouterUriConst.TRADINGPASSWORDSETTING).navigation();

                break;
        }
    }

    public class ClickHandlers {

        public void onClickGetVerificationCode() {
            if (TextUtils.isEmpty(mBinding.tvMobileNumber.getText().toString()))
                showShortToast(R.string.trade_mobile_error);
            else
                sendMessage();
        }

        public void onClickNext() {
            String password = mBinding.etLoginPassword.getText().toString();
            String mobile = mBinding.tvMobileNumber.getText().toString();
            String verificationCode = mBinding.etVerificationCode.getText().toString();

            if (TextUtils.isEmpty(mobile))
                showShortToast(R.string.trade_mobile_error);
            else if (!bFlag)
                showShortToast(R.string.login_verification_code_unget);
            else if (verificationCode.length() < 6)
                showShortToast(R.string.login_verification_code_error);
            else
                validateLoginPassword(password, verificationCode);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (null != mCountDownTimer)
            mCountDownTimer.cancel();
    }
}
