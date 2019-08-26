package com.jme.lsgoldtrade.ui.trade;

import android.annotation.TargetApi;
import android.os.Bundle;

import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseFragment;
import com.jme.lsgoldtrade.databinding.FragmentElectronicCardMoneyDetailBinding;

public class ElectronicCardDetailFragment extends JMEBaseFragment {

    private FragmentElectronicCardMoneyDetailBinding mBinding;

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_electronic_card_money_detail;
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
    }

    @Override
    public void initBinding() {
        super.initBinding();

        mBinding = (FragmentElectronicCardMoneyDetailBinding) mBindingUtil;
        mBinding.setHandlers(new ClickHanlders());
    }

    @Override
    protected void DataReturn(DTRequest request, Head head, Object response) {
        super.DataReturn(request, head, response);
    }

    public class ClickHanlders {

        @TargetApi(24)
        public void onClickSelectDate(int timeType) {

        }

    }
}
