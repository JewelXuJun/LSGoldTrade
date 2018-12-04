package com.jme.lsgoldtrade.ui.trade;

import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseActivity;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.ActivityCurrentHoldPositionBinding;

@Route(path = Constants.ARouterUriConst.CURRENTHOLDPOSITION)
public class CurrentHoldPositionActivity extends JMEBaseActivity {

    private ActivityCurrentHoldPositionBinding mBinding;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_current_hold_position;
    }

    @Override
    protected void initView() {
        super.initView();

        mBinding = (ActivityCurrentHoldPositionBinding) mBindingUtil;

        initToolbar(R.string.trade_curren_hold_position, true);
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

    @Override
    protected void DataReturn(DTRequest request, Head head, Object response) {
        super.DataReturn(request, head, response);
    }
}
