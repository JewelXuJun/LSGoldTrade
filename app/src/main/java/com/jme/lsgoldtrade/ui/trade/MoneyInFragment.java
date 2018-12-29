package com.jme.lsgoldtrade.ui.trade;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;

import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.common.ui.base.JMECountDownTimer;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseFragment;
import com.jme.lsgoldtrade.config.AppConfig;
import com.jme.lsgoldtrade.databinding.FragmentMoneyInBinding;
import com.jme.lsgoldtrade.domain.AccountVo;
import com.jme.lsgoldtrade.domain.UserInfoVo;
import com.jme.lsgoldtrade.service.TradeService;
import com.jme.lsgoldtrade.service.UserService;
import com.jme.lsgoldtrade.util.MarketUtil;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.math.BigDecimal;
import java.util.HashMap;

public class MoneyInFragment extends JMEBaseFragment implements OnRefreshListener {

    private FragmentMoneyInBinding mBinding;

    private boolean bHidden = false;
    private boolean bVisibleToUser = false;
    private boolean bFlag = false;
    private String mCurAccountBalance;

    private JMECountDownTimer mCountDownTimer;

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_money_in;
    }

    @Override
    protected void initView() {
        super.initView();

        mBinding = (FragmentMoneyInBinding) mBindingUtil;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);

        mCountDownTimer = new JMECountDownTimer(60000, 1000,
                mBinding.btnVerificationCode, getString(R.string.trade_get_verification_code));

        if (null != mUser) {
            UserInfoVo userInfoVo = mUser.getCurrentUser();

            mBinding.tvMobileNumber.setText(null == userInfoVo ? "" : userInfoVo.getMobile());
        }

    }

    @Override
    protected void initListener() {
        super.initListener();

        mBinding.swipeRefreshLayout.setOnRefreshListener(this);

        mBinding.etTransferAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > AppConfig.Lentth_Limit) {
                        s = s.toString().subSequence(0, s.toString().indexOf(".") + (AppConfig.Lentth_Limit + 1));

                        mBinding.etTransferAmount.setText(s);
                        mBinding.etTransferAmount.setSelection(s.length());
                    }
                }

                if (s.toString().trim().equals(".")) {
                    s = "0" + s;

                    mBinding.etTransferAmount.setText(s);
                    mBinding.etTransferAmount.setSelection(2);
                }

                if (s.toString().startsWith("0") && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        mBinding.etTransferAmount.setText(s.subSequence(0, 1));
                        mBinding.etTransferAmount.setSelection(1);

                        return;
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                updateUIWithValidation();
            }
        });

        mBinding.etVerificationCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                updateUIWithValidation();
            }
        });
    }

    @Override
    public void initBinding() {
        super.initBinding();

        mBinding.setHandlers(new ClickHandlers());
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        bVisibleToUser = isVisibleToUser;

        /*if (isVisibleToUser && null != mBinding)
            getAccount(true);*/
    }

    @Override
    public void onResume() {
        super.onResume();

       /* if (bVisibleToUser)
            getAccount(true);*/
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (null != mCountDownTimer)
            mCountDownTimer.cancel();
    }

    private void updateUIWithValidation() {
        mBinding.btnSubmit.setEnabled(populated(mBinding.etTransferAmount) && populated(mBinding.etVerificationCode));
    }

    private boolean populated(final EditText editText) {
        return editText.length() > 0;
    }

    private void doSubmit() {
        String mobile = mBinding.tvMobileNumber.getText().toString();
        String amount = mBinding.etTransferAmount.getText().toString();
        String verifyCode = mBinding.etVerificationCode.getText().toString();

        if (amount.endsWith("."))
            amount = amount.substring(0, amount.length() - 1);

        if (new BigDecimal(mCurAccountBalance).compareTo(new BigDecimal(0)) != 1)
            showShortToast(R.string.trade_amount_error);
        else if (TextUtils.isEmpty(mobile))
            showShortToast(R.string.trade_mobile_error);
        else if (new BigDecimal(amount).compareTo(new BigDecimal(0)) == 0)
            showShortToast(R.string.trade_money_int_min_error);
        else if (new BigDecimal(amount).compareTo(new BigDecimal(mCurAccountBalance)) == 1)
            showShortToast(R.string.trade_money_int_max_error);
        else if (!bFlag)
            showShortToast(R.string.login_verification_code_unget);
        else if (verifyCode.length() < 6)
            showShortToast(R.string.login_verification_code_error);
        else
            inoutMoney(amount);
    }

    private void getAccount(boolean enable) {
        sendRequest(TradeService.getInstance().account, new HashMap<>(), enable);
    }

    private void fundInoutMsg() {
        bFlag = true;

        sendRequest(UserService.getInstance().fundInoutMsg, new HashMap<>(), true);
    }

    private void inoutMoney(String amount) {
        HashMap<String, String> params = new HashMap<>();
        params.put("amount", new BigDecimal(amount).multiply(new BigDecimal(100)).toPlainString());
        params.put("direction", "0");

        sendRequest(TradeService.getInstance().inoutmoney, params, true);
    }

    @Override
    protected void DataReturn(DTRequest request, Head head, Object response) {
        super.DataReturn(request, head, response);

        switch (request.getApi().getName()) {
            case "Account":
                if (head.isSuccess()) {
                    AccountVo accountVo;

                    try {
                        accountVo = (AccountVo) response;
                    } catch (Exception e) {
                        accountVo = null;

                        e.printStackTrace();
                    }

                    if (null == accountVo)
                        return;

                    mCurAccountBalance = accountVo.getCurAccountBalanceStr();

                    mBinding.tvMoneyInMax.setText(MarketUtil.decimalFormatMoney(mCurAccountBalance));
                }

                break;
            case "FundInoutMsg":
                if (head.isSuccess()) {
                    showShortToast(R.string.login_verification_code_success);

                    if (null != mCountDownTimer)
                        mCountDownTimer.start();
                }

                break;
            case "InoutMoney":
                if (head.isSuccess())
                    showShortToast(R.string.trade_money_in_success);

                getAccount(true);

                break;
        }
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        getAccount(false);
    }

    public class ClickHandlers {

        public void onClickGetVerificationCode() {
            if (TextUtils.isEmpty(mBinding.tvMobileNumber.getText().toString()))
                showShortToast(R.string.trade_mobile_error);
            else
                fundInoutMsg();
        }

        public void onClickSubmit() {
            doSubmit();
        }
    }
}
