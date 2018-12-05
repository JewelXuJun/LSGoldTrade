package com.jme.lsgoldtrade.ui.personal;

import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseActivity;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.ActivityAboutBinding;

@Route(path = Constants.ARouterUriConst.ABOUT)
public class AboutActivity extends JMEBaseActivity {

    private ActivityAboutBinding mBinding;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_about;
    }

    @Override
    protected void initView() {
        super.initView();

        mBinding = (ActivityAboutBinding) mBindingUtil;

        initToolbar(R.string.setting_about, true);
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

        public void onClickPublicNumber() {

        }

        public void onClickFunction() {

        }

        public void onClickContact() {

        }

    }
}
