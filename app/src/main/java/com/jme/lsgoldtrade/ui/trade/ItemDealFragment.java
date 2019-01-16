package com.jme.lsgoldtrade.ui.trade;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.common.ui.view.MarginDividerItemDecoration;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseFragment;
import com.jme.lsgoldtrade.databinding.FragmentItemDealBinding;
import com.jme.lsgoldtrade.domain.DealPageVo;
import com.jme.lsgoldtrade.service.TradeService;

import java.util.HashMap;
import java.util.List;

public class ItemDealFragment extends JMEBaseFragment implements BaseQuickAdapter.RequestLoadMoreListener{

    private FragmentItemDealBinding mBinding;

    private DealAdapter mAdapter;

    private boolean bVisibleToUser = false;

    private int mCurrentPage = 1;
    private boolean bHasNext = false;

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_item_deal;
    }

    @Override
    protected void initView() {
        super.initView();

        mBinding = (FragmentItemDealBinding) mBindingUtil;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);

        mAdapter = new DealAdapter(mContext, R.layout.item_deal, null, "Current");

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
            initDealPage();*/
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
            initDealPage();*/
    }

    private void initDealPage() {
        mCurrentPage = 1;

        dealpage();
    }

    private void dealpage() {
        HashMap<String, String> params = new HashMap<>();
        params.put("pageNo", String.valueOf(mCurrentPage));

        sendRequest(TradeService.getInstance().dealpage, params, false, false, false);
    }

    @Override
    protected void DataReturn(DTRequest request, Head head, Object response) {
        super.DataReturn(request, head, response);

        switch (request.getApi().getName()) {
            case "OrderPage":
                if (head.isSuccess()) {
                    DealPageVo dealPageVo;

                    try {
                        dealPageVo = (DealPageVo) response;
                    } catch (Exception e) {
                        dealPageVo = null;

                        e.printStackTrace();
                    }

                    if (null == dealPageVo)
                        return;

                    bHasNext = dealPageVo.isHasNext();

                    List<DealPageVo.DealBean> dealBeanList = dealPageVo.getList();

                    if (mCurrentPage == 1) {
                        mAdapter.setNewData(dealBeanList);

                        if (null != dealBeanList && 0 < dealBeanList.size())
                            mAdapter.loadMoreComplete();
                    } else {
                        mAdapter.addData(dealBeanList);
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

                dealpage();
            } else {
                mAdapter.loadMoreEnd();
            }
        }, 0);
    }
}
