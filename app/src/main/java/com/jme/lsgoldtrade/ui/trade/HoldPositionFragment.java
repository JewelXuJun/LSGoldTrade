package com.jme.lsgoldtrade.ui.trade;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.Gravity;

import com.alibaba.android.arouter.launcher.ARouter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.common.util.BigDecimalUtil;
import com.jme.common.util.NetWorkUtils;
import com.jme.common.util.RxBus;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseFragment;
import com.jme.lsgoldtrade.config.AppConfig;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.FragmentHoldPositionBinding;
import com.jme.lsgoldtrade.domain.AccountVo;
import com.jme.lsgoldtrade.domain.FiveSpeedVo;
import com.jme.lsgoldtrade.domain.PositionPageVo;
import com.jme.lsgoldtrade.domain.PositionVo;
import com.jme.lsgoldtrade.service.ManagementService;
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

/**
 * 持仓
 */
public class HoldPositionFragment extends JMEBaseFragment implements OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {

    private FragmentHoldPositionBinding mBinding;

    private HoldPositionAdapter mAdapter;
    private AccountVo mAccountVo;
    private List<String> mList;
    private Subscription mRxbus;

    private int mCurrentPage = 1;
    private boolean bFlag = true;
    private boolean bHasNext = false;
    private boolean bVisibleToUser = false;
    private String mPagingKey = "";
    private String mTotal;
    private BigDecimal mUnliquidatedProfitTotal = new BigDecimal(0);

    private TradeMessagePopUpWindow mTradeMessagePopUpWindow;

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constants.Msg.MSG_TRADE_POSITION_UPDATE_DATA:
                    mHandler.removeMessages(Constants.Msg.MSG_TRADE_POSITION_UPDATE_DATA);

                    getMarket();

                    mHandler.sendEmptyMessageDelayed(Constants.Msg.MSG_TRADE_POSITION_UPDATE_DATA, getTimeInterval());

                    break;
                case Constants.Msg.MSG_TRADE_POSITION_UPDATE_ACCOUNT_DATA:
                    mHandler.removeMessages(Constants.Msg.MSG_TRADE_POSITION_UPDATE_ACCOUNT_DATA);

                    initValue(false);

                    mHandler.sendEmptyMessageDelayed(Constants.Msg.MSG_TRADE_POSITION_UPDATE_ACCOUNT_DATA, AppConfig.Minute);

