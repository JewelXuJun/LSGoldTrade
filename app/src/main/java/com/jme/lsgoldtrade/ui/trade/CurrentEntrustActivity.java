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
import com.jme.lsgoldtrade.databinding.ActivityCurrentEntrustBinding;
import com.jme.lsgoldtrade.domain.OrderPageVo;
import com.jme.lsgoldtrade.service.TradeService;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.HashMap;
import java.util.List;

@Route(path = Constants.ARouterUriConst.CURRENTENTRUST)
public class CurrentEntrustActivity extends JMEBaseActivity implements OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {

    private ActivityCurrentEntrustBinding mBinding;

    private EntrustAdapter mAdapter;
    private View mEmptyView;

    private static final String PAGE_NEXT = "2";

    private int mCurrentPage = 1;
    private boolean bHasNext = false;
    private String mDeclareTime;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_current_entrust;
    }

    @Override
    protected void initView() {
        super.initView();

        mBinding = (ActivityCurrentEntrustBinding) mBindingUtil;

        initToolbar(R.string.trade_current_entrust, true);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);

        mAdapter = new EntrustAdapter(R.layout.item_entrust, null);

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

        initOrderPage(true);
    }

    private void initOrderPage(boolean enable) {
        mCurrentPage = 1;

        orderpage(enable);
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

    private void setDeclareTime(List<OrderPageVo.OrderBean> list) {
        if (null == list || 0 == list.size()) {
            if (mCurrentPage == 1)
                mDeclareTime = "";
        } else {
            int size = list.size();

            OrderPageVo.OrderBean orderBean = list.get(size - 1);

            if (null != orderBean)
                mDeclareTime = orderBean.getDeclarTime();
        }
    }

    private void orderpage(boolean enable) {
        HashMap<String, String> params = new HashMap<>();
        params.put("pageNo", String.valueOf(mCurrentPage));
        params.put("pagingDeclareTime", mDeclareTime);
        params.put("qryFlg", PAGE_NEXT);

        sendRequest(TradeService.getInstance().orderpage, params, enable);
    }

    @Override
    protected void DataReturn(DTRequest request, Head head, Object response) {
        super.DataReturn(request, head, response);

        switch (request.getApi().getName()) {
            case "OrderPage":
                if (head.isSuccess()) {
                    OrderPageVo orderPageVo;

                    try {
                        orderPageVo = (OrderPageVo) response;
                    } catch (Exception e) {
                        orderPageVo = null;

                        e.printStackTrace();
                    }

                    if (null == orderPageVo) {
                        setEmptyData();
                    } else {
                        bHasNext = orderPageVo.isHasNext();

                        List<OrderPageVo.OrderBean> orderBeanList = orderPageVo.getList();

                        if (mCurrentPage == 1) {
                            mAdapter.setNewData(orderBeanList);

                            if (null == orderBeanList || 0 == orderBeanList.size())
                                mAdapter.setEmptyView(getEmptyView());

                            mBinding.swipeRefreshLayout.finishRefresh(true);
                        } else {
                            mAdapter.addData(orderBeanList);
                            mAdapter.loadMoreComplete();
                        }

                        setDeclareTime(orderBeanList);
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

                orderpage(true);
            } else {
                mAdapter.loadMoreEnd();
            }
        }, 0);
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        initOrderPage(false);
    }
}
