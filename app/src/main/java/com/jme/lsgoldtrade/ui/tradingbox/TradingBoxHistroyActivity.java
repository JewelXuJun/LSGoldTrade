package com.jme.lsgoldtrade.ui.tradingbox;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.google.gson.Gson;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseActivity;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.ActivityTradingBoxHistoryBinding;
import com.jme.lsgoldtrade.domain.TradingBoxHistoryItemVo;
import com.jme.lsgoldtrade.domain.TradingBoxHistoryItemSimpleVo;
import com.jme.lsgoldtrade.service.ManagementService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 历史匣子
 */
@Route(path = Constants.ARouterUriConst.TRADINGBOXHISTROY)
public class TradingBoxHistroyActivity extends JMEBaseActivity {

    private ActivityTradingBoxHistoryBinding mBinding;

    private TradingBoxHistroyAdapter mAdapter;

    private List<TradingBoxHistoryItemVo> mTradingBoxHistoryItemVoList;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_trading_box_history;
    }

    @Override
    protected void initView() {
        super.initView();

        initToolbar(R.string.trading_box_function_history, true);

        mBinding = (ActivityTradingBoxHistoryBinding) mBindingUtil;

        mAdapter = new TradingBoxHistroyAdapter(R.layout.item_trading_box_history, null);

        mBinding.recyclerView.setHasFixedSize(false);
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mBinding.recyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);

        getTradeBoxHistoryInfo();
    }

    @Override
    protected void initListener() {
        super.initListener();

        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            if (null == mTradingBoxHistoryItemVoList || 0 == mTradingBoxHistoryItemVoList.size())
                return;

            TradingBoxHistoryItemVo tradingBoxHistoryItemVo = mTradingBoxHistoryItemVoList.get(position);

            if (null == tradingBoxHistoryItemVo)
                return;

            List<TradingBoxHistoryItemVo.HistoryListVoListBean> historyListVoListBeans = tradingBoxHistoryItemVo.getHistoryListVoList();

            if (null == historyListVoListBeans || 0 == historyListVoListBeans.size())
                return;

            String json = new Gson().toJson(historyListVoListBeans);

            ARouter.getInstance()
                    .build(Constants.ARouterUriConst.TRADINGBOXDETAIL)
                    .withString("TradeId", json)
                    .withString("Type", "2")
                    .navigation();
        });
    }

    @Override
    protected void initBinding() {
        super.initBinding();
    }

    private void getTradeBoxHistoryInfo() {
        sendRequest(ManagementService.getInstance().tradeBoxHistoryInfo, new HashMap<>(), true);
    }

    @Override
    protected void DataReturn(DTRequest request, Head head, Object response) {
        super.DataReturn(request, head, response);

        switch (request.getApi().getName()) {
            case "TradeBoxHistoryInfo":
                if (head.isSuccess()) {
                    try {
                        mTradingBoxHistoryItemVoList = (List<TradingBoxHistoryItemVo>) response;
                    } catch (Exception e) {
                        mTradingBoxHistoryItemVoList = null;

                        e.printStackTrace();
                    }

                    if (null == mTradingBoxHistoryItemVoList || 0 == mTradingBoxHistoryItemVoList.size())
                        return;

                    List<TradingBoxHistoryItemSimpleVo> list = new ArrayList<>();

                    for (int i = 0; i < mTradingBoxHistoryItemVoList.size(); i++) {
                        List<TradingBoxHistoryItemVo.HistoryListVoListBean> historyListVoListBeanList = mTradingBoxHistoryItemVoList.get(i).getHistoryListVoList();

                        if (null != historyListVoListBeanList && historyListVoListBeanList.size() > 0) {
                            TradingBoxHistoryItemVo.HistoryListVoListBean historyListVoListBean = historyListVoListBeanList.get(0);
                            TradingBoxHistoryItemSimpleVo tradingBoxHistoryItemSimpleVo = new TradingBoxHistoryItemSimpleVo();
                            tradingBoxHistoryItemSimpleVo.setChance(historyListVoListBean.getChance());
                            tradingBoxHistoryItemSimpleVo.setDirection(historyListVoListBean.getDirection());
                            tradingBoxHistoryItemSimpleVo.setPushTime(historyListVoListBean.getPushTime());
                            tradingBoxHistoryItemSimpleVo.setTradeId(historyListVoListBean.getTradeId());
                            tradingBoxHistoryItemSimpleVo.setVariety(historyListVoListBean.getVariety());
                            tradingBoxHistoryItemSimpleVo.setPeriodName(mTradingBoxHistoryItemVoList.get(i).getPeriodName());

                            list.add(tradingBoxHistoryItemSimpleVo);
                        }
                    }

                    mAdapter.setNewData(list);
                }

                break;
        }
    }
}
