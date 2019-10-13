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
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseActivity;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.ActivityCurrentEntrustBinding;
import com.jme.lsgoldtrade.domain.OrderPageVo;
import com.jme.lsgoldtrade.service.TradeService;
import com.jme.lsgoldtrade.ui.transaction.EntrustAdapter;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.HashMap;
import java.util.List;

/**
 * 当日委托
 */
@Route(path = Constants.ARouterUriConst.CURRENTENTRUST)
public class CurrentEntrustActivity extends JMEBaseActivity implements OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {

    private ActivityCurrentEntrustBinding mBinding;

    private EntrustAdapter mAdapter;
    private View mEmptyView;

    private int mCurrentPage = 1;
    private boolean bHasNext = false;
    private String mPagingKey = "";

    @Override
    protected int getContentViewId() {
        return R.layout.activity_current_entrust;
    }

    @Override
    protected void initView() {
        super.initView();

        initToolbar(R.string.transaction_current_entrust, true);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);

        mAdapter = new EntrustAdapter(this, R.layout.item_entrust, null, "Current");

        mBinding.recyclerView.setHasFixedSize(false);
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mBinding.recyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void initListener() {
        super.initListener();

        mBinding.swipeRefreshLayout.setOnRefreshListener(this);
        mAdapter.setOnLoadMoreListener(this, mBinding.recyclerView);
    }

    @Override
    protected void initBinding() {
        super.initBinding();

        mBinding = (ActivityCurrentEntrustBinding) mBindingUtil;
    }

    @Override
    protected void onResume() {
        super.onResume();

        initOrderPage(true);
    }

    private void initOrderPage(boolean enable) {
        mCurrentPage = 1;
        mPagingKey = "";

        orderpage(enable);
    }

    private void setEmptyData() {
        mBinding.swipeRefreshLayout.finishRefresh(false);
        mAdapter.loadMoreFail();
    }

    private View getEmptyView() {
        if (null == mEmptyView)
            mEmptyView = LayoutInflater.from(mContext).inflate(R.layout.layout_empty, null);

        return mEmptyView;
    }

    private void orderpage(boolean enable) {
        if (null == mUser || !mUser.isLogin())
            return;

        HashMap<String, String> params = new HashMap<>();
        params.put("accountId", mUser.getAccountID());
        params.put("onlyRevocable", "false");
        params.put("pagingKey", mPagingKey);

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
                        mPagingKey = orderPageVo.getPagingKey();
                        List<OrderPageVo.OrderBean> orderBeanList = orderPageVo.getList();

                        if (bHasNext) {
                            if (mCurrentPage == 1)
                                mAdapter.setNewData(orderBeanList);
                            else
                                mAdapter.addData(orderBeanList);

                            mAdapter.loadMoreComplete();
                            mBinding.swipeRefreshLayout.finishRefresh(true);
                        } else {
                            if (mCurrentPage == 1) {
                                if (null == orderBeanList || 0 == orderBeanList.size()) {
                                    mAdapter.setNewData(null);
                                    mAdapter.setEmptyView(getEmptyView());
                                } else {
                                    mAdapter.setNewData(orderBeanList);
                                    mAdapter.loadMoreComplete();
                                }
                            } else {
                                mAdapter.addData(orderBeanList);
                                mAdapter.loadMoreComplete();
                            }

                            mBinding.swipeRefreshLayout.finishRefresh(true);
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
