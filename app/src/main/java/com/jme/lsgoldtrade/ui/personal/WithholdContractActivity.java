package com.jme.lsgoldtrade.ui.personal;

import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseActivity;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.ActivityWithholdContractBinding;

@Route(path = Constants.ARouterUriConst.WITHHOLDCONTRACT)
public class WithholdContractActivity extends JMEBaseActivity {

    private ActivityWithholdContractBinding mBinding;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_withhold_contract;
    }

    @Override
    protected void initView() {
        super.initView();

        initToolbar(R.string.incrementaccount_withhold_contract, true);
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

        mBinding = (ActivityWithholdContractBinding) mBindingUtil;
        mBinding.setHandlers(new ClickHandlers());
    }

    @Override
    protected void DataReturn(DTRequest request, Head head, Object response) {
        super.DataReturn(request, head, response);
    }

    public class ClickHandlers {

        public void onClickTips() {

        }

        public void onClickGetVerificationCode() {

        }

        public void onCliclAggrement() {

        }

        public void onCliclOpen() {

        }

    }

}
