package com.jme.lsgoldtrade.ui.trade;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseFragment;
import com.jme.lsgoldtrade.databinding.FragmentCancelOrderBinding;
import com.jme.lsgoldtrade.domain.OrderPageVo;
import com.jme.lsgoldtrade.service.TradeService;
import com.jme.lsgoldtrade.view.CancelOrderPopUpWindow;
import com.jme.lsgoldtrade.util.MarketUtil;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.HashMap;
import java.util.List;

/**
 * 撤单
 */
public class CancelOrderFragment extends JMEBaseFragment implements OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {

    private FragmentCancelOrderBinding mBinding;

    private CancelOrderAdapter mAdapter;
    private View mEmptyView;
    private CancelOrderPopUpWindow mWindow;

    private boolean bVisibleToUser = false;
    private int mCurrentPage = 1;
    private boolean bHasNext = false;
    private String mPagingKey = "";

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_cancel_order;
    }

    @Override
    protected void initView() {
        super.initView();
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);

        mAdapter = new CancelOrderAdapter(mContext, R.layout.item_cancel_order, null);
        mWindow = new CancelOrderPopUpWindow(mContext);

        mBinding.recyclerView.setHasFixedSize(false);
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mBinding.recyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void initListener() {
        super.initListener();

        mBinding.swipeRefreshLayout.setOnRefreshListener(this);
        mAdapter.setOnLoadMoreListener(this, mBinding.recyclerView);

        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            OrderPageVo.OrderBean orderBean = (OrderPageVo.OrderBean) adapter.getItem(position);

            if (null == orderBean)
                return;

            int selectPosition = mAdapter.getSelectPosition();

            if (selectPosition == -1) {
                mAdapter.setSelectPosition(position);
            } else {
                if (selectPosition == position)
                    mAdapter.setSelectPosition(-1);
                else
                    mAdapter.setSelectPosition(position);
            }

            mAdapter.notifyDataSetChanged();

            if (null != mWindow) {
                String time = orderBean.getDeclareTime();
                String contractId = orderBean.getContractId();

                mWindow.setData(contractId, TextUtils.isEmpty(time) ? "" : time.replace(".", ":"),
                        MarketUtil.getTradeDirection(orderBean.getBsFlag()) + MarketUtil.getOCState(orderBean.getOcFlag()),
                        orderBean.getMatchPriceStr(), String.valueOf(orderBean.getEntrustNumber()),
                        String.valueOf(orderBean.getRemnantNumber()), MarketUtil.getEntrustState(orderBean.getStatus()),
                        (View) -> {
                            mAdapter.setSelectPosition(-1);
                            mAdapter.notifyDataSetChanged();

                            mWindow.dismiss();
                        },
                        (View) -> {
                            revocateorder(contractId, String.valueOf(orderBean.getOrderNo()));

                            mWindow.dismiss();
                        });
                mWindow.showAtLocation(mBinding.recyclerView, Gravity.CENTER, 0, 0);
            }
        });

        mWindow.setOnDismissListener(() -> {
            mAdapter.setSelectPosition(-1);
            mAdapter.notifyDataSetChanged();
        });
    }

    @Override
    public void initBinding() {
        super.initBinding();

        mBinding = (FragmentCancelOrderBinding) mBindingUtil;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        bVisibleToUser = isVisibleToUser;

        if (null != mBinding && bVisibleToUser)
            initOrderPage(true);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        bVisibleToUser = !hidden;

        super.onHiddenChanged(hidden);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (null != mBinding && bVisibleToUser)
            initOrderPage(true);
    }

    private void initOrderPage(boolean enable) {
        if (null != mWindow && mWindow.isShowing())
            return;

        mCurrentPage = 1;
        mPagingKey = "";
        mAdapter.setSelectPosition(-1);

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
        params.put("onlyRevocable", "true");
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
                                    mBinding.layoutTabhost.setVisibility(View.GONE);
                                    mBinding.swipeRefreshLayout.setVisibility(View.GONE);
                                    mBinding.tvEmpty.setVisibility(View.VISIBLE);
//                                    mAdapter.setEmptyView(getEmptyView());
                                } else {
                                    mBinding.layoutTabhost.setVisibility(View.VISIBLE);
                                    mBinding.swipeRefreshLayout.setVisibility(View.VISIBLE);
                                    mBinding.tvEmpty.setVisibility(View.GONE);
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
            case "RevocateOrder":
                if (head.isSuccess())
                    showShortToast(R.string.trade_cancel_order_success);
                else
                    showShortToast(head.getMsg());
                initOrderPage(true);

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
