package com.jme.lsgoldtrade.ui.transaction;

import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseActivity;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.ActivityBindAccountHfBinding;
import com.jme.lsgoldtrade.service.TradeService;

import java.util.HashMap;

@Route(path = Constants.ARouterUriConst.BINDACCOUNTHF)
public class BindAccountHFActivity extends JMEBaseActivity {

    private ActivityBindAccountHfBinding mBinding;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_bind_account_hf;
    }

    @Override
    protected void initView() {
        super.initView();

        initToolbar(R.string.transaction_bind_account, true);
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

        mBinding = (ActivityBindAccountHfBinding) mBindingUtil;
        mBinding.setHandlers(new ClickHandlers());
    }

    private void hfbBindMsg() {
        sendRequest(TradeService.getInstance().hfbBindMsg, new HashMap<>(), true);
    }

    private void HFBBind() {
        HashMap<String, String> params = new HashMap<>();
        params.put("name", mBinding.tvName.getText().toString());
        params.put("idCard", mBinding.tvIdCard.getText().toString());
        params.put("smsCode", mBinding.etVerifyCode.getText().toString());

        sendRequest(TradeService.getInstance().HFBBind, params, true);
    }

    @Override
    protected void DataReturn(DTRequest request, Head head, Object response) {
        super.DataReturn(request, head, response);

        switch (request.getApi().getName()) {
            case "HfbBindMsg":

                break;
            case "HFBBind":

                break;
        }
    }

    public class ClickHandlers {

        public void onClickSoftWareAgreement() {

        }

        public void onClickBusinessAgreement() {

        }

        public void onClickGetVerificationCode() {

        }

        public void onClickBind() {

        }
    }
}
