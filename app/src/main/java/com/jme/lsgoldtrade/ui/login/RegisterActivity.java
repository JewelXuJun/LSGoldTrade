package com.jme.lsgoldtrade.ui.login;

import android.os.Bundle;
import android.text.TextUtils;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.google.gson.Gson;
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
import com.jme.lsgoldtrade.domain.LoginResponse;
import com.jme.lsgoldtrade.domain.UserInfoVo;
import com.jme.lsgoldtrade.service.TradeService;
import com.jme.lsgoldtrade.service.UserService;
import com.jme.lsgoldtrade.util.ValueUtils;

import java.net.ConnectException;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

        showLoadingDialog("");

        DTRequest request = new DTRequest(TradeService.getInstance().registerLogin, params, false, true);

        Call restResponse = request.getApi().request(request.getParams());

        restResponse.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                Head head = new Head();
                Object body = "";

                if (response.raw().code() != 200) {
                    head.setSuccess(false);
                    head.setCode("" + response.raw().code());
                    head.setMsg("服务器异常");
                } else {
                    if (!request.getApi().isResponseJson()) {
                        body = response.body();
                        head.setSuccess(true);
                        head.setCode("0");
                        head.setMsg("成功");
                    } else {
                        LoginResponse dtResponse = (LoginResponse) response.body();

                        head = new Head();
                        head.setCode(dtResponse.getCode());
                        head.setMsg(dtResponse.getMsg());

                        try {
                            body = new Gson().fromJson(dtResponse.getBodyToString(),
                                    request.getApi().getEntryType());
                        } catch (Exception e) {
                            body = dtResponse.getBodyToString();
                        }
                    }
                }

                OnResult(request, head, body);
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Head head = new Head();
                final Throwable cause = t.getCause() != null ? t.getCause() : t;

                if (cause != null) {
                    if (cause instanceof ConnectException) {
                        head.setSuccess(false);
                        head.setCode("500");
                        head.setMsg(getResources().getString(com.jme.common.R.string.text_error_server));
                    } else {
                        head.setSuccess(false);
                        head.setCode("408");
                        head.setMsg(getResources().getString(com.jme.common.R.string.text_error_timeout));
                    }
                }

                OnResult(request, head, null);
            }
        });
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
