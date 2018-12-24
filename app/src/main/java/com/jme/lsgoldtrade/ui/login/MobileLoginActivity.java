package com.jme.lsgoldtrade.ui.login;

import android.graphics.Paint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.common.ui.adapter.TextWatcherAdapter;
import com.jme.common.ui.base.JMECountDownTimer;
import com.jme.common.util.SharedPreUtils;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseActivity;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.ActivityMobileLoginBinding;
import com.jme.lsgoldtrade.domain.UserInfoVo;
import com.jme.lsgoldtrade.service.UserService;
import com.jme.lsgoldtrade.util.ValueUtils;

import java.util.HashMap;

@Route(path = Constants.ARouterUriConst.MOBILELOGIN)
public class MobileLoginActivity extends JMEBaseActivity {

    private ActivityMobileLoginBinding mBinding;

    private boolean bFlag = false;

    private TextWatcher mWatcher;
    private JMECountDownTimer mCountDownTimer;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_mobile_login;
    }

    @Override
    protected void initView() {
        super.initView();

        mBinding = (ActivityMobileLoginBinding) mBindingUtil;

        mBinding.etAccount.setText(SharedPreUtils.getString(this, SharedPreUtils.Login_Account));
        mBinding.tvLoginAccount.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);

        mCountDownTimer = new JMECountDownTimer(60000, 1000,
                mBinding.btnVerificationCode, getString(R.string.trade_get_verification_code));
    }

    @Override
    protected void initListener() {
        super.initListener();

        mWatcher = validationTextWatcher();

        mBinding.etAccount.addTextChangedListener(mWatcher);
        mBinding.etVerifyCode.addTextChangedListener(mWatcher);
    }

    @Override
    protected void initBinding() {
        super.initBinding();

        mBinding.setHandlers(new ClickHandlers());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (null != mCountDownTimer)
            mCountDownTimer.cancel();
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
        mBinding.btnLogin.setEnabled(populated(mBinding.etAccount) && populated(mBinding.etVerifyCode));
    }

    private boolean populated(final EditText editText) {
        return editText.length() > 0;
    }

    private void doLogin() {
        String account = mBinding.etAccount.getText().toString();
        String verifyCode = mBinding.etVerifyCode.getText().toString();

        if (!ValueUtils.isPhoneNumber(account))
            showShortToast(R.string.login_mobile_error);
        else if (!bFlag)
            showShortToast(R.string.login_verification_code_unget);
        else if (verifyCode.length() < 6)
            showShortToast(R.string.login_verification_code_error);
        else
            login(account, verifyCode);
    }

    private void login(String account, String verifyCode) {
        HashMap<String, String> params = new HashMap<>();
        params.put("traderid", account);
        params.put("password", ValueUtils.MD5(account + verifyCode));
        params.put("ip", null == ValueUtils.getLocalIPAddress() ? "" : ValueUtils.getLocalIPAddress());

        sendRequest(UserService.getInstance().login, params, true);
    }

    private void sendVerifyCode(String account) {
        bFlag = true;

        if (null != mCountDownTimer)
            mCountDownTimer.start();
    }

    @Override
    protected void DataReturn(DTRequest request, Head head, Object response) {
        super.DataReturn(request, head, response);

        switch (request.getApi().getName()) {
            case "Login":
                if (head.isSuccess()) {
                    UserInfoVo userInfoVo;

                    try {
                        userInfoVo = (UserInfoVo) response;
                    } catch (Exception e) {
                        userInfoVo = null;

                        e.printStackTrace();
                    }

                    if (null == userInfoVo)
                        return;

                    mUser.login(userInfoVo);

                    showShortToast(R.string.login_success);
                    SharedPreUtils.setString(this, SharedPreUtils.Login_Account, mBinding.etAccount.getText().toString());
                    finish();
                }

                break;
        }
    }

    public class ClickHandlers {

        public void onClickCancel() {
            finish();
        }

        public void onClickNews() {
            ARouter.getInstance()
                    .build(Constants.ARouterUriConst.NEWSACTIVITY)
                    .navigation();
        }

        public void onClickGetVerificationCode() {
            String account = mBinding.etAccount.getText().toString();

            if (!ValueUtils.isPhoneNumber(account))
                showShortToast(R.string.login_mobile_error);
            else
                sendVerifyCode(account);
        }

        public void onClickLogin() {
            doLogin();
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
