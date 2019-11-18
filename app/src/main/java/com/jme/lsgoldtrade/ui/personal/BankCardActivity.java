package com.jme.lsgoldtrade.ui.personal;

import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseActivity;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.ActivityBankcardBinding;

@Route(path = Constants.ARouterUriConst.BANKCARD)
public class BankCardActivity extends JMEBaseActivity {

    private ActivityBankcardBinding mBinding;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_bankcard;
    }

    @Override
    protected void initView() {
        super.initView();

        initToolbar(R.string.increment_bankcard, true);
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

        mBinding = (ActivityBankcardBinding) mBindingUtil;
    }

    @Override
    protected void DataReturn(DTRequest request, Head head, Object response) {
        super.DataReturn(request, head, response);
    }
}