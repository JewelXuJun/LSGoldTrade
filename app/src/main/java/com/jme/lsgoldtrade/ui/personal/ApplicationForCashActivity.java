package com.jme.lsgoldtrade.ui.personal;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseActivity;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.ActivityApplicationForCashBinding;

/**
 * 提现申请
 */
@Route(path = Constants.ARouterUriConst.APPLICATIONFORCASH)
public class ApplicationForCashActivity extends JMEBaseActivity {

    private ActivityApplicationForCashBinding mBinding;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_application_for_cash;
    }

    @Override
    protected void initView() {
        super.initView();
        initToolbar("提现申请", true);
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

        mBinding = (ActivityApplicationForCashBinding) mBindingUtil;
        mBinding.setHandlers(new ClickHandlers());
    }

    public class ClickHandlers {

        public void onClickCash() {

        }
    }
}
