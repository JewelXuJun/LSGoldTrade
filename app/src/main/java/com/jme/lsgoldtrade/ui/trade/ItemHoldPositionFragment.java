package com.jme.lsgoldtrade.ui.trade;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.common.ui.view.MarginDividerItemDecoration;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseFragment;
import com.jme.lsgoldtrade.databinding.FragmentItemHoldPositionBinding;
import com.jme.lsgoldtrade.domain.PositionPageVo;
import com.jme.lsgoldtrade.service.TradeService;

import java.util.HashMap;
import java.util.List;

public class ItemHoldPositionFragment extends JMEBaseFragment implements BaseQuickAdapter.RequestLoadMoreListener {

    private FragmentItemHoldPositionBinding mBinding;

    private HoldPositionAdapter mAdapter;

    private boolean bVisibleToUser = false;
    private int mCurrentPage = 1;
    private boolean bHasNext = false;

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_item_hold_position;
    }

    @Override
    protected void initView() {
        super.initView();

        mBinding = (FragmentItemHoldPositionBinding) mBindingUtil;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);

        mAdapter = new HoldPositionAdapter(R.layout.item_order_hold_position, null);

        mBinding.recyclerView.setHasFixedSize(false);
        mBinding.recyclerView.addItemDecoration(new MarginDividerItemDecoration(mContext, LinearLayoutManager.VERTICAL));
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mBinding.recyclerView.setAdapter(mAdapter);
        mBinding.recyclerView.setNestedScrollingEnabled(false);
    }

    @Override
    protected void initListener() {
        super.initListener();

        mAdapter.setOnLoadMoreListener(this, mBinding.recyclerView);
    }

    @Override
    public void initBinding() {
        super.initBinding();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        bVisibleToUser = isVisibleToUser;

        super.setUserVisibleHint(isVisibleToUser);

      /*  if (null != mBinding && bVisibleToUser)
            initPosition();*/
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        bVisibleToUser = !hidden;

        super.onHiddenChanged(hidden);
    }

    @Override
    public void onResume() {
        super.onResume();

        /*if (bVisibleToUser)
            initPosition();*/
    }

    private void initPosition() {
        mCurrentPage = 1;

        position();
    }

    private void position() {
        HashMap<String, String> params = new HashMap<>();
        params.put("pageNo", String.valueOf(mCurrentPage));

        sendRequest(TradeService.getInstance().position, params, false, false, false);
    }

    @Override
    protected void DataReturn(DTRequest request, Head head, Object response) {
        super.DataReturn(request, head, response);

        switch (request.getApi().getName()) {
            case "Position":
                if (head.isSuccess()) {
                    PositionPageVo positionPageVo;

                    try {
                        positionPageVo = (PositionPageVo) response;
                    } catch (Exception e) {
                        positionPageVo = null;

                        e.printStackTrace();
                    }

                    if (null == positionPageVo)
                        return;

                    bHasNext = positionPageVo.isHasNext();

                    List<PositionPageVo.PositionBean> positionBeanList = positionPageVo.getList();

                    if (mCurrentPage == 1) {
                        mAdapter.setNewData(positionBeanList);

                        if (null != positionBeanList && 0 < positionBeanList.size())
                            mAdapter.loadMoreComplete();
                    } else {
                        mAdapter.addData(positionBeanList);
                        mAdapter.loadMoreComplete();
                    }
                }

                break;
        }
    }

    @Override
    public void onLoadMoreRequested() {
        mBinding.recyclerView.postDelayed(() -> {
            if (bHasNext) {
                mCurrentPage++;

                position();
            } else {
                mAdapter.loadMoreEnd();
            }
        }, 0);
    }
}
