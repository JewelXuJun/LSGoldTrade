package com.jme.lsgoldtrade.ui.transaction;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.common.util.RxBus;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseFragment;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.FragmentCurrentHoldPositionsBinding;
import com.jme.lsgoldtrade.domain.FiveSpeedVo;
import com.jme.lsgoldtrade.domain.PositionVo;

import java.util.ArrayList;
import java.util.List;

public class CurrentHoldPositionsFragment extends JMEBaseFragment {

    private FragmentCurrentHoldPositionsBinding mBinding;

    private HoldPositionsAdapter mAdapter;
    private View mEmptyView;

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

        mAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            switch (view.getId()) {
                case R.id.layout_stop_transaction:

                    break;
                case R.id.btn_evening_up:
                    PositionVo positionVo = (PositionVo) adapter.getItem(position);

                    if (null == positionVo)
                        return;

                    RxBus.getInstance().post(Constants.RxBusConst.RXBUS_TRANSACTION_EVENING_UP, positionVo);

                    break;
            }
        });
    }

    @Override
    public void initBinding() {
        super.initBinding();

        mBinding = (FragmentCurrentHoldPositionsBinding) mBindingUtil;
        mBinding.setHandlers(new ClickHandlers());
    }

    private View getEmptyView() {
        if (null == mEmptyView)
            mEmptyView = LayoutInflater.from(mContext).inflate(R.layout.layout_empty_margin_small, null);

        TextView tv_empty = mEmptyView.findViewById(R.id.tv_empty);
        tv_empty.setText(R.string.transaction_hold_positions_empty);

        return mEmptyView;
    }

    public void setFloatingList(List<String> list) {
        mAdapter.setList(list);
    }

    public void setFiveSpeedVoList(List<FiveSpeedVo> fiveSpeedVoList) {
        mAdapter.setFiveSpeedVoList(fiveSpeedVoList);
    }

    public void setCurrentHoldPositionsData(List<PositionVo> positionVoList) {
        if (null == positionVoList) {
            mAdapter.setEmptyView(getEmptyView());
        } else {
            List<PositionVo> positionVos = new ArrayList<>();

            for (PositionVo positionVo : positionVoList) {
                if (null != positionVo && positionVo.getPosition() != 0) {
                    positionVos.add(positionVo);
                }
            }

            mAdapter.setNewData(positionVos);

            if (0 == positionVos.size())
                mAdapter.setEmptyView(getEmptyView());
        }
    }

    public List<PositionVo> getData() {
        return mAdapter.getData();
    }

    @Override
    protected void DataReturn(DTRequest request, Head head, Object response) {
        super.DataReturn(request, head, response);
    }

    public class ClickHandlers {

        public void onClickGoToTransaction() {
            RxBus.getInstance().post(Constants.RxBusConst.RXBUS_TRANSACTION_PLACE_ORDER, null);
        }

    }

}
