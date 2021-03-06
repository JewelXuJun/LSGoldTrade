package com.jme.lsgoldtrade.ui.transaction;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;

import androidx.annotation.NonNull;

import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.common.util.RxBus;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseFragment;
import com.jme.lsgoldtrade.config.AppConfig;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.FragmentElectronicCardMoneyOutBinding;
import com.jme.lsgoldtrade.domain.BalanceEnquiryVo;
import com.jme.lsgoldtrade.service.TradeService;
import com.jme.lsgoldtrade.util.MarketUtil;
import com.jme.lsgoldtrade.view.ConfirmSimplePopupwindow;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.math.BigDecimal;
import java.util.HashMap;

public class ElectronicCardMoneyOutFragment extends JMEBaseFragment implements OnRefreshListener {

    private FragmentElectronicCardMoneyOutBinding mBinding;

    private boolean bVisibleToUser = false;
    private String mMoney;

    private BalanceEnquiryVo mBalanceEnquiryVo;

    private ConfirmSimplePopupwindow mConfirmSimplePopupwindow;

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_electronic_card_money_out;
    }

    @Override
    protected void initView() {
        super.initView();
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);

        mConfirmSimplePopupwindow = new ConfirmSimplePopupwindow(mContext);
    }

    @Override
    protected void initListener() {
        super.initListener();

        mBinding.swipeRefreshLayout.setOnRefreshListener(this);

        mBinding.etTransferIcbcElectronicCardMoneyOut.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().contains(".") && !s.toString().equals(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                        s = s.toString().subSequence(0, s.toString().indexOf(".") + (2 + 1));

                        mBinding.etTransferIcbcElectronicCardMoneyOut.setText(s);
                        mBinding.etTransferIcbcElectronicCardMoneyOut.setSelection(s.length());
                    } else {
                        mBinding.etTransferIcbcElectronicCardMoneyOut.setSelection(s.length());
                    }
                } else if (s.toString().trim().equals(".")) {
                    s = "0" + s;

                    mBinding.etTransferIcbcElectronicCardMoneyOut.setText(s);
                    mBinding.etTransferIcbcElectronicCardMoneyOut.setSelection(2);
                } else if (s.toString().startsWith("0") && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        mBinding.etTransferIcbcElectronicCardMoneyOut.setText(s.subSequence(0, 1));
                        mBinding.etTransferIcbcElectronicCardMoneyOut.setSelection(1);

                        return;
                    }
                } else {
                    mBinding.etTransferIcbcElectronicCardMoneyOut.setSelection(s.length());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                mBinding.btnTransferOut.setEnabled(mBinding.etTransferIcbcElectronicCardMoneyOut.length() > 0);
            }
        });
    }

    @Override
    public void initBinding() {
        super.initBinding();

        mBinding = (FragmentElectronicCardMoneyOutBinding) mBindingUtil;
        mBinding.setHandlers(new ClickHanlders());
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        bVisibleToUser = isVisibleToUser;

        if (bVisibleToUser && null != mBinding)
            balanceEnquiry(true);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (bVisibleToUser)
            balanceEnquiry(true);
    }

    private void balanceEnquiry(boolean enable) {
        sendRequest(TradeService.getInstance().balanceEnquiry, new HashMap<>(), enable);
    }

    private void withdraw(String amount) {
        HashMap<String, String> params = new HashMap<>();
        params.put("amount", String.valueOf(new BigDecimal(amount).multiply(new BigDecimal(100)).longValue()));

        sendRequest(TradeService.getInstance().withdraw, params, true);
    }

    @Override
    protected void DataReturn(DTRequest request, Head head, Object response) {
        super.DataReturn(request, head, response);

        switch (request.getApi().getName()) {
            case "BalanceEnquiry":
                if (head.isSuccess()) {
                    mBinding.swipeRefreshLayout.finishRefresh(true);

                    try {
                        mBalanceEnquiryVo = (BalanceEnquiryVo) response;
                    } catch (Exception e) {
                        mBalanceEnquiryVo = null;

                        e.printStackTrace();
                    }

                    if (null == mBalanceEnquiryVo)
                        return;

                    mMoney = mBalanceEnquiryVo.getAccountBalance();
                    String relevanceId = mBalanceEnquiryVo.getRelevanceId();
                    String moneyValue = MarketUtil.decimalFormatMoney(mMoney);

                    mBinding.tvIcbcElectronicCardMoneyOutAvalible.setText(TextUtils.isEmpty(moneyValue)
                            ? getResources().getString(R.string.text_no_data_default) : moneyValue + mContext.getResources().getString(R.string.text_money_unit));
                    mBinding.tvMessage.setText(TextUtils.isEmpty(relevanceId) ? ""
                            : String.format(mContext.getResources().getString(R.string.transaction_transfer_icbc_electronic_card_money_out_message),
                            relevanceId.substring(relevanceId.length() - 4)));
                } else {
                    mBinding.swipeRefreshLayout.finishRefresh(false);
                }

                break;
            case "Withdraw":
                if (head.isSuccess()) {
                    mBinding.etTransferIcbcElectronicCardMoneyOut.setText("");

                    if (null != mConfirmSimplePopupwindow && !mConfirmSimplePopupwindow.isShowing()) {
                        mConfirmSimplePopupwindow.setData(mContext.getResources().getString(R.string.transaction_transfer_icbc_electronic_card_withdraw_message),
                                mContext.getResources().getString(R.string.text_confirm),
                                (view) -> {
                                    mConfirmSimplePopupwindow.dismiss();

                                    RxBus.getInstance().post(Constants.RxBusConst.RXBUS_ELECTRONICCARD_INOUT_SUCCESS, null);
                                }
                        );
                        mConfirmSimplePopupwindow.showAtLocation(mBinding.tvIcbcElectronicCardMoneyOutAvalible, Gravity.CENTER, 0, 0);
                    }

                }

                balanceEnquiry(false);

                break;
        }
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        balanceEnquiry(false);
    }

    public class ClickHanlders {

        public void onClickTransferOut() {
            if (null == mBalanceEnquiryVo)
                return;

            String amount = mBinding.etTransferIcbcElectronicCardMoneyOut.getText().toString();

            if (amount.endsWith("."))
                amount = amount.substring(0, amount.length() - 1);

            if (TextUtils.isEmpty(mMoney))
                showShortToast(R.string.transaction_amount_error);
            else if (new BigDecimal(amount).compareTo(new BigDecimal(0)) != 1)
                showShortToast(R.string.transaction_money_min_error);
            else if (new BigDecimal(amount).compareTo(new BigDecimal(mMoney)) == 1)
                showShortToast(String.format(mContext.getResources().getString(R.string.transaction_money_out_max_error_detail), mMoney));
            else
                withdraw(amount);
        }

    }
}
