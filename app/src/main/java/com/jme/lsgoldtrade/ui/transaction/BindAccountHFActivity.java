package com.jme.lsgoldtrade.ui.transaction;

import android.os.Bundle;
import android.text.TextUtils;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.common.ui.base.JMECountDownTimer;
import com.jme.common.util.StringUtils;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseActivity;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.ActivityBindAccountHfBinding;
import com.jme.lsgoldtrade.service.TradeService;

import java.util.HashMap;

@Route(path = Constants.ARouterUriConst.BINDACCOUNTHF)
public class BindAccountHFActivity extends JMEBaseActivity {

    private ActivityBindAccountHfBinding mBinding;

    private JMECountDownTimer mCountDownTimer;

    private String mName;
    private String mIDCard;
    private boolean bFlag = false;
    private boolean bAgreeFlag = true;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_bind_account_hf;
    }

    @Override
    protected void initView() {
        super.initView();

        initToolbar(R.string.transaction_bind_account, true);

        mBinding.checkboxAgree.setChecked(true);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);

        mName = getIntent().getStringExtra("Name");
        mIDCard = getIntent().getStringExtra("IDCard");

        mCountDownTimer = new JMECountDownTimer(60000, 1000,
                mBinding.btnVerificationCode, getString(R.string.transaction_get_verification_code));
    }

    @Override
    protected void initListener() {
        super.initListener();

        mBinding.checkboxAgree.setOnCheckedChangeListener((compoundButton, isChecked) -> bAgreeFlag = isChecked);
    }

    @Override
    protected void initBinding() {
        super.initBinding();

        mBinding = (ActivityBindAccountHfBinding) mBindingUtil;
        mBinding.setHandlers(new ClickHandlers());
    }

    private void hfbBindMsg() {
        sendRequest(TradeService.getInstance().hfbBindMsg, new HashMap<>(), true);
    }

    private void HFBBind(String verifyCode) {
        HashMap<String, String> params = new HashMap<>();
        params.put("name", mName);
        params.put("idCard", mIDCard);
        params.put("smsCode", verifyCode);

        sendRequest(TradeService.getInstance().HFBBind, params, true);
    }

    @Override
    protected void DataReturn(DTRequest request, Head head, Object response) {
        super.DataReturn(request, head, response);

        switch (request.getApi().getName()) {
            case "HfbBindMsg":
                bFlag = true;

                if (head.isSuccess()) {
                    showShortToast(R.string.login_verification_code_success);

                    if (null != mCountDownTimer)
                        mCountDownTimer.start();
                }

                break;
            case "HFBBind":

                break;
        }
    }

    public class ClickHandlers {

        public void onClickSoftWareAgreement() {
            ARouter.getInstance()
                    .build(Constants.ARouterUriConst.JMEWEBVIEW)
                    .withString("title", getString(R.string.transaction_soft_aggrement_title))
                    .withString("url", "http://www.taijs.com/upload/rjfwxy-hf.html" + "?name=" + mName + "&cardNo=" + StringUtils.formatIDCardNumber(mIDCard))
                    .navigation();
        }

        public void onClickGetVerificationCode() {
            hfbBindMsg();
        }

        public void onClickBind() {
            String verifyCode = mBinding.etVerifyCode.getText().toString();

            if (!bFlag)
                showShortToast(R.string.login_verification_code_unget);
            else if (TextUtils.isEmpty(verifyCode))
                showShortToast(R.string.login_verification_code_error);
            else if (verifyCode.length() < 6)
                showShortToast(R.string.login_verification_code_error);
            else if (!bAgreeFlag)
                showShortToast(R.string.transaction_bind_aggrement_message2);
            else
                HFBBind(verifyCode);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (null != mCountDownTimer)
            mCountDownTimer.cancel();
    }

}
