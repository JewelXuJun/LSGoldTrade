package com.jme.lsgoldtrade.ui.security;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MotionEvent;

import androidx.core.hardware.fingerprint.FingerprintManagerCompat;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.common.ui.view.fingerprint.FingerprintCallback;
import com.jme.common.ui.view.fingerprint.FingerprintVerifyManager;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseActivity;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.ActivityFingerprintBinding;
import com.jme.lsgoldtrade.domain.PasswordInfoVo;
import com.jme.lsgoldtrade.service.ManagementService;
import com.jme.lsgoldtrade.view.ConfirmSimplePopupwindow;

import java.util.HashMap;

@Route(path = Constants.ARouterUriConst.FINGERPRINT)
public class FingerprintActivity extends JMEBaseActivity {

    private ActivityFingerprintBinding mBinding;

    private boolean bFlag = true;
    private int mErrorIndex = 0;

    private FingerprintVerifyManager.Builder mBuilder;
    private ConfirmSimplePopupwindow mConfirmSimplePopupwindow;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    if (null != mConfirmSimplePopupwindow && !mConfirmSimplePopupwindow.isShowing()) {
                        mConfirmSimplePopupwindow.setData(getResources().getString(R.string.security_fingerprint_not_set),
                                getResources().getString(R.string.text_confirm),
                                (view) -> {
                                    mBinding.switchView.setChecked(false);

                                    mConfirmSimplePopupwindow.dismiss();

                                    finish();
                                });
                        mConfirmSimplePopupwindow.showAtLocation(mBinding.switchView, Gravity.CENTER, 0, 0);
                    }

                    break;
            }
        }

    };

    @Override
    protected int getContentViewId() {
        return R.layout.activity_fingerprint;
    }

    @Override
    protected void initView() {
        super.initView();

        initToolbar(R.string.security_fingerprint_setting, true);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);

        mBuilder = new FingerprintVerifyManager.Builder(this);
        mBuilder.enableAndroidP(false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && !FingerprintManagerCompat.from(this).hasEnrolledFingerprints())
            mHandler.sendEmptyMessageDelayed(0, 500);

        mConfirmSimplePopupwindow = new ConfirmSimplePopupwindow(this);
        mConfirmSimplePopupwindow.setOutsideTouchable(false);
        mConfirmSimplePopupwindow.setFocusable(false);
    }

    @Override
    protected void initListener() {
        super.initListener();

        mBuilder.callback(new FingerprintCallback() {
            @Override
            public void onHwUnavailable() {

            }

            @Override
            public void onNoneEnrolled() {

            }

            @Override
            public void onSucceeded() {
                if (null != mConfirmSimplePopupwindow && !mConfirmSimplePopupwindow.isShowing()) {
                    mConfirmSimplePopupwindow.setData(getResources().getString(R.string.security_enable_success),
                            getResources().getString(R.string.text_confirm),
                            (view) -> {
                                updatePasswordOpenStatus("Y");

                                mConfirmSimplePopupwindow.dismiss();
                            });
                    mConfirmSimplePopupwindow.showAtLocation(mBinding.switchView, Gravity.CENTER, 0, 0);
                }
            }

            @Override
            public void onFailed(String errorMessage) {
                mErrorIndex++;

                if (mErrorIndex == 3) {
                    mBuilder.dismiss();

                    if (null != mConfirmSimplePopupwindow && !mConfirmSimplePopupwindow.isShowing()) {
                        mConfirmSimplePopupwindow.setData(getResources().getString(R.string.security_enable_fail),
                                getResources().getString(R.string.text_confirm),
                                (view) -> {
                                    mConfirmSimplePopupwindow.dismiss();

                                    finish();
                                });
                        mConfirmSimplePopupwindow.showAtLocation(mBinding.switchView, Gravity.CENTER, 0, 0);
                    }
                }
            }

            @Override
            public void onCancel() {
                mBinding.switchView.setChecked(false);
            }
        });

        mBinding.switchView.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (bFlag)
                return;

            if (isChecked)
                mBuilder.build();
            else
                updatePasswordOpenStatus("N");
        });

    }

    @Override
    protected void initBinding() {
        super.initBinding();

        mBinding = (ActivityFingerprintBinding) mBindingUtil;
    }

    @Override
    protected void onResume() {
        super.onResume();

        bFlag = true;

        getUserPasswordSettingInfo();
    }

    private void getUserPasswordSettingInfo() {
        sendRequest(ManagementService.getInstance().getUserPasswordSettingInfo, new HashMap<>(), bFlag, false, bFlag);
    }

    private void updatePasswordOpenStatus(String status) {
        HashMap<String, String> params = new HashMap<>();
        params.put("passwordType", "2");
        params.put("status", status);

        sendRequest(ManagementService.getInstance().updatePasswordOpenStatus, params, true);
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

                    mBinding.switchView.setChecked(TextUtils.isEmpty(hasOpenFingerPrint) ? false : hasOpenFingerPrint.equals("Y"));
                }

                bFlag = false;

                break;
            case "UpdatePasswordOpenStatus":
                if (head.isSuccess()) {
                    if (request.getParams().get("status").equals("Y"))
                        finish();
                }

                break;

        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (null != mConfirmSimplePopupwindow && mConfirmSimplePopupwindow.isShowing())
            return false;

        return super.dispatchTouchEvent(event);
    }
}
