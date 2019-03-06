package com.jme.lsgoldtrade.ui.trade;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseActivity;
import com.jme.lsgoldtrade.config.AppConfig;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.ActivityGuaranteefundSettingBinding;
import com.jme.lsgoldtrade.service.TradeService;

import java.math.BigDecimal;
import java.util.HashMap;

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

        mBinding.etGuaranteeFund.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > AppConfig.Length_Limit) {
                        s = s.toString().subSequence(0, s.toString().indexOf(".") + (AppConfig.Length_Limit + 1));

                        mBinding.etGuaranteeFund.setText(s);
                        mBinding.etGuaranteeFund.setSelection(s.length());
                    }
                }

                if (s.toString().trim().equals(".")) {
                    s = "0" + s;

                    mBinding.etGuaranteeFund.setText(s);
                    mBinding.etGuaranteeFund.setSelection(2);
                }

                if (s.toString().startsWith("0") && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        mBinding.etGuaranteeFund.setText(s.subSequence(0, 1));
                        mBinding.etGuaranteeFund.setSelection(1);

                        return;
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                updateUIWithValidation();
            }
        });
    }

    @Override
    protected void initBinding() {
        super.initBinding();

        mBinding = (ActivityGuaranteefundSettingBinding) mBindingUtil;
        mBinding.setHandlers(new ClickHandlers());
    }

    private void updateUIWithValidation() {
        mBinding.btnConfirm.setEnabled(populated(mBinding.etGuaranteeFund));
    }

    private boolean populated(final EditText editText) {
        return editText.length() > 0;
    }

    private void getMinReserveFund() {
        String guaranteeFund = mBinding.etGuaranteeFund.getText().toString();

        if (guaranteeFund.endsWith("."))
            guaranteeFund = guaranteeFund.substring(0, guaranteeFund.length() - 1);

        HashMap<String, String> params = new HashMap<>();
        params.put("amount", String.valueOf(new BigDecimal(guaranteeFund).multiply(new BigDecimal(100)).longValue()));
        params.put("accountId", mUser.getAccountID());

        sendRequest(TradeService.getInstance().minReserveFund, params, true);
    }

    @Override
    protected void DataReturn(DTRequest request, Head head, Object response) {
        super.DataReturn(request, head, response);

        switch (request.getApi().getName()) {
            case "MinReserveFund":
                if (head.isSuccess()) {
                    showShortToast(R.string.trade_guarantee_fund_success);

                    finish();
                }

                break;
        }
    }

    public class ClickHandlers {

        public void onClickConfirm() {
            getMinReserveFund();
        }

    }
}
