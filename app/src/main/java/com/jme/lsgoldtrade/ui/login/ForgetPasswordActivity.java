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
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseActivity;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.ActivityForgetPasswordBinding;
import com.jme.lsgoldtrade.domain.ImageVerifyCodeVo;
import com.jme.lsgoldtrade.service.TradeService;
import com.jme.lsgoldtrade.service.UserService;
import com.jme.lsgoldtrade.util.AESUtil;
import com.jme.lsgoldtrade.util.ValueUtils;

import java.util.HashMap;

/**
 * 忘记密码
 */
@Route(path = Constants.ARouterUriConst.FORGETPASSWORD)
public class ForgetPasswordActivity extends JMEBaseActivity {

    private ActivityForgetPasswordBinding mBinding;

    private JMECountDownTimer mCountDownTimer;

    private boolean bFlag = false;
    private boolean bShowImgVerifyCode = false;
    private String mKaptchaId;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_forget_password;
    }

    @Override
    protected void initView() {
        super.initView();

        initToolbar(R.string.login_reset_passord, true);

        mBinding = (ActivityForgetPasswordBinding) mBindingUtil;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);

        mCountDownTimer = new JMECountDownTimer(60000, 1000, mBinding.btnVerificationCode, getString(R.string.trade_get_verification_code));

        kaptcha();
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

    private void doReset() {
        String mobile = mBinding.etMobile.getText().toString();
        String imgVerifyCode = mBinding.etImgVerifyCode.getText().toString();
        String verifyCode = mBinding.etVerifyCode.getText().toString();
        String newPassword = mBinding.etPasswordNew.getText().toString().trim();
        String newPasswordConfirm = mBinding.etPasswordNewConfirm.getText().toString().trim();

        if (!ValueUtils.isPhoneNumber(mobile))
            showShortToast(R.string.login_mobile_error);
        else if (bShowImgVerifyCode && TextUtils.isEmpty(imgVerifyCode))
            showShortToast(R.string.login_img_verify_code_error);
        else if (!bFlag)
            showShortToast(R.string.login_verification_code_unget);
        else if (TextUtils.isEmpty(verifyCode))
            showShortToast(R.string.login_verification_code_error);
        else if (verifyCode.length() < 6)
            showShortToast(R.string.login_verification_code_error);
        else if (TextUtils.isEmpty(newPassword))
            showShortToast(R.string.login_reset_passord_rule);
        else if (newPassword.length() < 6 || newPassword.length() > 18)
            showShortToast(R.string.login_reset_passord_rule);
        else if (TextUtils.isEmpty(newPasswordConfirm))
            showShortToast(R.string.login_reset_passord_new_confirm_input);
        else if (newPasswordConfirm.length() < 6 || newPasswordConfirm.length() > 18)
            showShortToast(R.string.login_reset_passord_new_confirm_input);
        else if (!newPassword.equals(newPasswordConfirm))
            showShortToast(R.string.login_reset_passord_not_equal);
        else
            resetLoginPassword(mobile, verifyCode, imgVerifyCode, newPassword, newPasswordConfirm);
    }

    private void kaptcha() {
        sendRequest(UserService.getInstance().kaptcha, new HashMap<>(), true, false, false);
    }

    private void resetLoginPasswordMsg(String mobile) {
        bFlag = true;

        HashMap<String, String> params = new HashMap<>();
        params.put("mobile", mobile);

        sendRequest(TradeService.getInstance().resetLoginPasswordMsg, params, true);
    }

    private void resetLoginPassword(String mobile, String verifyCode, String imgVerifyCode, String newPassword, String newPasswordConfirm) {
        HashMap<String, String> params = new HashMap<>();
        params.put("mobile", mobile);
        params.put("smsCode", verifyCode);
        params.put("kaptchaId", mKaptchaId);
        params.put("kaptchaCode", imgVerifyCode);
        params.put("newPass",  AESUtil.encryptString2Base64(newPassword, "0J4S9B5C0J4S9B5C", "16-Bytes--String").trim());
        params.put("confirmPass", AESUtil.encryptString2Base64(newPasswordConfirm, "0J4S9B5C0J4S9B5C", "16-Bytes--String").trim());

        sendRequest(TradeService.getInstance().resetLoginPassword, params, true);
    }

    @Override
    protected void DataReturn(DTRequest request, Head head, Object response) {
        super.DataReturn(request, head, response);

        switch (request.getApi().getName()) {
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
            case "ResetLoginPasswordMsg":
                if (head.isSuccess()) {
                    showShortToast(R.string.login_verification_code_success);

                    if (null != mCountDownTimer)
                        mCountDownTimer.start();
                }

                break;
            case "ResetLoginPassword":
                if (head.isSuccess()) {
                    showShortToast(R.string.login_reset_passord_success);

                    finish();
                }

                break;
        }
    }

    public class ClickHandlers {

        public void onClickLoadImageVerifyCode() {
            kaptcha();
        }

        public void onClickGetVerificationCode() {
            String mobile = mBinding.etMobile.getText().toString().trim();

            if (!ValueUtils.isPhoneNumber(mobile))
                showShortToast(R.string.login_mobile_error);
            else
                resetLoginPasswordMsg(mobile);
        }

        public void onClickResetPassword() {
            doReset();
        }
    }

}
