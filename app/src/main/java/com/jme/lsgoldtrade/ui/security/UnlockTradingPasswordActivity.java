package com.jme.lsgoldtrade.ui.security;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.github.ihsg.patternlocker.DefaultLockerNormalCellView;
import com.github.ihsg.patternlocker.DefaultStyleDecorator;
import com.github.ihsg.patternlocker.OnPatternChangeListener;
import com.github.ihsg.patternlocker.PatternLockerView;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.common.ui.view.VerificationCodeView;
import com.jme.common.ui.view.fingerprint.FingerprintCallback;
import com.jme.common.ui.view.fingerprint.FingerprintVerifyManager;
import com.jme.common.util.RxBus;
import com.jme.common.util.StatusBarUtil;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseActivity;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.ActivityUnlockTradingPasswordBinding;
import com.jme.lsgoldtrade.service.ManagementService;
import com.jme.lsgoldtrade.util.AESUtil;
import com.jme.lsgoldtrade.view.ConfirmSimplePopupwindow;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;

import rx.Subscription;

@Route(path = Constants.ARouterUriConst.UNLOCKTRADINGPASSWORD)
public class UnlockTradingPasswordActivity extends JMEBaseActivity {

    private ActivityUnlockTradingPasswordBinding mBinding;

    private int mType;
    private int mErrorIndex = 0;
    private boolean bFlag = false;

    private FingerprintVerifyManager.Builder mBuilder;
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

        mType = getIntent().getIntExtra("Type", 1);

        mBuilder = new FingerprintVerifyManager.Builder(this);
        mBuilder.enableAndroidP(false);
        mBuilder.type("Unlock");

        mBinding.layoutDigitalCipher.setVisibility(mType == 1 ? View.VISIBLE : View.GONE);
        mBinding.layoutFingerprint.setVisibility(mType == 2 ? View.VISIBLE : View.GONE);
        mBinding.layoutGesture.setVisibility(mType == 3 ? View.VISIBLE : View.GONE);

        if (mType == 3)
            initPatternLockerView();

        mConfirmSimplePopupwindow = new ConfirmSimplePopupwindow(this);
        mConfirmSimplePopupwindow.setOutsideTouchable(false);
        mConfirmSimplePopupwindow.setFocusable(false);
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

        mBuilder.callback(new FingerprintCallback() {
            @Override
            public void onHwUnavailable() {

            }

            @Override
            public void onNoneEnrolled() {

            }

            @Override
            public void onSucceeded() {
                unlockTradePassword("", "2");
            }

            @Override
            public void onFailed(String errorMessage) {
                mErrorIndex++;

                if (errorMessage.equals(getResources().getString(R.string.fingerprint_verify_failed))) {
                    if (mErrorIndex == 3) {
                        mBuilder.dismiss();

                        if (null != mConfirmSimplePopupwindow && !mConfirmSimplePopupwindow.isShowing()) {
                            mConfirmSimplePopupwindow.setData(getResources().getString(R.string.security_verify_fail),
                                    getResources().getString(R.string.text_confirm),
                                    (view) -> {
                                        mErrorIndex = 0;

                                        mConfirmSimplePopupwindow.dismiss();
                                    });
                            mConfirmSimplePopupwindow.showAtLocation(mBinding.tvErrorMessage, Gravity.CENTER, 0, 0);
                        }
                    }
                } else {
                    bFlag = true;
                }

            }

            @Override
            public void onCancel() {
                if (bFlag) {
                    mBinding.layoutDigitalCipher.setVisibility(View.VISIBLE);
                    mBinding.layoutFingerprint.setVisibility(View.GONE);

                    mType = 1;

                    updatePasswordOpenStatus("2", "N");
                }
            }
        });

        mBinding.patternLockerView.setOnPatternChangedListener(new OnPatternChangeListener() {
            @Override
            public void onStart(@NotNull PatternLockerView patternLockerView) {

            }

            @Override
            public void onChange(@NotNull PatternLockerView patternLockerView, @NotNull List<Integer> list) {

            }

            @Override
            public void onComplete(@NotNull PatternLockerView patternLockerView, @NotNull List<Integer> list) {
                if (null == list || list.size() < 4) {
                    mBinding.tvGestureErrorMessage.setText(R.string.security_gesture_password_error_point);
                } else {
                    StringBuffer stringBuffer = new StringBuffer();

                    for (int i = 0; i < list.size(); i++) {
                        stringBuffer.append(list.get(i));
                    }

                    unlockTradePassword(stringBuffer.toString(), "3");
                }

            }

            @Override
            public void onClear(@NotNull PatternLockerView patternLockerView) {

            }
        });

