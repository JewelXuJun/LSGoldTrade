package com.jme.lsgoldtrade.ui.trade;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.common.ui.view.MarginDividerItemDecoration;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseFragment;
import com.jme.lsgoldtrade.databinding.FragmentItemEntrustBinding;

public class ItemEntrustFragment extends JMEBaseFragment {

    private FragmentItemEntrustBinding mBinding;

    private ItemEntrustAdapter mAdapter;

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_item_entrust;
    }

    @Override
    protected void initView() {
        super.initView();

        mBinding = (FragmentItemEntrustBinding) mBindingUtil;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);

        mAdapter = new ItemEntrustAdapter(R.layout.item_order_entrust, null);

        mBinding.recyclerView.addItemDecoration(new MarginDividerItemDecoration(mContext, LinearLayoutManager.VERTICAL));
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mBinding.recyclerView.setAdapter(mAdapter);
        mBinding.recyclerView.setNestedScrollingEnabled(false);
    }

    @Override
    protected void initListener() {
        super.initListener();
    }

    @Override
    public void initBinding() {
        super.initBinding();
    }

    @Override
    protected void DataReturn(DTRequest request, Head head, Object response) {
        super.DataReturn(request, head, response);
    }
}
