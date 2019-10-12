package com.jme.lsgoldtrade.ui.transaction;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.Gravity;

import com.alibaba.android.arouter.launcher.ARouter;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.common.util.BigDecimalUtil;
import com.jme.common.util.NetWorkUtils;
import com.jme.common.util.RxBus;
import com.jme.common.util.SharedPreUtils;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseFragment;
import com.jme.lsgoldtrade.base.TabViewPagerAdapter;
import com.jme.lsgoldtrade.config.AppConfig;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.FragmentHoldPositionsBinding;
import com.jme.lsgoldtrade.domain.AccountVo;
import com.jme.lsgoldtrade.domain.FiveSpeedVo;
import com.jme.lsgoldtrade.domain.PositionPageVo;
import com.jme.lsgoldtrade.domain.PositionVo;
import com.jme.lsgoldtrade.service.ManagementService;
import com.jme.lsgoldtrade.service.MarketService;
import com.jme.lsgoldtrade.service.TradeService;
import com.jme.lsgoldtrade.util.MarketUtil;
import com.jme.lsgoldtrade.view.TransactionMessagePopUpWindow;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import rx.Subscription;

public class HoldPositionsFragment extends JMEBaseFragment implements OnRefreshListener {

    private FragmentHoldPositionsBinding mBinding;

    private int mCurrentPage = 1;
    private boolean bFlag = true;
    private boolean bVisibleToUser = false;
    private boolean bHasNext = false;
    private boolean bHiddenStatus = false;
    private String mPagingKey = "";
    private String mTotal;
    private String mAvailableFunds;
    private String mDesirableCapital;
    private String mMarketCapitalization;

    private BigDecimal mUnliquidatedProfitTotal = new BigDecimal(0);
    private BigDecimal mFloatTotal;

    private Fragment[] mFragmentArrays;
    private String[] mTabTitles;
    private List<String> mList;
    private List<FiveSpeedVo> mFiveSpeedVoList;

