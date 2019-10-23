package com.jme.lsgoldtrade.ui.security;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.inputmethod.InputMethodManager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.common.ui.view.VerificationCodeView;
import com.jme.common.util.RxBus;
import com.jme.common.util.StatusBarUtil;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseActivity;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.ActivityUnlockTradingPasswordBinding;
import com.jme.lsgoldtrade.service.ManagementService;
import com.jme.lsgoldtrade.util.AESUtil;
import com.jme.lsgoldtrade.view.ConfirmSimplePopupwindow;

import java.util.HashMap;

import rx.Subscription;

@Route(path = Constants.ARouterUriConst.UNLOCKTRADINGPASSWORD)
public class UnlockTradingPasswordActivity extends JMEBaseActivity {

    private ActivityUnlockTradingPasswordBinding mBinding;

    private ConfirmSimplePopupwindow mConfirmSimplePopupwindow;
    private Subscription mRxbus;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_unlock_trading_password;
    }

    @Override
    protected void initView() {
        super.initView();

        StatusBarUtil.setStatusBarMode(this, true, R.color.color_blue_deep);

        mConfirmSimplePopupwindow = new ConfirmSimplePopupwindow(this);
        mConfirmSimplePopupwindow.setOutsideTouchable(true);
        mConfirmSimplePopupwindow.setFocusable(true);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
    }

    @Override
    protected void initListener() {
        super.initListener();

        initRxBus();

        mBinding.verificationCodeView.setInputCompleteListener(new VerificationCodeView.InputCompleteListener() {
            @Override
            public void inputComplete() {
                String content = mBinding.verificationCodeView.getInputContent();

                if (!TextUtils.isEmpty(content) && content.length() == 6)
                    unlockTradePassword(content, "1");
            }

            @Override
            public void deleteContent() {

            }
        });
    }

    @Override
    protected void initBinding() {
        super.initBinding();

        mBinding = (ActivityUnlockTradingPasswordBinding) mBindingUtil;
        mBinding.setHandlers(new ClickHandlers());
    }

    @Override
    protected void onResume() {
        super.onResume();

        mBinding.tvErrorMessage.setText("");
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

    private void unlockTradePassword(String password, String type) {
        HashMap<String, String> params = new HashMap<>();
        params.put("password", AESUtil.encryptString2Base64(password, "0J4S9B5C0J4S9B5C", "16-Bytes--String").trim());
        params.put("passwordType", type);

        sendRequest(ManagementService.getInstance().unlockTradePassword, params, true, false, false);
    }

    @Override
    protected void DataReturn(DTRequest request, Head head, Object response) {
        super.DataReturn(request, head, response);

        switch (request.getApi().getName()) {
            case "UnlockTradePassword":
                if (head.isSuccess()) {
                    finish();
                } else {
                    mBinding.tvErrorMessage.setText(head.getMsg());
                    mBinding.verificationCodeView.clearInputContent();

                    if (head.getCode().equals("-3001")) {
                        if (null != mConfirmSimplePopupwindow && !mConfirmSimplePopupwindow.isShowing()) {
                            mConfirmSimplePopupwindow.setData(getResources().getString(R.string.security_password_error_message),
                                    getResources().getString(R.string.security_trading_password_reset),
                                    (view) -> {
                                        ARouter.getInstance().build(Constants.ARouterUriConst.TRADINGPASSWORDVALIDATE).navigation();

                                        mConfirmSimplePopupwindow.dismiss();
                                    });
                            mConfirmSimplePopupwindow.showAtLocation(mBinding.tvErrorMessage, Gravity.CENTER, 0, 0);
                        }
                    }
                }

                break;
        }
    }

    public class ClickHandlers {

        public void onClickCancel() {
            hiddenKeyBoard();

            RxBus.getInstance().post(Constants.RxBusConst.RXBUS_TRADING_PASSWORD_CANCEL, null);

            finish();
        }

        public void onClickForgetPassword() {
            ARouter.getInstance().build(Constants.ARouterUriConst.TRADINGPASSWORDVALIDATE).navigation();
        }

    }

    private void hiddenKeyBoard() {
        ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                mBinding.verificationCodeView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (!mRxbus.isUnsubscribed())
            mRxbus.unsubscribe();
    }

}