package com.jme.lsgoldtrade.ui.transaction;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.StyleSpan;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.common.util.RxBus;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseFragment;
import com.jme.lsgoldtrade.config.AppConfig;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.FragmentCreateConditionSheetBinding;
import com.jme.lsgoldtrade.domain.AccountVo;
import com.jme.lsgoldtrade.domain.ConditionOrderRunVo;
import com.jme.lsgoldtrade.domain.ContractInfoVo;
import com.jme.lsgoldtrade.domain.FiveSpeedVo;
import com.jme.lsgoldtrade.domain.PositionPageVo;
import com.jme.lsgoldtrade.domain.PositionVo;
import com.jme.lsgoldtrade.service.ConditionService;
import com.jme.lsgoldtrade.service.TradeService;
import com.jme.lsgoldtrade.util.MarketUtil;
import com.jme.lsgoldtrade.view.ConfirmDetailPopupwindow;
import com.jme.lsgoldtrade.view.ConfirmPopupwindow;
import com.jme.lsgoldtrade.view.RulePopupwindow;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import rx.Subscription;

public class CreateConditionSheetFragment extends JMEBaseFragment {

    private FragmentCreateConditionSheetBinding mBinding;

    private boolean bVisibleToUser = false;
    private boolean bFlag = true;
    private int mConditionOrderRunNum;
    private int mStopOrderRunNum;
    private int mSelectItem = 0;
    private int mType;
    private float mPriceMove = 0.00f;
    private long mMaxAmount;
    private long mMinOrderQty = 0;
    private long mMaxOrderQty = 0;
    private long mMaxHoldQty = 0;
    private String mContractID = "";
    private String mPagingKey = "";
    private String[] mContracIDs;

    private List<PositionVo> mPositionVoList = new ArrayList<>();
    private List<FiveSpeedVo> mFiveSpeedVoList;

    private RulePopupwindow mRulePopupwindow;
    private ConfirmDetailPopupwindow mConfirmDetailPopupwindow;
    private FiveSpeedVo mFiveSpeedVo;
    private AccountVo mAccountVo;
    private ContractInfoVo mContractInfoVo;
    private AlertDialog mDialog;
    private Subscription mRxbus;

