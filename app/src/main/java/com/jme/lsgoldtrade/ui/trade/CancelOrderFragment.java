package com.jme.lsgoldtrade.ui.trade;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.common.ui.view.MarginDividerItemDecoration;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseFragment;
import com.jme.lsgoldtrade.databinding.FragmentCancelOrderBinding;
import com.jme.lsgoldtrade.domain.OrderPageVo;
import com.jme.lsgoldtrade.service.TradeService;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.HashMap;
import java.util.List;

public class CancelOrderFragment extends JMEBaseFragment implements OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {

    private FragmentCancelOrderBinding mBinding;

    private CancelOrderAdapter mAdapter;
    private View mEmptyView;

    private static final String PAGE_NEXT = "2";

    private boolean bVisibleToUser = false;
    private int mCurrentPage = 1;
    private boolean bHasNext = false;
    private String mDeclareTime;

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_cancel_order;
    }

    @Override
    protected void initView() {
        super.initView();

        mBinding = (FragmentCancelOrderBinding) mBindingUtil;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);

        mAdapter = new CancelOrderAdapter(R.layout.item_cancel_order, null);

        mBinding.recyclerView.setHasFixedSize(false);
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mBinding.recyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void initListener() {
        super.initListener();

        mBinding.swipeRefreshLayout.setOnRefreshListener(this);
        mAdapter.setOnLoadMoreListener(this, mBinding.recyclerView);
    }

    @Override
    public void initBinding() {
        super.initBinding();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        bVisibleToUser = isVisibleToUser;

       /* if (null != mBinding && bVisibleToUser)
            initOrderPage(true);*/
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        bVisibleToUser = !hidden;

        super.onHiddenChanged(hidden);
    }

    @Override
    public void onResume() {
        super.onResume();

       /* if (bVisibleToUser)
            initOrderPage(true);*/
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
                mDeclareTime = orderBean.getDeclareTime();
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
