package com.jme.lsgoldtrade.ui.login;

import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.jme.common.util.RxBus;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseActivity;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.config.User;
import com.jme.lsgoldtrade.databinding.ActivitySetLoginPasswordSuccessBinding;

@Route(path = Constants.ARouterUriConst.SETLOGINPASSWORDSUCCESS)
public class SetLoginPasswordSuccessActivity extends JMEBaseActivity {

    private ActivitySetLoginPasswordSuccessBinding mBinding;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_set_login_password_success;
    }

    @Override
    protected void initView() {
        super.initView();

        initToolbar(R.string.login_set_password, true);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);

        mBinding.tvAccount.setText(User.getInstance().getTraderId());
    }

    @Override
    protected void initListener() {
        super.initListener();
    }

    @Override
    protected void initBinding() {
        super.initBinding();

        mBinding = (ActivitySetLoginPasswordSuccessBinding) mBindingUtil;
        mBinding.setHandlers(new ClickHandlers());
    }

    public class ClickHandlers {

        public void onClickGotoTrade() {
            RxBus.getInstance().post(Constants.RxBusConst.RXBUS_TRADE, null);
            ARouter.getInstance().build(Constants.ARouterUriConst.MAIN).navigation();

            finish();
        }

    }
}
