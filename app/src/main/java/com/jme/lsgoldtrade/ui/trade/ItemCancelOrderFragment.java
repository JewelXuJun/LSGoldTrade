package com.jme.lsgoldtrade.ui.trade;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.common.ui.view.MarginDividerItemDecoration;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseFragment;
import com.jme.lsgoldtrade.databinding.FragmentItemCancelOrderBinding;
import com.jme.lsgoldtrade.domain.OrderPageVo;
import com.jme.lsgoldtrade.service.TradeService;

import java.util.HashMap;
import java.util.List;

public class ItemCancelOrderFragment extends JMEBaseFragment implements BaseQuickAdapter.RequestLoadMoreListener {

    private FragmentItemCancelOrderBinding mBinding;

    private CancelOrderAdapter mAdapter;

    private static final String PAGE_NEXT = "2";

    private boolean bVisibleToUser = false;
    private int mCurrentPage = 1;
    private boolean bHasNext = false;
    private String mDeclareTime;

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_item_cancel_order;
    }

    @Override
    protected void initView() {
        super.initView();

        mBinding = (FragmentItemCancelOrderBinding) mBindingUtil;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);

        mAdapter = new CancelOrderAdapter(R.layout.item_cancel_order, null);

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

        mBinding.setHandlers(new ClickHandlers());
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        bVisibleToUser = isVisibleToUser;

        super.setUserVisibleHint(isVisibleToUser);

      /*  if (null != mBinding && bVisibleToUser)
            initOrderPage();*/
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
            initOrderPage();*/
    }

    private void initOrderPage() {
        mCurrentPage = 1;

        orderpage();
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

    private void orderpage() {
        HashMap<String, String> params = new HashMap<>();
        params.put("pageNo", String.valueOf(mCurrentPage));
        params.put("pagingDeclareTime", mDeclareTime);
        params.put("qryFlg", PAGE_NEXT);

        sendRequest(TradeService.getInstance().orderpage, params, false, false, false);
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

                    if (null == orderPageVo)
                        return;

                    bHasNext = orderPageVo.isHasNext();

                    List<OrderPageVo.OrderBean> orderBeanList = orderPageVo.getList();

                    if (mCurrentPage == 1) {
                        mAdapter.setNewData(orderBeanList);

                        if (null != orderBeanList && 0 < orderBeanList.size())
                            mAdapter.loadMoreComplete();
                    } else {
                        mAdapter.addData(orderBeanList);
                        mAdapter.loadMoreComplete();
                    }

                    setDeclareTime(orderBeanList);
                }

                break;
        }
    }

    @Override
    public void onLoadMoreRequested() {
        mBinding.recyclerView.postDelayed(() -> {
            if (bHasNext) {
                mCurrentPage++;

                orderpage();
            } else {
                mAdapter.loadMoreEnd();
            }
        }, 0);
    }

    public class ClickHandlers {

        public void onClickCancel() {

        }

    }
}
