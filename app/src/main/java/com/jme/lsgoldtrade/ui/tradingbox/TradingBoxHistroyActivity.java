package com.jme.lsgoldtrade.ui.tradingbox;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseActivity;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.ActivityHistoryBoxBinding;
import com.jme.lsgoldtrade.domain.HistoryBoxVo;
import com.jme.lsgoldtrade.domain.HistoryItemVo;
import com.jme.lsgoldtrade.service.ManagementService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 历史匣子
 */
@Route(path = Constants.ARouterUriConst.TRADINGBOXHISTROY)
public class TradingBoxHistroyActivity extends JMEBaseActivity {

    private ActivityHistoryBoxBinding mBinding;

    private TradingBoxHistroyAdapter adapter;

    private int mCurrentPage = 1;

    private List<HistoryBoxVo> value = new ArrayList<>();

    @Override
    protected int getContentViewId() {
        return R.layout.activity_history_box;
    }

    @Override
    protected void initView() {
        super.initView();
        mBinding = (ActivityHistoryBoxBinding) mBindingUtil;
//        adapter = new TradingBoxHistroyAdapter(R.layout.item_history_box, null);
        adapter = new TradingBoxHistroyAdapter(R.layout.item_history_box, null, mContext);

        mBinding.recyclerView.setHasFixedSize(false);
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mBinding.recyclerView.setAdapter(adapter);

        initToolbar("历史匣子", true);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        getDataFromNet();
    }

    private void getDataFromNet() {
        sendRequest(ManagementService.getInstance().historyBox, new HashMap<>(), false);
    }

    @Override
    protected void initListener() {
        super.initListener();
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                List<HistoryBoxVo.HistoryListVoListBean> historyListVoList = value.get(position).getHistoryListVoList();
                String json = new Gson().toJson(historyListVoList);

                ARouter.getInstance()
                        .build(Constants.ARouterUriConst.TRADINGBOXDETAILS)
                        .withString("tradeId", json)
                        .withString("type", "2")
                        .navigation();
            }
        });
    }

    @Override
    protected void DataReturn(DTRequest request, Head head, Object response) {
        super.DataReturn(request, head, response);
        switch (request.getApi().getName()) {
            case "HistoryBox":
                if (head.isSuccess()) {
                    try {
                        value = (List<HistoryBoxVo>) response;
                    } catch (Exception e) {
                        value = null;
                        e.printStackTrace();
                    }

                    if (null == value) {
                        return;
                    }

                    List<HistoryItemVo> list = new ArrayList<>();
                    for (int i = 0; i < value.size(); i++) {
                        HistoryBoxVo.HistoryListVoListBean historyListVoListBean = value.get(i).getHistoryListVoList().get(0);
                        HistoryItemVo historyItemVo = new HistoryItemVo();
                        historyItemVo.setChance(historyListVoListBean.getChance());
                        historyItemVo.setDirection(historyListVoListBean.getDirection());
                        historyItemVo.setPushTime(historyListVoListBean.getPushTime());
                        historyItemVo.setTradeId(historyListVoListBean.getTradeId());
                        historyItemVo.setVariety(historyListVoListBean.getVariety());
                        historyItemVo.setPeriodName(value.get(i).getPeriodName());
                        list.add(historyItemVo);
                    }
                    adapter.setNewData(list);


//                    List<MySection> mySectionList = new ArrayList<>();
//                    for (int i = 0; i < value.size(); i++) {
//                        HistoryBoxVo historyBoxVo = value.get(i);
//                        String time = historyBoxVo.getPeriodName();
//                        mySectionList.add(new MySection(true, time));
//
//                        List<HistoryBoxVo.HistoryListVoListBean> listContent = historyBoxVo.getHistoryListVoList();
//
//                        for (int j = 0; j < 1; j++) {
//                            HistoryBoxVo.HistoryListVoListBean listBean = listContent.get(j);
//                            String chance = listBean.getChance();
//                            String direction = listBean.getDirection();
//                            String pushTime = listBean.getPushTime();
//                            String tradeId = listBean.getTradeId();
//                            String variety = listBean.getVariety();
//                            MySection mySection = new MySection(new SectionBean(chance, direction, pushTime, tradeId, variety));
//                            mySectionList.add(mySection);
//                        }
//                    }
//                    adapter.setNewData(mySectionList);

//                    if (mCurrentPage == 1) {
//                        adapter.setNewData(mySectionList);
//                        if (null == historyListVoList || 0 == historyListVoList.size())
//                            adapter.setEmptyView(EmptyView.getEmptyView(this));
//                    } else {
//                        adapter.addData(mySectionList);
//                        adapter.loadMoreComplete();
//                    }
//                    mBinding.swipeRefreshLayout.finishRefresh(true);
                } else {
                    mBinding.swipeRefreshLayout.finishRefresh(false);
                }
                break;
        }
    }
}
