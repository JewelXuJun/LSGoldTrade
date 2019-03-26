package com.jme.lsgoldtrade.ui.trade;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.common.util.NetWorkUtils;
import com.jme.common.util.RxBus;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseActivity;
import com.jme.lsgoldtrade.config.AppConfig;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.ActivityCurrentHoldPositionBinding;
import com.jme.lsgoldtrade.domain.FiveSpeedVo;
import com.jme.lsgoldtrade.domain.PositionPageVo;
import com.jme.lsgoldtrade.domain.PositionVo;
import com.jme.lsgoldtrade.service.MarketService;
import com.jme.lsgoldtrade.service.TradeService;
import com.jme.lsgoldtrade.util.MarketUtil;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import rx.Subscription;

@Route(path = Constants.ARouterUriConst.CURRENTHOLDPOSITION)
public class CurrentHoldPositionActivity extends JMEBaseActivity implements OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {

    private ActivityCurrentHoldPositionBinding mBinding;

    private HoldPositionAdapter mAdapter;
    private View mEmptyView;
    private Subscription mRxbus;

    private List<String> mList;

    private int mCurrentPage = 1;
    private boolean bFlag = true;
    private boolean bHasNext = false;
    private String mPagingKey = "";

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constants.Msg.MSG_POSITION_UPDATE_DATA:
                    mHandler.removeMessages(Constants.Msg.MSG_POSITION_UPDATE_DATA);

                    getMarket();

                    mHandler.sendEmptyMessageDelayed(Constants.Msg.MSG_POSITION_UPDATE_DATA, getTimeInterval());

                    break;
                case Constants.Msg.MSG_POSITION_UPDATE_ACCOUNT_DATA:
                    mHandler.removeMessages(Constants.Msg.MSG_POSITION_UPDATE_ACCOUNT_DATA);

                    initPosition(false);

                    mHandler.sendEmptyMessageDelayed(Constants.Msg.MSG_POSITION_UPDATE_ACCOUNT_DATA, AppConfig.Minute);

