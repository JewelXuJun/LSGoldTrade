package com.jme.lsgoldtrade.ui.transaction;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.alibaba.android.arouter.launcher.ARouter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.common.util.RxBus;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseFragment;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.FragmentCurrentEntrustBinding;
import com.jme.lsgoldtrade.domain.OrderPageVo;
import com.jme.lsgoldtrade.service.TradeService;
import com.jme.lsgoldtrade.util.MarketUtil;
import com.jme.lsgoldtrade.view.CancelOrderPopUpWindow;

import java.util.HashMap;
import java.util.List;

import rx.Subscription;

public class CurrentEntrustFragment extends JMEBaseFragment implements BaseQuickAdapter.RequestLoadMoreListener {

    private FragmentCurrentEntrustBinding mBinding;

    private int mCurrentPage = 1;
    private boolean bHasNext = false;
    private boolean bVisibleToUser = false;
    private String mPagingKey = "";

    private EntrustAdapter mAdapter;
    private View mEmptyView;
    private CancelOrderPopUpWindow mCancelOrderPopUpWindow;
    private Subscription mRxbus;

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_current_entrust;
    }

    @Override
    protected void initView() {
        super.initView();

        mCancelOrderPopUpWindow = new CancelOrderPopUpWindow(mContext);
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

        mAdapter.setOnItemChildClickListener((adapter, view, position)  ->{
            switch (view.getId()) {
                case R.id.btn_cancel_order:
                    OrderPageVo.OrderBean orderBean = (OrderPageVo.OrderBean) adapter.getItem(position);

                    if (null == orderBean)
                        return;

                    if (null != mCancelOrderPopUpWindow && !mCancelOrderPopUpWindow.isShowing()) {
                        String time = orderBean.getDeclareTime();
                        String contractId = orderBean.getContractId();

                        mCancelOrderPopUpWindow.setData(contractId, TextUtils.isEmpty(time) ? "" : time.replace(".", ":"),
                                MarketUtil.getTradeDirection(orderBean.getBsFlag()) + MarketUtil.getOCState(orderBean.getOcFlag()),
                                orderBean.getMatchPriceStr(), String.valueOf(orderBean.getEntrustNumber()),
                                String.valueOf(orderBean.getRemnantNumber()), MarketUtil.getEntrustState(orderBean.getStatus()),
                                (View) -> mCancelOrderPopUpWindow.dismiss(),
                                (View) -> {
                                    revocateorder(contractId, String.valueOf(orderBean.getOrderNo()));

                                    mCancelOrderPopUpWindow.dismiss();
                                });
                        mCancelOrderPopUpWindow.showAtLocation(mBinding.recyclerView, Gravity.CENTER, 0, 0);
                    }

                    break;
            }
        });
    }

    @Override
    public void initBinding() {
        super.initBinding();

        mBinding = (FragmentCurrentEntrustBinding) mBindingUtil;
        mBinding.setHandlers(new ClickHandlers());
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        bVisibleToUser = isVisibleToUser;

        super.setUserVisibleHint(isVisibleToUser);

        if (null != mBinding && bVisibleToUser)
            initOrderPage(true);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        bVisibleToUser = !hidden;

        super.onHiddenChanged(hidden);

        if (hidden)
            initOrderPage(true);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (bVisibleToUser)
            initOrderPage(true);
    }

    private void initRxBus() {
        mRxbus = RxBus.getInstance().toObserverable(RxBus.Message.class).subscribe(message -> {
            String callType = message.getObject().toString();

            if (TextUtils.isEmpty(callType))
                return;

            switch (callType) {
                case Constants.RxBusConst.RXBUS_TRANSACTION_HOLDPOSITIONS_REFRESH:
                    initOrderPage(false);

                    break;
            }
        });
    }

    private void initOrderPage(boolean enable) {
        mCurrentPage = 1;
        mPagingKey = "";

        mBinding.tvQuery.setVisibility(View.GONE);

        orderpage(enable);
    }

    private void setEmptyData() {
        mAdapter.loadMoreFail();
    }

    private View getEmptyView() {
        mBinding.layoutTabhost.setVisibility(View.GONE);

        if (null == mEmptyView)
            mEmptyView = LayoutInflater.from(mContext).inflate(R.layout.layout_empty_margin_small, null);

        TextView tv_empty = mEmptyView.findViewById(R.id.tv_empty);
        tv_empty.setText(R.string.transaction_entrust_empty);

        mBinding.tvQuery.setVisibility(View.VISIBLE);

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

    private void revocateorder(String contractId, String orderNo) {
        if (null == mUser || !mUser.isLogin())
            return;

        HashMap<String, String> params = new HashMap<>();
        params.put("contractId", contractId);
        params.put("accountId", mUser.getAccountID());
        params.put("orderNo", orderNo);

        sendRequest(TradeService.getInstance().revocateorder, params, true);
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
                            mBinding.layoutTabhost.setVisibility(View.VISIBLE);

                            if (mCurrentPage == 1)
                                mAdapter.setNewData(orderBeanList);
                            else
                                mAdapter.addData(orderBeanList);

                            mAdapter.loadMoreComplete();
                        } else {
                            if (mCurrentPage == 1) {
                                if (null == orderBeanList || 0 == orderBeanList.size()) {
                                    mAdapter.setNewData(null);
                                    mAdapter.setEmptyView(getEmptyView());
                                } else {
                                    mBinding.layoutTabhost.setVisibility(View.VISIBLE);

                                    mAdapter.setNewData(orderBeanList);
                                    mAdapter.loadMoreComplete();
                                }
                            } else {
                                mBinding.layoutTabhost.setVisibility(View.VISIBLE);

                                mAdapter.addData(orderBeanList);
                                mAdapter.loadMoreComplete();
                            }
                        }
                    }
                } else {
                    setEmptyData();
                }

                break;
            case "RevocateOrder":
                if (head.isSuccess())
                    showShortToast(R.string.trade_cancel_order_success);

                initOrderPage(true);

                break;
        }
    }

    @Override
    public void onLoadMoreRequested() {
        mBinding.recyclerView.postDelayed(() -> {
            if (bHasNext) {
                mCurrentPage++;

                orderpage(false);
            } else {
                mAdapter.loadMoreEnd(true);

                mBinding.tvQuery.setVisibility(View.VISIBLE);
            }
        }, 0);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (!mRxbus.isUnsubscribed())
            mRxbus.unsubscribe();
    }

    public class ClickHandlers {

        public void onClickQuery() {
            ARouter.getInstance().build(Constants.ARouterUriConst.QUERY).navigation();
        }

    }

}
