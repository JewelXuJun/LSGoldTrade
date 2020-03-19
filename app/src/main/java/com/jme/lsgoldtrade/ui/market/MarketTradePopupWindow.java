package com.jme.lsgoldtrade.ui.market;

import android.content.Context;

import androidx.databinding.DataBindingUtil;

import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.jme.common.util.RxBus;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBasePopupWindow;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.PopupwindowMarketTradeBinding;
import com.jme.lsgoldtrade.domain.AccountVo;
import com.jme.lsgoldtrade.domain.ContractInfoVo;
import com.jme.lsgoldtrade.domain.PositionVo;
import com.jme.lsgoldtrade.domain.TenSpeedVo;
import com.jme.lsgoldtrade.util.MarketUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class MarketTradePopupWindow extends JMEBasePopupWindow {

    private PopupwindowMarketTradeBinding mBinding;

    private Context mContext;

    private AccountVo mAccount;
    private PositionVo mPositionVo;
    private ContractInfoVo mContractInfoVo;

    private float mPriceMove = 0.00f;
    private String mContractID;
    private String mLowerLimitPrice;
    private String mHighLimitPrice;
    private long mPositionMargin = 0;
    private long mMinOrderQty = 0;
    private long mMaxOrderQty = 0;
    private long mMaxHoldQty = 0;
    private long mMaxAmount = 0;
    private int mBsFlag = 0;
    private int mOcFlag = 0;
    private int mLength = 2;

    public MarketTradePopupWindow(Context context) {
        super(context);

        mContext = context;
    }

    @Override
    protected void initPopupWindow() {
        super.initPopupWindow();

        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
    }

    @Override
    public void iniWindow() {
        super.iniWindow();

        mBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.popupwindow_market_trade, null, false);

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
                if (s.toString().contains(".") && !s.toString().equals(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > mLength) {
                        s = s.toString().subSequence(0, s.toString().indexOf(".") + (mLength + 1));

                        if (s.toString().endsWith("."))
                            s = s.toString().substring(0, s.toString().length() - 1);

                        mBinding.etPrice.setText(s);
                        mBinding.etPrice.setSelection(s.length());
                    } else {
                        mBinding.etPrice.setSelection(s.length());
                    }
                } else if (s.toString().trim().equals(".")) {
                    s = "0" + s;

                    mBinding.etPrice.setText(s);
                    mBinding.etPrice.setSelection(2);
                } else if (s.toString().startsWith("0") && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        mBinding.etPrice.setText(s.subSequence(0, 1));
                        mBinding.etPrice.setSelection(1);

                        return;
                    }
                } else {
                    mBinding.etPrice.setSelection(s.length());
                }

                calculateMaxAmount();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void setData(TenSpeedVo tenSpeedVo, AccountVo accountVo, PositionVo positionVo, ContractInfoVo contractInfoVo, String account,
                        long positionMargin, int bsFlag, int ocFlag) {
        mAccount = accountVo;
        mPositionVo = positionVo;
        mContractInfoVo = contractInfoVo;
        mContractID = tenSpeedVo.getContractId();
        mLength = mContractID.equals("Ag(T+D)") ? 0 : 2;

        mBinding.tvTitle.setText(bsFlag == 1 ? mContext.getResources().getString(R.string.transaction_buy_more_confirm)
                : mContext.getResources().getString(R.string.transaction_sale_empty_confirm));
        mBinding.tvGoldAccount.setText(account);
        mBinding.tvBusinessType.setText(bsFlag == 1 ? mContext.getResources().getString(R.string.transaction_buy_open)
                : mContext.getResources().getString(R.string.transaction_sale_open));
        mBinding.etPrice.setText(bsFlag == 1 ? tenSpeedVo.getTenAskLists().get(9)[1] : tenSpeedVo.getTenBidLists().get(0)[1]);
        mBinding.etPrice.setInputType(mContractID.equals("Ag(T+D)") ? InputType.TYPE_CLASS_NUMBER : EditorInfo.TYPE_CLASS_NUMBER | EditorInfo.TYPE_NUMBER_FLAG_DECIMAL);
        mBinding.etAmount.setText("1");

        mPriceMove = null == mContractInfoVo ? 0 : new BigDecimal(mContractInfoVo.getMinPriceMove()).divide(new BigDecimal(100)).floatValue();
        mLowerLimitPrice = tenSpeedVo.getLowerLimitPrice();
        mHighLimitPrice = tenSpeedVo.getHighLimitPrice();
        mMinOrderQty = null == mContractInfoVo ? 0 : mContractInfoVo.getMinOrderQty();
        mMaxOrderQty = null == mContractInfoVo ? 0 : mContractInfoVo.getMaxOrderQty();
        mMaxHoldQty = null == mContractInfoVo ? 0 : mContractInfoVo.getMaxHoldQty();
        mPositionMargin = positionMargin;
        mBsFlag = bsFlag;
        mOcFlag = ocFlag;

        calculateMaxAmount();
    }

    private String getPrice() {
        String price = mBinding.etPrice.getText().toString();

        if (TextUtils.isEmpty(price) || price.equals(mContext.getResources().getString(R.string.text_no_data_default)))
            return "";

        if (price.endsWith("."))
            price = price.substring(0, price.length() - 1);

        return price;
    }

    private void calculateMaxAmount() {
        if (TextUtils.isEmpty(mContractID) || null == mAccount || null == mContractInfoVo) {
            mMaxAmount = 0;
        } else {
            String price = mBinding.etPrice.getText().toString();

            if (TextUtils.isEmpty(price)) {
                mMaxAmount = mMaxOrderQty;
            } else {
                if (price.endsWith("."))
                    price = price.substring(0, price.length() - 1);

                if (new BigDecimal(price).compareTo(new BigDecimal(0)) == 0) {
                    mMaxAmount = mMaxOrderQty;
                } else {
                    String transactionBalance = mAccount.getTransactionBalanceStr();

                    long bankLongMarginRate = mContractInfoVo.getBankLongMarginRate();
                    long bankShortMarginRate = mContractInfoVo.getBankShortMarginRate();
                    long bankFeeRate = mContractInfoVo.getBankFeeRate();
                    long exchangeFeeRate = mContractInfoVo.getExchangeFeeRate();
                    long handWeight = MarketUtil.getHandWeight(mContractInfoVo.getHandWeight());
                    long handWeightValue = mContractID.equals("Ag(T+D)") ?
                            new BigDecimal(handWeight).divide(new BigDecimal(1000), 0, BigDecimal.ROUND_HALF_UP).longValue() : handWeight;
                    long bankMarginRate = mBsFlag == 1 ? bankLongMarginRate : bankShortMarginRate;

                    BigDecimal bankFeeRateValue = new BigDecimal(bankFeeRate).divide(new BigDecimal(10000)).divide(new BigDecimal(10000));
                    BigDecimal exchangeFeeRateValue = new BigDecimal(exchangeFeeRate).divide(new BigDecimal(10000)).divide(new BigDecimal(10000));
                    BigDecimal feeRate = bankFeeRateValue.add(exchangeFeeRateValue);
                    BigDecimal bankMarginRateValue = new BigDecimal(bankMarginRate).divide(new BigDecimal(10000));
                    BigDecimal handWeightMoney = new BigDecimal(price).multiply(new BigDecimal(handWeightValue));
                    BigDecimal fee = handWeightMoney.multiply(feeRate);
                    BigDecimal contractFee = bankMarginRateValue.add(bankFeeRateValue).add(exchangeFeeRateValue);
                    BigDecimal money = new BigDecimal(transactionBalance).add(new BigDecimal(MarketUtil.getPriceValue(mPositionMargin)));
                    BigDecimal contractMoney = handWeightMoney.multiply(contractFee);
                    BigDecimal totalAmount = money.divide(contractMoney, 0, BigDecimal.ROUND_DOWN);

                    mMaxAmount = (new BigDecimal(Math.min(totalAmount.longValue(), mMaxOrderQty))
                            .subtract(new BigDecimal(null == mPositionVo ? 0 : mPositionVo.getPosition()))).longValue();

                    if (new BigDecimal(mMaxAmount).compareTo(new BigDecimal(0)) == -1)
                        mMaxAmount = 0;
                }
            }
        }

        mBinding.tvMaxAmount.setText(String.valueOf(mMaxAmount));

    }

    private void sendData(String price, String amount) {
        dismiss();

        List<String> list = new ArrayList<>();
        list.add(mContractID);
        list.add(price);
        list.add(amount);
        list.add(String.valueOf(mBsFlag));
        list.add(String.valueOf(mOcFlag));

        RxBus.getInstance().post(Constants.RxBusConst.RXBUS_MARKETDETAIL_QUICK, list);
    }

    private void doTrade() {
        String price = mBinding.etPrice.getText().toString();
        String amount = mBinding.etAmount.getText().toString();

        if (TextUtils.isEmpty(price))
            Toast.makeText(mContext, R.string.transaction_price_error, Toast.LENGTH_SHORT).show();
        else if (new BigDecimal(price).compareTo(new BigDecimal(mLowerLimitPrice)) == -1)
            Toast.makeText(mContext, R.string.transaction_limit_down_price_error, Toast.LENGTH_SHORT).show();
        else if (new BigDecimal(price).compareTo(new BigDecimal(mHighLimitPrice)) == 1)
            Toast.makeText(mContext, R.string.transaction_limit_up_price_error, Toast.LENGTH_SHORT).show();
        else if (TextUtils.isEmpty(amount))
            Toast.makeText(mContext, R.string.transaction_number_error, Toast.LENGTH_SHORT).show();
        else if (new BigDecimal(amount).compareTo(new BigDecimal(0)) == 0)
            Toast.makeText(mContext, R.string.transaction_number_error_zero, Toast.LENGTH_SHORT).show();
        else if (mMinOrderQty != -1 && new BigDecimal(amount).compareTo(new BigDecimal(mMinOrderQty)) == -1)
            Toast.makeText(mContext, R.string.transaction_limit_min_amount_error, Toast.LENGTH_SHORT).show();
        else if (mMaxOrderQty == -1 && new BigDecimal(amount).compareTo(new BigDecimal(mMaxHoldQty == -1 ? mMaxAmount : Math.min(mMaxAmount, mMaxHoldQty))) == 1)
            Toast.makeText(mContext, R.string.transaction_limit_max_amount_error_canbuy, Toast.LENGTH_SHORT).show();
        else if (mMaxOrderQty != -1 && new BigDecimal(amount).compareTo(new BigDecimal(Math.min(mMaxAmount, mMaxOrderQty))) == 1)
            Toast.makeText(mContext, R.string.transaction_limit_max_amount_error_canbuy, Toast.LENGTH_SHORT).show();
        else
            sendData(price, amount);
    }

    public class ClickHandlers {

        public void onClickCancel() {
            dismiss();
        }

        public void onClickPriceMinus() {
            hiddenKeyBoard();

            String price = getPrice();

            if (TextUtils.isEmpty(price) || TextUtils.isEmpty(mLowerLimitPrice))
                return;

            float value = new BigDecimal(price).subtract(new BigDecimal(mPriceMove)).floatValue();

            if (new BigDecimal(String.valueOf(value)).compareTo(new BigDecimal(mLowerLimitPrice)) == -1) {
                Toast.makeText(mContext, R.string.transaction_limit_down_price_error, Toast.LENGTH_SHORT).show();

                mBinding.etPrice.setText(price);
            } else {
                String valueStr = MarketUtil.formatValue(String.valueOf(value), mLength);

                mBinding.etPrice.setText(valueStr);
            }
        }

        public void onClickPriceAdd() {
            hiddenKeyBoard();

            String price = getPrice();

            if (TextUtils.isEmpty(price) || TextUtils.isEmpty(mHighLimitPrice))
                return;

            float value = new BigDecimal(price).add(new BigDecimal(mPriceMove)).floatValue();

            if (new BigDecimal(String.valueOf(value)).compareTo(new BigDecimal(mHighLimitPrice)) == 1) {
                Toast.makeText(mContext, R.string.transaction_limit_up_price_error, Toast.LENGTH_SHORT).show();

                mBinding.etPrice.setText(price);
            } else {
                String valueStr = MarketUtil.formatValue(String.valueOf(value), mLength);

                mBinding.etPrice.setText(valueStr);
            }
        }

        public void onClickAmountMinus() {
            hiddenKeyBoard();

            String amount = mBinding.etAmount.getText().toString();

            if (TextUtils.isEmpty(amount))
                amount = "0";

            long value = new BigDecimal(amount).subtract(new BigDecimal(1)).longValue();

            if (mMinOrderQty == -1) {
                if (new BigDecimal(value).compareTo(new BigDecimal(0)) == 1)
                    mBinding.etAmount.setText(String.valueOf(value));
                else
                    Toast.makeText(mContext, R.string.transaction_number_error_zero, Toast.LENGTH_SHORT).show();
            } else {
                if (new BigDecimal(value).compareTo(new BigDecimal(mMinOrderQty)) == -1)
                    Toast.makeText(mContext, R.string.transaction_limit_min_amount_error, Toast.LENGTH_SHORT).show();
                else
                    mBinding.etAmount.setText(String.valueOf(value));
            }
        }

        public void onClickAmountAdd() {
            hiddenKeyBoard();

            String amount = mBinding.etAmount.getText().toString();

            if (TextUtils.isEmpty(amount))
                amount = "0";

            long value = new BigDecimal(amount).add(new BigDecimal(1)).longValue();

            if (mMaxOrderQty == -1) {
                if (new BigDecimal(value).compareTo(new BigDecimal(mMaxHoldQty == -1 ? mMaxAmount : Math.min(mMaxAmount, mMaxHoldQty))) == 1)
                    Toast.makeText(mContext, R.string.transaction_limit_max_amount_error_canbuy, Toast.LENGTH_SHORT).show();
                else
                    mBinding.etAmount.setText(String.valueOf(value));
            } else {
                if (new BigDecimal(value).compareTo(new BigDecimal(Math.min(mMaxAmount, mMaxOrderQty))) == 1)
                    Toast.makeText(mContext, R.string.transaction_limit_max_amount_error_canbuy, Toast.LENGTH_SHORT).show();
                else
                    mBinding.etAmount.setText(String.valueOf(value));
            }
        }

        public void onClickTrade() {
            doTrade();
        }

    }

    private void hiddenKeyBoard() {
        ((InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                mBinding.etAmount.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

}
