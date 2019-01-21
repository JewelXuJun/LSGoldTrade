package com.jme.lsgoldtrade.ui.market;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.jme.common.util.DensityUtil;
import com.jme.common.util.RxBus;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBasePopupWindow;
import com.jme.lsgoldtrade.config.AppConfig;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.config.Contract;
import com.jme.lsgoldtrade.databinding.PopupwindowMarketOrderBinding;
import com.jme.lsgoldtrade.domain.ContractInfoVo;
import com.jme.lsgoldtrade.domain.TenSpeedVo;
import com.jme.lsgoldtrade.util.MarketUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by XuJun on 2018/12/2.
 */
public class MarketOrderPopUpWindow extends JMEBasePopupWindow {

    private PopupwindowMarketOrderBinding mBinding;

    private TenSpeedVo mTenSpeedVo;
    private ContractInfoVo mContractInfoVo;

    private String mContractID;
    private String mLowerLimitPrice;
    private String mHighLimitPrice;
    private int mPriceType;
    private float mPriceMove = 0.00f;
    private long mMinOrderQty = 0;
    private long mMaxOrderQty = 0;

    private static int TYPE_NONE = 0;
    private static int TYPE_RIVALPRICE = 1;
    private static int TYPE_QUEUINGPRICE = 2;
    private static int TYPE_LASTPRICE = 3;

    public MarketOrderPopUpWindow(Context context, String contractID) {
        super(context);

        mContractID = contractID;
        mPriceType = TYPE_RIVALPRICE;
        mContractInfoVo = Contract.getInstance().getContractInfoFromID(mContractID);

        setContractData();
        setPriceTypeLayout();
    }