    private CurrentHoldPositionsFragment mCurrentHoldPositionsFragment;
    private CurrentEntrustFragment mCurrentEntrustFragment;
    private TabViewPagerAdapter mAdapter;
    private AccountVo mAccountVo;
    private TransactionMessagePopUpWindow mTransactionMessagePopUpWindow;
    private Subscription mRxbus;

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
        return R.layout.fragment_hold_positions;
    }

    @Override
    protected void initView() {
        super.initView();

        mTransactionMessagePopUpWindow = new TransactionMessagePopUpWindow(mContext);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);

        bHiddenStatus = SharedPreUtils.getBoolean(mContext, SharedPreUtils.Key_Transaction_Hidden_Status, false);
        mList = new ArrayList<>();
        mCurrentHoldPositionsFragment = new CurrentHoldPositionsFragment();
        mCurrentEntrustFragment = new CurrentEntrustFragment();

        mBinding.imgHiddenStatus.setBackgroundResource(bHiddenStatus ? R.mipmap.ic_hidden : R.mipmap.ic_show);

        initTabs();
    }

    @Override
    protected void initListener() {
        super.initListener();

        initRxBus();

        mBinding.swipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    public void initBinding() {
        super.initBinding();

        mBinding = (FragmentHoldPositionsBinding) mBindingUtil;
        mBinding.setHandlers(new ClickHandlers());
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        bVisibleToUser = !hidden;

        if (null != mHandler && !bVisibleToUser) {
            mHandler.removeMessages(Constants.Msg.MSG_TRADE_POSITION_UPDATE_DATA);
            mHandler.removeMessages(Constants.Msg.MSG_TRADE_POSITION_UPDATE_ACCOUNT_DATA);
        }

        super.onHiddenChanged(hidden);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        bVisibleToUser = isVisibleToUser;

        if (null != mBinding && bVisibleToUser) {
            bFlag = true;

            getMarket();
        } else {
            if (null != mHandler) {
                mHandler.removeMessages(Constants.Msg.MSG_TRADE_POSITION_UPDATE_DATA);
                mHandler.removeMessages(Constants.Msg.MSG_TRADE_POSITION_UPDATE_ACCOUNT_DATA);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (null != mBinding && bVisibleToUser) {
            bFlag = true;

            getMarket();
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        mHandler.removeMessages(Constants.Msg.MSG_TRADE_POSITION_UPDATE_DATA);
        mHandler.removeMessages(Constants.Msg.MSG_TRADE_POSITION_UPDATE_ACCOUNT_DATA);
    }

    private void initTabs() {
        mTabTitles = new String[2];
        mTabTitles[0] = mContext.getResources().getString(R.string.transaction_current_positions);
        mTabTitles[1] = mContext.getResources().getString(R.string.transaction_current_entrust);

        mFragmentArrays = new Fragment[2];
        mFragmentArrays[0] = mCurrentHoldPositionsFragment;
        mFragmentArrays[1] = mCurrentEntrustFragment;

        mAdapter = new TabViewPagerAdapter(getChildFragmentManager(), mTabTitles, mFragmentArrays);

        mBinding.tabViewpager.removeAllViewsInLayout();
        mBinding.tabViewpager.setAdapter(mAdapter);
        mBinding.tablayout.setupWithViewPager(mBinding.tabViewpager);
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
                case Constants.RxBusConst.RXBUS_CAPITALTRANSFER_SUCCESS:
                case Constants.RxBusConst.RXBUS_LOGIN_SUCCESS:
                    bFlag = true;

                    getMarket();

                    break;
                case Constants.RxBusConst.RXBUS_LOGOUT_SUCCESS:
                    setInitData();

                    break;
                case Constants.RxBusConst.RXBUS_TRANSACTION_EVENING_UP:

                    break;
            }
        });
    }

    private void setInitData() {
        mBinding.tvTotalEquity.setText(bHiddenStatus ? R.string.transaction_hidden_value : R.string.text_no_data_default);
        mBinding.tvFloating.setText(bHiddenStatus ? R.string.transaction_hidden_value : R.string.text_no_data_default);
        mBinding.tvAvailableFunds.setText(bHiddenStatus ? R.string.transaction_hidden_value : R.string.text_no_data_default);
        mBinding.tvDesirableCapital.setText(bHiddenStatus ? R.string.transaction_hidden_value : R.string.text_no_data_default);
        mBinding.tvMarketCapitalization.setText(bHiddenStatus ? R.string.transaction_hidden_value : R.string.text_no_data_default);
        mBinding.tvGuaranteeFund.setText(R.string.text_no_data_default);
        mBinding.tvRiskRate.setText(R.string.text_no_data_default);

        mCurrentHoldPositionsFragment.setCurrentHoldPositionsData(null);
    }

    private void calculateFloat(List<FiveSpeedVo> fiveSpeedVoList, List<PositionVo> positionVoList) {
        if (null != fiveSpeedVoList && 0 != fiveSpeedVoList.size() && null != positionVoList && 0 != positionVoList.size()) {
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

                                    floatProfit = MarketUtil.formatValueNum((new BigDecimal(MarketUtil.getPriceValue(margin))
                                            .multiply(new BigDecimal(contractValue)).multiply(new BigDecimal(position))).toPlainString(), 2);
                                }

                                mList.add(floatProfit);
                            }
                        }
                    }
                }
            }

            mCurrentHoldPositionsFragment.setFloatingList(mList);
        }

        calculateValue();
    }

    private void calculateValue() {
        if (null == mList || 0 == mList.size()) {
            mBinding.tvFloating.setText(bHiddenStatus ? R.string.transaction_hidden_value : R.string.text_no_data_default);

            if (null != mAccountVo) {
                BigDecimal floatTotal = new BigDecimal(0);
                mTotal = new BigDecimal(mAccountVo.getTransactionBalanceStr())
                        .add(new BigDecimal(mAccountVo.getFreezeBalanceStr()))
                        .add(floatTotal)
                        .add(new BigDecimal(mAccountVo.getPositionMarginStr()))
                        .add(mUnliquidatedProfitTotal.compareTo(new BigDecimal(0)) == -1 ? new BigDecimal(0) : mUnliquidatedProfitTotal)
                        .subtract(new BigDecimal(mAccountVo.getFee()))
                        .toPlainString();
                mBinding.tvTotalEquity.setText(bHiddenStatus ? mContext.getResources().getString(R.string.transaction_hidden_value)
                        : TextUtils.isEmpty(mTotal) ? mContext.getResources().getString(R.string.text_no_data_default) : MarketUtil.decimalFormatMoney(mTotal));

                String minReserveFund = mAccountVo.getMinReserveFundStr();
                mDesirableCapital = MarketUtil.getPriceValue(Math.min((new BigDecimal(mTotal).subtract(new BigDecimal(minReserveFund))).multiply(new BigDecimal(100)).longValue(),
                        new BigDecimal(mAccountVo.getExtractableBalance()).subtract(new BigDecimal(mAccountVo.getRuntimeFee())).longValue()));

                mBinding.tvDesirableCapital.setText(bHiddenStatus ? mContext.getResources().getString(R.string.transaction_hidden_value)
                        : TextUtils.isEmpty(mDesirableCapital) ? mContext.getResources().getString(R.string.text_no_data_default) : MarketUtil.decimalFormatMoney(mDesirableCapital));
            }
        } else {
            mFloatTotal = new BigDecimal(0);

            for (String value : mList) {
                if (!TextUtils.isEmpty(value))
                    mFloatTotal = mFloatTotal.add(new BigDecimal(value));
            }

            mBinding.tvFloating.setText(bHiddenStatus ? mContext.getResources().getString(R.string.transaction_hidden_value)
                    : null == mFloatTotal ? mContext.getResources().getString(R.string.text_no_data_default) : MarketUtil.decimalFormatFloating(mFloatTotal.toPlainString()));

            if (null != mAccountVo) {
                mTotal = new BigDecimal(mAccountVo.getTransactionBalanceStr())
                        .add(new BigDecimal(mAccountVo.getFreezeBalanceStr()))
                        .add(mFloatTotal)
                        .add(new BigDecimal(mAccountVo.getPositionMarginStr()))
                        .add(mUnliquidatedProfitTotal.compareTo(new BigDecimal(0)) == -1 ? new BigDecimal(0) : mUnliquidatedProfitTotal)
                        .subtract(new BigDecimal(mAccountVo.getFee()))
                        .toPlainString();
                String minReserveFund = mAccountVo.getMinReserveFundStr();
                String runtimeFee = mAccountVo.getRuntimeFeeStr();

                mBinding.tvTotalEquity.setText(bHiddenStatus ? mContext.getResources().getString(R.string.transaction_hidden_value)
                        : TextUtils.isEmpty(mTotal) ? mContext.getResources().getString(R.string.text_no_data_default) : MarketUtil.decimalFormatMoney(mTotal));

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

                mDesirableCapital = MarketUtil.getPriceValue(Math.min((new BigDecimal(mTotal).subtract(new BigDecimal(minReserveFund))).multiply(new BigDecimal(100)).longValue(),
                        new BigDecimal(mAccountVo.getExtractableBalance()).subtract(new BigDecimal(mAccountVo.getRuntimeFee())).longValue()));

                mBinding.tvDesirableCapital.setText(bHiddenStatus ? mContext.getResources().getString(R.string.transaction_hidden_value)
                        : TextUtils.isEmpty(mDesirableCapital) ? mContext.getResources().getString(R.string.text_no_data_default) : MarketUtil.decimalFormatMoney(mDesirableCapital));
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

                    mAvailableFunds = MarketUtil.getPriceValue(mAccountVo.getTransactionBalance() - mAccountVo.getRuntimeFee());
                    mMarketCapitalization = MarketUtil.getPriceValue(mAccountVo.getPositionMargin());
                    String minReserveFund = mAccountVo.getMinReserveFundStr();

                    mBinding.tvAvailableFunds.setText(bHiddenStatus ? mContext.getResources().getString(R.string.transaction_hidden_value)
                            : TextUtils.isEmpty(mAvailableFunds) ? mContext.getResources().getString(R.string.text_no_data_default) : MarketUtil.decimalFormatMoney(mAvailableFunds));
                    mBinding.tvMarketCapitalization.setText(bHiddenStatus ? mContext.getResources().getString(R.string.transaction_hidden_value)
                            : TextUtils.isEmpty(mMarketCapitalization) ? mContext.getResources().getString(R.string.text_no_data_default) : MarketUtil.decimalFormatMoney(mMarketCapitalization));
                    mBinding.tvGuaranteeFund.setText(TextUtils.isEmpty(minReserveFund) ? getResources().getString(R.string.text_no_data_default)
                            : new BigDecimal(minReserveFund).compareTo(new BigDecimal(0)) == 0 ? getResources().getString(R.string.text_no_data_default) : MarketUtil.decimalFormatMoney(minReserveFund));

                    calculateFloat(mFiveSpeedVoList, mCurrentHoldPositionsFragment.getData());
                } else {
                    getAccount(false);
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
                        mBinding.swipeRefreshLayout.finishRefresh(true);

                        calculateValue();
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

                        if (bFlag) {
                            calculateFloat(mFiveSpeedVoList, mCurrentHoldPositionsFragment.getData());
                        } else {
                            mCurrentHoldPositionsFragment.setFloatingList(mList);

                            calculateValue();
                        }

                        if (bHasNext) {
                            if (mCurrentPage == 1)
                                mCurrentHoldPositionsFragment.setCurrentHoldPositionsData(positionVoList);
                            else
                                mCurrentHoldPositionsFragment.addCurrentHoldPositionsData(positionVoList);

                            mBinding.swipeRefreshLayout.finishRefresh(true);
                        } else {
                            if (mCurrentPage == 1) {
                                if (null == positionVoList || 0 == positionVoList.size())
                                    mCurrentHoldPositionsFragment.setCurrentHoldPositionsData(null);
                                else
                                    mCurrentHoldPositionsFragment.setCurrentHoldPositionsData(positionVoList);
                            } else {
                                mCurrentHoldPositionsFragment.addCurrentHoldPositionsData(positionVoList);
                            }

                            mBinding.swipeRefreshLayout.finishRefresh(true);
                        }
                    }

                    if (bFlag) {
                        bFlag = false;

                        mHandler.sendEmptyMessageDelayed(Constants.Msg.MSG_TRADE_POSITION_UPDATE_DATA, 0);
                        mHandler.sendEmptyMessageDelayed(Constants.Msg.MSG_TRADE_POSITION_UPDATE_ACCOUNT_DATA, AppConfig.Minute);
                    }
                } else {
                    position();

                    mBinding.swipeRefreshLayout.finishRefresh(false);
                }

                break;
            case "GetFiveSpeedQuotes":
                if (head.isSuccess()) {
                    try {
                        mFiveSpeedVoList = (List<FiveSpeedVo>) response;
                    } catch (Exception e) {
                        mFiveSpeedVoList = null;

                        e.getMessage();
                    }

                    mCurrentHoldPositionsFragment.setFiveSpeedVoList(mFiveSpeedVoList);

                    if (bFlag)
                        initValue(true);
                    else
                        calculateFloat(mFiveSpeedVoList, mCurrentHoldPositionsFragment.getData());
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
                    if (null != mTransactionMessagePopUpWindow && !mTransactionMessagePopUpWindow.isShowing()) {
                        mTransactionMessagePopUpWindow.setData(mContext.getResources().getString(R.string.trade_increment_error),
                                mContext.getResources().getString(R.string.trade_increment_goto_open),
                                (view) -> {
                                    ARouter.getInstance().build(Constants.ARouterUriConst.VALUEADDEDSERVICE).navigation();

                                    mTransactionMessagePopUpWindow.dismiss();
                                });
                        mTransactionMessagePopUpWindow.showAtLocation(mBinding.tvRiskRate, Gravity.CENTER, 0, 0);
                    }
                }

                break;
            case "GetStatus":
                if (head.isSuccess()) {
                    String status;

                    if (null == response)
                        status = "";
                    else
                        status = response.toString();

                    if (status.equals("1")) {
                        if (null != mTransactionMessagePopUpWindow && !mTransactionMessagePopUpWindow.isShowing()) {
                            mTransactionMessagePopUpWindow.setData(mContext.getResources().getString(R.string.transaction_account_error),
                                    mContext.getResources().getString(R.string.transaction_account_goto_recharge),
                                    (view) -> {
                                        ARouter.getInstance().build(Constants.ARouterUriConst.RECHARGE).navigation();

                                        mTransactionMessagePopUpWindow.dismiss();
                                    });
                            mTransactionMessagePopUpWindow.showAtLocation(mBinding.tvRiskRate, Gravity.CENTER, 0, 0);
                        }
                    } else {
                        ARouter.getInstance()
                                .build(Constants.ARouterUriConst.GUARANTEEFUNDSETTINGACTIVITY)
                                .withString("Total", mTotal)
                                .withFloat("Warnth", mAccountVo.getWarnth())
                                .withFloat("Forcecloseth", mAccountVo.getForcecloseth())
                                .navigation();
                    }
                }

                break;
        }
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        bFlag = true;

        getMarket();
    }

    public class ClickHandlers {

        public void onClickHiddenTotalEquity() {
            if (bHiddenStatus) {
                bHiddenStatus = false;

                mBinding.imgHiddenStatus.setBackgroundResource(R.mipmap.ic_show);
                mBinding.tvTotalEquity.setText(TextUtils.isEmpty(mTotal) ? mContext.getResources().getString(R.string.text_no_data_default)
                        : MarketUtil.decimalFormatMoney(mTotal));
                mBinding.tvFloating.setText(null == mFloatTotal ? mContext.getResources().getString(R.string.text_no_data_default)
                        : MarketUtil.decimalFormatFloating(mFloatTotal.toPlainString()));
                mBinding.tvAvailableFunds.setText(TextUtils.isEmpty(mAvailableFunds) ? mContext.getResources().getString(R.string.text_no_data_default)
                        : MarketUtil.decimalFormatMoney(mAvailableFunds));
                mBinding.tvDesirableCapital.setText(TextUtils.isEmpty(mDesirableCapital) ? mContext.getResources().getString(R.string.text_no_data_default)
                        : MarketUtil.decimalFormatMoney(mDesirableCapital));
                mBinding.tvMarketCapitalization.setText(TextUtils.isEmpty(mMarketCapitalization) ? mContext.getResources().getString(R.string.text_no_data_default)
                        : MarketUtil.decimalFormatMoney(mMarketCapitalization));
            } else {
                bHiddenStatus = true;

                mBinding.imgHiddenStatus.setBackgroundResource(R.mipmap.ic_hidden);
                mBinding.tvTotalEquity.setText(R.string.transaction_hidden_value);
                mBinding.tvFloating.setText(R.string.transaction_hidden_value);
                mBinding.tvAvailableFunds.setText(R.string.transaction_hidden_value);
                mBinding.tvDesirableCapital.setText(R.string.transaction_hidden_value);
                mBinding.tvMarketCapitalization.setText(R.string.transaction_hidden_value);
            }

            SharedPreUtils.setBoolean(mContext, SharedPreUtils.Key_Transaction_Hidden_Status, bHiddenStatus);
        }

        public void onClickGuaranteeFundSetting() {

        }

        public void onClickRiskRateTips() {

        }

        public void onClickDealQuery() {

        }

        public void onClickInOutMoney() {

        }

        public void onClickEntrustRiskManagement() {

        }

        public void onClickDailyStatementSheet() {

        }

        public void onClickConditionSheet() {

        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (!mRxbus.isUnsubscribed())
            mRxbus.unsubscribe();
    }

}
