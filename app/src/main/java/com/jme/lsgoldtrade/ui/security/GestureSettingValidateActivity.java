package com.jme.lsgoldtrade.ui.security;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.common.ui.view.VerificationCodeView;
import com.jme.common.util.RxBus;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseActivity;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.ActivityGestureSettingValidateBinding;
import com.jme.lsgoldtrade.service.ManagementService;
import com.jme.lsgoldtrade.util.AESUtil;
import com.jme.lsgoldtrade.view.ConfirmSimplePopupwindow;

import java.util.HashMap;

import rx.Subscription;

@Route(path = Constants.ARouterUriConst.GESTURESETTINGVALIDATE)
public class GestureSettingValidateActivity extends JMEBaseActivity {

    private ActivityGestureSettingValidateBinding mBinding;

    private ConfirmSimplePopupwindow mConfirmSimplePopupwindow;
    private Subscription mRxbus;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_gesture_setting_validate;
    }

    @Override
    protected void initView() {
        super.initView();

        initToolbar(R.string.security_gesture_password_setting, true);
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

        initRxBus();

        mBinding.verificationCodeView.setInputCompleteListener(new VerificationCodeView.InputCompleteListener() {
            @Override
            public void inputComplete() {
                String content = mBinding.verificationCodeView.getInputContent();

                if (!TextUtils.isEmpty(content) && content.length() == 6) {
                    hiddenKeyBoard();

                    unlockTradePassword(content);
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

        mBinding = (ActivityGestureSettingValidateBinding) mBindingUtil;
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
                case Constants.RxBusConst.RXBUS_GESTURU_MODIFY_SUCCESS:
                    finish();

                    break;
            }
        });
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
                    showShortToast(R.string.security_verify_success);

                    ARouter.getInstance().build(Constants.ARouterUriConst.GESTURESETTING).navigation();
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

    private void hiddenKeyBoard() {
        ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                mBinding.verificationCodeView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (null != mConfirmSimplePopupwindow && mConfirmSimplePopupwindow.isShowing())
            return false;

        return super.dispatchTouchEvent(event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (!mRxbus.isUnsubscribed())
            mRxbus.unsubscribe();
    }
}