    @Override
    protected void initPopupWindow() {
        super.initPopupWindow();

        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(DensityUtil.dpTopx(getContext(), 230));
        setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    @Override
    public void iniWindow() {
        super.iniWindow();

        mBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.popupwindow_market_order, null, false);

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
                    if (s.length() - 1 - s.toString().indexOf(".") > AppConfig.Lentth_Limit) {
                        s = s.toString().subSequence(0, s.toString().indexOf(".") + (AppConfig.Lentth_Limit + 1));

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
                if (!TextUtils.isEmpty(s.toString()))
                    mPriceType = TYPE_NONE;

                setPriceTypeLayout();
            }
        });
    }

    public void setData(TenSpeedVo tenSpeedVo) {
        if (null == tenSpeedVo)
            return;

        mTenSpeedVo = tenSpeedVo;
        mLowerLimitPrice = tenSpeedVo.getLowerLimitPrice();
        mHighLimitPrice = tenSpeedVo.getHighLimitPrice();

        mBinding.tvLimitDownPrice.setText(mLowerLimitPrice);
        mBinding.tvLimitUpPrice.setText(mHighLimitPrice);

        setPriceData();
    }

    private void setContractData() {
        if (null == mContractInfoVo) {
            mPriceMove = 0;
            mMinOrderQty = 0;
            mMaxOrderQty = 0;
        } else {
            mPriceMove = new BigDecimal(mContractInfoVo.getMinPriceMove()).divide(new BigDecimal(100)).floatValue();
            mMinOrderQty = mContractInfoVo.getMinOrderQty();
            mMaxOrderQty = mContractInfoVo.getMaxOrderQty();

            mBinding.etAmount.setText(String.valueOf(mMinOrderQty));
        }
    }

    private void setPriceTypeLayout() {
        mBinding.btnRivalPrice.setBackground(mPriceType == TYPE_RIVALPRICE
                ? ContextCompat.getDrawable(mContext, R.drawable.bg_btn_blue_solid)
                : ContextCompat.getDrawable(mContext, R.drawable.bg_btn_blue_hollow));
        mBinding.btnRivalPrice.setTextColor(mPriceType == TYPE_RIVALPRICE
                ? ContextCompat.getColor(mContext, R.color.white)
                : ContextCompat.getColor(mContext, R.color.color_blue));
        mBinding.btnQueuingPrice.setBackground(mPriceType == TYPE_QUEUINGPRICE
                ? ContextCompat.getDrawable(mContext, R.drawable.bg_btn_blue_solid)
                : ContextCompat.getDrawable(mContext, R.drawable.bg_btn_blue_hollow));
        mBinding.btnQueuingPrice.setTextColor(mPriceType == TYPE_QUEUINGPRICE
                ? ContextCompat.getColor(mContext, R.color.white)
                : ContextCompat.getColor(mContext, R.color.color_blue));
        mBinding.btnLastPrice.setBackground(mPriceType == TYPE_LASTPRICE
                ? ContextCompat.getDrawable(mContext, R.drawable.bg_btn_blue_solid)
                : ContextCompat.getDrawable(mContext, R.drawable.bg_btn_blue_hollow));
        mBinding.btnLastPrice.setTextColor(mPriceType == TYPE_LASTPRICE
                ? ContextCompat.getColor(mContext, R.color.white)
                : ContextCompat.getColor(mContext, R.color.color_blue));

        if (mPriceType == TYPE_RIVALPRICE)
            mBinding.etPrice.setHint(R.string.market_rival_price);
        else if (mPriceType == TYPE_QUEUINGPRICE)
            mBinding.etPrice.setHint(R.string.market_queuing_price);
        else if (mPriceType == TYPE_LASTPRICE)
            mBinding.etPrice.setHint(R.string.market_last_price);
        else
            mBinding.etPrice.setHint(R.string.market_price_hint);

        setPriceData();
    }

    private void setPriceData() {
        if (null == mTenSpeedVo) {
            mBinding.tvPriceBuyMore.setText(R.string.text_no_data_default);
            mBinding.tvPriceSaleEmpty.setText(R.string.text_no_data_default);
            mBinding.tvPriceEqualMore.setText(R.string.text_no_data_default);
            mBinding.tvPriceEqualEmpty.setText(R.string.text_no_data_default);
        } else {
            List<String[]> askLists = mTenSpeedVo.getAskLists();
            List<String[]> bidLists = mTenSpeedVo.getBidLists();

            if (mPriceType == TYPE_NONE) {
                String price = MarketUtil.formatValue(mBinding.etPrice.getText().toString(), 2);
                String priceStr = TextUtils.isEmpty(price) ? mContext.getResources().getString(R.string.text_no_data_default) : price;

                mBinding.tvPriceBuyMore.setText(priceStr);
                mBinding.tvPriceSaleEmpty.setText(priceStr);
                mBinding.tvPriceEqualMore.setText(priceStr);
                mBinding.tvPriceEqualEmpty.setText(priceStr);
            } else if (mPriceType == TYPE_RIVALPRICE) {
                mBinding.tvPriceBuyMore.setText(MarketUtil.formatValue(askLists.get(9)[1], 2));
                mBinding.tvPriceSaleEmpty.setText(MarketUtil.formatValue(bidLists.get(0)[1], 2));
                mBinding.tvPriceEqualMore.setText(MarketUtil.formatValue(bidLists.get(0)[1], 2));
                mBinding.tvPriceEqualEmpty.setText(MarketUtil.formatValue(askLists.get(9)[1], 2));
            } else if (mPriceType == TYPE_QUEUINGPRICE) {
                mBinding.tvPriceBuyMore.setText(MarketUtil.formatValue(bidLists.get(0)[1], 2));
                mBinding.tvPriceSaleEmpty.setText(MarketUtil.formatValue(askLists.get(9)[1], 2));
                mBinding.tvPriceEqualMore.setText(MarketUtil.formatValue(askLists.get(9)[1], 2));
                mBinding.tvPriceEqualEmpty.setText(MarketUtil.formatValue(bidLists.get(0)[1], 2));
            } else if (mPriceType == TYPE_LASTPRICE) {
                String latestPrice = MarketUtil.formatValue(mTenSpeedVo.getLatestPrice(), 2);

                mBinding.tvPriceBuyMore.setText(latestPrice);
                mBinding.tvPriceSaleEmpty.setText(latestPrice);
                mBinding.tvPriceEqualMore.setText(latestPrice);
                mBinding.tvPriceEqualEmpty.setText(latestPrice);
            } else {
                mBinding.tvPriceBuyMore.setText(R.string.text_no_data_default);
                mBinding.tvPriceSaleEmpty.setText(R.string.text_no_data_default);
                mBinding.tvPriceEqualMore.setText(R.string.text_no_data_default);
                mBinding.tvPriceEqualEmpty.setText(R.string.text_no_data_default);
            }
        }
    }

    private String getPrice() {
        String price = mBinding.etPrice.getText().toString();

        if (TextUtils.isEmpty(price))
            price = null == mTenSpeedVo ? "" : mTenSpeedVo.getLatestPrice();

        if (TextUtils.isEmpty(price) || price.equals(mContext.getResources().getString(R.string.text_no_data_default)))
            return "";

        if (price.endsWith("."))
            price = price.substring(0, price.length() - 1);

        return price;
    }

    private void sendData(String contractID, String price, String amount, int bsFlag, int ocFlag) {
        dismiss();

        List<String> list = new ArrayList<>();
        list.add(contractID);
        list.add(price);
        list.add(amount);
        list.add(String.valueOf(bsFlag));
        list.add(String.valueOf(ocFlag));

        RxBus.getInstance().post(Constants.RxBusConst.RXBUS_MARKETDETAIL_QUICK, list);
    }

    private void doTrade(String price, String amount, int bsFlag, int ocFlag) {
        if (TextUtils.isEmpty(mContractID))
            Toast.makeText(mContext, R.string.trade_contract_error, Toast.LENGTH_SHORT).show();
        else if (TextUtils.isEmpty(price) || price.equals(mContext.getResources().getString(R.string.text_no_data_default)))
            Toast.makeText(mContext, R.string.trade_price_error, Toast.LENGTH_SHORT).show();
        else if (new BigDecimal(price).compareTo(new BigDecimal(mLowerLimitPrice)) == -1)
            Toast.makeText(mContext, R.string.trade_limit_down_price_error, Toast.LENGTH_SHORT).show();
        else if (new BigDecimal(price).compareTo(new BigDecimal(mHighLimitPrice)) == 1)
            Toast.makeText(mContext, R.string.trade_limit_up_price_error, Toast.LENGTH_SHORT).show();
        else if (TextUtils.isEmpty(amount))
            Toast.makeText(mContext, R.string.trade_number_error, Toast.LENGTH_SHORT).show();
        else if (new BigDecimal(amount).compareTo(new BigDecimal(mMinOrderQty)) == -1)
            Toast.makeText(mContext, R.string.trade_limit_min_amount_error, Toast.LENGTH_SHORT).show();
        else if (new BigDecimal(amount).compareTo(new BigDecimal(mMaxOrderQty)) == 1)
            Toast.makeText(mContext, R.string.trade_limit_max_amount_error, Toast.LENGTH_SHORT).show();
        else
            sendData(mContractID, price, amount, bsFlag, ocFlag);
    }

    public class ClickHandlers {

        public void onClickCancel() {
            dismiss();
        }

        public void onClickLimitDownPrice() {
            mPriceType = TYPE_NONE;
            mBinding.etPrice.setText(mLowerLimitPrice);

            hiddenKeyBoard();
            setPriceTypeLayout();
        }

        public void onClickLimitUpPrice() {
            mPriceType = TYPE_NONE;
            mBinding.etPrice.setText(mHighLimitPrice);

            hiddenKeyBoard();
            setPriceTypeLayout();
        }

        public void onClickPriceMinus() {
            hiddenKeyBoard();

            String price = getPrice();

            if (TextUtils.isEmpty(price) || TextUtils.isEmpty(mLowerLimitPrice))
                return;

            float value = new BigDecimal(price).subtract(new BigDecimal(mPriceMove)).floatValue();

            if (new BigDecimal(String.valueOf(value)).compareTo(new BigDecimal(mLowerLimitPrice)) == -1) {
                Toast.makeText(mContext, R.string.trade_limit_down_price_error, Toast.LENGTH_SHORT).show();

                mBinding.etPrice.setSelection(price.length());
                mBinding.tvPriceBuyMore.setText(MarketUtil.formatValue(price, 2));
                mBinding.tvPriceSaleEmpty.setText(MarketUtil.formatValue(price, 2));
                mBinding.tvPriceEqualMore.setText(MarketUtil.formatValue(price, 2));
                mBinding.tvPriceEqualEmpty.setText(MarketUtil.formatValue(price, 2));
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

                mBinding.etPrice.setSelection(price.length());
                mBinding.tvPriceBuyMore.setText(MarketUtil.formatValue(price, 2));
                mBinding.tvPriceSaleEmpty.setText(MarketUtil.formatValue(price, 2));
                mBinding.tvPriceEqualMore.setText(MarketUtil.formatValue(price, 2));
                mBinding.tvPriceEqualEmpty.setText(MarketUtil.formatValue(price, 2));
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

            if (new BigDecimal(value).compareTo(new BigDecimal(mMinOrderQty)) == -1)
                Toast.makeText(mContext, R.string.trade_limit_min_amount_error, Toast.LENGTH_SHORT).show();
            else
                mBinding.etAmount.setText(String.valueOf(value));
        }

        public void onClickAmountAdd() {
            hiddenKeyBoard();

            String amount = mBinding.etAmount.getText().toString();

            if (TextUtils.isEmpty(amount))
                amount = "0";

            long value = new BigDecimal(amount).add(new BigDecimal(1)).longValue();

            if (new BigDecimal(value).compareTo(new BigDecimal(mMaxOrderQty)) == 1)
                Toast.makeText(mContext, R.string.trade_limit_max_amount_error, Toast.LENGTH_SHORT).show();
            else
                mBinding.etAmount.setText(String.valueOf(value));
        }

        public void onClickRivalPrice() {
            hiddenKeyBoard();

            if (mPriceType == TYPE_RIVALPRICE)
                return;

            mPriceType = TYPE_RIVALPRICE;
            mBinding.etPrice.setText("");
            mBinding.etPrice.clearFocus();

            setPriceTypeLayout();
        }

        public void onClickQueuingPrice() {
            hiddenKeyBoard();

            if (mPriceType == TYPE_QUEUINGPRICE)
                return;

            mPriceType = TYPE_QUEUINGPRICE;
            mBinding.etPrice.setText("");
            mBinding.etPrice.clearFocus();

            setPriceTypeLayout();
        }

        public void onClickLastPrice() {
            hiddenKeyBoard();

            if (mPriceType == TYPE_LASTPRICE)
                return;

            mPriceType = TYPE_LASTPRICE;
            mBinding.etPrice.setText("");
            mBinding.etPrice.clearFocus();

            setPriceTypeLayout();
        }


        public void onClickBuyMore() {
            hiddenKeyBoard();

            doTrade(mBinding.tvPriceBuyMore.getText().toString(), mBinding.etAmount.getText().toString(), 1, 0);
        }

        public void onClickSaleEmpty() {
            hiddenKeyBoard();

            doTrade(mBinding.tvPriceSaleEmpty.getText().toString(), mBinding.etAmount.getText().toString(), 2, 0);
        }

        public void onClickEqualMore() {
            hiddenKeyBoard();

            doTrade(mBinding.tvPriceEqualMore.getText().toString(), mBinding.etAmount.getText().toString(), 2, 1);
        }

        public void onClickEqualEmpty() {
            hiddenKeyBoard();

            doTrade(mBinding.tvPriceEqualEmpty.getText().toString(), mBinding.etAmount.getText().toString(), 1, 1);
        }

    }

    private void hiddenKeyBoard() {
        ((InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                mBinding.etAmount.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

}
