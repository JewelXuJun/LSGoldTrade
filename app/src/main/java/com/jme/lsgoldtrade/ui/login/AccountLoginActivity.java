package com.jme.lsgoldtrade.ui.login;

import android.graphics.Paint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.common.ui.adapter.TextWatcherAdapter;
import com.jme.common.util.SharedPreUtils;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseActivity;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.ActivityAccountLoginBinding;
import com.jme.lsgoldtrade.domain.UserInfoVo;
import com.jme.lsgoldtrade.service.UserService;
import com.jme.lsgoldtrade.util.ValueUtils;

import java.util.HashMap;

@Route(path = Constants.ARouterUriConst.ACCOUNTLOGIN)
public class AccountLoginActivity extends JMEBaseActivity {

    private ActivityAccountLoginBinding mBinding;

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
        mBinding.btnLogin.setEnabled(populated(mBinding.etAccount) && populated(mBinding.etPassword));
    }

    private boolean populated(final EditText editText) {
        return editText.length() > 0;
    }

    private void doLogin() {
        String account = mBinding.etAccount.getText().toString();
        String password = mBinding.etPassword.getText().toString();

        if (!ValueUtils.isPhoneNumber(account))
            showShortToast(R.string.login_account_error);
        else if (!ValueUtils.isPasswordRight(password))
            showShortToast(R.string.login_password_error);
        else
            login(account, password);
    }

    private void login(String account, String password) {
        HashMap<String, String> params = new HashMap<>();
        params.put("traderid", account);
        params.put("password", ValueUtils.MD5(account + password));
        params.put("ip", null == ValueUtils.getLocalIPAddress() ? "" : ValueUtils.getLocalIPAddress());

        sendRequest(UserService.getInstance().login, params, true);
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
                }

                break;
        }
    }

    public class ClickHandlers {

        public void onClickCancel() {
            finish();
        }

        public void onClickNews() {
            ARouter.getInstance()
                    .build(Constants.ARouterUriConst.NEWSACTIVITY)
                    .navigation();
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
