package com.jme.lsgoldtrade.ui.transaction;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;

import androidx.fragment.app.Fragment;

import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.common.util.RxBus;
import com.jme.common.util.StringUtils;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseFragment;
import com.jme.lsgoldtrade.config.AppConfig;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.FragmentElectronicCardMoneyInBinding;
import com.jme.lsgoldtrade.service.TradeService;
import com.jme.lsgoldtrade.view.ConfirmSimplePopupwindow;

import java.math.BigDecimal;
import java.util.HashMap;

public class ElectronicCardMoneyInFragment extends JMEBaseFragment {

    private FragmentElectronicCardMoneyInBinding mBinding;

    private String mElectronicAccounts;
    private String mRelevanceId;

    private ConfirmSimplePopupwindow mConfirmSimplePopupwindow;

    public static Fragment newInstance(String electronicAccounts, String relevanceId) {
        ElectronicCardMoneyInFragment fragment = new ElectronicCardMoneyInFragment();

        Bundle bundle = new Bundle();
        bundle.putString("ElectronicAccounts", electronicAccounts);
        bundle.putString("RelevanceId", relevanceId);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_electronic_card_money_in;
    }

    @Override
    protected void initView() {
        super.initView();
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);

        mElectronicAccounts = getArguments().getString("ElectronicAccounts");
        mRelevanceId = getArguments().getString("RelevanceId");

        mConfirmSimplePopupwindow = new ConfirmSimplePopupwindow(mContext);

        mBinding.tvIcbcElectronicBankCard.setText(StringUtils.formatBankCard(mElectronicAccounts));
        mBinding.tvMessage.setText(TextUtils.isEmpty(mRelevanceId) ? ""
                : String.format(mContext.getResources().getString(R.string.transaction_transfer_icbc_electronic_card_money_in_message),
                mRelevanceId.substring(mRelevanceId.length() - 4)));
    }

    @Override
    protected void initListener() {
        super.initListener();

        mBinding.etTransferIcbcElectronicCardMoneyIn.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().contains(".") && !s.toString().equals(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                        s = s.toString().subSequence(0, s.toString().indexOf(".") + (2 + 1));

                        mBinding.etTransferIcbcElectronicCardMoneyIn.setText(s);
                        mBinding.etTransferIcbcElectronicCardMoneyIn.setSelection(s.length());
                    } else {
                        mBinding.etTransferIcbcElectronicCardMoneyIn.setSelection(s.length());
                    }
                } else if (s.toString().trim().equals(".")) {
                    s = "0" + s;

                    mBinding.etTransferIcbcElectronicCardMoneyIn.setText(s);
                    mBinding.etTransferIcbcElectronicCardMoneyIn.setSelection(2);
                } else if (s.toString().startsWith("0") && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        mBinding.etTransferIcbcElectronicCardMoneyIn.setText(s.subSequence(0, 1));
                        mBinding.etTransferIcbcElectronicCardMoneyIn.setSelection(1);

                        return;
                    }
                } else {
                    mBinding.etTransferIcbcElectronicCardMoneyIn.setSelection(s.length());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                mBinding.btnTransferIn.setEnabled(mBinding.etTransferIcbcElectronicCardMoneyIn.length() > 0);
            }
        });
    }

    @Override
    public void initBinding() {
        super.initBinding();

        mBinding = (FragmentElectronicCardMoneyInBinding) mBindingUtil;
        mBinding.setHandlers(new ClickHanlders());
    }

    private void recharge(String amount) {
        HashMap<String, String> params = new HashMap<>();
        params.put("amount", String.valueOf(new BigDecimal(amount).multiply(new BigDecimal(100)).longValue()));

        sendRequest(TradeService.getInstance().recharge, params, true);
    }

    @Override
    protected void DataReturn(DTRequest request, Head head, Object response) {
        super.DataReturn(request, head, response);

        switch (request.getApi().getName()) {
            case "Recharge":
                if (head.isSuccess()) {
                    mBinding.etTransferIcbcElectronicCardMoneyIn.setText("");

                    if (null != mConfirmSimplePopupwindow && !mConfirmSimplePopupwindow.isShowing()) {
                        mConfirmSimplePopupwindow.setData(mContext.getResources().getString(R.string.transaction_transfer_icbc_electronic_card_recharge_message),
                                mContext.getResources().getString(R.string.text_confirm),
                                (view) -> {
                                    mConfirmSimplePopupwindow.dismiss();

                                    RxBus.getInstance().post(Constants.RxBusConst.RXBUS_ELECTRONICCARD_INOUT_SUCCESS, null);
                                }
                        );
                        mConfirmSimplePopupwindow.showAtLocation(mBinding.tvIcbcElectronicBankCard, Gravity.CENTER, 0, 0);
                    }
                }

                break;
        }
    }

    public class ClickHanlders {

        public void onClickTransferIn() {
            String amount = mBinding.etTransferIcbcElectronicCardMoneyIn.getText().toString();

            if (amount.endsWith("."))
                amount = amount.substring(0, amount.length() - 1);

            if (TextUtils.isEmpty(mElectronicAccounts) || TextUtils.isEmpty(mRelevanceId))
                showShortToast(R.string.transaction_bankcard_error);
            else if (new BigDecimal(amount).compareTo(new BigDecimal(0)) != 1)
                showShortToast(R.string.transaction_money_min_error);
            else if (new BigDecimal(amount).compareTo(new BigDecimal(200000)) == 1)
                showShortToast(R.string.transaction_money_in_max_error_detail);
            else
                recharge(amount);
        }

    }

}
