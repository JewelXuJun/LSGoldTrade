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
import com.jme.lsgoldtrade.databinding.ActivityCurrentDealBinding;
import com.jme.lsgoldtrade.domain.DealPageVo;
import com.jme.lsgoldtrade.service.TradeService;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.HashMap;
import java.util.List;

@Route(path = Constants.ARouterUriConst.CURRENTDEAL)
public class CurrentDealActivity extends JMEBaseActivity implements OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {

    private ActivityCurrentDealBinding mBinding;

    private DealAdapter mAdapter;
    private View mEmptyView;

    private int mCurrentPage = 1;
    private boolean bHasNext = false;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_current_deal;
    }

    @Override
    protected void initView() {
        super.initView();

        mBinding = (ActivityCurrentDealBinding) mBindingUtil;

        initToolbar(R.string.trade_current_deal, true);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);

        mAdapter = new DealAdapter(R.layout.item_deal, null, "Current");

        mBinding.recyclerView.setHasFixedSize(false);
        mBinding.recyclerView.addItemDecoration(new MarginDividerItemDecoration(this, LinearLayoutManager.VERTICAL));
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

        initDealPage(true);
    }

    private void initDealPage(boolean enable) {
        mCurrentPage = 1;

        dealpage(enable);
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

    private void dealpage(boolean enable) {
        HashMap<String, String> params = new HashMap<>();
        params.put("pageNo", String.valueOf(mCurrentPage));

        sendRequest(TradeService.getInstance().dealpage, params, enable);
    }

    @Override
    protected void DataReturn(DTRequest request, Head head, Object response) {
        super.DataReturn(request, head, response);

        switch (request.getApi().getName()) {
            case "DealPage":
                if (head.isSuccess()) {
                    DealPageVo dealPageVo;

                    try {
                        dealPageVo = (DealPageVo) response;
                    } catch (Exception e) {
                        dealPageVo = null;

                        e.printStackTrace();
                    }

                    if (null == dealPageVo) {
                        setEmptyData();
                    } else {
                        bHasNext = dealPageVo.isHasNext();

                        List<DealPageVo.DealBean> dealPageVoList = dealPageVo.getList();

                        if (mCurrentPage == 1) {
                            mAdapter.setNewData(dealPageVoList);

                            if (null == dealPageVoList || 0 == dealPageVoList.size())
                                mAdapter.setEmptyView(getEmptyView());

                            mBinding.swipeRefreshLayout.finishRefresh(true);
                        } else {
                            mAdapter.addData(dealPageVoList);
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

                dealpage(true);
            } else {
                mAdapter.loadMoreEnd();
            }
        }, 0);
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        initDealPage(false);
    }
}

