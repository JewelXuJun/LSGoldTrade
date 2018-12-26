package com.jme.lsgoldtrade.ui.login;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.common.ui.adapter.TextWatcherAdapter;
import com.jme.common.util.Base64;
import com.jme.common.util.SharedPreUtils;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseActivity;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.ActivityAccountLoginBinding;
import com.jme.lsgoldtrade.domain.ImageVerifyCodeVo;
import com.jme.lsgoldtrade.domain.UserInfoVo;
import com.jme.lsgoldtrade.service.UserService;
import com.jme.lsgoldtrade.util.ValueUtils;

import java.util.HashMap;

@Route(path = Constants.ARouterUriConst.ACCOUNTLOGIN)
public class AccountLoginActivity extends JMEBaseActivity {

    private ActivityAccountLoginBinding mBinding;

    private boolean bShowImgVerifyCode = false;
    private String mKaptchaId;

    private TextWatcher mWatcher;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_account_login;
    }

    @Override
    protected void initView() {
        super.initView();

        mBinding = (ActivityAccountLoginBinding) mBindingUtil;

        mBinding.etAccount.setText(SharedPreUtils.getString(this, SharedPreUtils.Login_Account));
        mBinding.tvLoginMobile.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
    }

    @Override
    protected void initListener() {
        super.initListener();

        mWatcher = validationTextWatcher();

        mBinding.etAccount.addTextChangedListener(mWatcher);
        mBinding.etPassword.addTextChangedListener(mWatcher);
    }

    @Override
    protected void initBinding() {
        super.initBinding();

        mBinding.setHandlers(new ClickHandlers());
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
                ? populated(mBinding.etAccount) && populated(mBinding.etPassword) && populated(mBinding.etImgVerifyCode)
                : populated(mBinding.etAccount) && populated(mBinding.etPassword));
    }

    private boolean populated(final EditText editText) {
        return editText.length() > 0;
    }

    private void doLogin() {
        String account = mBinding.etAccount.getText().toString();
        String password = mBinding.etPassword.getText().toString();
        String imgVerifyCode = mBinding.etImgVerifyCode.getText().toString();

        if (!ValueUtils.isPhoneNumber(account))
            showShortToast(R.string.login_account_error);
        else if (!ValueUtils.isPasswordRight(password))
            showShortToast(R.string.login_password_error);
        else if (bShowImgVerifyCode && TextUtils.isEmpty(imgVerifyCode))
            showShortToast(R.string.login_img_verify_code_error);
        else
            login(account, password, imgVerifyCode);
    }

    private void login(String account, String password, String imgVerifyCode) {
        HashMap<String, String> params = new HashMap<>();
        params.put("loginName", account);
        params.put("password", ValueUtils.MD5(account + password));
        params.put("ip", null == ValueUtils.getLocalIPAddress() ? "" : ValueUtils.getLocalIPAddress());
        params.put("loginType", "1");

        sendRequest(UserService.getInstance().login, params, true);
    }

    private void kaptcha() {
        sendRequest(UserService.getInstance().kaptcha, new HashMap<>(), false);
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

                    showShortToast(R.string.login_success);
                    SharedPreUtils.setString(this, SharedPreUtils.Login_Account, mBinding.etAccount.getText().toString());

                    finish();
                } else {
                    kaptcha();
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

                    byte[] decodedString = Base64.decode(kaptchaImg.getBytes());
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                    mBinding.imgVerifyCode.setImageBitmap(decodedByte);
                    mBinding.layoutImgVerifyCode.setVisibility(View.VISIBLE);

                    bShowImgVerifyCode = true;

                    mKaptchaId = imageVerifyCodeVo.getKaptchaId();
                }

                break;
        }
    }

    public class ClickHandlers {

        public void onClickCancel() {
            finish();
        }

        public void onClickLoadImageVerifyCode() {
            kaptcha();
        }

        public void onClickLogin() {
            doLogin();
        }

        public void onClickLoginMobile() {
            ARouter.getInstance()
                    .build(Constants.ARouterUriConst.MOBILELOGIN)
                    .navigation();

            finish();
        }

        public void onClickRegister() {

        }

    }
}
