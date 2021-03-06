package com.jme.lsgoldtrade.ui.login;

import android.os.Bundle;
import android.text.TextUtils;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseActivity;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.ActivitySetLoginPasswordBinding;
import com.jme.lsgoldtrade.service.TradeService;
import com.jme.lsgoldtrade.util.AESUtil;

import java.util.HashMap;

/**
 * 设置登录密码
 */
@Route(path = Constants.ARouterUriConst.SETLOGINPASSWORD)
public class SetLoginPasswordActivity extends JMEBaseActivity {

    private ActivitySetLoginPasswordBinding mBinding;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_set_login_password;
    }

    @Override
    protected void initView() {
        super.initView();

        initToolbar(R.string.login_set_password, true);
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

        mBinding = (ActivitySetLoginPasswordBinding) mBindingUtil;
        mBinding.setHandlers(new ClickHandlers());
    }

    private void setLoginPassword(String newPassword, String newPasswordConfirm) {
        String secretNewPassword = AESUtil.encryptString2Base64(newPassword, "0J4S9B5C0J4S9B5C", "16-Bytes--String").trim();
        String secretNewPasswordConfirm = AESUtil.encryptString2Base64(newPasswordConfirm, "0J4S9B5C0J4S9B5C", "16-Bytes--String").trim();

        HashMap<String, String> params = new HashMap<>();
        params.put("newPass", secretNewPassword);
        params.put("confirmPass", secretNewPasswordConfirm);

        sendRequest(TradeService.getInstance().setLoginPassword, params, true);
    }

    public class ClickHandlers {

        public void onClickSubmit() {
            String newPassword = mBinding.etPasswordNew.getText().toString().trim();
            String newPasswordConfirm = mBinding.etPasswordNewConfirm.getText().toString().trim();

            if (TextUtils.isEmpty(newPassword))
                showShortToast(R.string.login_reset_password_rule);
            else if (newPassword.length() < 6 || newPassword.length() > 18)
                showShortToast(R.string.login_reset_password_rule);
            else if (!isLetterDigit(newPassword))
                showShortToast(R.string.login_reset_password_rule);
            else if (TextUtils.isEmpty(newPasswordConfirm))
                showShortToast(R.string.login_reset_password_new_confirm_input);
            else if (newPasswordConfirm.length() < 6 || newPasswordConfirm.length() > 18)
                showShortToast(R.string.login_reset_password_new_confirm_input);
            else if (!newPassword.equals(newPasswordConfirm))
                showShortToast(R.string.login_reset_password_not_equal);
            else
                setLoginPassword(newPassword, newPasswordConfirm);
        }

    }

    @Override
    protected void DataReturn(DTRequest request, Head head, Object response) {
        super.DataReturn(request, head, response);

        switch (request.getApi().getName()) {
            case "SetLoginPassword":
                if (head.isSuccess()) {
                    showShortToast(R.string.transaction_setting_success);

//                    ARouter.getInstance()
//                            .build(Constants.ARouterUriConst.WITHHOLDCONTRACT)
//                            .withString("Resource", "Trade")
//                            .navigation();

                    finish();
                }

                break;

        }
    }

    private boolean isLetterDigit(String str) {
        boolean isDigit = false;//定义一个boolean值，用来表示是否包含数字
        boolean isLetter = false;//定义一个boolean值，用来表示是否包含字母
        for (int i = 0; i < str.length(); i++) {
            if (Character.isDigit(str.charAt(i))) {   //用char包装类中的判断数字的方法判断每一个字符
                isDigit = true;
            } else if (Character.isLetter(str.charAt(i))) {  //用char包装类中的判断字母的方法判断每一个字符
                isLetter = true;
            }
        }
        String regex = "^[a-zA-Z0-9]+$";
        boolean isRight = isDigit && isLetter && str.matches(regex);
        return isRight;
    }
}
