package com.jme.lsgoldtrade.ui.tradingbox;

import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseActivity;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.ActivityTradingBoxBinding;
import com.jme.lsgoldtrade.domain.SubscribeStateVo;
import com.jme.lsgoldtrade.domain.TradingBoxVo;
import com.jme.lsgoldtrade.service.ManagementService;
import com.jme.lsgoldtrade.util.TradeBoxFunctionUtils;

import java.util.HashMap;
import java.util.List;

@Route(path = Constants.ARouterUriConst.TRADINGBOX)
public class TradingBoxActivity extends JMEBaseActivity {

    private ActivityTradingBoxBinding mBinding;

    private TradingBoxAdapter mAdapter;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_trading_box;
    }

    @Override
    protected void initView() {
        super.initView();

        initToolbar(R.string.trading_box_title, true);

        setRightNavigation("", R.mipmap.ic_more, 0, () -> TradeBoxFunctionUtils.show(this, "", "", "",0, false));
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);

        mAdapter = new TradingBoxAdapter(this, null, "TradingBox");

        mBinding.recyclerView.setHasFixedSize(false);
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mBinding.recyclerView.setAdapter(mAdapter);

        querySubscriberCount();
        getListExt();
        queryTradeBoxList();
    }

    @Override
    protected void initListener() {
        super.initListener();
    }

    @Override
    protected void initBinding() {
        super.initBinding();

        mBinding = (ActivityTradingBoxBinding) mBindingUtil;
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

    private void queryTradeBoxList() {
        sendRequest(ManagementService.getInstance().queryTradeBoxList, new HashMap<>(), true);
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
            case "QueryTradeBoxList":
                if (head.isSuccess()) {
                    List<TradingBoxVo> tradingBoxVoList;

                    try {
                        tradingBoxVoList = (List<TradingBoxVo>) response;
                    } catch (Exception e) {
                        tradingBoxVoList = null;

                        e.printStackTrace();
                    }

                    if (null == tradingBoxVoList || 0 == tradingBoxVoList.size()) {
                        mBinding.layoutNoData.setVisibility(View.VISIBLE);
                        mBinding.nestedScrollView.setVisibility(View.GONE);
                    } else {
                        mBinding.layoutNoData.setVisibility(View.GONE);
                        mBinding.nestedScrollView.setVisibility(View.VISIBLE);
                    }

                    mAdapter.setNewData(tradingBoxVoList);
                }

                break;
        }
    }

    public class ClickHandlers {

        public void onClickSubcribe() {
            setAppSubscribe();
        }

        public void onClickCheckHistory() {
            ARouter.getInstance().build(Constants.ARouterUriConst.TRADINGBOXHISTROY).navigation();
        }

    }

}
