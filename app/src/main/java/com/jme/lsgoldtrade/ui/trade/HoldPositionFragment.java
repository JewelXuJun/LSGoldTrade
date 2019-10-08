package com.jme.lsgoldtrade.ui.trade;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
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
import com.jme.lsgoldtrade.domain.ContractInfoVo;
import com.jme.lsgoldtrade.domain.FiveSpeedVo;
import com.jme.lsgoldtrade.domain.PositionPageVo;
import com.jme.lsgoldtrade.domain.PositionVo;
import com.jme.lsgoldtrade.domain.TenSpeedVo;
import com.jme.lsgoldtrade.service.ManagementService;
import com.jme.lsgoldtrade.service.MarketService;
import com.jme.lsgoldtrade.service.TradeService;
import com.jme.lsgoldtrade.view.TransactionMessagePopUpWindow;
import com.jme.lsgoldtrade.util.MarketUtil;
import com.jme.lsgoldtrade.view.ConfirmPopupwindow;
import com.jme.lsgoldtrade.view.EveningUpPopupWindow;
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

    private DeclarationFormHoldPositionAdapter mAdapter;
    private AccountVo mAccountVo;
    private PositionVo mPositionVo;
    private ContractInfoVo mEveningUpContractInfoVo;
    private List<String> mList;
    private List<FiveSpeedVo> mFiveSpeedVoList;
    private Subscription mRxbus;

    private int mCurrentPage = 1;
    private boolean bFlag = true;
    private boolean bHasNext = false;
    private boolean bVisibleToUser = false;
    private String mPagingKey = "";
    private String mTotal;
    private String mEveningUpContractID;
    private BigDecimal mUnliquidatedProfitTotal = new BigDecimal(0);

    private TransactionMessagePopUpWindow mTransactionMessagePopUpWindow;
    private EveningUpPopupWindow mEveningUpPopupWindow;
    private ConfirmPopupwindow mConfirmPopupwindow;

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

        mTransactionMessagePopUpWindow = new TransactionMessagePopUpWindow(mContext);
        mEveningUpPopupWindow = new EveningUpPopupWindow(mContext);
        mConfirmPopupwindow = new ConfirmPopupwindow(mContext);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);

        mAdapter = new DeclarationFormHoldPositionAdapter(mContext, R.layout.item_declaration_form_hold_position, null);
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

        mAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            mPositionVo = (PositionVo) adapter.getItem(position);

            if (null == mPositionVo)
                return;

            mEveningUpContractID = mPositionVo.getContractId();
            mEveningUpContractInfoVo = mContract.getContractInfoFromID(mEveningUpContractID);

            getTenSpeedQuotes(mEveningUpContractID);
        });
    }

    @Override
    public void initBinding() {
        super.initBinding();

        mBinding = (FragmentHoldPositionBinding) mBindingUtil;
        mBinding.setHandlers(new ClickHandlers());
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        bVisibleToUser = isVisibleToUser;

        if (null != mBinding && bVisibleToUser) {
            bFlag = true;

            getMarket();
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

    private void setInitData() {
        mBinding.tvGuaranteeFund.setText(R.string.text_no_data_default);
        mBinding.tvAvailableFunds.setText(R.string.text_no_data_default);
        mBinding.tvMarketValue.setText(R.string.text_no_data_default);
        mBinding.tvTotal.setText(R.string.text_no_data_default);
        mBinding.tvFloating.setText(R.string.text_no_data_default);
        mBinding.tvDesirableCapital.setText(R.string.text_no_data_default);
        mBinding.tvRiskRate.setText(R.string.text_no_data_default);
        mAdapter.setNewData(null);
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
            }
        });
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
                                long latestprice = Long.parseLong(fiveSpeedVo.getLatestPrice());
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

            mAdapter.setList(mList);
            mAdapter.notifyDataSetChanged();
        }

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
                        .subtract(new BigDecimal(mAccountVo.getFee()))
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
                        .add(mUnliquidatedProfitTotal.compareTo(new BigDecimal(0)) == -1 ? new BigDecimal(0) : mUnliquidatedProfitTotal)
                        .subtract(new BigDecimal(mAccountVo.getFee()))
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

    private void getTenSpeedQuotes(String contractID) {
        HashMap<String, String> params = new HashMap<>();
        params.put("list", contractID);

        sendRequest(MarketService.getInstance().getTenSpeedQuotes, params, true);
    }

    private void getUserAddedServicesStatus() {
        sendRequest(ManagementService.getInstance().getUserAddedServicesStatus, new HashMap<>(), true);
    }

    private void getStatus() {
        sendRequest(ManagementService.getInstance().getStatus, new HashMap<>(), true);
    }

    private void limitOrder(String contractId, String price, String amount, int bsFlag, int ocFlag) {
        HashMap<String, String> params = new HashMap<>();
        params.put("contractId", contractId);
        params.put("accountId", mUser.getAccountID());
        params.put("entrustPrice", String.valueOf(new BigDecimal(price).multiply(new BigDecimal(100)).longValue()));
        params.put("entrustNumber", amount);
        params.put("bsFlag", String.valueOf(bsFlag));
        params.put("ocFlag", String.valueOf(ocFlag));
        params.put("tradingType", "0");

        sendRequest(TradeService.getInstance().limitOrder, params, true);
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

                    calculateFloat(mFiveSpeedVoList, mAdapter.getData());
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
                            calculateFloat(mFiveSpeedVoList, mAdapter.getData());
                        } else {
                            mAdapter.setList(mList);

                            calculateValue();
                        }

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

                    if (bFlag)
                        initValue(true);
                    else
                        calculateFloat(mFiveSpeedVoList, mAdapter.getData());
                }

                break;
            case "GetTenSpeedQuotes":
                if (head.isSuccess()) {
                    List<TenSpeedVo> tenSpeedVoList;

                    try {
                        tenSpeedVoList = (List<TenSpeedVo>) response;
                    } catch (Exception e) {
                        tenSpeedVoList = null;

                        e.getMessage();
                    }

                    if (null == tenSpeedVoList || 0 == tenSpeedVoList.size())
                        return;

                    TenSpeedVo tenSpeedVo = tenSpeedVoList.get(0);

                    if (null == tenSpeedVo)
                        return;

                    if (!mEveningUpContractID.equals(tenSpeedVo.getContractId()))
                        return;

                    if (null != mEveningUpPopupWindow && !mEveningUpPopupWindow.isShowing() && null != mEveningUpContractInfoVo) {
                        String lowerLimitPrice = tenSpeedVo.getLowerLimitPrice();
                        String highLimitPrice = tenSpeedVo.getHighLimitPrice();
                        String type = mPositionVo.getType();
                        long minOrderQty = mEveningUpContractInfoVo.getMinOrderQty();
                        long maxOrderQty = mEveningUpContractInfoVo.getMaxOrderQty();
                        long maxHoldQty = mEveningUpContractInfoVo.getMaxHoldQty();
                        long maxAmount = mPositionVo.getPosition() - mPositionVo.getOffsetFrozen();

                        mEveningUpPopupWindow.setData(mUser.getAccount(), mEveningUpContractID,
                                type.equals("多") ? tenSpeedVo.getFiveBidLists().get(0)[1] : tenSpeedVo.getFiveAskLists().get(4)[1],
                                type, new BigDecimal(mEveningUpContractInfoVo.getMinPriceMove()).divide(new BigDecimal(100)).floatValue(),
                                lowerLimitPrice, highLimitPrice, minOrderQty, maxOrderQty, maxHoldQty, maxAmount,
                                (view) -> {
                                    String price = mEveningUpPopupWindow.getPrice();
                                    String amount = mEveningUpPopupWindow.getAmount();

                                    if (TextUtils.isEmpty(price) || price.equals(mContext.getResources().getString(R.string.text_no_data_default))) {
                                        showShortToast(R.string.transaction_price_error);
                                    } else if (new BigDecimal(price).compareTo(new BigDecimal(lowerLimitPrice)) == -1) {
                                        showShortToast(R.string.transaction_limit_down_price_error);
                                    } else if (new BigDecimal(price).compareTo(new BigDecimal(highLimitPrice)) == 1) {
                                        showShortToast(R.string.transaction_limit_up_price_error);
                                    } else if (TextUtils.isEmpty(amount)) {
                                        showShortToast(R.string.transaction_number_error);
                                    } else if (new BigDecimal(amount).compareTo(new BigDecimal(0)) == 0) {
                                        showShortToast(R.string.transaction_number_error_zero);
                                    } else if (minOrderQty != -1 && new BigDecimal(amount).compareTo(new BigDecimal(minOrderQty)) == -1) {
                                        showShortToast(R.string.transaction_limit_min_amount_error);
                                    } else if (maxOrderQty == -1 && new BigDecimal(amount).compareTo(new BigDecimal(maxHoldQty == -1 ? maxAmount : Math.min(maxAmount, maxHoldQty))) == 1) {
                                        showShortToast(R.string.trade_limit_max_amount_error_canbuy);
                                    } else if (maxOrderQty != -1 && new BigDecimal(amount).compareTo(new BigDecimal(Math.min(maxAmount, maxOrderQty))) == 1) {
                                        showShortToast(R.string.trade_limit_max_amount_error_canbuy);
                                    } else {
                                        limitOrder(mEveningUpContractID, mEveningUpPopupWindow.getPrice(),
                                                mEveningUpPopupWindow.getAmount(), mPositionVo.getType().equals("多") ? 2 : 1, 1);

                                        mEveningUpPopupWindow.dismiss();
                                    }
                                });
                        mEveningUpPopupWindow.showAtLocation(mBinding.tvRiskRate, Gravity.BOTTOM, 0, 0);
                    }
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
            case "LimitOrder":
                if (head.isSuccess()) {
                    showShortToast(R.string.transaction_success);

                    mHandler.removeMessages(Constants.Msg.MSG_TRADE_POSITION_UPDATE_DATA);
                    mHandler.removeMessages(Constants.Msg.MSG_TRADE_POSITION_UPDATE_ACCOUNT_DATA);

                    bFlag = true;

                    getMarket();
                } else {
                    if (head.getMsg().contains("可用资金不足")) {
                        if (null != mConfirmPopupwindow && !mConfirmPopupwindow.isShowing()) {
                            mConfirmPopupwindow.setData(mContext.getResources().getString(R.string.transaction_money_error),
                                    mContext.getResources().getString(R.string.transaction_money_in),
                                    (view) -> ARouter.getInstance().build(Constants.ARouterUriConst.CAPITALTRANSFER).navigation());
                            mConfirmPopupwindow.showAtLocation(mBinding.tvRiskRate, Gravity.CENTER, 0, 0);
                        }
                    }
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
        bFlag = true;

        getMarket();
    }

    public class ClickHandlers {

        public void onClickCapitalTransfer() {
            if (null == mUser)
                return;

            if (null == mUser.getCurrentUser())
                return;

            if (!TextUtils.isEmpty(mUser.getIsFromTjs()) && mUser.getIsFromTjs().equals("true")) {
                if (mUser.getCurrentUser().getCardType().equals("2") && mUser.getCurrentUser().getReserveFlag().equals("N"))
                    ARouter.getInstance().build(Constants.ARouterUriConst.BANKRESERVE).navigation();
                else
                    ARouter.getInstance().build(Constants.ARouterUriConst.CAPITALTRANSFER).navigation();
            } else {
                ARouter.getInstance().build(Constants.ARouterUriConst.CAPITALTRANSFER).navigation();
            }
        }

        public void onClickGuaranteeFundSetting() {
            if (null == mUser || !mUser.isLogin())
                gotoLogin();
            else
                getUserAddedServicesStatus();
        }

    }
}
