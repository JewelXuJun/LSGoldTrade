package com.jme.lsgoldtrade.ui.login;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.common.ui.base.JMECountDownTimer;
import com.jme.common.util.AppManager;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseActivity;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.ActivityForgetPwdBinding;
import com.jme.lsgoldtrade.domain.ImageVerifyCodeVo;
import com.jme.lsgoldtrade.service.TradeService;
import com.jme.lsgoldtrade.service.UserService;
import com.jme.lsgoldtrade.util.AESUtil;
import com.jme.lsgoldtrade.util.ValueUtils;
import com.orhanobut.logger.Logger;

import java.util.HashMap;

/**
 * 忘记密码
 */
@Route(path = Constants.ARouterUriConst.FORGETPWD)
public class ForgetPwdActivity extends JMEBaseActivity {

    private ActivityForgetPwdBinding mBinding;

    private JMECountDownTimer mCountDownTimer;

    private boolean bShowImgVerifyCode = false;

    private String mKaptchaId;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_forget_pwd;
    }

    @Override
    protected void initView() {
        super.initView();
        mBinding = (ActivityForgetPwdBinding) mBindingUtil;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        mCountDownTimer = new JMECountDownTimer(60000, 1000,
                mBinding.btnVerificationCode, getString(R.string.trade_get_verification_code));
        sendRequest(UserService.getInstance().kaptcha, new HashMap<>(), true, false, false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (null != mCountDownTimer)
            mCountDownTimer.cancel();
    }

    @Override
    protected void initListener() {
        super.initListener();

    }

    @Override
    protected void initBinding() {
        super.initBinding();
        mBinding.setHandlers(new ClickHandlers());
    }

    public class ClickHandlers {

        public void onClickCancel() {
            AppManager.getAppManager().finishActivity();
        }
        public void onClickLoadImageVerifyCode() {
            sendRequest(UserService.getInstance().kaptcha, new HashMap<>(), true, false, false);
        }
        public void onClickGetVerificationCode() {
            String mobile = mBinding.etMobile.getText().toString().trim();
            HashMap<String, String> params = new HashMap<>();
            params.put("mobile", mobile);
            sendRequest(TradeService.getInstance().resetLoginPasswordMsg, params, true);
        }
        public void onClickLogin() {
            String mobile = mBinding.etMobile.getText().toString();
            String verifyCode = mBinding.etVerifyCode.getText().toString();
            String imgVerifyCode = mBinding.etImgVerifyCode.getText().toString();
            String newpwd = mBinding.newpwd.getText().toString().trim();
            String surepwd = mBinding.surepwd.getText().toString().trim();

            String secretNewpwd = AESUtil.encryptString2Base64(newpwd,"0J4S9B5C0J4S9B5C","16-Bytes--String").trim();
            String secretSurepwd = AESUtil.encryptString2Base64(surepwd,"0J4S9B5C0J4S9B5C","16-Bytes--String").trim();

            if (!ValueUtils.isPhoneNumber(mobile))
                showShortToast(R.string.login_mobile_error);
            else if (TextUtils.isEmpty(verifyCode))
                showShortToast(R.string.login_verification_code_unget);
            else if (verifyCode.length() < 6)
                showShortToast(R.string.login_verification_code_error);
            else if (bShowImgVerifyCode && TextUtils.isEmpty(imgVerifyCode))
                showShortToast(R.string.login_img_verify_code_error);
            else if (bShowImgVerifyCode && TextUtils.isEmpty(newpwd))
                showShortToast("请输入密码");
            else if (bShowImgVerifyCode && TextUtils.isEmpty(surepwd))
                showShortToast("请输入再次密码");
            else
                login(mobile, verifyCode, imgVerifyCode, secretNewpwd, secretSurepwd);
        }
    }

    private void login(String mobile, String verifyCode, String imgVerifyCode, String newpwd, String surepwd) {
        HashMap<String, String> params = new HashMap<>();
        params.put("mobile", mobile);
        params.put("smsCode", verifyCode);
        params.put("kaptchaId", mKaptchaId);
        params.put("kaptchaCode", imgVerifyCode);
        params.put("newPass", newpwd);
        params.put("confirmPass", surepwd);
        sendRequest(TradeService.getInstance().resetLoginPassword, params, true);
    }

    @Override
    protected void DataReturn(DTRequest request, Head head, Object response) {
        super.DataReturn(request, head, response);
        switch (request.getApi().getName()) {
            case "ResetLoginPasswordMsg":
                if (head.isSuccess()) {
                    showShortToast(R.string.login_verification_code_success);

                    if (null != mCountDownTimer)
                        mCountDownTimer.start();
                } else {
                    showShortToast(head.getMsg());
                }
                break;
            case "Kaptcha":
                if (head.isSuccess()) {
                    ImageVerifyCodeVo imageVerifyCodeVo;

                    try {
                        imageVerifyCodeVo = (ImageVerifyCodeVo) response;
                    } catch (Exception e) {
                        imageVerifyCodeVo = null;

                        e.printStackTrace();
                    }

                    if (null == imageVerifyCodeVo)
                        return;

                    String kaptchaImg = imageVerifyCodeVo.getKaptchaImg();

                    if (TextUtils.isEmpty(kaptchaImg))
                        return;

                    if (kaptchaImg.contains(","))
                        kaptchaImg = kaptchaImg.split(",")[1];

                    mBinding.imgVerifyCode.setImageBitmap(getBitmap(kaptchaImg));
                    mBinding.layoutImgVerifyCode.setVisibility(View.VISIBLE);
                    mBinding.etImgVerifyCode.setText("");

                    bShowImgVerifyCode = true;

                    mKaptchaId = imageVerifyCodeVo.getKaptchaId();
                }

                break;
            case "ResetLoginPassword":
                if (head.isSuccess()) {
                    showShortToast(head.getMsg());
                    AppManager.getAppManager().finishActivity();
                    showLoginDialog();
                } else {
                    showShortToast(head.getMsg());
                }
                break;
        }
    }

    private Bitmap getBitmap(String kaptchaImg) {
        Bitmap bitmap = null;

        try {
            byte[] bitmapArray = Base64.decode(kaptchaImg, Base64.DEFAULT);

            bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bitmap;
    }
}