                    break;
            }

            super.handleMessage(msg);
        }
    };

    @Override
    protected int getContentViewId() {
        return R.layout.activity_current_hold_position;
    }

    @Override
    protected void initView() {
        super.initView();

        mBinding = (ActivityCurrentHoldPositionBinding) mBindingUtil;

        initToolbar(R.string.trade_curren_hold_position, true);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);

        mAdapter = new HoldPositionAdapter(this, R.layout.item_hold_position, null);
        mList = new ArrayList<>();

        mBinding.recyclerView.setHasFixedSize(false);
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mBinding.recyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void initListener() {
        super.initListener();

        initRxBus();

        mBinding.swipeRefreshLayout.setOnRefreshListener(this);
        mAdapter.setOnLoadMoreListener(this, mBinding.recyclerView);
    }

    @Override
    protected void initBinding() {
        super.initBinding();
    }

    @Override
    protected void onResume() {
        super.onResume();

        initPosition(true);
    }

    @Override
    protected void onPause() {
        super.onPause();

        mHandler.removeMessages(Constants.Msg.MSG_POSITION_UPDATE_DATA);
        mHandler.removeMessages(Constants.Msg.MSG_POSITION_UPDATE_ACCOUNT_DATA);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (!mRxbus.isUnsubscribed())
            mRxbus.unsubscribe();
    }

    private void initPosition(boolean enable) {
        bFlag = true;
        mCurrentPage = 1;
        mPagingKey = "";
        mList.clear();

        mHandler.removeMessages(Constants.Msg.MSG_POSITION_UPDATE_DATA);
        mHandler.removeMessages(Constants.Msg.MSG_POSITION_UPDATE_ACCOUNT_DATA);

        position(enable);
    }

    private void initRxBus() {
        mRxbus = RxBus.getInstance().toObserverable(RxBus.Message.class).subscribe(message -> {
            String callType = message.getObject().toString();

            if (TextUtils.isEmpty(callType))
                return;

            switch (callType) {
                case Constants.RxBusConst.RXBUS_ORDER_SUCCESS:
                    getMarket();

                    break;
            }
        });
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

    private long getTimeInterval() {
        return NetWorkUtils.isWifiConnected(mContext) ? AppConfig.TimeInterval_WiFi : AppConfig.TimeInterval_NetWork;
    }

    private void getMarket() {
        HashMap<String, String> params = new HashMap<>();
        params.put("list", "");

        sendRequest(MarketService.getInstance().getFiveSpeedQuotes, params, false);
    }

    private void position(boolean enable) {
        HashMap<String, String> params = new HashMap<>();
        params.put("accountId", mUser.getAccountID());
        params.put("pagingKey", mPagingKey);

        sendRequest(TradeService.getInstance().position, params, enable);
    }

    @Override
    protected void DataReturn(DTRequest request, Head head, Object response) {
        super.DataReturn(request, head, response);

        switch (request.getApi().getName()) {
            case "GetFiveSpeedQuotes":
                if (head.isSuccess()) {
                    List<FiveSpeedVo> fiveSpeedVoList;

                    try {
                        fiveSpeedVoList = (List<FiveSpeedVo>) response;
                    } catch (Exception e) {
                        fiveSpeedVoList = null;

                        e.getMessage();
                    }

                    List<PositionVo> positionVoList = mAdapter.getData();

                    if (null == fiveSpeedVoList || 0 == fiveSpeedVoList.size() || null == positionVoList || 0 == positionVoList.size())
                        return;

                    mList.clear();

                    for (PositionVo positionVo : positionVoList) {
                        if (null != positionVo) {
                            String contractID = positionVo.getContractId();

                            for (FiveSpeedVo fiveSpeedVo : fiveSpeedVoList) {
                                if (null != fiveSpeedVo) {
                                    if (contractID.equals(fiveSpeedVo.getContractId())) {
                                        long latestprice = fiveSpeedVo.getLatestPrice();
                                        long average = positionVo.getPositionAverage();
                                        long handWeight = mContract.getHandWeightFromID(contractID);
                                        long position = positionVo.getPosition();
                                        long contractValue = contractID.equals("Ag(T+D)") ?
                                                new BigDecimal(handWeight).divide(new BigDecimal(1000), 0, BigDecimal.ROUND_HALF_UP).longValue() : handWeight;

                                        long margin;
                                        String floatProfit;

                                        if (new BigDecimal(latestprice).compareTo(new BigDecimal(0)) == 0
                                                || new BigDecimal(position).compareTo(new BigDecimal(0)) == 0) {
                                            floatProfit = "0";
                                        } else {
                                            if (positionVo.getType().equals("多"))
                                                margin = new BigDecimal(latestprice).subtract(new BigDecimal(average)).longValue();
                                            else
                                                margin = new BigDecimal(average).subtract(new BigDecimal(latestprice)).longValue();

                                            floatProfit = (new BigDecimal(MarketUtil.getPriceValue(margin))
                                                    .multiply(new BigDecimal(contractValue)).multiply(new BigDecimal(position)))
                                                    .setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString();
                                        }

                                        mList.add(floatProfit);
                                    }
                                }
                            }
                        }
                    }

                    mAdapter.setList(mList);
                    mAdapter.notifyDataSetChanged();
                }

                break;
            case "Position":
                if (head.isSuccess()) {
                    PositionPageVo positionPageVo;

                    try {
                        positionPageVo = (PositionPageVo) response;
                    } catch (Exception e) {
                        positionPageVo = null;

                        e.printStackTrace();
                    }

                    if (null == positionPageVo) {
                        setEmptyData();
                    } else {
                        bHasNext = positionPageVo.isHasNext();
                        mPagingKey = positionPageVo.getPagingKey();
                        List<PositionVo> positionVoList = positionPageVo.getPositionList();

                        if (null != positionVoList && 0 != positionVoList.size()) {
                            for (PositionVo positionVo : positionVoList) {
                                if (null != positionVo)
                                    mList.add(MarketUtil.getPriceValue(positionVo.getFloatProfit()));
                            }
                        }

                        if (!bFlag)
                            mAdapter.setList(mList);

                        if (bHasNext) {
                            if (mCurrentPage == 1)
                                mAdapter.setNewData(positionVoList);
                            else
                                mAdapter.addData(positionVoList);

                            mAdapter.loadMoreComplete();
                            mBinding.swipeRefreshLayout.finishRefresh(true);
                        } else {
                            if (mCurrentPage == 1) {
                                if (null == positionVoList || 0 == positionVoList.size()) {
                                    mAdapter.setNewData(null);
                                    mAdapter.setEmptyView(getEmptyView());
                                } else {
                                    mAdapter.setNewData(positionVoList);
                                    mAdapter.loadMoreComplete();
                                }
                            } else {
                                mAdapter.addData(positionVoList);
                                mAdapter.loadMoreComplete();
                            }

                            mBinding.swipeRefreshLayout.finishRefresh(true);
                        }
                    }
                } else {
                    setEmptyData();
                }

                if (bFlag) {
                    bFlag = false;

                    mHandler.sendEmptyMessageDelayed(Constants.Msg.MSG_POSITION_UPDATE_DATA, 0);
                    mHandler.sendEmptyMessageDelayed(Constants.Msg.MSG_POSITION_UPDATE_ACCOUNT_DATA, AppConfig.Minute);
                }

                break;
        }
    }

    @Override
    public void onLoadMoreRequested() {
        mBinding.recyclerView.postDelayed(() -> {
            if (bHasNext) {
                mCurrentPage++;

                position(true);
            } else {
                mAdapter.loadMoreEnd();
            }
        }, 0);
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        initPosition(false);
    }
}
