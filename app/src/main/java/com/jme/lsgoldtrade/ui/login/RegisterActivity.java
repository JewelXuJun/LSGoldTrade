package com.jme.lsgoldtrade.ui.login;

import android.os.Bundle;
import android.text.TextUtils;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.igexin.sdk.PushManager;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.common.ui.base.JMECountDownTimer;
import com.jme.common.util.SharedPreUtils;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseActivity;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.config.User;
import com.jme.lsgoldtrade.databinding.ActivityRegisterBinding;
import com.jme.lsgoldtrade.domain.UserInfoVo;
import com.jme.lsgoldtrade.service.TradeService;
import com.jme.lsgoldtrade.util.ValueUtils;

import java.util.HashMap;

/**
 * 注册
 */
@Route(path = Constants.ARouterUriConst.REGISTER)
public class RegisterActivity extends JMEBaseActivity {

    private ActivityRegisterBinding mBinding;

    private JMECountDownTimer mCountDownTimer;

    private boolean bFlag = false;
    private boolean bAgreeFlag = false;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_register;
    }

    @Override
    protected void initView() {
        super.initView();

        initToolbar(R.string.register, true);

        mBinding = (ActivityRegisterBinding) mBindingUtil;
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

        mBinding.checkboxAgree.setOnCheckedChangeListener((buttonView, isChecked) -> bAgreeFlag = isChecked);
    }

    @Override
    protected void initBinding() {
        super.initBinding();

        mBinding.setHandlers(new ClickHandlers());
    }

    private void doRegister() {
        String mobile = mBinding.etMobile.getText().toString().trim();
        String smsCode = mBinding.etVerifyCode.getText().toString().trim();

        if (!ValueUtils.isPhoneNumber(mobile))
            showShortToast(R.string.login_mobile_error);
        else if (!bFlag)
            showShortToast(R.string.login_verification_code_unget);
        else if (TextUtils.isEmpty(smsCode))
            showShortToast(R.string.login_verification_code_error);
        else if (smsCode.length() < 6)
            showShortToast(R.string.login_verification_code_error);
        else if (!bAgreeFlag)
            showShortToast(R.string.register_aggrement_unread);
        else
            register(mobile, smsCode);
    }

    private void registerMsg(String mobile) {
        bFlag = true;

        HashMap<String, String> params = new HashMap<>();
        params.put("mobile", mobile);

        sendRequest(TradeService.getInstance().registerMsg, params, true);
    }

    private void register(String mobile, String smsCode) {
        String referralCode = mBinding.etReferralCode.getText().toString().trim();

        HashMap<String, String> params = new HashMap<>();
        params.put("mobile", mobile);
        params.put("smsCode", smsCode);
        if (!TextUtils.isEmpty(referralCode))
            params.put("brokerageNo", referralCode);

        sendRequest(TradeService.getInstance().registerLogin, params, true);
    }

    @Override
    protected void DataReturn(DTRequest request, Head head, Object response) {
        super.DataReturn(request, head, response);

        switch (request.getApi().getName()) {
            case "RegisterMsg":
                if (head.isSuccess()) {
                    showShortToast(R.string.login_verification_code_success);

                    if (null != mCountDownTimer)
                        mCountDownTimer.start();
                }

                break;
            case "RegisterLogin":
                if (head.isSuccess()) {
                    UserInfoVo userInfoVo;

                    try {
                        userInfoVo = (UserInfoVo) response;
                    } catch (Exception e) {
                        userInfoVo = null;

                        e.printStackTrace();
                    }

                    showShortToast(R.string.register_success);

                    mUser.login(userInfoVo);

                    SharedPreUtils.setString(this, SharedPreUtils.Token, userInfoVo.getToken());

                    if (!TextUtils.isEmpty(userInfoVo.getTraderId()))
                        PushManager.getInstance().bindAlias(this, userInfoVo.getTraderId());

                    ARouter.getInstance().build(Constants.ARouterUriConst.REGISTERSUCCESS).navigation();

                    finish();
                }

                break;
        }
    }

    public class ClickHandlers {

        public void onClickSms() {
            String mobile = mBinding.etMobile.getText().toString().trim();

            if (!ValueUtils.isPhoneNumber(mobile))
                showShortToast(R.string.login_mobile_error);
            else
                registerMsg(mobile);
        }

        public void onClickAgreement() {
            ARouter.getInstance()
                    .build(Constants.ARouterUriConst.JMEWEBVIEW)
                    .withString("title", getString(R.string.register_aggrement_title))
                    .withString("url", Constants.HttpConst.URL_REGISTER_AGGREMENT)
                    .navigation();
        }

        public void onClickLogin() {
            gotoLogin();

            finish();
        }

        public void onClickRegister() {
            doRegister();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (null != mCountDownTimer)
            mCountDownTimer.cancel();
    }
}
