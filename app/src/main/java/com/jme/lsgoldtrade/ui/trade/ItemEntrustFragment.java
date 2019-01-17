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
import com.jme.lsgoldtrade.databinding.FragmentItemEntrustBinding;
import com.jme.lsgoldtrade.domain.OrderPageVo;
import com.jme.lsgoldtrade.service.TradeService;

import java.util.HashMap;
import java.util.List;

import rx.Subscription;

public class ItemEntrustFragment extends JMEBaseFragment implements BaseQuickAdapter.RequestLoadMoreListener {

    private FragmentItemEntrustBinding mBinding;

    private EntrustAdapter mAdapter;
    private Subscription mRxbus;

    private int mCurrentPage = 1;
    private boolean bHasNext = false;
    private boolean bVisibleToUser = false;
    private String mPagingKey = "";

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_item_entrust;
    }

    @Override
    protected void initView() {
        super.initView();

        mBinding = (FragmentItemEntrustBinding) mBindingUtil;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);

        mAdapter = new EntrustAdapter(mContext, R.layout.item_entrust, null, "Current");

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
            initOrderPage();
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
            initOrderPage();
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
                    initOrderPage();

                    break;
            }
        });
    }

    private void initOrderPage() {
        mCurrentPage = 1;
        mPagingKey = "";

        orderpage();
    }

    private void orderpage() {
        HashMap<String, String> params = new HashMap<>();
        params.put("accountId", mUser.getAccountID());
        params.put("onlyRevocable", "false");
        params.put("pagingKey", mPagingKey);

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
                    mPagingKey = orderPageVo.getPagingKey();
                    List<OrderPageVo.OrderBean> orderBeanList = orderPageVo.getList();

                    if (bHasNext) {
                        if (mCurrentPage == 1)
                            mAdapter.setNewData(orderBeanList);
                        else
                            mAdapter.addData(orderBeanList);

                        mAdapter.loadMoreComplete();
                    } else {
                        if (mCurrentPage == 1) {
                            if (null == orderBeanList || 0 == orderBeanList.size()) {
                                mAdapter.setNewData(null);
                            } else {
                                mAdapter.setNewData(orderBeanList);
                                mAdapter.loadMoreComplete();
                            }
                        } else {
                            mAdapter.addData(orderBeanList);
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

                orderpage();
            } else {
                mAdapter.loadMoreEnd();
            }
        }, 0);
    }
}
