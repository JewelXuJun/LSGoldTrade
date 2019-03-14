package com.jme.lsgoldtrade.ui.trade;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.common.util.NetWorkUtils;
import com.jme.common.util.RxBus;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseFragment;
import com.jme.lsgoldtrade.config.AppConfig;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.FragmentItemHoldPositionBinding;
import com.jme.lsgoldtrade.domain.FiveSpeedVo;
import com.jme.lsgoldtrade.domain.PositionPageVo;
import com.jme.lsgoldtrade.domain.PositionVo;
import com.jme.lsgoldtrade.service.MarketService;
import com.jme.lsgoldtrade.service.TradeService;
import com.jme.lsgoldtrade.util.MarketUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import rx.Subscription;

public class ItemHoldPositionFragment extends JMEBaseFragment implements BaseQuickAdapter.RequestLoadMoreListener {

    private FragmentItemHoldPositionBinding mBinding;

    private HoldPositionAdapter mAdapter;
    private List<String> mList;
    private List<PositionVo> mPositionVoList;
    private Subscription mRxbus;

    private int mCurrentPage = 1;
    private boolean bFlag = true;
    private boolean bHasNext = false;
    private boolean bVisibleToUser = false;
    private String mPagingKey = "";

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constants.Msg.MSG_DECLARATIONFORM_POSITION_UPDATE_DATA:
                    mHandler.removeMessages(Constants.Msg.MSG_DECLARATIONFORM_POSITION_UPDATE_DATA);

                    getMarket();

                    mHandler.sendEmptyMessageDelayed(Constants.Msg.MSG_DECLARATIONFORM_POSITION_UPDATE_DATA, getTimeInterval());

                    break;
                case Constants.Msg.MSG_DECLARATIONFORM_POSITION_UPDATE_ACCOUNT_DATA:
                    mHandler.removeMessages(Constants.Msg.MSG_DECLARATIONFORM_POSITION_UPDATE_ACCOUNT_DATA);

                    initPosition();

                    mHandler.sendEmptyMessageDelayed(Constants.Msg.MSG_DECLARATIONFORM_POSITION_UPDATE_ACCOUNT_DATA, AppConfig.Minute);

