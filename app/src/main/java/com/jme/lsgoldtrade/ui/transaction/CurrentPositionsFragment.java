package com.jme.lsgoldtrade.ui.transaction;

import android.os.Bundle;

import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseFragment;
import com.jme.lsgoldtrade.databinding.FragmentCurrentPositionsBinding;

public class CurrentPositionsFragment extends JMEBaseFragment {

    private FragmentCurrentPositionsBinding mBinding;

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_current_positions;
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

        mBinding = (FragmentCurrentPositionsBinding) mBindingUtil;
    }

    @Override
    protected void DataReturn(DTRequest request, Head head, Object response) {
        super.DataReturn(request, head, response);
    }
}
