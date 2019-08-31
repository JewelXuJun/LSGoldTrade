package com.jme.lsgoldtrade.ui.trade;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;

import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.common.ui.adapter.TextWatcherAdapter;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseFragment;
import com.jme.lsgoldtrade.databinding.FragmentElectronicCardMoneyOutBinding;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.math.BigDecimal;

public class ElectronicCardMoneyOutFragment extends JMEBaseFragment implements OnRefreshListener {

    private FragmentElectronicCardMoneyOutBinding mBinding;

    private boolean bVisibleToUser = false;

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
    }

    @Override
    protected void initListener() {
        super.initListener();

        mBinding.swipeRefreshLayout.setOnRefreshListener(this);

        mBinding.etTransferIcbcElectronicCardMoneyOut.addTextChangedListener(validationTextWatcher());
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

       /* if (bVisibleToUser && null != mBinding)
            getAccount(true);*/
    }

    @Override
    public void onResume() {
        super.onResume();

       /* if (bVisibleToUser)
            getAccount(true);*/
    }

    private TextWatcher validationTextWatcher() {
        return new TextWatcherAdapter() {
            @Override
            public void afterTextChanged(final Editable s) {
                mBinding.btnTransferOut.setEnabled(mBinding.etTransferIcbcElectronicCardMoneyOut.length() > 0);
            }
        };
    }

    @Override
    protected void DataReturn(DTRequest request, Head head, Object response) {
        super.DataReturn(request, head, response);
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {

    }

    public class ClickHanlders {

        public void onClickTransferOut() {
            String maxMoney = mBinding.tvIcbcElectronicCardMoneyOutAvalible.getText().toString();
            String amount = mBinding.etTransferIcbcElectronicCardMoneyOut.getText().toString();

            if (TextUtils.isEmpty(maxMoney))
                showShortToast(R.string.trade_amount_error);
            else if (new BigDecimal(amount).compareTo(new BigDecimal(0)) != 1)
                showShortToast(R.string.trade_money_min_error);
            else if (new BigDecimal(amount).compareTo(new BigDecimal(maxMoney)) == 1)
                showShortToast(R.string.trade_money_in_max_error_detail);
        }

    }
}
