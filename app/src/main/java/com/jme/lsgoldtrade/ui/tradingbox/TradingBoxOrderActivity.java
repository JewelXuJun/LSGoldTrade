package com.jme.lsgoldtrade.ui.tradingbox;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseActivity;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.ActivityTradingBoxOrderBinding;
import com.jme.lsgoldtrade.domain.TradingBoxOrderVo;
import com.jme.lsgoldtrade.service.ManagementService;
import com.jme.lsgoldtrade.view.EmptyView;
import com.jme.lsgoldtrade.view.OrderConfirmPopupwindow;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.HashMap;
import java.util.List;

/**
 * 我的订单
 */
@Route(path = Constants.ARouterUriConst.TRADINGBOXORDER)
public class TradingBoxOrderActivity extends JMEBaseActivity implements OnRefreshListener {

    private ActivityTradingBoxOrderBinding mBinding;

    private OrderConfirmPopupwindow mWindow;
    private TradingBoxOrderAdapter adapter;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_trading_box_order;
    }

    @Override
    protected void initView() {
        super.initView();

        initToolbar(R.string.trading_box_order, true);

        mWindow = new OrderConfirmPopupwindow(this);
        mWindow.setOutsideTouchable(true);
        mWindow.setFocusable(true);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);

        adapter = new TradingBoxOrderAdapter(R.layout.item_trading_box_order, null);

        mBinding.recyclerView.setHasFixedSize(false);
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mBinding.recyclerView.setAdapter(adapter);

        getOrderList(true);
    }

    @Override
    protected void initListener() {
        super.initListener();

        mBinding.swipeRefreshLayout.setOnRefreshListener(this);

        adapter.setOnItemChildClickListener((adapter, view, position) -> {
            TradingBoxOrderVo tradingBoxOrderVo = (TradingBoxOrderVo) adapter.getItem(position);

            if (null == tradingBoxOrderVo)
                return;

            switch (view.getId()) {
                case R.id.btn_cancel:
                    if (null != mWindow || !mWindow.isShowing()) {
                        mWindow.setData(getString(R.string.trading_box_order_cancel_message),
                                (View) -> {
                                    revocateorder(tradingBoxOrderVo.getId());

                                    mWindow.dismiss();
                                });
                        mWindow.showAtLocation(mBinding.recyclerView, Gravity.CENTER, 0, 0);
                    }

                    break;
                case R.id.btn_detail:
                    ARouter.getInstance()
                            .build(Constants.ARouterUriConst.ORDERDETAILS)
                            .withString("ID", tradingBoxOrderVo.getId())
                            .navigation();

                    break;
            }
        });

    }

    @Override
    protected void initBinding() {
        super.initBinding();

        mBinding = (ActivityTradingBoxOrderBinding) mBindingUtil;
    }

    private void getOrderList(boolean enable) {
        sendRequest(ManagementService.getInstance().getOrderList, new HashMap<>(), enable);
    }

    private void revocateorder(String id) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("id", id);

        sendRequest(ManagementService.getInstance().revocation, hashMap, true);
    }

    @Override
    protected void DataReturn(DTRequest request, Head head, Object response) {
        super.DataReturn(request, head, response);

        switch (request.getApi().getName()) {
            case "GetOrderList":
                if (head.isSuccess()) {
                    List<TradingBoxOrderVo> tradingBoxOrderVoList;

                    try {
                        tradingBoxOrderVoList = (List<TradingBoxOrderVo>) response;
                    } catch (Exception e) {
                        tradingBoxOrderVoList = null;

                        e.printStackTrace();
                    }

                    adapter.setNewData(tradingBoxOrderVoList);

                    if (null == tradingBoxOrderVoList || 0 == tradingBoxOrderVoList.size())
                        adapter.setEmptyView(EmptyView.getEmptyView(this));

                    mBinding.swipeRefreshLayout.finishRefresh(true);
                } else {
                    mBinding.swipeRefreshLayout.finishRefresh(false);
                }
                break;
            case "Revocation":
                if (head.isSuccess()) {
                    showShortToast(R.string.trade_cancel_order_success);

                    getOrderList(true);
                }

                break;
        }
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        getOrderList(false);
    }
}
