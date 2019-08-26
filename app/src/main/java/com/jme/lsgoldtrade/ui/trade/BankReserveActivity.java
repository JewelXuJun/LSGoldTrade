package com.jme.lsgoldtrade.ui.trade;

import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseActivity;
import com.jme.lsgoldtrade.config.Constants;

@Route(path = Constants.ARouterUriConst.BANKRESERVE)
public class BankReserveActivity extends JMEBaseActivity {
    @Override
    protected int getContentViewId() {
        return R.layout.activity_bank_reserve;
    }

    @Override
    protected void initView() {
        super.initView();

        initToolbar(R.string.trade_transfer_bank_reserve, true);
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

    public class ClickHandlers {

        public void onClickGetVerificationCode() {

        }

        public void onClickSave() {

        }

    }
}
