package com.jme.lsgoldtrade.ui.security;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.common.ui.view.VerificationCodeView;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseActivity;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.ActivityValidateTradingPasswordBinding;
import com.jme.lsgoldtrade.service.ManagementService;
import com.jme.lsgoldtrade.util.AESUtil;
import com.jme.lsgoldtrade.view.ConfirmSimplePopupwindow;

import java.util.HashMap;

@Route(path = Constants.ARouterUriConst.VALIDATETRADINGPASSWORD)
public class ValidateTradingPasswordActivity extends JMEBaseActivity {

    private ActivityValidateTradingPasswordBinding mBinding;

    private TextView tvClose;

    private ConfirmSimplePopupwindow mConfirmSimplePopupwindow;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_validate_trading_password;
    }

    @Override
    protected void initView() {
        super.initView();

        initToolbar(R.string.security_trading_password_validate, false);

        tvClose = findViewById(R.id.tv_close);
        tvClose.setText(R.string.text_cancel);
        tvClose.setTextColor(ContextCompat.getColor(this, R.color.color_blue_deep));
        tvClose.setVisibility(View.VISIBLE);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);

        mConfirmSimplePopupwindow = new ConfirmSimplePopupwindow(this);
        mConfirmSimplePopupwindow.setOutsideTouchable(false);
        mConfirmSimplePopupwindow.setFocusable(false);
    }

    @Override
    protected void initListener() {
        super.initListener();

        tvClose.setOnClickListener((view) -> finish());

        mBinding.verificationCodeView.setInputCompleteListener(new VerificationCodeView.InputCompleteListener() {
            @Override
            public void inputComplete() {
                String content = mBinding.verificationCodeView.getInputContent();

                if (!TextUtils.isEmpty(content) && content.length() == 6)
                    unlockTradePassword(content);
            }

            @Override
            public void deleteContent() {

            }
        });
    }

    @Override
    protected void initBinding() {
        super.initBinding();

        mBinding = (ActivityValidateTradingPasswordBinding) mBindingUtil;
    }

    @Override
    protected void onResume() {
        super.onResume();

        mBinding.tvErrorMessage.setText("");
    }

    private void unlockTradePassword(String password) {
        HashMap<String, String> params = new HashMap<>();
        params.put("password", AESUtil.encryptString2Base64(password, "0J4S9B5C0J4S9B5C", "16-Bytes--String").trim());
        params.put("passwordType", "1");

        sendRequest(ManagementService.getInstance().unlockTradePassword, params, true, false, false);
    }

    @Override
    protected void DataReturn(DTRequest request, Head head, Object response) {
        super.DataReturn(request, head, response);

        switch (request.getApi().getName()) {
            case "UnlockTradePassword":
                if (head.isSuccess()) {
                    ARouter.getInstance().build(Constants.ARouterUriConst.GESTURE).navigation();

                    finish();
                } else {
                    mBinding.tvErrorMessage.setText(head.getMsg());
                    mBinding.verificationCodeView.clearInputContent();

                    if (head.getCode().equals("-3001") && null != mConfirmSimplePopupwindow && !mConfirmSimplePopupwindow.isShowing()) {
                        mConfirmSimplePopupwindow.setData(getResources().getString(R.string.security_password_error_message),
                                getResources().getString(R.string.security_trading_password_reset),
                                (view) -> {
                                    ARouter.getInstance().build(Constants.ARouterUriConst.TRADINGPASSWORDVALIDATE).navigation();

                                    mConfirmSimplePopupwindow.dismiss();
                                });
                        mConfirmSimplePopupwindow.showAtLocation(mBinding.tvErrorMessage, Gravity.CENTER, 0, 0);
                    }
                }

                break;
        }
    }
}
