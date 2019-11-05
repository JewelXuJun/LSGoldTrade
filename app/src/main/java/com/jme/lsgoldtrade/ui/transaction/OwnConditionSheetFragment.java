package com.jme.lsgoldtrade.ui.transaction;

import android.annotation.TargetApi;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseFragment;
import com.jme.lsgoldtrade.databinding.FragmentOwnConditionSheetBinding;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

public class OwnConditionSheetFragment extends JMEBaseFragment implements OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener{

    private FragmentOwnConditionSheetBinding mBinding;

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_own_condition_sheet;
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

        mBinding = (FragmentOwnConditionSheetBinding) mBindingUtil;
        mBinding.setHandlers(new ClickHandlers());
    }

    @Override
    protected void DataReturn(DTRequest request, Head head, Object response) {
        super.DataReturn(request, head, response);
    }

    @Override
    public void onLoadMoreRequested() {

    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {

    }

    public class ClickHandlers {

        @TargetApi(24)
        public void onClickSelectDate(int timeType) {

        }
    }
}
