package com.jme.lsgoldtrade.ui.personal;

import android.os.Bundle;
import android.text.TextUtils;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.common.ui.base.JMECountDownTimer;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseActivity;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.config.User;
import com.jme.lsgoldtrade.databinding.ActivityCheckUserInfoBinding;
import com.jme.lsgoldtrade.service.AccountService;
import java.util.HashMap;

/**
 * Created by Android Studio.
 * Author : zhangzhongqiang
 * Email  : betterzhang.dev@gmail.com
 * Time   : 2019/07/31 16:57
 * Desc   : 信息验证页面
 */
@Route(path = Constants.ARouterUriConst.CHECKUSERINFO)
public class CheckUserInfoActivity extends JMEBaseActivity {

    private ActivityCheckUserInfoBinding mBinding;

    private JMECountDownTimer mCountDownTimer;

    private boolean bFlag = false;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_check_user_info;
    }

    @Override
    protected void initView() {
        super.initView();
        initToolbar("信息验证", true);
        mBinding = (ActivityCheckUserInfoBinding) mBindingUtil;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        if (User.getInstance().isLogin())
            mBinding.tvMobile.setText(User.getInstance().getCurrentUser().getMobile());

        mCountDownTimer = new JMECountDownTimer(60000, 1000,
                mBinding.btnSendSms, getString(R.string.trade_get_verification_code));

        getWithdrawFeeRate();
    }

    @Override
    protected void initBinding() {
        super.initBinding();
        mBinding.setHandlers(new ClickHandlers());
    }

    private void getWithdrawFeeRate() {
        sendRequest(AccountService.getInstance().getWithdrawFeeRate, new HashMap<>(), true);
    }

    private void sendVerifyCode() {
        bFlag = true;
        sendRequest(AccountService.getInstance().sendVerifyCode, new HashMap<>(), true);
    }

    private void checkVerifyCode(String smsCode) {
        HashMap<String, String> params = new HashMap<>();
        params.put("smsCode", smsCode);
        sendRequest(AccountService.getInstance().checkVerifyCode, params, true);
    }

    @Override
    protected void DataReturn(DTRequest request, Head head, Object response) {
        super.DataReturn(request, head, response);
        switch (request.getApi().getName()) {
            case "GetWithdrawFeeRate":
                if (head.isSuccess()) {
                    String rate;
                    try {
                        rate = (String) response;
                    } catch (Exception e) {
                        rate = null;
                        e.printStackTrace();
                    }
                    if (TextUtils.isEmpty(rate))
                        return;
                    mBinding.tvWithdrawRule.setText(String.format(getString(R.string.text_withdraw_rule), rate + "%"));
                }
                break;
            case "SendVerifyCode":
                if (head.isSuccess()) {
                    showShortToast(R.string.login_verification_code_success);
                    if (null != mCountDownTimer)
                        mCountDownTimer.start();
                }
                break;
            case "CheckVerifyCode":
                if (head.isSuccess()) {
                    ARouter.getInstance().build(Constants.ARouterUriConst.WITHDRAW).navigation();
                    this.finish();
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != mCountDownTimer)
            mCountDownTimer.cancel();
    }

    public class ClickHandlers {

        public void onClickSendSmsCode() {
            sendVerifyCode();
        }

        public void onClickVerifyCode() {
            String smsCode = mBinding.etVerifyCode.getText().toString().trim();
            if (!bFlag)
                showShortToast(R.string.login_verification_code_unget);
            else if (TextUtils.isEmpty(smsCode))
                showShortToast(R.string.login_verification_code_error);
            else if (smsCode.length() < 6)
                showShortToast(R.string.login_verification_code_error);
            else
                checkVerifyCode(smsCode);
        }

    }
}
