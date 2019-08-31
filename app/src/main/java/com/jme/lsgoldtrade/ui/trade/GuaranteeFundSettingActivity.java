package com.jme.lsgoldtrade.ui.trade;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.widget.EditText;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.common.util.BigDecimalUtil;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseActivity;
import com.jme.lsgoldtrade.config.AppConfig;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.ActivityGuaranteefundSettingBinding;
import com.jme.lsgoldtrade.service.TradeService;

import java.math.BigDecimal;
import java.util.HashMap;

/**
 * 保底资金
 */
@Route(path = Constants.ARouterUriConst.GUARANTEEFUNDSETTINGACTIVITY)
public class GuaranteeFundSettingActivity extends JMEBaseActivity {

    private ActivityGuaranteefundSettingBinding mBinding;

    private GuaranteeFundPopUpWindow mWindow;

    private String mTotal;
    private float mWarnth;
    private float mForcecloseth;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_guaranteefund_setting;
    }

    @Override
    protected void initView() {
        super.initView();

        initToolbar(R.string.trade_guarantee_fund_text, true);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);

        mTotal = getIntent().getStringExtra("Total");
        mWarnth = getIntent().getFloatExtra("Warnth", 1.2f);
        mForcecloseth = getIntent().getFloatExtra("Forcecloseth", 1.1f);

        setMessage(String.format(getResources().getString(R.string.trade_guarantee_fund_message), "100%",
                new BigDecimal(mWarnth).multiply(new BigDecimal(100)).setScale(0, BigDecimal.ROUND_DOWN).toPlainString() + "%",
                new BigDecimal(mForcecloseth).multiply(new BigDecimal(100)).setScale(0, BigDecimal.ROUND_DOWN).toPlainString() + "%"));

        mWindow = new GuaranteeFundPopUpWindow(this);
        mWindow.setOutsideTouchable(true);
        mWindow.setFocusable(true);
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

            }

        });
    }

    @Override
    protected void initBinding() {
        super.initBinding();

        mBinding = (ActivityGuaranteefundSettingBinding) mBindingUtil;
        mBinding.setHandlers(new ClickHandlers());
    }

    private void setMessage(String value) {
        SpannableString spannableString = new SpannableString(value);
        spannableString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this, R.color.color_red)),
                value.indexOf("5、"), value.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mBinding.tvMessage.setMovementMethod(LinkMovementMethod.getInstance());
        mBinding.tvMessage.setText(spannableString);
    }

    private void getMinReserveFund() {
        if (null == mUser || !mUser.isLogin())
            return;

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
            if (null == mWindow || mWindow.isShowing())
                return;

            if (TextUtils.isEmpty(mTotal)) {
                showShortToast(R.string.trade_guarantee_total_error);
            } else {
                String value = mBinding.etGuaranteeFund.getText().toString();
                String message;

                if (TextUtils.isEmpty(value)) {
                    showShortToast(R.string.trade_guarantee_fund_message1);
                } else {
                    BigDecimal riskRate;

                    if (value.endsWith("."))
                        value = value.substring(0, value.length() - 1);

                    if (new BigDecimal(value).compareTo(new BigDecimal(0)) == 0)
                        riskRate = new BigDecimal(0);
                    else
                        riskRate = new BigDecimal(mTotal).divide(new BigDecimal(value), 4, BigDecimal.ROUND_HALF_UP);

                    if (riskRate.compareTo(new BigDecimal(0)) == 0) {
                        message = getString(R.string.trade_guarantee_fund_message0);
                    } else {
                        if (new BigDecimal(value).compareTo(new BigDecimal(mTotal)) == 1) {
                            message = getString(R.string.trade_guarantee_fund_message5);
                        } else {
                            String riskRateValue = BigDecimalUtil.formatRate(riskRate.multiply(new BigDecimal(100)).toPlainString());

                            if (riskRate.compareTo(new BigDecimal(mForcecloseth)) == -1)
                                message = String.format(getString(R.string.trade_guarantee_fund_message2), riskRateValue);
                            else if (riskRate.compareTo(new BigDecimal(mForcecloseth)) == 1 && riskRate.compareTo(new BigDecimal(mWarnth)) == -1)
                                message = String.format(getString(R.string.trade_guarantee_fund_message3), riskRateValue);
                            else
                                message = String.format(getString(R.string.trade_guarantee_fund_message4), riskRateValue);
                        }
                    }

                    mWindow.setData(message, (view) -> {
                        getMinReserveFund();

                        mWindow.dismiss();
                    });
                    mWindow.showAtLocation(mBinding.etGuaranteeFund, Gravity.CENTER, 0, 0);
                }
            }
        }
    }
}
