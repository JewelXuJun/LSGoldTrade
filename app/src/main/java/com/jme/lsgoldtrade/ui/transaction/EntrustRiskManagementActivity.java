package com.jme.lsgoldtrade.ui.transaction;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import androidx.core.content.ContextCompat;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.common.util.BigDecimalUtil;
import com.jme.common.util.NetWorkUtils;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseActivity;
import com.jme.lsgoldtrade.config.AppConfig;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.ActivityEntrustRiskManagementBinding;
import com.jme.lsgoldtrade.domain.AccountVo;
import com.jme.lsgoldtrade.domain.FiveSpeedVo;
import com.jme.lsgoldtrade.domain.PositionPageVo;
import com.jme.lsgoldtrade.domain.PositionVo;
import com.jme.lsgoldtrade.service.MarketService;
import com.jme.lsgoldtrade.service.TradeService;
import com.jme.lsgoldtrade.util.MarketUtil;
import com.jme.lsgoldtrade.view.ConfirmSimplePopupwindow;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Route(path = Constants.ARouterUriConst.ENTRUSTRISKMANAGEMENT)
public class EntrustRiskManagementActivity extends JMEBaseActivity {

    private ActivityEntrustRiskManagementBinding mBinding;

    private boolean bFlag = true;
    private float mWarnth;
    private float mForcecloseth;
    private String mPagingKey = "";
    private String mTotal;
    private String mGuaranteeFund;

    private List<FiveSpeedVo> mFiveSpeedVoList;
    private List<String> mList;
    private List<PositionVo> mPositionVoList = new ArrayList<>();

    private BigDecimal mUnliquidatedProfitTotal = new BigDecimal(0);

