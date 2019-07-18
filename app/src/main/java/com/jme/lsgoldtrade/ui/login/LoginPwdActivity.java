package com.jme.lsgoldtrade.ui.login;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.common.util.AppManager;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseActivity;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.config.User;
import com.jme.lsgoldtrade.databinding.ActivityLoginPwdBinding;
import com.jme.lsgoldtrade.service.TradeService;
import com.jme.lsgoldtrade.service.UserService;
import com.jme.lsgoldtrade.util.AESUtil;

import java.util.HashMap;

/**
 * 设置登录密码
 */
@Route(path = Constants.ARouterUriConst.LOGINPWD)
public class LoginPwdActivity extends JMEBaseActivity {

    private ActivityLoginPwdBinding mBinding;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_login_pwd;
    }

    @Override
    protected void initView() {
        super.initView();
        mBinding = (ActivityLoginPwdBinding) mBindingUtil;
        initToolbar("设置登录密码", true);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);

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

        public void onClickLoginPwd() {
            String newpwd = mBinding.newpwd.getText().toString().trim();
            String surepwd = mBinding.surepwd.getText().toString().trim();

            if (TextUtils.isEmpty(newpwd)) {
                showShortToast("请输入密码");
                return;
            }

            if (TextUtils.isEmpty(surepwd)) {
                showShortToast("请再次输入密码");
            }
            String secretNewpwd = AESUtil.encryptString2Base64(newpwd,"0J4S9B5C0J4S9B5C","16-Bytes--String").trim();
            String secretSurepwd = AESUtil.encryptString2Base64(surepwd,"0J4S9B5C0J4S9B5C","16-Bytes--String").trim();
            //setLoginPassword
            HashMap<String, String> params = new HashMap<>();
            params.put("newPass", secretNewpwd);
            params.put("confirmPass", secretSurepwd);
            sendRequest(TradeService.getInstance().setLoginPassword, params, true);
        }

        public void onClickPwdSuccess() {
            AppManager.getAppManager().finishActivity();
        }
    }

    @Override
    protected void DataReturn(DTRequest request, Head head, Object response) {
        super.DataReturn(request, head, response);
        switch (request.getApi().getName()) {
            case "SetLoginPassword":
                if (head.isSuccess()) {
                    mBinding.pwdsuccess.setVisibility(View.VISIBLE);
                    mBinding.setpwd.setVisibility(View.GONE);
                    mBinding.businessUsername.setText("密码已设置成功，请妥善保存。\n您的交易账号是" + User.getInstance().getAccount());
                } else {
                    showShortToast(head.getMsg());
                }
                break;

        }
    }
}
