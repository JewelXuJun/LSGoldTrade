package com.jme.lsgoldtrade.ui.security;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;

import androidx.core.hardware.fingerprint.FingerprintManagerCompat;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseActivity;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.ActivityAccountSecurityBinding;
import com.jme.lsgoldtrade.domain.OnlineTimeVo;
import com.jme.lsgoldtrade.domain.PasswordInfoVo;
import com.jme.lsgoldtrade.service.ManagementService;
import com.jme.lsgoldtrade.view.ConfirmSimplePopupwindow;

import java.util.HashMap;

@Route(path = Constants.ARouterUriConst.ACCOUNTSECURITY)
public class AccountSecurityActivity extends JMEBaseActivity {

    private ActivityAccountSecurityBinding mBinding;

    private ConfirmSimplePopupwindow mConfirmSimplePopupwindow;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_account_security;
    }

    @Override
    protected void initView() {
        super.initView();

        initToolbar(R.string.personal_account_security, true);

        mConfirmSimplePopupwindow = new ConfirmSimplePopupwindow(this);
        mConfirmSimplePopupwindow.setOutsideTouchable(true);
        mConfirmSimplePopupwindow.setFocusable(true);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            mBinding.layoutFingerprint.setVisibility(FingerprintManagerCompat.from(this).isHardwareDetected() ? View.VISIBLE : View.GONE);
        else
            mBinding.layoutFingerprint.setVisibility(View.GONE);

        mBinding.tvAccount.setText(null == mUser || !mUser.isLogin() ? "" : mUser.getTraderId());
    }

    @Override
    protected void initListener() {
        super.initListener();
    }

    @Override
    protected void initBinding() {
        super.initBinding();

        mBinding = (ActivityAccountSecurityBinding) mBindingUtil;
        mBinding.setHandlers(new ClickHandlers());
    }

    @Override
    protected void onResume() {
        super.onResume();

        getUserPasswordSettingInfo();
        getUserOnlineTime();
    }

    private void getUserPasswordSettingInfo() {
        sendRequest(ManagementService.getInstance().getUserPasswordSettingInfo, new HashMap<>(), true);
    }

    private void getUserOnlineTime() {
        sendRequest(ManagementService.getInstance().getUserOnlineTime, new HashMap<>(), false);
    }

    @Override
    protected void DataReturn(DTRequest request, Head head, Object response) {
        super.DataReturn(request, head, response);

        switch (request.getApi().getName()) {
            case "GetUserPasswordSettingInfo":
                if (head.isSuccess()) {
                    PasswordInfoVo passwordInfoVo;

                    try {
                        passwordInfoVo = (PasswordInfoVo) response;
                    } catch (Exception e) {
                        passwordInfoVo = null;

                        e.printStackTrace();
                    }

                    if (null == passwordInfoVo)
                        return;

                    String hasOpenFingerPrint = passwordInfoVo.getHasOpenFingerPrint();
                    String hasOpenGestures = passwordInfoVo.getHasOpenGestures();

                    mBinding.tvFingerprintStatus.setText(TextUtils.isEmpty(hasOpenFingerPrint) ? R.string.security_not_enable
                            : hasOpenFingerPrint.equals("Y") ? R.string.security_enable : R.string.security_not_enable);
                    mBinding.tvGestureStatus.setText(TextUtils.isEmpty(hasOpenGestures) ? R.string.security_not_enable
                            : hasOpenGestures.equals("Y") ? R.string.security_enable : R.string.security_not_enable);
                }

                break;
            case "GetUserOnlineTime":
                if (head.isSuccess()) {
                    OnlineTimeVo onlineTimeVo;

                    try {
                        onlineTimeVo = (OnlineTimeVo) response;
                    } catch (Exception e) {
                        onlineTimeVo = null;

                        e.printStackTrace();
                    }

                    if (null == onlineTimeVo)
                        return;

                    mBinding.tvOnlineDuration.setText(onlineTimeVo.getTimeName());
                }

                break;
        }
    }

    public class ClickHandlers {

        public void onClickTips() {
            if (null != mConfirmSimplePopupwindow && !mConfirmSimplePopupwindow.isShowing()) {
                mConfirmSimplePopupwindow.setData(getResources().getString(R.string.security_account_security_tips),
                        getResources().getString(R.string.text_confirm),
                        (view) -> mConfirmSimplePopupwindow.dismiss());
                mConfirmSimplePopupwindow.showAtLocation(mBinding.tvAccount, Gravity.CENTER, 0, 0);
            }
        }

        public void onClickTradingPassword() {
            ARouter.getInstance().build(Constants.ARouterUriConst.TRADINGPASSWORDVALIDATE).navigation();
        }

        public void onClickFingerprint() {
            ARouter.getInstance().build(Constants.ARouterUriConst.FINGERPRINT).navigation();
        }

        public void onClickGesture() {
            ARouter.getInstance().build(Constants.ARouterUriConst.GESTURE).navigation();
        }

        public void onClickOnlineDuration() {
            ARouter.getInstance().build(Constants.ARouterUriConst.ONLINEDURATION).navigation();
        }

    }

}
