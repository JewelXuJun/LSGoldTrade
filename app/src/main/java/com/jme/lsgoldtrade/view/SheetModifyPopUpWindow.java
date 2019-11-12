package com.jme.lsgoldtrade.view;

import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.alibaba.android.arouter.launcher.ARouter;
import com.jme.common.util.DensityUtil;
import com.jme.common.util.RxBus;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBasePopupWindow;
import com.jme.lsgoldtrade.config.AppConfig;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.PopupwindowSheetModifyBinding;
import com.jme.lsgoldtrade.domain.AccountVo;
import com.jme.lsgoldtrade.domain.ConditionOrderInfoVo;
import com.jme.lsgoldtrade.domain.ContractInfoVo;
import com.jme.lsgoldtrade.domain.FiveSpeedVo;
import com.jme.lsgoldtrade.domain.PositionVo;
import com.jme.lsgoldtrade.util.MarketUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class SheetModifyPopUpWindow extends JMEBasePopupWindow {

    private PopupwindowSheetModifyBinding mBinding;

    private FiveSpeedVo mFiveSpeedVo;
    private AccountVo mAccount;
    private PositionVo mPositionVo;
    private ContractInfoVo mContractInfoVo;
    private ConditionOrderInfoVo mConditionOrderInfoVo;

    private float mPriceMove = 0.01f;
    private String mContractID;
    private String mLowerLimitPrice;
    private String mHighLimitPrice;
    private long mMinOrderQty = 0;
    private long mMaxOrderQty = 0;
    private long mMaxHoldQty = 0;
    private long mMaxAmount = 0;
    private int mBsFlag;
    private int mOcFlag;
    private int mEffectiveTimeFlag;
    private int mLength = 2;

    public SheetModifyPopUpWindow(Context context) {
        super(context);
    }

    @Override
    protected void initPopupWindow() {
        super.initPopupWindow();

        setHeight(DensityUtil.dpTopx(getContext(), 440));
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
    }

    @Override
    public void iniWindow() {
        super.iniWindow();

        mBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.popupwindow_sheet_modify, null, false);

        if (null == mBinding)
            return;

        setContentView(mBinding.getRoot());

        mBinding.setHandlers(new ClickHandlers());

        initListener();
    }

    private void initListener() {
        mBinding.etPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > mLength) {
                        s = s.toString().subSequence(0, s.toString().indexOf(".") + (mLength + 1));

                        if (s.toString().endsWith("."))
                            s = s.toString().substring(0, s.toString().length() - 1);

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

    public void setData(FiveSpeedVo fiveSpeedVo, AccountVo accountVo, PositionVo positionVo,
                        ContractInfoVo contractInfoVo, ConditionOrderInfoVo conditionOrderInfoVo) {
        mFiveSpeedVo = fiveSpeedVo;
        mAccount = accountVo;
        mPositionVo = positionVo;
        mContractInfoVo = contractInfoVo;
        mConditionOrderInfoVo = conditionOrderInfoVo;
        mContractID = conditionOrderInfoVo.getContractId();
        mBsFlag = conditionOrderInfoVo.getBsFlag();
        mOcFlag = conditionOrderInfoVo.getOcFlag();
        mEffectiveTimeFlag = conditionOrderInfoVo.getEffectiveTimeFlag();
        mPriceMove = null == mContractInfoVo ? 0.01f : new BigDecimal(mContractInfoVo.getMinPriceMove()).divide(new BigDecimal(100)).floatValue();
        mLowerLimitPrice = fiveSpeedVo.getLowerLimitPrice();
        mHighLimitPrice = fiveSpeedVo.getHighLimitPrice();
        mMinOrderQty = null == mContractInfoVo ? 0 : mContractInfoVo.getMinOrderQty();
        mMaxOrderQty = null == mContractInfoVo ? 0 : mContractInfoVo.getMaxOrderQty();
        mMaxHoldQty = null == mContractInfoVo ? 0 : mContractInfoVo.getMaxHoldQty();
        mLength = mContractID.equals("Ag(T+D)") ? 0 : 2;

        mBinding.tvContractName.setText(mContractID);
        mBinding.tvDirection.setText(mBsFlag == 1 && mOcFlag == 0 ? mContext.getResources().getString(R.string.market_buy_more)
                : mBsFlag == 2 && mOcFlag == 0 ? mContext.getResources().getString(R.string.market_sale_empty) : "");
        mBinding.tvDirection.setTextColor(mBsFlag == 1 && mOcFlag == 0 ? ContextCompat.getColor(mContext, R.color.color_red)
                : mBsFlag == 2 && mOcFlag == 0 ? ContextCompat.getColor(mContext, R.color.color_green)
                : ContextCompat.getColor(mContext, R.color.color_text_normal));
        mBinding.tvLastPrice.setText(getTriggerPriceValue(mBsFlag, mOcFlag));
        mBinding.etPrice.setText(conditionOrderInfoVo.getTriggerPriceStr());
        mBinding.etPrice.setInputType(mContractID.equals("Ag(T+D)") ? InputType.TYPE_CLASS_NUMBER : EditorInfo.TYPE_CLASS_NUMBER | EditorInfo.TYPE_NUMBER_FLAG_DECIMAL);
        mBinding.etAmount.setText(String.valueOf(conditionOrderInfoVo.getEntrustNumber()));
        mBinding.checkboxEffectiveOnThatDay.setChecked(mEffectiveTimeFlag == 0);
        mBinding.checkboxEffectiveBeforeCancel.setChecked(mEffectiveTimeFlag == 1);

        setConditionSheetDetail();
        calculateMaxAmount();
    }

    public void setFiveSpeedVo(List<FiveSpeedVo> fiveSpeedVoList) {
        if (null != fiveSpeedVoList && 0 != fiveSpeedVoList.size()) {
            for (FiveSpeedVo fiveSpeedVo : fiveSpeedVoList) {
                if (null != fiveSpeedVo && !TextUtils.isEmpty(mContractID) && mContractID.equals(fiveSpeedVo.getContractId()))
                    mFiveSpeedVo = fiveSpeedVo;
            }
        }
    }

    private String getTriggerPriceValue(int bsFlag, int ocFlag) {
        String value = "";

        if (bsFlag == 1 && ocFlag == 0)
            value = mContext.getResources().getString(R.string.transaction_last_price_buy_more_simple);
        else if (bsFlag == 2 && ocFlag == 0)
            value = mContext.getResources().getString(R.string.transaction_last_price_sell_empty_simple);

        return value;
    }

    private void setConditionSheetDetail() {
        mBinding.tvConditionSheetDetail.setText(String.format(mContext.getResources().getString(R.string.transaction_condition_sheet_detail_message),
                mBinding.tvLastPrice.getText().toString(), mBinding.etPrice.getText().toString(), mContractID,
                mBsFlag == 1 ? mContext.getResources().getString(R.string.transaction_more)
                        : mBsFlag == 2 ? mContext.getResources().getString(R.string.transaction_empty) : "",
                mContext.getResources().getString(R.string.transaction_open_position),
                mContext.getResources().getString(R.string.transaction_market_price_fak),
                mBinding.etAmount.getText().toString()));
    }

    private void calculateMaxAmount() {
        if (TextUtils.isEmpty(mContractID) || null == mFiveSpeedVo || null == mAccount || null == mContractInfoVo) {
            mMaxAmount = 0;
        } else {
            String price = mFiveSpeedVo.getHighLimitPrice();

            if (TextUtils.isEmpty(price)) {
                mMaxAmount = mMaxOrderQty;
            } else {
                if (new BigDecimal(price).compareTo(new BigDecimal(0)) == 0) {
                    mMaxAmount = mMaxOrderQty;
                } else {
                    String transactionBalance = mAccount.getTransactionBalanceStr();
                    String positionMargin = null == mAccount ? "0" : mAccount.getPositionMarginStr();

                    long bankLongMarginRate = mContractInfoVo.getBankLongMarginRate();
                    long bankShortMarginRate = mContractInfoVo.getBankShortMarginRate();
                    long bankFeeRate = mContractInfoVo.getBankFeeRate();
                    long exchangeFeeRate = mContractInfoVo.getExchangeFeeRate();
                    long handWeight = MarketUtil.getHandWeight(mContractInfoVo.getHandWeight());
                    long bankMarginRate = mBsFlag == 1 ? bankLongMarginRate : bankShortMarginRate;

                    BigDecimal money = new BigDecimal(transactionBalance).add(new BigDecimal(positionMargin));
                    BigDecimal contractMoney = new BigDecimal(price).multiply(new BigDecimal(mContractID.equals("Ag(T+D)") ?
                            new BigDecimal(handWeight).divide(new BigDecimal(1000), 0, BigDecimal.ROUND_HALF_UP).longValue() : handWeight));
                    BigDecimal bankMarginRateValue = new BigDecimal(bankMarginRate).divide(new BigDecimal(10000));
                    BigDecimal bankFeeRateValue = new BigDecimal(bankFeeRate).divide(new BigDecimal(10000)).divide(new BigDecimal(10000));
                    BigDecimal exchangeFeeRateValue = new BigDecimal(exchangeFeeRate).divide(new BigDecimal(10000)).divide(new BigDecimal(10000));
                    BigDecimal feeRate = bankMarginRateValue.add(bankFeeRateValue).add(exchangeFeeRateValue);
                    BigDecimal totalAmount = money.divide(contractMoney.multiply(feeRate), 0, BigDecimal.ROUND_DOWN);

                    mMaxAmount = (new BigDecimal(Math.min(totalAmount.longValue(), mMaxOrderQty))
                            .subtract(null == mPositionVo ? BigDecimal.ZERO : new BigDecimal(mPositionVo.getPosition()))).longValue();

                    if (new BigDecimal(mMaxAmount).compareTo(new BigDecimal(0)) == -1)
                        mMaxAmount = 0;
                }
            }
        }

        mBinding.tvCount.setText(mMaxAmount + mContext.getResources().getString(R.string.text_amount_unit));
    }

    private String getPrice() {
        String price = mBinding.etPrice.getText().toString();

        if (TextUtils.isEmpty(price))
            return mConditionOrderInfoVo.getTriggerPriceStr();

        if (price.endsWith("."))
            price = price.substring(0, price.length() - 1);

        return price;
    }

    private void sendData(String triggerPrice, String entrustNumber) {
        dismiss();

        List<String> list = new ArrayList<>();
        list.add(String.valueOf(mConditionOrderInfoVo.getId()));
        list.add(mBinding.checkboxEffectiveOnThatDay.isChecked() ? "0" : "1");
        list.add(triggerPrice);
        list.add(entrustNumber);

        RxBus.getInstance().post(Constants.RxBusConst.RXBUS_TRANSACTION_CONDITION_SHEET_MODIFY, list);
    }

    public class ClickHandlers {

        public void onClickPriceMinus() {
            hiddenKeyBoard();

            float value = new BigDecimal(getPrice()).subtract(new BigDecimal(mPriceMove)).floatValue();
            String valueStr = MarketUtil.formatValue(String.valueOf(value), mLength);

            mBinding.etPrice.setText(valueStr);
            mBinding.etPrice.setSelection(valueStr.length());
        }

        public void onClickPriceAdd() {
            hiddenKeyBoard();

            float value = new BigDecimal(getPrice()).add(new BigDecimal(mPriceMove)).floatValue();
            String valueStr = MarketUtil.formatValue(String.valueOf(value), mLength);

            mBinding.etPrice.setText(valueStr);
            mBinding.etPrice.setSelection(valueStr.length());
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

        public void onCliclConditionSheetRiskTips() {
            ARouter.getInstance()
                    .build(Constants.ARouterUriConst.JMEWEBVIEW)
                    .withString("title", mContext.getResources().getString(R.string.transaction_condition_sheet_risk_tips_title))
                    .withString("url", Constants.HttpConst.URL_CONDITION_SHEET)
                    .navigation();
        }

        public void onClickCancel() {
            dismiss();
        }

        public void onClickModify() {
            String price = mBinding.etPrice.getText().toString();
            String amount = mBinding.etAmount.getText().toString();

            if (!TextUtils.isEmpty(price) && price.endsWith("."))
                price = price.substring(0, price.length() - 1);

            if (TextUtils.isEmpty(price))
                Toast.makeText(mContext, R.string.transaction_price_error, Toast.LENGTH_SHORT).show();
            else if (mBsFlag == 1 && new BigDecimal(price).compareTo(new BigDecimal(mLowerLimitPrice)) == -1)
                Toast.makeText(mContext, String.format(mContext.getResources().getString(R.string.transaction_price_setting_range),
                        mLowerLimitPrice, mFiveSpeedVo.getLatestPriceValue()), Toast.LENGTH_SHORT).show();
            else if (mBsFlag == 1 && new BigDecimal(price).compareTo(new BigDecimal(mFiveSpeedVo.getLatestPriceValue())) == 1)
                Toast.makeText(mContext, String.format(mContext.getResources().getString(R.string.transaction_price_setting_range),
                        mLowerLimitPrice, mFiveSpeedVo.getLatestPriceValue()), Toast.LENGTH_SHORT).show();
            else if (mBsFlag == 2 && new BigDecimal(price).compareTo(new BigDecimal(mFiveSpeedVo.getLatestPriceValue())) == -1)
                Toast.makeText(mContext, String.format(mContext.getResources().getString(R.string.transaction_price_setting_range),
                        mFiveSpeedVo.getLatestPriceValue(), mHighLimitPrice), Toast.LENGTH_SHORT).show();
            else if (mBsFlag == 2 && new BigDecimal(price).compareTo(new BigDecimal(mHighLimitPrice)) == 1)
                Toast.makeText(mContext, String.format(mContext.getResources().getString(R.string.transaction_price_setting_range),
                        mFiveSpeedVo.getLatestPriceValue(), mHighLimitPrice), Toast.LENGTH_SHORT).show();
            else if (TextUtils.isEmpty(amount))
                Toast.makeText(mContext, R.string.transaction_number_error, Toast.LENGTH_SHORT).show();
            else if (new BigDecimal(amount).compareTo(new BigDecimal(0)) == 0)
                Toast.makeText(mContext, String.format(mContext.getResources().getString(R.string.transaction_entrust_less), 1), Toast.LENGTH_SHORT).show();
            else if (!mBinding.checkboxAgree.isChecked())
                Toast.makeText(mContext, R.string.transaction_condition_sheet_risk_agree, Toast.LENGTH_SHORT).show();
            else
                sendData(price, amount);
        }

    }

    private void hiddenKeyBoard() {
        ((InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                mBinding.tvContractName.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

}
