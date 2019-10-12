package com.jme.lsgoldtrade.ui.transaction;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.common.util.RxBus;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseFragment;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.FragmentCurrentHoldPositionsBinding;
import com.jme.lsgoldtrade.domain.FiveSpeedVo;
import com.jme.lsgoldtrade.domain.PositionVo;

import java.util.List;

public class CurrentHoldPositionsFragment extends JMEBaseFragment implements BaseQuickAdapter.RequestLoadMoreListener {

    private FragmentCurrentHoldPositionsBinding mBinding;

    private HoldPositionsAdapter mAdapter;

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_current_hold_positions;
    }

    @Override
    protected void initView() {
        super.initView();
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);

        mAdapter = new HoldPositionsAdapter(mContext, null);

        mBinding.recyclerView.setHasFixedSize(false);
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mBinding.recyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void initListener() {
        super.initListener();

        mAdapter.setOnLoadMoreListener(this, mBinding.recyclerView);

        mAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            switch (view.getId()) {
                case R.id.layout_stop_transaction:

                    break;
                case R.id.btn_evening_up:
                    PositionVo positionVo = (PositionVo) adapter.getItem(position);

                    if (null == positionVo)
                        return;

                    String contractId = positionVo.getContractId();

                    if (!TextUtils.isEmpty(contractId))
                        RxBus.getInstance().post(Constants.RxBusConst.RXBUS_TRANSACTION_EVENING_UP, contractId);

                    break;
            }
        });
    }

    @Override
    public void initBinding() {
        super.initBinding();

        mBinding = (FragmentCurrentHoldPositionsBinding) mBindingUtil;
    }

    public void setFloatingList(List<String> list) {
        mAdapter.setList(list);
    }

    public void setFiveSpeedVoList(List<FiveSpeedVo> fiveSpeedVoList) {
        mAdapter.setFiveSpeedVoList(fiveSpeedVoList);
    }

    public void setCurrentHoldPositionsData(List<PositionVo> positionVoList) {
        mAdapter.setNewData(positionVoList);

        if (null != positionVoList)
            mAdapter.loadMoreComplete();
    }

    public void addCurrentHoldPositionsData(List<PositionVo> positionVoList) {
        mAdapter.addData(positionVoList);
        mAdapter.loadMoreComplete();
    }

    public List<PositionVo> getData() {
        return mAdapter.getData();
    }

    @Override
    protected void DataReturn(DTRequest request, Head head, Object response) {
        super.DataReturn(request, head, response);
    }

    @Override
    public void onLoadMoreRequested() {

    }

}
