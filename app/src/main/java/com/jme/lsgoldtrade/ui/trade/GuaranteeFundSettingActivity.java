package com.jme.lsgoldtrade.ui.trade;

import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseActivity;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.ActivityGuaranteefundSettingBinding;

@Route(path = Constants.ARouterUriConst.GUARANTEEFUNDSETTINGACTIVITY)
public class GuaranteeFundSettingActivity extends JMEBaseActivity {

    private ActivityGuaranteefundSettingBinding mBinding;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_guaranteefund_setting;
    }

    @Override
    protected void initView() {
        super.initView();

        initToolbar(R.string.trade_guarantee_fund_title, true);
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

        mBinding = (ActivityGuaranteefundSettingBinding) mBindingUtil;
    }

    @Override
    protected void DataReturn(DTRequest request, Head head, Object response) {
        super.DataReturn(request, head, response);
    }

    public class ClickHandlers {

        public void onClickConfirm() {

        }

    }
}