        if (mType == 2)
            mBuilder.build();
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
        mBinding.tvGestureErrorMessage.setText("");
    }

    private void initPatternLockerView() {
        DefaultStyleDecorator decorator = ((DefaultLockerNormalCellView) mBinding.patternLockerView.getNormalCellView()).getStyleDecorator();

        GestureNormalCellView normalCellView = new GestureNormalCellView();
        normalCellView.setDefaultColor(decorator.getNormalColor());
        normalCellView.setDivide(2.5f);

        GestureHitCellView hitCellView = new GestureHitCellView();
        hitCellView.setHitColor(decorator.getHitColor());
        hitCellView.setIndicatorView(false);

        mBinding.patternLockerView.setNormalCellView(normalCellView);
        mBinding.patternLockerView.setHitCellView(hitCellView);
    }

    private void initRxBus() {
        mRxbus = RxBus.getInstance().toObserverable(RxBus.Message.class).subscribe(message -> {
            String callType = message.getObject().toString();

            if (TextUtils.isEmpty(callType))
                return;

            switch (callType) {
                case Constants.RxBusConst.RXBUS_TRANSACTION_PLACE_ORDER:
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

    private void updatePasswordOpenStatus(String passwordType, String status) {
        HashMap<String, String> params = new HashMap<>();
        params.put("passwordType", passwordType);
        params.put("status", status);

        sendRequest(ManagementService.getInstance().updatePasswordOpenStatus, params, false, false, false);
    }

    @Override
    protected void DataReturn(DTRequest request, Head head, Object response) {
        super.DataReturn(request, head, response);

        switch (request.getApi().getName()) {
            case "UnlockTradePassword":
                if (head.isSuccess()) {
                    finish();
                } else {
                    if (mType == 1) {
                        mBinding.tvErrorMessage.setText(head.getMsg());
                        mBinding.verificationCodeView.clearInputContent();
                    } else if (mType == 3) {
                        mBinding.tvGestureErrorMessage.setText(head.getMsg());
                    }

                    if (head.getCode().equals("-3001")) {
                        if (null != mConfirmSimplePopupwindow && !mConfirmSimplePopupwindow.isShowing() && mType == 1) {
                            mConfirmSimplePopupwindow.setData(getResources().getString(R.string.security_password_error_message),
                                    getResources().getString(R.string.security_trading_password_reset),
                                    (view) -> {
                                        ARouter.getInstance().build(Constants.ARouterUriConst.TRADINGPASSWORDVALIDATE).navigation();

                                        mConfirmSimplePopupwindow.dismiss();
                                    });
                            mConfirmSimplePopupwindow.showAtLocation(mBinding.tvErrorMessage, Gravity.CENTER, 0, 0);
                        }
                    } else if (head.getCode().equals("-3003")) {
                        if (null != mConfirmSimplePopupwindow && !mConfirmSimplePopupwindow.isShowing() && mType == 3) {
                            mConfirmSimplePopupwindow.setData(getResources().getString(R.string.security_gesture_password_error_message),
                                    getResources().getString(R.string.security_trading_password_login),
                                    (view) -> {
                                        mType = 1;

                                        mBinding.layoutDigitalCipher.setVisibility(View.VISIBLE);
                                        mBinding.layoutFingerprint.setVisibility(View.GONE);
                                        mBinding.layoutGesture.setVisibility(View.GONE);

                                        updatePasswordOpenStatus("3", "N");

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

        public void onClickFingerPrint() {
            mBuilder.build();
        }

        public void onClickForgetGesture() {
            ARouter.getInstance().build(Constants.ARouterUriConst.GESTURESETTINGVALIDATE).navigation();
        }

        public void onClickUseTradingPassword() {
            mType = 1;

            mBinding.layoutDigitalCipher.setVisibility(View.VISIBLE);
            mBinding.layoutFingerprint.setVisibility(View.GONE);
            mBinding.layoutGesture.setVisibility(View.GONE);
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

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (null != mConfirmSimplePopupwindow && mConfirmSimplePopupwindow.isShowing())
            return false;

        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_BACK == keyCode) {
            hiddenKeyBoard();

            RxBus.getInstance().post(Constants.RxBusConst.RXBUS_TRADING_PASSWORD_CANCEL, null);

            finish();

            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

}
