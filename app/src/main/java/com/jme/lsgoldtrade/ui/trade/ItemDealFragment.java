package com.jme.lsgoldtrade.ui.trade;

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
import com.jme.lsgoldtrade.databinding.FragmentItemDealBinding;
import com.jme.lsgoldtrade.domain.DealPageVo;
import com.jme.lsgoldtrade.service.TradeService;

import java.util.HashMap;
import java.util.List;

import rx.Subscription;

public class ItemDealFragment extends JMEBaseFragment implements BaseQuickAdapter.RequestLoadMoreListener {

    private FragmentItemDealBinding mBinding;

    private DealAdapter mAdapter;
    private Subscription mRxbus;

    private int mCurrentPage = 1;
    private boolean bHasNext = false;
    private boolean bVisibleToUser = false;
    private String mPagingKey = "";

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
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mBinding.recyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void initListener() {
        super.initListener();

        initRxBus();

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

        if (null != mBinding && bVisibleToUser)
            initDealPage();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        bVisibleToUser = !hidden;

        super.onHiddenChanged(hidden);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (bVisibleToUser)
            initDealPage();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (!mRxbus.isUnsubscribed())
            mRxbus.unsubscribe();
    }

    private void initRxBus() {
        mRxbus = RxBus.getInstance().toObserverable(RxBus.Message.class).subscribe(message -> {
            String callType = message.getObject().toString();

            if (TextUtils.isEmpty(callType))
                return;

            switch (callType) {
                case Constants.RxBusConst.RXBUS_DECLARATIONFORM_UPDATE:
                    initDealPage();

                    break;
            }
        });
    }

    private void initDealPage() {
        mCurrentPage = 1;
        mPagingKey = "";

        dealpage();
    }

    private void dealpage() {
        HashMap<String, String> params = new HashMap<>();
        params.put("accountId", mUser.getAccountID());
        params.put("pagingKey", mPagingKey);

        sendRequest(TradeService.getInstance().dealpage, params, false, false, false);
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

                    if (null == dealPageVo)
                        return;

                    bHasNext = dealPageVo.isHasNext();
                    mPagingKey = dealPageVo.getPagingKey();
                    List<DealPageVo.DealBean> dealPageVoList = dealPageVo.getList();

                    if (bHasNext) {
                        if (mCurrentPage == 1)
                            mAdapter.setNewData(dealPageVoList);
                        else
                            mAdapter.addData(dealPageVoList);

                        mAdapter.loadMoreComplete();
                    } else {
                        if (mCurrentPage == 1) {
                            if (null == dealPageVoList || 0 == dealPageVoList.size()) {
                                mAdapter.setNewData(null);
                            } else {
                                mAdapter.setNewData(dealPageVoList);
                                mAdapter.loadMoreComplete();
                            }
                        } else {
                            mAdapter.addData(dealPageVoList);
                            mAdapter.loadMoreComplete();
                        }
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
