package com.jme.lsgoldtrade.ui.security;

import android.content.Context;
import android.os.Bundle;

import androidx.core.content.ContextCompat;

import android.text.TextUtils;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.common.ui.view.VerificationCodeView;
import com.jme.common.util.RxBus;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseActivity;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.ActivityTradingPasswordSettingConfirmBinding;
import com.jme.lsgoldtrade.domain.PasswordStatusVo;
import com.jme.lsgoldtrade.service.ManagementService;
import com.jme.lsgoldtrade.util.AESUtil;
import com.jme.lsgoldtrade.view.OperatePopupwindow;
import com.jme.lsgoldtrade.view.ConfirmSimplePopupwindow;

import java.util.HashMap;

@Route(path = Constants.ARouterUriConst.TRADINGPASSWORDSETTINGCONFIRM)
public class TradingPasswordSettingConfirmActivity extends JMEBaseActivity {

    private ActivityTradingPasswordSettingConfirmBinding mBinding;

    private String mTradingPassword;

    private TextView tvClose;
    private ConfirmSimplePopupwindow mConfirmSimplePopupwindow;
    private OperatePopupwindow mOperatePopupwindow;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_trading_password_setting_confirm;
    }

    @Override
    protected void initView() {
        super.initView();

        initToolbar(R.string.security_trading_password_setting, false);

        tvClose = findViewById(R.id.tv_close);
        tvClose.setText(R.string.text_cancel);
        tvClose.setTextColor(ContextCompat.getColor(this, R.color.color_blue_deep));
        tvClose.setVisibility(View.VISIBLE);

        mConfirmSimplePopupwindow = new ConfirmSimplePopupwindow(this);
        mConfirmSimplePopupwindow.setOutsideTouchable(false);
        mConfirmSimplePopupwindow.setFocusable(false);

        mOperatePopupwindow = new OperatePopupwindow(this);
        mOperatePopupwindow.setOutsideTouchable(false);
        mOperatePopupwindow.setFocusable(false);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);

        mTradingPassword = getIntent().getStringExtra("TradingPassword");
    }

    @Override
    protected void initListener() {
        super.initListener();

        tvClose.setOnClickListener((view) -> finish());

        mBinding.verificationCodeView.setInputCompleteListener(new VerificationCodeView.InputCompleteListener() {
            @Override
            public void inputComplete() {
                String content = mBinding.verificationCodeView.getInputContent();

                if (!TextUtils.isEmpty(content) && content.length() == 6) {
                    hiddenKeyBoard();

                    if (content.equals(mTradingPassword)) {
                        setTradePassword(mTradingPassword);
                    } else {
                        if (null != mConfirmSimplePopupwindow && !mConfirmSimplePopupwindow.isShowing()) {
                            mConfirmSimplePopupwindow.setData(getResources().getString(R.string.security_password_not_the_same),
                                    getResources().getString(R.string.text_confirm),
                                    (view) -> {
                                        mBinding.verificationCodeView.clearInputContent();

                                        mConfirmSimplePopupwindow.dismiss();
                                    });
                            mConfirmSimplePopupwindow.showAtLocation(mBinding.verificationCodeView, Gravity.CENTER, 0, 0);
                        }
                    }
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

    private void setTradePassword(String tradingPassword) {
        HashMap<String, String> params = new HashMap<>();
        params.put("passwordType", "1");
        params.put("password", AESUtil.encryptString2Base64(tradingPassword, "0J4S9B5C0J4S9B5C", "16-Bytes--String").trim());

        sendRequest(ManagementService.getInstance().setTradePassword, params, true);
    }

    @Override
    protected void DataReturn(DTRequest request, Head head, Object response) {
        super.DataReturn(request, head, response);

        switch (request.getApi().getName()) {
            case "SetTradePassword":
                if (head.isSuccess()) {
                    PasswordStatusVo passwordStatusVo;

                    try {
                        passwordStatusVo = (PasswordStatusVo) response;
                    } catch (Exception e) {
                        passwordStatusVo = null;

                        e.printStackTrace();
                    }

                    if (null == passwordStatusVo)
                        return;

                    String hasFirstSetPassword = passwordStatusVo.getHasFirstSetPassword();

                    if (TextUtils.isEmpty(hasFirstSetPassword) || hasFirstSetPassword.equals("Y")) {
                        if (null != mOperatePopupwindow && !mOperatePopupwindow.isShowing()) {
                            mOperatePopupwindow.setData(getResources().getString(R.string.security_password_setting_success_title),
                                    getResources().getString(R.string.security_password_setting_success_message),
                                    getResources().getString(R.string.security_next_time), getResources().getString(R.string.security_goto_setting),
                                    (view) -> {
                                        RxBus.getInstance().post(Constants.RxBusConst.RXBUS_TRANSACTION_PLACE_ORDER, null);
                                        ARouter.getInstance().build(Constants.ARouterUriConst.MAIN).navigation();

                                        mOperatePopupwindow.dismiss();

                                        finish();
                                    }, (view) -> {
                                        RxBus.getInstance().post(Constants.RxBusConst.RXBUS_TRADING_PASSWORD_SETTING_SUCCESS, null);
                                        ARouter.getInstance().build(Constants.ARouterUriConst.ACCOUNTSECURITY).navigation();

                                        mOperatePopupwindow.dismiss();

                                        finish();
                                    });
                            mOperatePopupwindow.showAtLocation(mBinding.verificationCodeView, Gravity.CENTER, 0, 0);
                        }
                    } else {
                        if (null != mConfirmSimplePopupwindow && !mConfirmSimplePopupwindow.isShowing()) {
                            mConfirmSimplePopupwindow.setData(getResources().getString(R.string.security_password_setting_success),
                                    mContext.getResources().getString(R.string.text_confirm),
                                    (view) -> {
                                        RxBus.getInstance().post(Constants.RxBusConst.RXBUS_TRANSACTION_PLACE_ORDER, null);
                                        ARouter.getInstance().build(Constants.ARouterUriConst.MAIN).navigation();

                                        mConfirmSimplePopupwindow.dismiss();

                                        finish();
                                    });
                            mConfirmSimplePopupwindow.showAtLocation(mBinding.verificationCodeView, Gravity.CENTER, 0, 0);
                        }
                    }
                }

                break;
        }
    }

    private void hiddenKeyBoard() {
        ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                mBinding.verificationCodeView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (null != mConfirmSimplePopupwindow && mConfirmSimplePopupwindow.isShowing())
            return false;
        else if (null != mOperatePopupwindow && mOperatePopupwindow.isShowing())
            return false;

        return super.dispatchTouchEvent(event);
    }

}
