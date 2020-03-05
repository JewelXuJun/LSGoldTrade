package com.jme.lsgoldtrade.ui.transaction;

import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseActivity;
import com.jme.lsgoldtrade.config.Constants;

@Route(path = Constants.ARouterUriConst.BINDSTATUSHF)
public class BindStatusHFActivity extends JMEBaseActivity {
    @Override
    protected int getContentViewId() {
        return R.layout.activity_bind_status_hf;
    }

    @Override
    protected void initView() {
        super.initView();

        initToolbar(R.string.transaction_open_account_hf_download_title, true);
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
    }

    public class ClickHandlers {

        public void onClickCopy() {

        }

    }
}