    private AccountVo mAccountVo;
    private GuaranteeFundSettingPopUpWindow mGuaranteeFundSettingPopUpWindow;
    private GuaranteeFundPopUpWindow mGuaranteeFundPopUpWindow;
    private ConfirmSimplePopupwindow mConfirmSimplePopupwindow;

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
        return R.layout.activity_entrust_risk_management;
    }

    @Override
    protected void initView() {
        super.initView();

        initToolbar(R.string.transaction_entrust_risk_management, true);

        mGuaranteeFundSettingPopUpWindow = new GuaranteeFundSettingPopUpWindow(this);
        mGuaranteeFundSettingPopUpWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        mGuaranteeFundPopUpWindow = new GuaranteeFundPopUpWindow(this);
        mConfirmSimplePopupwindow = new ConfirmSimplePopupwindow(this);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);

        mList = new ArrayList<>();
    }

    @Override
    protected void initListener() {
        super.initListener();

        mGuaranteeFundSettingPopUpWindow.setOnDismissListener(() -> {
            if (null != mGuaranteeFundSettingPopUpWindow)
                mGuaranteeFundSettingPopUpWindow.hiddenSoftKeyboard();
        });
    }

    @Override
    protected void initBinding() {
        super.initBinding();

        mBinding = (ActivityEntrustRiskManagementBinding) mBindingUtil;
        mBinding.setHandlers(new ClickHandlers());
    }

    @Override
    protected void onResume() {
        super.onResume();

        bFlag = true;

        getMarket();
    }

    @Override
    protected void onPause() {
        super.onPause();

        mHandler.removeMessages(Constants.Msg.MSG_TRADE_POSITION_UPDATE_DATA);
        mHandler.removeMessages(Constants.Msg.MSG_TRADE_POSITION_UPDATE_ACCOUNT_DATA);
    }

    private void initValue(boolean enable) {
        getAccount(enable);
        initPosition();
    }

    private void initPosition() {
        bFlag = true;
        mPagingKey = "";
        mPositionVoList.clear();

        mHandler.removeMessages(Constants.Msg.MSG_TRADE_POSITION_UPDATE_DATA);
        mHandler.removeMessages(Constants.Msg.MSG_TRADE_POSITION_UPDATE_ACCOUNT_DATA);

        getPosition();
    }

    private void setMessage(String value) {
        SpannableString spannableString = new SpannableString(value);
        spannableString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this, R.color.color_red)),
                value.indexOf("5、"), value.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mBinding.tvMessage.setMovementMethod(LinkMovementMethod.getInstance());
        mBinding.tvMessage.setText(spannableString);
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
        }

        calculateValue();
    }

    private void calculateValue() {
        if (null != mList && 0 != mList.size()) {
            BigDecimal floatTotal = new BigDecimal(0);

            for (String value : mList) {
                if (!TextUtils.isEmpty(value))
                    floatTotal = floatTotal.add(new BigDecimal(value));
            }

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
            }
        }
    }

    private void setGuaranteeFundLayout() {
        if (null == mAccountVo)
            return;

        long minReserveFund = mAccountVo.getMinReserveFund();
        long minReserveFundCust = mAccountVo.getMinReserveFundCust();

        if (0 == minReserveFund) {
            if (0 == minReserveFundCust) {
                mBinding.tvGuaranteeFund.setText(R.string.text_no_data_default);
                mBinding.imgToBeEffective.setVisibility(View.GONE);
                mBinding.tvGuaranteeFundSetting.setText(R.string.transaction_setting);
            } else {
                mBinding.tvGuaranteeFund.setText(MarketUtil.decimalFormatMoney(MarketUtil.getPriceValue(minReserveFundCust)));
                mBinding.imgToBeEffective.setVisibility(View.VISIBLE);
                mBinding.tvGuaranteeFundSetting.setText(R.string.transaction_modify);
            }

            mBinding.tvGuaranteeFundSetting.setVisibility(View.VISIBLE);
            mBinding.layoutToBeEffective.setVisibility(View.GONE);
        } else {
            if (0 == minReserveFundCust) {
                mBinding.tvGuaranteeFundSetting.setText(R.string.transaction_modify);
                mBinding.tvGuaranteeFundSetting.setVisibility(View.VISIBLE);
                mBinding.layoutToBeEffective.setVisibility(View.GONE);
            } else {
                mBinding.tvGuaranteeFundToBeEffective.setText(MarketUtil.decimalFormatMoney(MarketUtil.getPriceValue(minReserveFundCust)));
                mBinding.tvGuaranteeFundSetting.setVisibility(View.GONE);
                mBinding.layoutToBeEffective.setVisibility(View.VISIBLE);
            }

            mBinding.tvGuaranteeFund.setText(MarketUtil.decimalFormatMoney(MarketUtil.getPriceValue(minReserveFund)));
            mBinding.imgToBeEffective.setVisibility(View.GONE);
        }
    }

    private long getTimeInterval() {
        return NetWorkUtils.isWifiConnected(mContext) ? AppConfig.TimeInterval_WiFi : AppConfig.TimeInterval_NetWork;
    }

    private void getMarket() {
        HashMap<String, String> params = new HashMap<>();
        params.put("list", "");

        sendRequest(MarketService.getInstance().getFiveSpeedQuotes, params, false);
    }

    private void getAccount(boolean enable) {
        String accountID = mUser.getAccountID();

        if (TextUtils.isEmpty(accountID))
            return;

        HashMap<String, String> params = new HashMap<>();
        params.put("accountId", accountID);

        sendRequest(TradeService.getInstance().account, params, enable);
    }

    private void getPosition() {
        String accountID = mUser.getAccountID();

        if (TextUtils.isEmpty(accountID))
            return;

        HashMap<String, String> params = new HashMap<>();
        params.put("accountId", accountID);
        params.put("pagingKey", mPagingKey);

        sendRequest(TradeService.getInstance().position, params, false, false, false);
    }

    private void getMinReserveFund() {
        if (null == mUser || !mUser.isLogin())
            return;

        HashMap<String, String> params = new HashMap<>();
        params.put("amount", String.valueOf(new BigDecimal(mGuaranteeFund).multiply(new BigDecimal(100)).longValue()));
        params.put("accountId", mUser.getAccountID());

        sendRequest(TradeService.getInstance().minReserveFund, params, true);
    }

    @Override
    protected void DataReturn(DTRequest request, Head head, Object response) {
        super.DataReturn(request, head, response);

        switch (request.getApi().getName()) {
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
                        calculateFloat(mFiveSpeedVoList, mPositionVoList);
                }

                break;
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

                    mWarnth = mAccountVo.getWarnth();
                    mForcecloseth = mAccountVo.getForcecloseth();

                    setMessage(String.format(getResources().getString(R.string.transaction_guarantee_fund_message), "100%",
                            new BigDecimal(mWarnth).multiply(new BigDecimal(100)).setScale(0, BigDecimal.ROUND_DOWN).toPlainString() + "%",
                            new BigDecimal(mForcecloseth).multiply(new BigDecimal(100)).setScale(0, BigDecimal.ROUND_DOWN).toPlainString() + "%"));
                    setGuaranteeFundLayout();
                    calculateFloat(mFiveSpeedVoList, mPositionVoList);
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

                    if (null != positionPageVo) {
                        boolean hasNext = positionPageVo.isHasNext();
                        mPagingKey = positionPageVo.getPagingKey();

                        List<PositionVo> positionVoList = positionPageVo.getPositionList();

                        mPositionVoList.clear();

                        if (null != positionVoList && 0 != positionVoList.size())
                            mPositionVoList.addAll(positionVoList);

                        if (hasNext) {
                            getPosition();
                        } else {
                            mList.clear();
                            mUnliquidatedProfitTotal = new BigDecimal(0);

                            if (null != mPositionVoList && 0 != mPositionVoList.size()) {
                                for (PositionVo positionVo : mPositionVoList) {
                                    if (null != positionVo) {
                                        mList.add(MarketUtil.getPriceValue(positionVo.getFloatProfit()));
                                        mUnliquidatedProfitTotal = mUnliquidatedProfitTotal.add(new BigDecimal(positionVo.getUnliquidatedProfitStr()));
                                    }
                                }
                            }

                            calculateValue();
                        }
                    }
                } else {
                    getPosition();
                }

                if (bFlag) {
                    bFlag = false;

                    mHandler.sendEmptyMessageDelayed(Constants.Msg.MSG_TRADE_POSITION_UPDATE_DATA, 0);
                    mHandler.sendEmptyMessageDelayed(Constants.Msg.MSG_TRADE_POSITION_UPDATE_ACCOUNT_DATA, AppConfig.Minute);
                }

                break;
            case "MinReserveFund":
                if (head.isSuccess()) {
                    showShortToast(R.string.transaction_guarantee_fund_success);

                    getAccount(false);
                }

                break;
        }
    }

    public class ClickHandlers {

        public void onClickEntrustRiskManagementSetting() {
            if (null != mGuaranteeFundSettingPopUpWindow && !mGuaranteeFundSettingPopUpWindow.isShowing()
                    && null != mGuaranteeFundPopUpWindow && !mGuaranteeFundPopUpWindow.isShowing()) {
                mGuaranteeFundSettingPopUpWindow.setData((view) -> {
                    if (TextUtils.isEmpty(mTotal)) {
                        showShortToast(R.string.transaction_guarantee_total_error);
                    } else {
                        mGuaranteeFund = mGuaranteeFundSettingPopUpWindow.getGuaranteeFund();
                        String message;

                        if (TextUtils.isEmpty(mGuaranteeFund)) {
                            showShortToast(R.string.transaction_guarantee_fund_message1);
                        } else {
                            if (mGuaranteeFund.endsWith("."))
                                mGuaranteeFund = mGuaranteeFund.substring(0, mGuaranteeFund.length() - 1);

                            if (new BigDecimal(mGuaranteeFund).compareTo(new BigDecimal(mTotal)) == 1) {
                                message = getString(R.string.transaction_guarantee_fund_message5);
                            } else {
                                BigDecimal riskRate;

                                if (new BigDecimal(mGuaranteeFund).compareTo(new BigDecimal(0)) == 0)
                                    riskRate = new BigDecimal(0);
                                else
                                    riskRate = new BigDecimal(mTotal).divide(new BigDecimal(mGuaranteeFund), 4, BigDecimal.ROUND_HALF_UP);

                                if (riskRate.compareTo(new BigDecimal(0)) == 0) {
                                    message = getString(R.string.transaction_guarantee_fund_message0);
                                } else {
                                    String riskRateValue = BigDecimalUtil.formatRate(riskRate.multiply(new BigDecimal(100)).toPlainString());

                                    if (riskRate.compareTo(new BigDecimal(mForcecloseth)) == -1)
                                        message = String.format(getString(R.string.transaction_guarantee_fund_message2), riskRateValue);
                                    else if (riskRate.compareTo(new BigDecimal(mForcecloseth)) == 1 && riskRate.compareTo(new BigDecimal(mWarnth)) == -1)
                                        message = String.format(getString(R.string.transaction_guarantee_fund_message3), riskRateValue);
                                    else
                                        message = String.format(getString(R.string.transaction_guarantee_fund_message4), riskRateValue);
                                }
                            }

                            mGuaranteeFundSettingPopUpWindow.dismiss();
                            mGuaranteeFundSettingPopUpWindow.hiddenSoftKeyboard();

                            mGuaranteeFundPopUpWindow.setData(message, (v) -> {
                                getMinReserveFund();

                                mGuaranteeFundPopUpWindow.dismiss();
                            });
                            mGuaranteeFundPopUpWindow.showAtLocation(mBinding.tvMessage, Gravity.CENTER, 0, 0);
                        }
                    }
                });
                mGuaranteeFundSettingPopUpWindow.showAtLocation(mBinding.tvMessage, Gravity.BOTTOM, 0, 0);
            }
        }

        public void onClickRiskRateTips() {
            if (null != mConfirmSimplePopupwindow && !mConfirmSimplePopupwindow.isShowing()) {
                mConfirmSimplePopupwindow.setData(String.format(getResources().getString(R.string.transaction_riskrate_rule), "100%"),
                        getResources().getString(R.string.text_confirm),
                        (view) -> mConfirmSimplePopupwindow.dismiss());
                mConfirmSimplePopupwindow.showAtLocation(mBinding.tvMessage, Gravity.CENTER, 0, 0);
            }
        }

    }
}