    private static final int TYPE_BUY_MORE = 1;
    private static final int TYPE_SELL_EMPTY = 2;

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_create_condition_sheet;
    }

    @Override
    protected void initView() {
        super.initView();
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);

        mRulePopupwindow = new RulePopupwindow(mContext);
        mConfirmDetailPopupwindow = new ConfirmDetailPopupwindow(mContext);
        mType = TYPE_BUY_MORE;

        initContractNameValue();
        setContractNameData();
        setMarketType();
    }

    @Override
    protected void initListener() {
        super.initListener();

        initRxBus();

        mBinding.etPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length() != mFiveSpeedVo.getLatestPriceValue().length())
                    bFlag = false;

                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > AppConfig.Length_Limit) {
                        s = s.toString().subSequence(0, s.toString().indexOf(".") + (AppConfig.Length_Limit + 1));

                        mBinding.etPrice.setText(s);
                        mBinding.etPrice.setSelection(s.length());
                    }
                }

                if (s.toString().trim().equals(".")) {
                    s = "0" + s;

                    mBinding.etPrice.setText(s);
                    mBinding.etPrice.setSelection(2);
                }

                if (s.toString().startsWith("0") && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        mBinding.etPrice.setText(s.subSequence(0, 1));
                        mBinding.etPrice.setSelection(1);

                        return;
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                setConditionSheetDetail();
            }
        });

        mBinding.etAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                setConditionSheetDetail();
            }
        });

        mBinding.checkboxEffectiveOnThatDay.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                mBinding.checkboxEffectiveOnThatDay.setClickable(false);
                mBinding.checkboxEffectiveBeforeCancel.setChecked(false);
                mBinding.checkboxEffectiveBeforeCancel.setClickable(true);
            }
        });

        mBinding.checkboxEffectiveBeforeCancel.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                mBinding.checkboxEffectiveBeforeCancel.setClickable(false);
                mBinding.checkboxEffectiveOnThatDay.setChecked(false);
                mBinding.checkboxEffectiveOnThatDay.setClickable(true);
            }
        });
    }

    @Override
    public void initBinding() {
        super.initBinding();

        mBinding = (FragmentCreateConditionSheetBinding) mBindingUtil;
        mBinding.setHandlers(new ClickHandlers());
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        bVisibleToUser = isVisibleToUser;

        if (null != mBinding && bVisibleToUser) {
            mPagingKey = "";
            mPositionVoList.clear();

            queryConditionOrderRun();
            getAccount();
            getPosition();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (bVisibleToUser) {
            mPagingKey = "";
            mPositionVoList.clear();

            queryConditionOrderRun();
            getAccount();
            getPosition();
        }
    }

    private void initRxBus() {
        mRxbus = RxBus.getInstance().toObserverable(RxBus.Message.class).subscribe(message -> {
            String callType = message.getObject().toString();

            if (TextUtils.isEmpty(callType))
                return;

            switch (callType) {
                case Constants.RxBusConst.RXBUS_TRANSACTION_CONDITION_SHEET_FIVESPEED:
                    Object object = message.getObject2();

                    if (null == object)
                        return;

                    mFiveSpeedVoList = (List<FiveSpeedVo>) object;

                    setMarketData();

                    break;
                case Constants.RxBusConst.RXBUS_ORDER_SUCCESS:
                    mPositionVoList.clear();

                    getAccount();
                    getPosition();

                    break;
                case Constants.RxBusConst.RXBUS_LOGOUT_SUCCESS:
                    mBinding.etPrice.setText("");
                    mBinding.etAmount.setText("1");
                    mBinding.tvCount.setText("- - -手");

                    break;
            }
        });
    }

    private void initContractNameValue() {
        if (null != mContract) {
            String listStr = mContract.getContractIDListStr();

            if (!TextUtils.isEmpty(listStr))
                mContracIDs = listStr.split(",");
        }
    }

    private void setContractNameData() {
        mContractID = AppConfig.Select_ContractId;

        mBinding.tvContractId.setText(mContractID);
        mBinding.etPrice.setInputType(mContractID.equals("Ag(T+D)") ? InputType.TYPE_CLASS_NUMBER : InputType.TYPE_NUMBER_FLAG_DECIMAL);

        if (null != mContract) {
            mSelectItem = mContract.getContractIDPosition(mContractID);
            mContractInfoVo = mContract.getContractInfoFromID(mContractID);

            setContractData();
        }
    }

    private void setContractData() {
        if (null == mContractInfoVo) {
            mPriceMove = 0;
            mMinOrderQty = 0;
            mMaxOrderQty = 0;
            mMaxHoldQty = 0;
        } else {
            mPriceMove = new BigDecimal(mContractInfoVo.getMinPriceMove()).divide(new BigDecimal(100)).floatValue();
            mMinOrderQty = mContractInfoVo.getMinOrderQty();
            mMaxOrderQty = mContractInfoVo.getMaxOrderQty();
            mMaxHoldQty = mContractInfoVo.getMaxHoldQty();

            mBinding.etAmount.setText(mMinOrderQty == -1 ? "1" : String.valueOf(mMinOrderQty));
        }
    }

    private void showContractNameDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setSingleChoiceItems(mContracIDs, mSelectItem, (dialogInterface, position) -> {
            if (mSelectItem != position) {
                mSelectItem = position;
                bFlag = true;

                mContractID = mContracIDs[mSelectItem];

                mBinding.tvContractId.setText(mContractID);
                mContractInfoVo = mContract.getContractInfoFromID(mContractID);

                mBinding.etPrice.setInputType(mContractID.equals("Ag(T+D)") ? InputType.TYPE_CLASS_NUMBER : InputType.TYPE_NUMBER_FLAG_DECIMAL);

                setMarketData();
                setContractData();
                calculateMaxAmount();
            }

            mBinding.imgSelect.setBackground(ContextCompat.getDrawable(mContext, R.mipmap.ic_down));

            mDialog.dismiss();
        });

        mDialog = builder.create();
        mDialog.show();
    }

    private void setMarketData() {
        if (null == mFiveSpeedVoList || 0 == mFiveSpeedVoList.size())
            return;

        for (FiveSpeedVo fiveSpeedVo : mFiveSpeedVoList) {
            if (null != fiveSpeedVo && fiveSpeedVo.getContractId().equals(mContractID))
                mFiveSpeedVo = fiveSpeedVo;
        }

        if (null == mFiveSpeedVo)
            return;

        String upDown = mFiveSpeedVo.getUpDownValue();
        String lastPrice = mFiveSpeedVo.getLatestPriceValue();

        int rateType = TextUtils.isEmpty(upDown) ? 0 : new BigDecimal(upDown).compareTo(new BigDecimal(0));

        mBinding.tvLastPrice.setText(lastPrice);
        mBinding.tvRange.setText(MarketUtil.getMarketRangeValue(rateType, upDown));
        mBinding.tvRate.setText(MarketUtil.getMarketRateValue(rateType, mFiveSpeedVo.getUpDownRateValue()));

        if (bFlag) {
            mBinding.etPrice.setText(MarketUtil.formatValue(String.valueOf(lastPrice), 2));
            mBinding.etPrice.setSelection(MarketUtil.formatValue(String.valueOf(lastPrice), 2).length());
        }

        if (new BigDecimal(lastPrice).compareTo(new BigDecimal(0)) == 0) {
            mBinding.tvLastPrice.setTextColor(ContextCompat.getColor(mContext, R.color.color_text_black));
            mBinding.tvRange.setTextColor(ContextCompat.getColor(mContext, R.color.color_text_black));
            mBinding.tvRate.setTextColor(ContextCompat.getColor(mContext, R.color.color_text_black));
        } else {
            mBinding.tvLastPrice.setTextColor(ContextCompat.getColor(mContext, MarketUtil.getMarketStateColor(rateType)));
            mBinding.tvRange.setTextColor(ContextCompat.getColor(mContext, MarketUtil.getMarketStateColor(rateType)));
            mBinding.tvRate.setTextColor(ContextCompat.getColor(mContext, MarketUtil.getMarketStateColor(rateType)));
        }

        setMarketRange();
        setConditionSheetDetail();
    }

    private void setMarketType() {
        mBinding.layoutBuyMore.setBackground(mType == TYPE_BUY_MORE ? ContextCompat.getDrawable(mContext, R.drawable.bg_btn_buy_more_solid)
                : ContextCompat.getDrawable(mContext, R.drawable.bg_btn_buy_more_hollow));
        mBinding.tvBuyMore.setTextColor(mType == TYPE_BUY_MORE ? ContextCompat.getColor(mContext, R.color.white)
                : ContextCompat.getColor(mContext, R.color.color_red));
        mBinding.imgBuyMore.setVisibility(mType == TYPE_BUY_MORE ? View.VISIBLE : View.GONE);
        mBinding.layoutSellEmpty.setBackground(mType == TYPE_SELL_EMPTY ? ContextCompat.getDrawable(mContext, R.drawable.bg_btn_sale_empty_solid)
                : ContextCompat.getDrawable(mContext, R.drawable.bg_btn_sale_empty_hollow));
        mBinding.tvSellEmpty.setTextColor(mType == TYPE_SELL_EMPTY ? ContextCompat.getColor(mContext, R.color.white)
                : ContextCompat.getColor(mContext, R.color.color_green));
        mBinding.imgSellEmpty.setVisibility(mType == TYPE_SELL_EMPTY ? View.VISIBLE : View.GONE);
        mBinding.tvLastPriceRange.setText(mType == TYPE_BUY_MORE ? R.string.transaction_last_price_buy_more_simple : R.string.transaction_last_price_sell_empty_simple);

        setMarketRange();
    }

    private void setMarketRange() {
        String range;

        if (null == mFiveSpeedVo) {
            range = "";
        } else {
            if (mFiveSpeedVo.getContractId().equals(mContractID)) {
                if (mType == TYPE_BUY_MORE)
                    range = String.format(mContext.getResources().getString(R.string.transaction_price_range),
                            mFiveSpeedVo.getLowerLimitPrice(), mFiveSpeedVo.getLatestPriceValue());
                else
                    range = String.format(mContext.getResources().getString(R.string.transaction_price_range),
                            mFiveSpeedVo.getLatestPriceValue(), mFiveSpeedVo.getHighLimitPrice());
            } else {
                range = "";
            }
        }

        mBinding.tvPriceRange.setText(range);
    }

    private String getPrice() {
        String price = mBinding.etPrice.getText().toString();

        if (TextUtils.isEmpty(price) && null != mFiveSpeedVo && mFiveSpeedVo.getContractId().equals(mContractID))
            return mFiveSpeedVo.getLatestPriceValue();

        if (price.endsWith("."))
            price = price.substring(0, price.length() - 1);

        return price;
    }

    private void setConditionSheetDetail() {
        mBinding.tvConditionSheetDetail.setText(String.format(mContext.getResources().getString(R.string.transaction_condition_sheet_detail_message),
                mBinding.tvLastPriceRange.getText().toString(), mBinding.etPrice.getText().toString(), mContractID,
                mType == TYPE_BUY_MORE ? mContext.getResources().getString(R.string.transaction_more)
                        : mContext.getResources().getString(R.string.transaction_empty),
                mContext.getResources().getString(R.string.transaction_open_position),
                mContext.getResources().getString(R.string.transaction_market_price_fak),
                mBinding.etAmount.getText().toString()));
    }

    private void calculateMaxAmount() {
        if (TextUtils.isEmpty(mContractID) || null == mFiveSpeedVo || null == mAccountVo || null == mContractInfoVo) {
            mMaxAmount = 0;
        } else {
            String price = mFiveSpeedVo.getHighLimitPrice();

            if (TextUtils.isEmpty(price)) {
                mMaxAmount = mMaxOrderQty;
            } else {
                if (new BigDecimal(price).compareTo(new BigDecimal(0)) == 0) {
                    mMaxAmount = mMaxOrderQty;
                } else {
                    PositionVo positionVoValue = null;

                    if (null != mPositionVoList && 0 != mPositionVoList.size()) {
                        for (PositionVo positionVo : mPositionVoList) {
                            if (null != positionVo && positionVo.getContractId().equals(mContractID)) {
                                if (mType == TYPE_BUY_MORE && positionVo.getType().equals("空"))
                                    positionVoValue = positionVo;
                                else if (mType == TYPE_SELL_EMPTY && positionVo.getType().equals("多"))
                                    positionVoValue = positionVo;
                            }
                        }
                    }

                    String transactionBalance = mAccountVo.getTransactionBalanceStr();
                    String positionMargin = null == mAccountVo ? "0" : mAccountVo.getPositionMarginStr();

                    long bankLongMarginRate = mContractInfoVo.getBankLongMarginRate();
                    long bankShortMarginRate = mContractInfoVo.getBankShortMarginRate();
                    long bankFeeRate = mContractInfoVo.getBankFeeRate();
                    long exchangeFeeRate = mContractInfoVo.getExchangeFeeRate();
                    long handWeight = MarketUtil.getHandWeight(mContractInfoVo.getHandWeight());
                    long bankMarginRate = mType == TYPE_BUY_MORE ? bankLongMarginRate : bankShortMarginRate;

                    BigDecimal money = new BigDecimal(transactionBalance).add(new BigDecimal(positionMargin));
                    BigDecimal contractMoney = new BigDecimal(price).multiply(new BigDecimal(mContractID.equals("Ag(T+D)") ?
                            new BigDecimal(handWeight).divide(new BigDecimal(1000), 0, BigDecimal.ROUND_HALF_UP).longValue() : handWeight));
                    BigDecimal bankMarginRateValue = new BigDecimal(bankMarginRate).divide(new BigDecimal(10000));
                    BigDecimal bankFeeRateValue = new BigDecimal(bankFeeRate).divide(new BigDecimal(10000)).divide(new BigDecimal(10000));
                    BigDecimal exchangeFeeRateValue = new BigDecimal(exchangeFeeRate).divide(new BigDecimal(10000)).divide(new BigDecimal(10000));
                    BigDecimal feeRate = bankMarginRateValue.add(bankFeeRateValue).add(exchangeFeeRateValue);
                    BigDecimal totalAmount = money.divide(contractMoney.multiply(feeRate), 0, BigDecimal.ROUND_DOWN);

                    mMaxAmount = (new BigDecimal(Math.min(totalAmount.longValue(), mMaxOrderQty))
                            .subtract(null == positionVoValue ? BigDecimal.ZERO : new BigDecimal(positionVoValue.getPosition()))).longValue();

                    if (new BigDecimal(mMaxAmount).compareTo(new BigDecimal(0)) == -1)
                        mMaxAmount = 0;
                }
            }
        }

        mBinding.tvCount.setText(mMaxAmount + mContext.getResources().getString(R.string.text_amount_unit));
        mBinding.etAmount.setText(0 == mMaxAmount ? "0" : "1");
    }

    private void showConfirmPopupWindow(String price, String amount) {
        if (null != mConfirmDetailPopupwindow && !mConfirmDetailPopupwindow.isShowing()) {
            mConfirmDetailPopupwindow.setData(mContext.getResources().getString(R.string.transaction_condition_sheet_confirm),
                    mBinding.tvConditionSheetDetail.getText().toString(), (view) -> {
                        mConfirmDetailPopupwindow.dismiss();

                        entrustConditionOrder(price, amount);
                    });
            mConfirmDetailPopupwindow.showAtLocation(mBinding.etPrice, Gravity.CENTER, 0, 0);
        }
    }

    private void hiddenKeyBoard() {
        ((InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                mBinding.etAmount.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private void getAccount() {
        if (null == mUser || !mUser.isLogin())
            return;

        String accountID = mUser.getAccountID();

        if (TextUtils.isEmpty(accountID))
            return;

        HashMap<String, String> params = new HashMap<>();
        params.put("accountId", accountID);

        sendRequest(TradeService.getInstance().account, params, false);
    }

    private void getPosition() {
        if (null == mUser || !mUser.isLogin())
            return;

        HashMap<String, String> params = new HashMap<>();
        params.put("accountId", mUser.getAccountID());
        params.put("pagingKey", mPagingKey);

        sendRequest(TradeService.getInstance().position, params, false, false, false);
    }

    private void queryConditionOrderRun() {
        sendRequest(ConditionService.getInstance().queryConditionOrderRun, new HashMap<>(), false, false, false);
    }

    private void entrustConditionOrder(String price, String amount) {
        if (null == mUser || !mUser.isLogin())
            return;

        String accountID = mUser.getAccountID();

        if (TextUtils.isEmpty(accountID))
            return;

        HashMap<String, String> params = new HashMap<>();
        params.put("accountId", accountID);
        params.put("bsFlag", String.valueOf(mType));
        params.put("contractId", mContractID);
        params.put("effectiveTimeFlag", mBinding.checkboxEffectiveOnThatDay.isChecked() ? "0" : "1");
        params.put("entrustNumber", amount);
        params.put("ocFlag", "0");
        params.put("tradingType", "3");
        params.put("type", "1");
        params.put("triggerPrice",  String.valueOf(new BigDecimal(price).multiply(new BigDecimal(100)).longValue()));

        sendRequest(ConditionService.getInstance().entrustConditionOrder, params, true);
    }

    @Override
    protected void DataReturn(DTRequest request, Head head, Object response) {
        super.DataReturn(request, head, response);

        switch (request.getApi().getName()) {
            case "QueryConditionOrderRun":
                if (head.isSuccess()) {
                    ConditionOrderRunVo conditionOrderRunVo;

                    try {
                        conditionOrderRunVo = (ConditionOrderRunVo) response;
                    } catch (Exception e) {
                        conditionOrderRunVo = null;

                        e.printStackTrace();
                    }

                    if (null == conditionOrderRunVo)
                        return;

                    mConditionOrderRunNum = conditionOrderRunVo.getConditionOrderRunNum();
                    mStopOrderRunNum = conditionOrderRunVo.getStopOrderRunNum();

                    if (0 == mConditionOrderRunNum && 0 == mStopOrderRunNum) {
                        mBinding.tvRunningMessage.setVisibility(View.GONE);
                    } else {
                        String message;

                        if (0 != mConditionOrderRunNum && 0 == mStopOrderRunNum)
                            message = String.format(mContext.getResources().getString(R.string.transaction_condition_sheet_running_message), mConditionOrderRunNum);
                        else if (0 == mConditionOrderRunNum && 0 != mStopOrderRunNum)
                            message = String.format(mContext.getResources().getString(R.string.transaction_stop_running_message), mStopOrderRunNum);
                        else
                            message = String.format(mContext.getResources().getString(R.string.transaction_all_running_message), mConditionOrderRunNum, mStopOrderRunNum);

                        mBinding.tvRunningMessage.setText(message);
                        mBinding.tvRunningMessage.setVisibility(View.VISIBLE);
                    }
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

                    calculateMaxAmount();
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

                        if (null != positionVoList && 0 != positionVoList.size()) {
                            for (PositionVo positionVo : positionVoList) {
                                if (null != positionVo)
                                    mPositionVoList.add(positionVo);
                            }
                        }

                        if (hasNext)
                            getPosition();
                        else
                            calculateMaxAmount();
                    } else {
                        calculateMaxAmount();
                    }
                }

                break;
            case "EntrustConditionOrder":
                if (head.isSuccess()) {
                    showShortToast(R.string.transaction_condition_sheet_create_success);

                    bFlag = true;

                    if (null != mFiveSpeedVo && mFiveSpeedVo.getContractId().equals(mContractID)) {
                        mBinding.etPrice.setText(mFiveSpeedVo.getLatestPriceValue());
                        mBinding.etPrice.setSelection(mFiveSpeedVo.getLatestPriceValue().length());
                    }

                    calculateMaxAmount();

                    RxBus.getInstance().post(Constants.RxBusConst.RXBUS_TRANSACTION_CONDITION_OWN, null);
                }

                break;
        }
    }

    public class ClickHandlers {

        public void onClickConditionOrderRun() {
            RxBus.getInstance().post(Constants.RxBusConst.RXBUS_TRANSACTION_CONDITION_ORDER_RUN, 0 != mConditionOrderRunNum ? 1 : 2);
        }

        public void onClickSelectContract() {
            hiddenKeyBoard();

            if (null != mContracIDs && mContracIDs.length > 0) {
                mBinding.imgSelect.setBackground(ContextCompat.getDrawable(mContext, R.mipmap.ic_up));

                showContractNameDialog();
            }
        }

        public void onClickBuyMore() {
            if (mType == TYPE_BUY_MORE)
                return;

            mType = TYPE_BUY_MORE;
            bFlag = true;

            if (null != mFiveSpeedVo && mFiveSpeedVo.getContractId().equals(mContractID)) {
                mBinding.etPrice.setText(mFiveSpeedVo.getLatestPriceValue());
                mBinding.etPrice.setSelection(mFiveSpeedVo.getLatestPriceValue().length());
            }

            setMarketType();
            setConditionSheetDetail();
            calculateMaxAmount();
        }

        public void onClickSellEmpty() {
            if (mType == TYPE_SELL_EMPTY)
                return;

            mType = TYPE_SELL_EMPTY;
            bFlag = true;

            if (null != mFiveSpeedVo && mFiveSpeedVo.getContractId().equals(mContractID)) {
                mBinding.etPrice.setText(mFiveSpeedVo.getLatestPriceValue());
                mBinding.etPrice.setSelection(mFiveSpeedVo.getLatestPriceValue().length());
            }

            setMarketType();
            setConditionSheetDetail();
            calculateMaxAmount();
        }

        public void onClickPriceMinus() {
            hiddenKeyBoard();

            String price = getPrice();

            if (TextUtils.isEmpty(price))
                return;

            float value = new BigDecimal(price).subtract(new BigDecimal(mPriceMove)).floatValue();

            if (mFiveSpeedVo.getContractId().equals(mContractID)) {
                String minPrice = mType == TYPE_BUY_MORE ? mFiveSpeedVo.getLowerLimitPrice() : mFiveSpeedVo.getLatestPriceValue();

                if (new BigDecimal(value).compareTo(new BigDecimal(minPrice)) == -1) {
                    showShortToast(R.string.transaction_sheet_limit_down_price_error);

                    mBinding.etPrice.setText(price);
                    mBinding.etPrice.setSelection(price.length());
                } else {
                    bFlag = false;

                    String valueStr = MarketUtil.formatValue(String.valueOf(value), 2);

                    mBinding.etPrice.setText(valueStr);
                    mBinding.etPrice.setSelection(valueStr.length());
                }
            } else {
                bFlag = false;

                String valueStr = MarketUtil.formatValue(String.valueOf(value), 2);

                mBinding.etPrice.setText(valueStr);
                mBinding.etPrice.setSelection(valueStr.length());
            }
        }

        public void onClickPriceAdd() {
            hiddenKeyBoard();

            String price = getPrice();

            if (TextUtils.isEmpty(price))
                return;

            float value = new BigDecimal(price).add(new BigDecimal(mPriceMove)).floatValue();

            if (mFiveSpeedVo.getContractId().equals(mContractID)) {
                String maxPrice = mType == TYPE_BUY_MORE ? mFiveSpeedVo.getLatestPriceValue() : mFiveSpeedVo.getHighLimitPrice();

                if (new BigDecimal(value).compareTo(new BigDecimal(maxPrice)) == 1) {
                    showShortToast(R.string.transaction_sheet_limit_up_price_error);

                    mBinding.etPrice.setText(price);
                    mBinding.etPrice.setSelection(price.length());
                } else {
                    bFlag = false;

                    String valueStr = MarketUtil.formatValue(String.valueOf(value), 2);

                    mBinding.etPrice.setText(valueStr);
                    mBinding.etPrice.setSelection(valueStr.length());
                }
            } else {
                bFlag = false;

                String valueStr = MarketUtil.formatValue(String.valueOf(value), 2);

                mBinding.etPrice.setText(valueStr);
                mBinding.etPrice.setSelection(valueStr.length());
            }
        }

        public void onClickEntrustTypeTips() {
            if (null != mRulePopupwindow && !mRulePopupwindow.isShowing()) {
                mRulePopupwindow.setData(mContext.getResources().getString(R.string.transaction_market_price_fak),
                        new SpannableString(mContext.getResources().getString(R.string.transaction_market_price_fak_rule)));
                mRulePopupwindow.showAtLocation(mBinding.etPrice, Gravity.CENTER, 0, 0);
            }
        }

        public void onClickAmountMinus() {
            hiddenKeyBoard();

            String amount = mBinding.etAmount.getText().toString();

            if (TextUtils.isEmpty(amount))
                amount = "0";

            long value = new BigDecimal(amount).subtract(new BigDecimal(1)).longValue();

            if (mMinOrderQty == -1) {
                if (new BigDecimal(value).compareTo(new BigDecimal(0)) == 1) {
                    mBinding.etAmount.setText(String.valueOf(value));
                    mBinding.etAmount.setSelection(String.valueOf(value).length());
                } else {
                    Toast.makeText(mContext, String.format(mContext.getResources().getString(R.string.transaction_entrust_less), 1), Toast.LENGTH_SHORT).show();
                }
            } else {
                if (new BigDecimal(value).compareTo(new BigDecimal(mMinOrderQty)) == -1) {
                    Toast.makeText(mContext, String.format(mContext.getResources().getString(R.string.transaction_entrust_less), mMinOrderQty), Toast.LENGTH_SHORT).show();
                } else {
                    mBinding.etAmount.setText(String.valueOf(value));
                    mBinding.etAmount.setSelection(String.valueOf(value).length());
                }
            }

            setConditionSheetDetail();
        }

        public void onClickAmountAdd() {
            hiddenKeyBoard();

            String amount = mBinding.etAmount.getText().toString();

            if (TextUtils.isEmpty(amount))
                amount = "0";

            long value = new BigDecimal(amount).add(new BigDecimal(1)).longValue();

            if (mMaxOrderQty == -1) {
                if (new BigDecimal(value).compareTo(new BigDecimal(mMaxHoldQty == -1 ? mMaxAmount : Math.min(mMaxAmount, mMaxHoldQty))) == 1) {
                    Toast.makeText(mContext, R.string.transaction_entrust_larger, Toast.LENGTH_SHORT).show();
                } else {
                    mBinding.etAmount.setText(String.valueOf(value));
                    mBinding.etAmount.setSelection(String.valueOf(value).length());
                }
            } else {
                if (new BigDecimal(value).compareTo(new BigDecimal(Math.min(mMaxAmount, mMaxOrderQty))) == 1) {
                    Toast.makeText(mContext, R.string.transaction_entrust_larger, Toast.LENGTH_SHORT).show();
                } else {
                    mBinding.etAmount.setText(String.valueOf(value));
                    mBinding.etAmount.setSelection(String.valueOf(value).length());
                }
            }

            setConditionSheetDetail();
        }

        public void onClickEffectiveTimeTips() {
            if (null != mRulePopupwindow && !mRulePopupwindow.isShowing()) {
                String value = mContext.getResources().getString(R.string.transaction_effective_time_rule);
                int firstPosition = value.indexOf("当日有效：");
                int secondPostion = value.indexOf("撤销前有效：");

                SpannableString spannableString = new SpannableString(value);
                spannableString.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), firstPosition, firstPosition + 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                spannableString.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), secondPostion, secondPostion + 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                mRulePopupwindow.setData(mContext.getResources().getString(R.string.transaction_effective_time), spannableString);
                mRulePopupwindow.showAtLocation(mBinding.etPrice, Gravity.CENTER, 0, 0);
            }
        }

        public void onCliclConditionSheetRiskTips() {

        }

        public void onClickSubmit() {
            String price = mBinding.etPrice.getText().toString();
            String amount = mBinding.etAmount.getText().toString();

            if (TextUtils.isEmpty(mContractID))
                showShortToast(R.string.transaction_contract_error);
            else if (TextUtils.isEmpty(price))
                showShortToast(R.string.transaction_price_error);
            else if (mType == TYPE_BUY_MORE && new BigDecimal(price).compareTo(new BigDecimal(mFiveSpeedVo.getLowerLimitPrice())) == -1)
                showShortToast(String.format(mContext.getResources().getString(R.string.transaction_price_setting_range),
                        mFiveSpeedVo.getLowerLimitPrice(), mFiveSpeedVo.getLatestPriceValue()));
            else if (mType == TYPE_BUY_MORE && new BigDecimal(price).compareTo(new BigDecimal(mFiveSpeedVo.getLatestPriceValue())) == 1)
                showShortToast(String.format(mContext.getResources().getString(R.string.transaction_price_setting_range),
                        mFiveSpeedVo.getLowerLimitPrice(), mFiveSpeedVo.getLatestPriceValue()));
            else if (mType == TYPE_SELL_EMPTY && new BigDecimal(price).compareTo(new BigDecimal(mFiveSpeedVo.getLatestPriceValue())) == -1)
                showShortToast(String.format(mContext.getResources().getString(R.string.transaction_price_setting_range),
                        mFiveSpeedVo.getLatestPriceValue(), mFiveSpeedVo.getHighLimitPrice()));
            else if (mType == TYPE_SELL_EMPTY && new BigDecimal(price).compareTo(new BigDecimal(mFiveSpeedVo.getHighLimitPrice())) == 1)
                showShortToast(String.format(mContext.getResources().getString(R.string.transaction_price_setting_range),
                        mFiveSpeedVo.getLatestPriceValue(), mFiveSpeedVo.getHighLimitPrice()));
            else if (TextUtils.isEmpty(amount))
                showShortToast(R.string.transaction_number_error);
            else if (new BigDecimal(amount).compareTo(new BigDecimal(0)) == 0)
                showShortToast(String.format(mContext.getResources().getString(R.string.transaction_entrust_less), 1));
            else if (new BigDecimal(amount).compareTo(new BigDecimal(mMaxAmount)) == 1)
                showShortToast(R.string.transaction_entrust_larger2);
            else if (!mBinding.checkboxAgree.isChecked())
                Toast.makeText(mContext, R.string.transaction_condition_sheet_risk_agree, Toast.LENGTH_SHORT).show();
            else
                showConfirmPopupWindow(price, amount);
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (!mRxbus.isUnsubscribed())
            mRxbus.unsubscribe();
    }
}