                    break;
            }

            super.handleMessage(msg);
        }
    };

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_item_hold_position;
    }

    @Override
    protected void initView() {
        super.initView();

        mBinding = (FragmentItemHoldPositionBinding) mBindingUtil;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);

        mAdapter = new HoldPositionAdapter(mContext, R.layout.item_hold_position, null);
        mList = new ArrayList<>();

        mBinding.recyclerView.setHasFixedSize(false);
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mBinding.recyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void initListener() {
        super.initListener();

        initRxBus();

        mAdapter.setOnLoadMoreListener(this, mBinding.recyclerView);

        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            PositionVo positionVo = (PositionVo) adapter.getItem(position);

            if (null == positionVo)
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

            RxBus.getInstance().post(Constants.RxBusConst.RXBUS_DECLARATIONFORM_HOLDPOSITION_SELECT,
                    mAdapter.getSelectPosition() == -1 ? null : positionVo);
        });
    }

    @Override
    public void initBinding() {
        super.initBinding();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        bVisibleToUser = isVisibleToUser;

        super.setUserVisibleHint(isVisibleToUser);

        if (null != mBinding && bVisibleToUser) {
            initPosition();
        } else {
            mHandler.removeMessages(Constants.Msg.MSG_DECLARATIONFORM_POSITION_UPDATE_DATA);
            mHandler.removeMessages(Constants.Msg.MSG_DECLARATIONFORM_POSITION_UPDATE_ACCOUNT_DATA);
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        bVisibleToUser = !hidden;

        super.onHiddenChanged(hidden);

        if (hidden) {
            mHandler.removeMessages(Constants.Msg.MSG_DECLARATIONFORM_POSITION_UPDATE_DATA);
            mHandler.removeMessages(Constants.Msg.MSG_DECLARATIONFORM_POSITION_UPDATE_ACCOUNT_DATA);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (bVisibleToUser)
            initPosition();
    }

    @Override
    public void onPause() {
        super.onPause();

        mHandler.removeMessages(Constants.Msg.MSG_DECLARATIONFORM_POSITION_UPDATE_DATA);
        mHandler.removeMessages(Constants.Msg.MSG_DECLARATIONFORM_POSITION_UPDATE_ACCOUNT_DATA);
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
                case Constants.RxBusConst.RXBUS_DECLARATIONFORM_HOLDPOSITION_UNSELECT:
                    mAdapter.setSelectPosition(-1);
                    mAdapter.notifyDataSetChanged();

                    break;
                case Constants.RxBusConst.RXBUS_DECLARATIONFORM_UPDATE:
                    mAdapter.setSelectPosition(-1);
                    mAdapter.notifyDataSetChanged();

                    initPosition();

                    break;
                case Constants.RxBusConst.RXBUS_ORDER_SUCCESS:
                    getMarket();

                    break;
            }
        });
    }

    private void initPosition() {
        bFlag = true;
        mCurrentPage = 1;
        mPagingKey = "";

        mHandler.removeMessages(Constants.Msg.MSG_DECLARATIONFORM_POSITION_UPDATE_DATA);
        mHandler.removeMessages(Constants.Msg.MSG_DECLARATIONFORM_POSITION_UPDATE_ACCOUNT_DATA);

        position();
    }

    private void calculateFloat(List<FiveSpeedVo> fiveSpeedVoList) {
        if (null == fiveSpeedVoList || 0 == fiveSpeedVoList.size() || null == mPositionVoList || 0 == mPositionVoList.size())
            return;

        mList.clear();

        for (PositionVo positionVo : mPositionVoList) {
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

    private long getTimeInterval() {
        return NetWorkUtils.isWifiConnected(mContext) ? AppConfig.TimeInterval_WiFi : AppConfig.TimeInterval_NetWork;
    }

    public long getPosition(String contractID) {
        if (null == mPositionVoList || 0 == mPositionVoList.size())
            return 0;

        long value = 0;

        for (PositionVo positionVo : mPositionVoList) {
            if (null != positionVo) {
                if (positionVo.getContractId().equalsIgnoreCase(contractID)){
                    value = value + positionVo.getPosition();
                }
            }
        }

        return value;
    }

    private void getMarket() {
        HashMap<String, String> params = new HashMap<>();
        params.put("list", "");

        sendRequest(MarketService.getInstance().getFiveSpeedQuotes, params, false);
    }

    private void position() {
        HashMap<String, String> params = new HashMap<>();
        params.put("accountId", mUser.getAccountID());
        params.put("pagingKey", mPagingKey);

        sendRequest(TradeService.getInstance().position, params, false, false, false);
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

                    mPositionVoList = mAdapter.getData();

                    calculateFloat(fiveSpeedVoList);
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

                    if (null == positionPageVo)
                        return;

                    bHasNext = positionPageVo.isHasNext();
                    mPagingKey = positionPageVo.getPagingKey();
                    List<PositionVo> positionVoList = positionPageVo.getPositionList();

                    mList.clear();

                    if (null != positionVoList && 0 != positionVoList.size()) {
                        for (PositionVo positionVo : positionVoList) {
                            if (null != positionVo)
                                mList.add(MarketUtil.getPriceValue(positionVo.getFloatProfit()));
                        }
                    }

                    mAdapter.setList(mList);

                    if (bHasNext) {
                        if (mCurrentPage == 1)
                            mAdapter.setNewData(positionVoList);
                        else
                            mAdapter.addData(positionVoList);

                        mAdapter.loadMoreComplete();
                    } else {
                        if (mCurrentPage == 1) {
                            if (null == positionVoList || 0 == positionVoList.size()) {
                                mAdapter.setNewData(null);
                            } else {
                                mAdapter.setNewData(positionVoList);
                                mAdapter.loadMoreComplete();
                            }
                        } else {
                            mAdapter.addData(positionVoList);
                            mAdapter.loadMoreComplete();
                        }
                    }
                }

                if (bFlag) {
                    bFlag = false;

                    mHandler.sendEmptyMessageDelayed(Constants.Msg.MSG_DECLARATIONFORM_POSITION_UPDATE_DATA, getTimeInterval());
                    mHandler.sendEmptyMessageDelayed(Constants.Msg.MSG_DECLARATIONFORM_POSITION_UPDATE_ACCOUNT_DATA, AppConfig.Minute);
                }

                break;
        }
    }

    @Override
    public void onLoadMoreRequested() {
        mBinding.recyclerView.postDelayed(() -> {
            if (bHasNext) {
                mCurrentPage++;

                position();
            } else {
                mAdapter.loadMoreEnd();
            }
        }, 0);
    }
}
