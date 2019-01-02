package com.jme.lsgoldtrade.ui.trade;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.common.ui.view.MarginDividerItemDecoration;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseActivity;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.ActivityCurrentHoldPositionBinding;
import com.jme.lsgoldtrade.domain.DealPageVo;
import com.jme.lsgoldtrade.domain.PositionPageVo;
import com.jme.lsgoldtrade.service.TradeService;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.HashMap;
import java.util.List;

@Route(path = Constants.ARouterUriConst.CURRENTHOLDPOSITION)
public class CurrentHoldPositionActivity extends JMEBaseActivity implements OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {

    private ActivityCurrentHoldPositionBinding mBinding;

    private HoldPositionAdapter mAdapter;
    private View mEmptyView;

    private int mCurrentPage = 1;
    private boolean bHasNext = false;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_current_hold_position;
    }

    @Override
    protected void initView() {
        super.initView();

        mBinding = (ActivityCurrentHoldPositionBinding) mBindingUtil;

        initToolbar(R.string.trade_curren_hold_position, true);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);

        mAdapter = new HoldPositionAdapter(R.layout.item_hold_position, null);

        mBinding.recyclerView.setHasFixedSize(false);
        mBinding.recyclerView.addItemDecoration(new MarginDividerItemDecoration(mContext, LinearLayoutManager.VERTICAL));
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mBinding.recyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void initListener() {
        super.initListener();

        mAdapter.setOnLoadMoreListener(this, mBinding.recyclerView);
        mBinding.swipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    protected void initBinding() {
        super.initBinding();
    }

    @Override
    protected void onResume() {
        super.onResume();

        initPosition(true);
    }

    private void initPosition(boolean enable) {
        mCurrentPage = 1;

        position(enable);
    }

    private void setEmptyData() {
        if (mCurrentPage == 1)
            mBinding.swipeRefreshLayout.finishRefresh(false);
        else
            mAdapter.loadMoreFail();
    }

    private View getEmptyView() {
        if (null == mEmptyView)
            mEmptyView = LayoutInflater.from(mContext).inflate(R.layout.layout_empty, null);

        return mEmptyView;
    }

    private void position(boolean enable) {
        HashMap<String, String> params = new HashMap<>();
        params.put("pageNo", String.valueOf(mCurrentPage));

        sendRequest(TradeService.getInstance().position, params, enable);
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

                    if (null == positionPageVo) {
                        setEmptyData();
                    } else {
                        bHasNext = positionPageVo.isHasNext();

                        List<PositionPageVo.PositionBean> positionBeanList = positionPageVo.getList();

                        if (mCurrentPage == 1) {
                            mAdapter.setNewData(positionBeanList);

                            if (null == positionBeanList || 0 == positionBeanList.size())
                                mAdapter.setEmptyView(getEmptyView());

                            mBinding.swipeRefreshLayout.finishRefresh(true);
                        } else {
                            mAdapter.addData(positionBeanList);
                            mAdapter.loadMoreComplete();
                        }
                    }
                } else {
                    setEmptyData();
                }

                break;
        }
    }

    @Override
    public void onLoadMoreRequested() {
        mBinding.recyclerView.postDelayed(() -> {
            if (bHasNext) {
                mCurrentPage++;

                position(true);
            } else {
                mAdapter.loadMoreEnd();
            }
        }, 0);
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        initPosition(false);
    }
}
