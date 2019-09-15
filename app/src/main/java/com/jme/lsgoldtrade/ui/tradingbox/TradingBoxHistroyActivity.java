package com.jme.lsgoldtrade.ui.tradingbox;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseActivity;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.ActivityTradingBoxHistoryBinding;
import com.jme.lsgoldtrade.domain.SubscribeStateVo;
import com.jme.lsgoldtrade.domain.TradingBoxHistoryVo;
import com.jme.lsgoldtrade.domain.TradingBoxVo;
import com.jme.lsgoldtrade.service.ManagementService;

import java.util.HashMap;
import java.util.List;

@Route(path = Constants.ARouterUriConst.TRADINGBOXHISTROY)
public class TradingBoxHistroyActivity extends JMEBaseActivity implements BaseQuickAdapter.RequestLoadMoreListener{

    private ActivityTradingBoxHistoryBinding mBinding;

    private TradingBoxAdapter mAdapter;

    private int mCurrentPage = 1;
    private int mTotal = 0;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_trading_box_history;
    }

    @Override
    protected void initView() {
        super.initView();

        initToolbar(R.string.trading_box_function_history, true);

        mAdapter = new TradingBoxAdapter(this, null, "TradingBoxHistory");

        mBinding.recyclerView.setHasFixedSize(false);
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mBinding.recyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);

        querySubscriberCount();
        getListExt();
        queryTradeBoxLossHistoryInfo();
    }

    @Override
    protected void initListener() {
        super.initListener();

        mAdapter.setOnLoadMoreListener(this, mBinding.recyclerView);
    }

    @Override
    protected void initBinding() {
        super.initBinding();

        mBinding = (ActivityTradingBoxHistoryBinding) mBindingUtil;
        mBinding.setHandlers(new ClickHandlers());
    }

    private void querySubscriberCount() {
        sendRequest(ManagementService.getInstance().querySubscriberCount, new HashMap<>(), false);
    }

    private void getListExt() {
        sendRequest(ManagementService.getInstance().getListExt, new HashMap<>(), false);
    }

    private void setAppSubscribe() {
        sendRequest(ManagementService.getInstance().setAppSubscribe, new HashMap<>(), true);
    }

    private void queryTradeBoxLossHistoryInfo() {
        HashMap<String, String> params = new HashMap<>();
        params.put("current", String.valueOf(mCurrentPage));
        params.put("pageSize", "10");

        sendRequest(ManagementService.getInstance().queryTradeBoxLossHistoryInfo, params, true);
    }

    @Override
    protected void DataReturn(DTRequest request, Head head, Object response) {
        super.DataReturn(request, head, response);

        switch (request.getApi().getName()) {
            case "QuerySubscriberCount":
                if (head.isSuccess())
                    mBinding.tvSubscribeNumber.setText(String.format(getString(R.string.trading_box_subscribe_number), null == response ? 0 : (int) response));

                break;
            case "GetListExt":
                if (head.isSuccess()) {
                    SubscribeStateVo subscribeStateVo;

                    try {
                        subscribeStateVo = (SubscribeStateVo) response;
                    } catch (Exception e) {
                        subscribeStateVo = null;

                        e.printStackTrace();
                    }

                    if (null == subscribeStateVo) {
                        mBinding.tvUnSubscribe.setVisibility(View.GONE);
                        mBinding.tvSubscribe.setVisibility(View.GONE);
                    } else {
                        List<SubscribeStateVo.SubscribeBean> subscribeBeanList = subscribeStateVo.getList();

                        boolean subscribeFlag = null == subscribeBeanList || 0 == subscribeBeanList.size() ? false : true;

                        mBinding.tvUnSubscribe.setVisibility(subscribeFlag ? View.GONE : View.VISIBLE);
                        mBinding.tvSubscribe.setVisibility(subscribeFlag ? View.VISIBLE : View.GONE);
                    }
                }

                break;
            case "SetAppSubscribe":
                if (head.isSuccess())
                    getListExt();

                break;
            case "QueryTradeBoxLossHistoryInfo":
                if (head.isSuccess()) {
                    TradingBoxHistoryVo tradingBoxHistoryVo;

                    try {
                        tradingBoxHistoryVo = (TradingBoxHistoryVo) response;
                    } catch (Exception e) {
                        tradingBoxHistoryVo = null;

                        e.printStackTrace();
                    }

                    if (null == tradingBoxHistoryVo) {
                        mBinding.layoutNoData.setVisibility(View.VISIBLE);
                        mBinding.nestedScrollView.setVisibility(View.GONE);
                    } else {
                        mTotal = tradingBoxHistoryVo.getTotal();

                        List<TradingBoxVo> tradingBoxVoList = tradingBoxHistoryVo.getRecords();

                        if (null == tradingBoxVoList || 0 == tradingBoxVoList.size()) {
                            mBinding.layoutNoData.setVisibility(View.VISIBLE);
                            mBinding.nestedScrollView.setVisibility(View.GONE);
                        } else {
                            mBinding.layoutNoData.setVisibility(View.GONE);
                            mBinding.nestedScrollView.setVisibility(View.VISIBLE);

                            if (mCurrentPage == 1) {
                                mAdapter.setNewData(tradingBoxVoList);
                            } else {
                                mAdapter.addData(tradingBoxVoList);
                                mAdapter.loadMoreComplete();
                            }
                        }
                    }
                }

                break;
        }
    }

    @Override
    public void onLoadMoreRequested() {
        mBinding.recyclerView.postDelayed(() -> {
            if (mCurrentPage < mTotal) {
                mCurrentPage++;

                queryTradeBoxLossHistoryInfo();
            } else {
                mAdapter.loadMoreEnd();
            }
        }, 0);
    }

    public class ClickHandlers {

        public void onClickSubcribe() {
            setAppSubscribe();
        }

    }

}
