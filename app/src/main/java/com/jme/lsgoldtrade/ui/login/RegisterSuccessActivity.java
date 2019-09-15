package com.jme.lsgoldtrade.ui.login;

import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseActivity;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.ActivityRegisterSuccessBinding;
import com.jme.lsgoldtrade.util.IntentUtils;

/**
 * 注册成功
 */
@Route(path = Constants.ARouterUriConst.REGISTERSUCCESS)
public class RegisterSuccessActivity extends JMEBaseActivity {

    private ActivityRegisterSuccessBinding mBinding;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_register_success;
    }

    @Override
    protected void initView() {
        super.initView();

        initToolbar(R.string.register, true);
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

        mBinding = (ActivityRegisterSuccessBinding) mBindingUtil;
        mBinding.setHandlers(new ClickHandlers());
    }

    public class ClickHandlers {

        public void onClickOpenAccount() {
            if (IntentUtils.isWeChatAvilible(RegisterSuccessActivity.this)) {
                IntentUtils.intentICBCSmall(RegisterSuccessActivity.this);

                finish();
            } else {
                showShortToast(R.string.text_wechat_uninstalled);
            }
        }
    }
}
