package com.jme.lsgoldtrade.ui.trade;

import android.os.Bundle;

import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseFragment;
import com.jme.lsgoldtrade.databinding.FragmentMoneyOutBinding;

public class MoneyOutFragment extends JMEBaseFragment {

    private FragmentMoneyOutBinding mBinding;

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_money_out;
    }

    @Override
    protected void initView() {
        super.initView();

        mBinding = (FragmentMoneyOutBinding) mBindingUtil;
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

        mBinding.setHandlers(new ClickHandlers());
    }

    @Override
    protected void DataReturn(DTRequest request, Head head, Object response) {
        super.DataReturn(request, head, response);
    }

    public class ClickHandlers {

        public void onClickOutAll() {

        }

        public void onClickGetVerificationCode() {

        }

        public void onClickSubmit() {

        }

    }
}
