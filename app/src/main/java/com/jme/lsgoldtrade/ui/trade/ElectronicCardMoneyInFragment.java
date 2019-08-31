package com.jme.lsgoldtrade.ui.trade;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;

import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.common.ui.adapter.TextWatcherAdapter;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseFragment;
import com.jme.lsgoldtrade.databinding.FragmentElectronicCardMoneyInBinding;

import java.math.BigDecimal;

public class ElectronicCardMoneyInFragment extends JMEBaseFragment {

    private FragmentElectronicCardMoneyInBinding mBinding;

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
    }

    @Override
    protected void initListener() {
        super.initListener();

        mBinding.etTransferIcbcElectronicCardMoneyIn.addTextChangedListener(validationTextWatcher());
    }

    @Override
    public void initBinding() {
        super.initBinding();

        mBinding = (FragmentElectronicCardMoneyInBinding) mBindingUtil;
        mBinding.setHandlers(new ClickHanlders());
    }

    private TextWatcher validationTextWatcher() {
        return new TextWatcherAdapter() {
            @Override
            public void afterTextChanged(final Editable s) {
                mBinding.btnTransferIn.setEnabled(mBinding.etTransferIcbcElectronicCardMoneyIn.length() > 0);
            }
        };
    }

    @Override
    protected void DataReturn(DTRequest request, Head head, Object response) {
        super.DataReturn(request, head, response);
    }

    public class ClickHanlders {

        public void onClickTransferIn() {
            String amount = mBinding.etTransferIcbcElectronicCardMoneyIn.getText().toString();

            if (new BigDecimal(amount).compareTo(new BigDecimal(0)) != 1)
                showShortToast(R.string.trade_money_min_error);
            else if (new BigDecimal(amount).compareTo(new BigDecimal(200000)) == 1)
                showShortToast(R.string.trade_money_in_max_error_detail);
        }

    }

}
