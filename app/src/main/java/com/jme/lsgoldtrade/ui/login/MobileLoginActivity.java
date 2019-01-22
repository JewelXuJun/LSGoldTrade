package com.jme.lsgoldtrade.ui.login;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.common.ui.adapter.TextWatcherAdapter;
import com.jme.common.ui.base.JMECountDownTimer;
import com.jme.common.util.SharedPreUtils;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseActivity;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.ActivityMobileLoginBinding;
import com.jme.lsgoldtrade.domain.ContractInfoVo;
import com.jme.lsgoldtrade.domain.ImageVerifyCodeVo;
import com.jme.lsgoldtrade.domain.UserInfoVo;
import com.jme.lsgoldtrade.service.TradeService;
import com.jme.lsgoldtrade.service.UserService;
import com.jme.lsgoldtrade.util.ValueUtils;

import java.util.HashMap;
import java.util.List;

@Route(path = Constants.ARouterUriConst.MOBILELOGIN)
public class MobileLoginActivity extends JMEBaseActivity {

    private ActivityMobileLoginBinding mBinding;

    private boolean bFlag = false;
    private boolean bShowImgVerifyCode = false;
    private String mKaptchaId;

    private TextWatcher mWatcher;
    private JMECountDownTimer mCountDownTimer;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_mobile_login;
    }

    @Override
    protected void initView() {
        super.initView();

        mBinding = (ActivityMobileLoginBinding) mBindingUtil;

        String mobile = SharedPreUtils.getString(this, SharedPreUtils.Login_Mobile);

        mBinding.etMobile.setText(mobile);
        mBinding.etMobile.setSelection(TextUtils.isEmpty(mobile) ? 0 : mobile.length());
        mBinding.tvLoginAccount.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);

        mCountDownTimer = new JMECountDownTimer(60000, 1000,
                mBinding.btnVerificationCode, getString(R.string.trade_get_verification_code));
    }

    @Override
    protected void initListener() {
        super.initListener();

        mWatcher = validationTextWatcher();

        mBinding.etMobile.addTextChangedListener(mWatcher);
        mBinding.etVerifyCode.addTextChangedListener(mWatcher);
        mBinding.etImgVerifyCode.addTextChangedListener(mWatcher);
    }

    @Override
    protected void initBinding() {
        super.initBinding();

        mBinding.setHandlers(new ClickHandlers());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (null != mCountDownTimer)
            mCountDownTimer.cancel();
    }

    private TextWatcher validationTextWatcher() {
        return new TextWatcherAdapter() {
            @Override
            public void afterTextChanged(final Editable s) {
                updateUIWithValidation();
            }
        };
    }

    private void updateUIWithValidation() {
        mBinding.btnLogin.setEnabled(bShowImgVerifyCode
                ? populated(mBinding.etMobile) && populated(mBinding.etVerifyCode) && populated(mBinding.etImgVerifyCode)
                : populated(mBinding.etMobile) && populated(mBinding.etVerifyCode));
    }

    private boolean populated(final EditText editText) {
        return editText.length() > 0;
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

    private void doLogin() {
        String mobile = mBinding.etMobile.getText().toString();
        String verifyCode = mBinding.etVerifyCode.getText().toString();
        String imgVerifyCode = mBinding.etImgVerifyCode.getText().toString();

        if (!ValueUtils.isPhoneNumber(mobile))
            showShortToast(R.string.login_mobile_error);
        else if (!bFlag)
            showShortToast(R.string.login_verification_code_unget);
        else if (verifyCode.length() < 6)
            showShortToast(R.string.login_verification_code_error);
        else if (bShowImgVerifyCode && TextUtils.isEmpty(imgVerifyCode))
            showShortToast(R.string.login_img_verify_code_error);
        else
            login(mobile, verifyCode, imgVerifyCode);
    }

    private void login(String mobile, String verifyCode, String imgVerifyCode) {
        HashMap<String, String> params = new HashMap<>();
        params.put("loginName", mobile);
        params.put("password", verifyCode);
        params.put("loginIP", null == ValueUtils.getLocalIPAddress() ? "" : ValueUtils.getLocalIPAddress());
        params.put("loginType", "2");
        if (bShowImgVerifyCode) {
            params.put("kaptchaId", mKaptchaId);
            params.put("kaptchaCode", imgVerifyCode);
        }

        sendRequest(UserService.getInstance().login, params, true);
    }

    private void loginMsg(String mobile) {
        bFlag = true;

        HashMap<String, String> params = new HashMap<>();
        params.put("mobile", mobile);

        sendRequest(UserService.getInstance().loginMsg, params, true);
    }

    private void kaptcha() {
        sendRequest(UserService.getInstance().kaptcha, new HashMap<>(), true, false, false);
    }

    private void getContractInfo() {
        HashMap<String, String> parmas = new HashMap<>();
        parmas.put("contractId", "");
        parmas.put("accountId", mUser.getAccountID());

        sendRequest(TradeService.getInstance().contractInfo, parmas, true);
    }

    @Override
    protected void DataReturn(DTRequest request, Head head, Object response) {
        super.DataReturn(request, head, response);

        switch (request.getApi().getName()) {
            case "Login":
                if (head.isSuccess()) {
                    UserInfoVo userInfoVo;

                    try {
                        userInfoVo = (UserInfoVo) response;
                    } catch (Exception e) {
                        userInfoVo = null;

                        e.printStackTrace();
                    }

                    if (null == userInfoVo)
                        return;

                    mUser.login(userInfoVo);

                    getContractInfo();

                    showShortToast(R.string.login_success);
                    SharedPreUtils.setString(this, SharedPreUtils.Login_Mobile, mBinding.etMobile.getText().toString());
                } else {
                    kaptcha();
                }

                break;
            case "LoginMsg":
                if (head.isSuccess()) {
                    showShortToast(R.string.login_verification_code_success);

                    if (null != mCountDownTimer)
                        mCountDownTimer.start();
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
                    mBinding.btnLogin.setEnabled(false);

                    bShowImgVerifyCode = true;

                    mKaptchaId = imageVerifyCodeVo.getKaptchaId();
                }

                break;
            case "ContractInfo":
                if (head.isSuccess()) {
                    List<ContractInfoVo> list;

                    try {
                        list = (List<ContractInfoVo>) response;
                    } catch (Exception e) {
                        list = null;

                        e.printStackTrace();
                    }

                    mContract.setContractList(list);
                }

                finish();

                break;
        }
    }

    public class ClickHandlers {

        public void onClickCancel() {
            finish();
        }

        public void onClickGetVerificationCode() {
            String mobile = mBinding.etMobile.getText().toString();

            if (!ValueUtils.isPhoneNumber(mobile))
                showShortToast(R.string.login_mobile_error);
            else
                loginMsg(mobile);
        }

        public void onClickLoadImageVerifyCode() {
            kaptcha();
        }

        public void onClickLogin() {
            doLogin();
        }

        public void onClickLgoinAccount() {
            ARouter.getInstance()
                    .build(Constants.ARouterUriConst.ACCOUNTLOGIN)
                    .navigation();

            finish();
        }

        public void onClickRegister() {
            ARouter.getInstance()
                    .build(Constants.ARouterUriConst.JMEWEBVIEW)
                    .withString("title", mContext.getResources().getString(R.string.personal_open_account_online))
                    .withString("url", Constants.HttpConst.URL_OPEN_ACCOUNT)
                    .navigation();
        }

    }
}
