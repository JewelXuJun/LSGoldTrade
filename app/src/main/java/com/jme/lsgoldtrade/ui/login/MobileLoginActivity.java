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
import com.google.gson.Gson;
import com.igexin.sdk.PushManager;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.common.ui.adapter.TextWatcherAdapter;
import com.jme.common.ui.base.JMECountDownTimer;
import com.jme.common.util.RxBus;
import com.jme.common.util.SharedPreUtils;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseActivity;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.ActivityMobileLoginBinding;
import com.jme.lsgoldtrade.domain.ContractInfoVo;
import com.jme.lsgoldtrade.domain.ImageVerifyCodeVo;
import com.jme.lsgoldtrade.domain.LoginResponse;
import com.jme.lsgoldtrade.domain.UserInfoVo;
import com.jme.lsgoldtrade.service.TradeService;
import com.jme.lsgoldtrade.service.UserService;
import com.jme.lsgoldtrade.util.ValueUtils;
import java.net.ConnectException;
import java.util.HashMap;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 手机号登录
 */
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

        showLoadingDialog("");

        DTRequest request = new DTRequest(UserService.getInstance().login, params, false, true);

        Call restResponse = request.getApi().request(request.getParams());

        restResponse.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                Head head = new Head();
                Object body = "";

                if (response.raw().code() != 200) {
                    head.setSuccess(false);
                    head.setCode("" + response.raw().code());
                    head.setMsg("服务器异常");
                } else {
                    if (!request.getApi().isResponseJson()) {
                        body = response.body();
                        head.setSuccess(true);
                        head.setCode("0");
                        head.setMsg("成功");
                    } else {
                        LoginResponse dtResponse = (LoginResponse) response.body();

                        head = new Head();
                        head.setCode(dtResponse.getCode());
                        head.setMsg(dtResponse.getMsg());

                        try {
                            body = new Gson().fromJson(dtResponse.getBodyToString(),
                                    request.getApi().getEntryType());
                        } catch (Exception e) {
                            body = dtResponse.getBodyToString();
                        }
                    }
                }

                OnResult(request, head, body);
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Head head = new Head();
                final Throwable cause = t.getCause() != null ? t.getCause() : t;

                if (cause != null) {
                    if (cause instanceof ConnectException) {
                        head.setSuccess(false);
                        head.setCode("500");
                        head.setMsg(getResources().getString(com.jme.common.R.string.text_error_server));
                    } else {
                        head.setSuccess(false);
                        head.setCode("408");
                        head.setMsg(getResources().getString(com.jme.common.R.string.text_error_timeout));
                    }
                }

                OnResult(request, head, null);
            }
        });
    }

    private void loginMsg(String mobile) {
        bFlag = true;

        HashMap<String, String> params = new HashMap<>();
        params.put("mobile", mobile);

        sendRequest(UserService.getInstance().loginMsg, params, true);
    }

    private void kaptcha() {
        sendRequest(UserService.getInstance().kaptcha, new HashMap<>(), true);
    }

    private void getContractInfo() {
        String accountID = mUser.getAccountID();

        if (TextUtils.isEmpty(accountID)) {
            finish();
        } else {
            HashMap<String, String> parmas = new HashMap<>();
            parmas.put("contractId", "");
            parmas.put("accountId", accountID);

            sendRequest(TradeService.getInstance().contractInfo, parmas, true);
        }
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

                    SharedPreUtils.setString(this, SharedPreUtils.Token, userInfoVo.getToken());

                    if (!TextUtils.isEmpty(userInfoVo.getTraderId()))
                        PushManager.getInstance().bindAlias(this, userInfoVo.getTraderId());

                    RxBus.getInstance().post(Constants.RxBusConst.RXBUS_LOGIN_SUCCESS, null);
                    RxBus.getInstance().post(Constants.RxBusConst.RXBUS_FAST_MANAGEMENT_EDIT, null);

                    getContractInfo();

                    showShortToast(R.string.login_success);

                    SharedPreUtils.setString(this, SharedPreUtils.Login_Type, "Mobile");
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
                    mBinding.etImgVerifyCode.setText("");
                    mBinding.btnLogin.setEnabled(false);

                    bShowImgVerifyCode = true;

                    mKaptchaId = imageVerifyCodeVo.getKaptchaId();
                }

                mBinding.layoutImgVerifyCode.setVisibility(View.VISIBLE);

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
            ARouter.getInstance().build(Constants.ARouterUriConst.ACCOUNTLOGIN).navigation();

            finish();
        }

        public void onClickRegister() {
            ARouter.getInstance().build(Constants.ARouterUriConst.REGISTER).navigation();

            finish();
        }

        public void onClickForgetPwd() {
            ARouter.getInstance().build(Constants.ARouterUriConst.FORGETPASSWORD).navigation();
        }
    }
}
