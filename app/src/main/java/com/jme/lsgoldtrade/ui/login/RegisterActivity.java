package com.jme.lsgoldtrade.ui.login;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.CompoundButton;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.common.ui.base.JMECountDownTimer;
import com.jme.common.util.AppManager;
import com.jme.common.util.SharedPreUtils;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseActivity;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.ActivityRegisterBinding;
import com.jme.lsgoldtrade.domain.UserInfoVo;
import com.jme.lsgoldtrade.service.TradeService;
import com.jme.lsgoldtrade.util.SpanUtils;

import java.util.HashMap;

/**
 * 注册
 */
@Route(path = Constants.ARouterUriConst.REGISTER)
public class RegisterActivity extends JMEBaseActivity {

    private ActivityRegisterBinding mBinding;

    private JMECountDownTimer mCountDownTimer;

    private int isAgree = 1;

    private String url = "http://www.taijs.com/upload/yhxy.htm";

    @Override
    protected int getContentViewId() {
        return R.layout.activity_register;
    }

    @Override
    protected void initView() {
        super.initView();
        mBinding = (ActivityRegisterBinding) mBindingUtil;
//        initToolbar("注册", false);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        mCountDownTimer = new JMECountDownTimer(60000, 1000,
                mBinding.btnVerificationCode, getString(R.string.trade_get_verification_code));
        mBinding.login.setText(new SpanUtils(this)
                .append("已有账号")
                .setForegroundColor(getResources().getColor(R.color.color_000))
                .append("去登陆")
                .setForegroundColor(getResources().getColor(R.color.color_0080ff))
                .create());
    }

    @Override
    protected void initListener() {
        super.initListener();
        mBinding.cbAgree.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    isAgree = 2;
                } else {
                    isAgree = 1;
                }
            }
        });
    }

    @Override
    protected void initBinding() {
        super.initBinding();
        mBinding.setHandlers(new ClickHandlers());
    }

    public class ClickHandlers {

        public void onClickSms() {
            String mobile = mBinding.etMobile.getText().toString().trim();
            HashMap<String, String> params = new HashMap<>();
            params.put("mobile", mobile);
            sendRequest(TradeService.getInstance().registerMsg, params, true);
        }
        public void onClickAgreement() {
            ARouter.getInstance()
                    .build(Constants.ARouterUriConst.AGREEMENT)
                    .withString("title", "用户使用协议")
                    .withString("url", url)
                    .navigation();
        }
        public void onClickGetVerificationCode() {

        }
        public void onClickLogin() {
            AppManager.getAppManager().finishActivity();
        }
        public void onClickCancel() {
            AppManager.getAppManager().finishActivity();
        }
        public void onClickRegister() {

            String mobile = mBinding.etMobile.getText().toString().trim();
            String smsCode = mBinding.etVerifyCode.getText().toString().trim();
            if (TextUtils.isEmpty(mobile)) {
                showShortToast("手机号码不能为空");
                return;
            }
            if (TextUtils.isEmpty(smsCode)) {
                showShortToast("短信验证码不能为空");
                return;
            }
            if (isAgree == 1) {
                showShortToast("请仔细阅读用户使用协议并同意该协议");
                return;
            }
            HashMap<String, String> params = new HashMap<>();
            params.put("mobile", mobile);
            params.put("smsCode", smsCode);
            sendRequest(TradeService.getInstance().registerLogin, params, true);
        }
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

                    if (null == userInfoVo)
                        return;

                    mUser.login(userInfoVo);

                    showShortToast("注册成功");
                    SharedPreUtils.setString(this, SharedPreUtils.Login_Mobile, mBinding.etMobile.getText().toString());

                    ARouter.getInstance()
                            .build(Constants.ARouterUriConst.REGISTERSUCCESS)
                            .navigation();
                } else {
                    showShortToast(head.getMsg());
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (null != mCountDownTimer)
            mCountDownTimer.cancel();
    }
}
