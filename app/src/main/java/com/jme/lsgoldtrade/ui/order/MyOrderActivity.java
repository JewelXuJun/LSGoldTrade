package com.jme.lsgoldtrade.ui.order;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.common.ui.view.MarginDividerItemDecoration;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseActivity;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.ActivityMyOrderBinding;
import com.jme.lsgoldtrade.domain.InfoVo;
import com.jme.lsgoldtrade.domain.MyOrderVo;
import com.jme.lsgoldtrade.service.ManagementService;
import com.jme.lsgoldtrade.view.EmptyView;
import com.jme.lsgoldtrade.view.NormalPopupwindow;

import java.util.HashMap;
import java.util.List;

/**
 * 我的订单
 */
@Route(path = Constants.ARouterUriConst.MYORDER)
public class MyOrderActivity extends JMEBaseActivity {

    private ActivityMyOrderBinding mBinding;

    private MyOrderAdapter adapter;

    private int mCurrentPage = 1;

    private NormalPopupwindow mWindow;

    private List<MyOrderVo> value;

    public int position;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_my_order;
    }

    @Override
    protected void initView() {
        super.initView();
        mBinding = (ActivityMyOrderBinding) mBindingUtil;
        initToolbar(R.string.my_order, true);
        adapter = new MyOrderAdapter(mContext, R.layout.item_my_order, null);

        mBinding.recyclerView.addItemDecoration(new MarginDividerItemDecoration(mContext, LinearLayoutManager.VERTICAL));
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mBinding.recyclerView.setAdapter(adapter);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        mWindow = new NormalPopupwindow(mContext);
        mWindow.setOutsideTouchable(true);
        mWindow.setFocusable(true);

        getDataFromNet();
    }

    private void getDataFromNet() {
        sendRequest(ManagementService.getInstance().myOrder, new HashMap<>(), false);
    }

    @Override
    protected void DataReturn(DTRequest request, Head head, Object response) {
        super.DataReturn(request, head, response);
        switch (request.getApi().getName()) {
            case "MyOrder":
                if (head.isSuccess()) {
                    try {
                        value = (List<MyOrderVo>) response;
                    } catch (Exception e) {
                        value = null;
                        e.printStackTrace();
                    }

                    if (null == value) {
                        mBinding.swipeRefreshLayout.finishRefresh(false);
                    } else {
                        if (mCurrentPage == 1) {
                            adapter.setNewData(value);

                            if (null == value || 0 == value.size())
                                adapter.setEmptyView(EmptyView.getEmptyView(this));
                        } else {
                            adapter.addData(value);
                            adapter.loadMoreComplete();
                        }

                        mBinding.swipeRefreshLayout.finishRefresh(true);
                    }
                } else {
                    mBinding.swipeRefreshLayout.finishRefresh(false);
                }
                break;
            case "Revocation":
                if (head.isSuccess())
                    showShortToast(R.string.trade_cancel_order_success);

                MyOrderVo myOrderVo = (MyOrderVo) adapter.getItem(position);
                myOrderVo.setStatus("7");
                adapter.notifyDataSetChanged();
                break;
        }
    }

    @Override
    protected void initListener() {
        super.initListener();
        adapter.setOnCheDanListener(new MyOrderAdapter.OnCheDanListener() {
            @Override
            public void cheDanListener(int position) {
                if (null != mWindow) {
                    mWindow.setData("您确认取消该笔委托吗?",
                            (View) -> {
                                adapter.notifyDataSetChanged();
                                mWindow.dismiss();
                            },
                            (View) -> {
                                revocateorder(value.get(position).getId(), position);
                                mWindow.dismiss();
                            });
                    mWindow.showAtLocation(mBinding.recyclerView, Gravity.CENTER, 0, 0);
                }
            }
        });
    }

    private void revocateorder(String id, int position) {
        this.position = position;
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("id", id);
        sendRequest(ManagementService.getInstance().revocation, hashMap, false);
    }
}