                    break;
            }

            super.handleMessage(msg);
        }
    };

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_hold_position;
    }

    @Override
    protected void initView() {
        super.initView();

        mBinding = (FragmentHoldPositionBinding) mBindingUtil;

        mTradeMessagePopUpWindow = new TradeMessagePopUpWindow(mContext);
        mTradeMessagePopUpWindow.setOutsideTouchable(true);
        mTradeMessagePopUpWindow.setFocusable(true);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);

        mAdapter = new HoldPositionAdapter(mContext, R.layout.item_hold_position, null, 1);
        mList = new ArrayList<>();

        mBinding.recyclerView.setHasFixedSize(false);
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mBinding.recyclerView.setAdapter(mAdapter);
        mBinding.recyclerView.setNestedScrollingEnabled(false);
    }

    @Override
    protected void initListener() {
        super.initListener();

        initRxBus();

        mBinding.swipeRefreshLayout.setOnRefreshListener(this);
        mAdapter.setOnLoadMoreListener(this, mBinding.recyclerView);
    }

    @Override
    public void initBinding() {
        super.initBinding();

        mBinding.setHandlers(new ClickHandlers());
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        bVisibleToUser = isVisibleToUser;

        if (null != mBinding && bVisibleToUser) {
            initValue(true);
        } else {
            mHandler.removeMessages(Constants.Msg.MSG_TRADE_POSITION_UPDATE_DATA);
            mHandler.removeMessages(Constants.Msg.MSG_TRADE_POSITION_UPDATE_ACCOUNT_DATA);
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        bVisibleToUser = !hidden;

        super.onHiddenChanged(hidden);

        if (hidden) {
            mHandler.removeMessages(Constants.Msg.MSG_TRADE_POSITION_UPDATE_DATA);
            mHandler.removeMessages(Constants.Msg.MSG_TRADE_POSITION_UPDATE_ACCOUNT_DATA);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (null != mBinding && bVisibleToUser)
            initValue(true);
    }

    @Override
    public void onPause() {
        super.onPause();

        mHandler.removeMessages(Constants.Msg.MSG_TRADE_POSITION_UPDATE_DATA);
        mHandler.removeMessages(Constants.Msg.MSG_TRADE_POSITION_UPDATE_ACCOUNT_DATA);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (!mRxbus.isUnsubscribed())
            mRxbus.unsubscribe();
    }

    private void initValue(boolean enable) {
        getAccount(enable);
        initPosition();
    }

    private void initPosition() {
        bFlag = true;
        mCurrentPage = 1;
        mPagingKey = "";

        mHandler.removeMessages(Constants.Msg.MSG_TRADE_POSITION_UPDATE_DATA);
        mHandler.removeMessages(Constants.Msg.MSG_TRADE_POSITION_UPDATE_ACCOUNT_DATA);

        position();
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

    private void calculateFloat(List<FiveSpeedVo> fiveSpeedVoList, List<PositionVo> positionVoList) {
        if (null == fiveSpeedVoList || 0 == fiveSpeedVoList.size() || null == positionVoList || 0 == positionVoList.size())
            return;

        mList.clear();

        mUnliquidatedProfitTotal = new BigDecimal(0);

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

                            mUnliquidatedProfitTotal = mUnliquidatedProfitTotal.add(new BigDecimal(positionVo.getUnliquidatedProfitStr()));

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

        calculateValue();
    }

    private void calculateValue() {
        if (null == mList || 0 == mList.size()) {
            mBinding.tvFloating.setText(R.string.text_no_data_default);
            if (null != mAccountVo) {
                BigDecimal floatTotal = new BigDecimal(0);
                mTotal = new BigDecimal(mAccountVo.getTransactionBalanceStr())
                        .add(new BigDecimal(mAccountVo.getFreezeBalanceStr()))
                        .add(floatTotal)
                        .add(new BigDecimal(mAccountVo.getPositionMarginStr()))
                        .add(mUnliquidatedProfitTotal.compareTo(new BigDecimal(0)) == -1 ? new BigDecimal(0) : mUnliquidatedProfitTotal)
                        .toPlainString();
                mBinding.tvTotal.setText(MarketUtil.decimalFormatMoney(mTotal));

                String minReserveFund = mAccountVo.getMinReserveFundStr();
                long value = Math.min((new BigDecimal(mTotal).subtract(new BigDecimal(minReserveFund))).multiply(new BigDecimal(100)).longValue(),
                        new BigDecimal(mAccountVo.getExtractableBalance()).subtract(new BigDecimal(mAccountVo.getRuntimeFee())).longValue());
                mBinding.tvDesirableCapital.setText(MarketUtil.decimalFormatMoney(MarketUtil.getPriceValue(value)));
            }
        } else {
            BigDecimal floatTotal = new BigDecimal(0);

            for (String value : mList) {
                if (!TextUtils.isEmpty(value))
                    floatTotal = floatTotal.add(new BigDecimal(value));
            }

            mBinding.tvFloating.setText(MarketUtil.decimalFormatMoney(floatTotal.toPlainString()));

            if (null != mAccountVo) {
                mTotal = new BigDecimal(mAccountVo.getTransactionBalanceStr())
                        .add(new BigDecimal(mAccountVo.getFreezeBalanceStr()))
                        .add(floatTotal)
                        .add(new BigDecimal(mAccountVo.getPositionMarginStr()))
                        .subtract(new BigDecimal(mAccountVo.getRuntimeFeeStr()))
                        .toPlainString();
                String minReserveFund = mAccountVo.getMinReserveFundStr();
                String runtimeFee = mAccountVo.getRuntimeFeeStr();

                mBinding.tvTotal.setText(MarketUtil.decimalFormatMoney(mTotal));

                if (new BigDecimal(minReserveFund).compareTo(new BigDecimal(0)) == 0) {
                    mBinding.tvRiskRate.setText(R.string.text_no_data_default);
                } else {
                    BigDecimal fee = new BigDecimal(minReserveFund).add(new BigDecimal(runtimeFee));

                    if (fee.compareTo(new BigDecimal(0)) == 0) {
                        mBinding.tvRiskRate.setText(R.string.text_no_data_default);
                    } else {
                        String minReserveFundStr = mAccountVo.getMinReserveFundStr();
                        float riskRate;

                        if (TextUtils.isEmpty(minReserveFundStr)) {
                            riskRate = 0.00f;
                        } else {
                            if (new BigDecimal(minReserveFundStr).compareTo(new BigDecimal(0)) == 0)
                                riskRate = 0.00f;
                            else
                                riskRate = Math.min(new BigDecimal(mTotal).divide(new BigDecimal(minReserveFundStr), 4, BigDecimal.ROUND_HALF_UP)
                                        .multiply(new BigDecimal(100)).floatValue(), 10000.00f);
                        }

                        mBinding.tvRiskRate.setText(BigDecimalUtil.formatRate(String.valueOf(riskRate)));
                    }
                }

                long value = Math.min((new BigDecimal(mTotal).subtract(new BigDecimal(minReserveFund))).multiply(new BigDecimal(100)).longValue(),
                        new BigDecimal(mAccountVo.getExtractableBalance()).subtract(new BigDecimal(mAccountVo.getRuntimeFee())).longValue());

                mBinding.tvDesirableCapital.setText(MarketUtil.decimalFormatMoney(MarketUtil.getPriceValue(value)));
            }
        }
    }

    private long getTimeInterval() {
        return NetWorkUtils.isWifiConnected(mContext) ? AppConfig.TimeInterval_WiFi : AppConfig.TimeInterval_NetWork;
    }

    private void getAccount(boolean enable) {
        String accountID = mUser.getAccountID();

        if (TextUtils.isEmpty(accountID))
            return;

        HashMap<String, String> params = new HashMap<>();
        params.put("accountId", accountID);

        sendRequest(TradeService.getInstance().account, params, enable);
    }

    private void position() {
        String accountID = mUser.getAccountID();

        if (TextUtils.isEmpty(accountID))
            return;

        HashMap<String, String> params = new HashMap<>();
        params.put("accountId", accountID);
        params.put("pagingKey", mPagingKey);

        sendRequest(TradeService.getInstance().position, params, false, false, false);
    }

    private void getMarket() {
        HashMap<String, String> params = new HashMap<>();
        params.put("list", "");

        sendRequest(MarketService.getInstance().getFiveSpeedQuotes, params, false);
    }

    private void getUserAddedServicesStatus() {
        sendRequest(ManagementService.getInstance().getUserAddedServicesStatus, new HashMap<>(), true);
    }

    private void getStatus() {
        sendRequest(ManagementService.getInstance().getStatus, new HashMap<>(), true);
    }

    @Override
    protected void DataReturn(DTRequest request, Head head, Object response) {
        super.DataReturn(request, head, response);

        switch (request.getApi().getName()) {
            case "Account":
                if (head.isSuccess()) {
                    try {
                        mAccountVo = (AccountVo) response;
                    } catch (Exception e) {
                        mAccountVo = null;

                        e.printStackTrace();
                    }

                    if (null == mAccountVo)
                        return;

                    long availableFunds = mAccountVo.getTransactionBalance() - mAccountVo.getRuntimeFee();

                    mBinding.tvGuaranteeFund.setText(MarketUtil.decimalFormatMoney(mAccountVo.getMinReserveFundStr()));
                    mBinding.tvAvailableFunds.setText(MarketUtil.decimalFormatMoney(MarketUtil.getPriceValue(availableFunds)));
                    mBinding.tvMarketValue.setText(MarketUtil.decimalFormatMoney(MarketUtil.getPriceValue(mAccountVo.getPositionMargin())));
                    calculateValue();
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
                        mBinding.swipeRefreshLayout.finishRefresh(false);
                    } else {
                        bHasNext = positionPageVo.isHasNext();
                        mPagingKey = positionPageVo.getPagingKey();
                        List<PositionVo> positionVoList = positionPageVo.getPositionList();

                        BigDecimal marketValueTotal = new BigDecimal(0);

                        mList.clear();

                        if (null != positionVoList && 0 != positionVoList.size()) {
                            for (PositionVo positionVo : positionVoList) {
                                if (null != positionVo) {
                                    mList.add(MarketUtil.getPriceValue(positionVo.getFloatProfit()));

                                    String contractID = positionVo.getContractId();
                                    long handWeight = mContract.getHandWeightFromID(contractID);

                                    marketValueTotal = marketValueTotal.add(
                                            new BigDecimal(positionVo.getPositionAverage())
                                                    .multiply(new BigDecimal(contractID.equals("Ag(T+D)") ?
                                                            new BigDecimal(handWeight).divide(new BigDecimal(1000), 0, BigDecimal.ROUND_HALF_UP).longValue() : handWeight))
                                                    .multiply(new BigDecimal(positionVo.getPosition())));
                                }
                            }
                        }

                        if (!bFlag)
                            mAdapter.setList(mList);

                        calculateValue();

//                        mBinding.tvMarketValue.setText(MarketUtil.decimalFormatMoney(MarketUtil.getPriceValue(marketValueTotal.longValue())));
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
                    mBinding.swipeRefreshLayout.finishRefresh(false);
                }

                if (bFlag) {
                    bFlag = false;

                    mHandler.sendEmptyMessageDelayed(Constants.Msg.MSG_TRADE_POSITION_UPDATE_DATA, 0);
                    mHandler.sendEmptyMessageDelayed(Constants.Msg.MSG_TRADE_POSITION_UPDATE_ACCOUNT_DATA, AppConfig.Minute);
                }

                break;
            case "GetFiveSpeedQuotes":
                if (head.isSuccess()) {
                    List<FiveSpeedVo> fiveSpeedVoList;

                    try {
                        fiveSpeedVoList = (List<FiveSpeedVo>) response;
                    } catch (Exception e) {
                        fiveSpeedVoList = null;

                        e.getMessage();
                    }

                    calculateFloat(fiveSpeedVoList, mAdapter.getData());
                }

                break;
            case "GetUserAddedServicesStatus":
                String incrementState;

                if (null == response)
                    incrementState = "";
                else
                    incrementState = response.toString();

                if (incrementState.equals("T")) {
                    getStatus();
                } else {
                    if (null != mTradeMessagePopUpWindow && !mTradeMessagePopUpWindow.isShowing()) {
                        mTradeMessagePopUpWindow.setData(mContext.getResources().getString(R.string.trade_increment_error),
                                mContext.getResources().getString(R.string.trade_increment_goto_open),
                                (view) -> {
                                    ARouter.getInstance().build(Constants.ARouterUriConst.VALUEADDEDSERVICE).navigation();

                                    mTradeMessagePopUpWindow.dismiss();
                                });
                        mTradeMessagePopUpWindow.showAtLocation(mBinding.tvRiskRate, Gravity.CENTER, 0, 0);
                    }
                }

                break;
            case "GetStatus":
                String status;

                if (null == response)
                    status = "";
                else
                    status = response.toString();

                if (status.equals("1")) {
                    if (null != mTradeMessagePopUpWindow && !mTradeMessagePopUpWindow.isShowing()) {
                        mTradeMessagePopUpWindow.setData(mContext.getResources().getString(R.string.trade_account_error),
                                mContext.getResources().getString(R.string.trade_account_goto_recharge),
                                (view) -> {
                                    ARouter.getInstance().build(Constants.ARouterUriConst.RECHARGE).navigation();

                                    mTradeMessagePopUpWindow.dismiss();
                                });
                        mTradeMessagePopUpWindow.showAtLocation(mBinding.tvRiskRate, Gravity.CENTER, 0, 0);
                    }
                } else {
                    ARouter.getInstance()
                            .build(Constants.ARouterUriConst.GUARANTEEFUNDSETTINGACTIVITY)
                            .withString("Total", mTotal)
                            .withFloat("Warnth", mAccountVo.getWarnth())
                            .withFloat("Forcecloseth", mAccountVo.getForcecloseth())
                            .navigation();
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

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        initValue(false);
    }

    public class ClickHandlers {

        public void onClickCapitalTransfer() {
            ARouter.getInstance()
                    .build(Constants.ARouterUriConst.CAPITALTRANSFER)
                    .navigation();
        }

        public void onClickGuaranteeFundSetting() {
            if (null == mUser || !mUser.isLogin())
                ARouter.getInstance().build(Constants.ARouterUriConst.ACCOUNTLOGIN).navigation();
            else
                getUserAddedServicesStatus();
        }

    }
}
