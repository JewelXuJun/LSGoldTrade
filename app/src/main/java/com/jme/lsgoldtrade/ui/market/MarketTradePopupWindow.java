package com.jme.lsgoldtrade.ui.market;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.jme.common.util.RxBus;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBasePopupWindow;
import com.jme.lsgoldtrade.config.AppConfig;
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
    private long mMinOrderQty = 0;
    private long mMaxOrderQty = 0;
    private long mMaxHoldQty = 0;
    private long mMaxAmount = 0;
    private int mBsFlag = 0;
    private int mOcFlag = 0;

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

            }
        });
    }

    public void setData(TenSpeedVo tenSpeedVo, AccountVo accountVo, PositionVo positionVo, ContractInfoVo contractInfoVo, String account, int bsFlag, int ocFlag) {
        mAccount = accountVo;
        mPositionVo = positionVo;
        mContractInfoVo = contractInfoVo;

        mBinding.tvTitle.setText(bsFlag == 1 ? mContext.getResources().getString(R.string.trade_buy_more_confirm)
                : mContext.getResources().getString(R.string.trade_sale_empty_confirm));
        mBinding.tvGoldAccount.setText(account);
        mBinding.tvBusinessType.setText(bsFlag == 1 ? mContext.getResources().getString(R.string.trade_buy_open)
                : mContext.getResources().getString(R.string.trade_sale_open));
        mBinding.etPrice.setText(bsFlag == 1 ? tenSpeedVo.getTenAskLists().get(9)[1] : tenSpeedVo.getTenBidLists().get(0)[1]);
        mBinding.etAmount.setText("1");

        mContractID = tenSpeedVo.getContractId();
        mPriceMove = new BigDecimal(contractInfoVo.getMinPriceMove()).divide(new BigDecimal(100)).floatValue();
        mLowerLimitPrice = tenSpeedVo.getLowerLimitPrice();
        mHighLimitPrice = tenSpeedVo.getHighLimitPrice();
        mMinOrderQty = mContractInfoVo.getMinOrderQty();
        mMaxOrderQty = mContractInfoVo.getMaxOrderQty();
        mMaxHoldQty = mContractInfoVo.getMaxHoldQty();
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
        String transactionBalance = mAccount.getTransactionBalanceStr();
        String positionMargin = null == mPositionVo ? "0" : mPositionVo.getPositionMargin();
        String price = mBinding.etPrice.getText().toString();
        long bankLongMarginRate = mContractInfoVo.getBankLongMarginRate();
        long bankShortMarginRate = mContractInfoVo.getBankShortMarginRate();
        long bankFeeRate = mContractInfoVo.getBankFeeRate();
        long exchangeFeeRate = mContractInfoVo.getExchangeFeeRate();
        long handWeight = MarketUtil.getHandWeight(mContractInfoVo.getHandWeight());
        long bankMarginRate = mBsFlag == 1 ? bankLongMarginRate : bankShortMarginRate;

        BigDecimal money = new BigDecimal(transactionBalance).add(new BigDecimal(positionMargin));
        BigDecimal contractMoney = new BigDecimal(price).multiply(new BigDecimal(mContractID.equals("Ag(T+D)") ?
                new BigDecimal(handWeight).divide(new BigDecimal(1000), 0, BigDecimal.ROUND_HALF_UP).longValue() : handWeight));
        BigDecimal feeRate = new BigDecimal(bankMarginRate).divide(new BigDecimal(10000))
                .add(new BigDecimal(bankFeeRate).divide(new BigDecimal(10000).divide(new BigDecimal(10000))))
                .add(new BigDecimal(exchangeFeeRate).divide(new BigDecimal(10000)).divide(new BigDecimal(10000)));

        mMaxAmount = Math.min(money.divide(contractMoney.multiply(feeRate), 0, BigDecimal.ROUND_HALF_DOWN).longValue(), mMaxOrderQty);

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
            Toast.makeText(mContext, R.string.trade_price_error, Toast.LENGTH_SHORT).show();
        else if (new BigDecimal(price).compareTo(new BigDecimal(mLowerLimitPrice)) == -1)
            Toast.makeText(mContext, R.string.trade_limit_down_price_error, Toast.LENGTH_SHORT).show();
        else if (new BigDecimal(price).compareTo(new BigDecimal(mHighLimitPrice)) == 1)
            Toast.makeText(mContext, R.string.trade_limit_up_price_error, Toast.LENGTH_SHORT).show();
        else if (TextUtils.isEmpty(amount))
            Toast.makeText(mContext, R.string.trade_number_error, Toast.LENGTH_SHORT).show();
        else if (new BigDecimal(amount).compareTo(new BigDecimal(mMaxAmount)) == 1)
            Toast.makeText(mContext, R.string.trade_limit_max_amount_error3, Toast.LENGTH_SHORT).show();
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
                Toast.makeText(mContext, R.string.trade_limit_down_price_error, Toast.LENGTH_SHORT).show();

                mBinding.etPrice.setText(price);
                mBinding.etPrice.setSelection(price.length());
            } else {
                String valueStr = MarketUtil.formatValue(String.valueOf(value), 2);

                mBinding.etPrice.setText(valueStr);
                mBinding.etPrice.setSelection(valueStr.length());
            }
        }

        public void onClickPriceAdd() {
            hiddenKeyBoard();

            String price = getPrice();

            if (TextUtils.isEmpty(price) || TextUtils.isEmpty(mHighLimitPrice))
                return;

            float value = new BigDecimal(price).add(new BigDecimal(mPriceMove)).floatValue();

            if (new BigDecimal(String.valueOf(value)).compareTo(new BigDecimal(mHighLimitPrice)) == 1) {
                Toast.makeText(mContext, R.string.trade_limit_up_price_error, Toast.LENGTH_SHORT).show();

                mBinding.etPrice.setText(price);
                mBinding.etPrice.setSelection(price.length());
            } else {
                String valueStr = MarketUtil.formatValue(String.valueOf(value), 2);

                mBinding.etPrice.setText(valueStr);
                mBinding.etPrice.setSelection(valueStr.length());
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
                    Toast.makeText(mContext, R.string.trade_number_error_zero, Toast.LENGTH_SHORT).show();
            } else {
                if (new BigDecimal(value).compareTo(new BigDecimal(mMinOrderQty)) == -1)
                    Toast.makeText(mContext, R.string.trade_limit_min_amount_error, Toast.LENGTH_SHORT).show();
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
                if (mMaxHoldQty == -1) {
                    mBinding.etAmount.setText(String.valueOf(value));
                } else {
                    if (new BigDecimal(value).compareTo(new BigDecimal(mMaxHoldQty)) == 1)
                        Toast.makeText(mContext, R.string.trade_limit_max_amount_error2, Toast.LENGTH_SHORT).show();
                    else
                        mBinding.etAmount.setText(String.valueOf(value));
                }
            } else {
                if (new BigDecimal(value).compareTo(new BigDecimal(mMaxOrderQty)) == 1)
                    Toast.makeText(mContext, R.string.trade_limit_max_amount_error, Toast.LENGTH_SHORT).show();
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