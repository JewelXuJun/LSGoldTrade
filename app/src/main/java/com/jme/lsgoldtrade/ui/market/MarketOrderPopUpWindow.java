package com.jme.lsgoldtrade.ui.market;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.jme.common.util.RxBus;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBasePopupWindow;
import com.jme.lsgoldtrade.config.AppConfig;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.config.Contract;
import com.jme.lsgoldtrade.config.User;
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
    private String value;

    private String mContractID;
    private String mLowerLimitPrice;
    private String mHighLimitPrice;
    private int mPriceType;
    private int i;
    private float mPriceMove = 0.00f;
    private long mMinOrderQty = 0;
    private long mMaxOrderQty = 0;

    private static int TYPE_NONE = 0;
    private static int TYPE_RIVALPRICE = 1;
    private static int TYPE_QUEUINGPRICE = 2;
    private static int TYPE_LASTPRICE = 3;

    public MarketOrderPopUpWindow(Context context, String contractID, int i) {
        super(context);

        this.i = i;
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
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
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
        mBinding.gonghangUsername.setText(User.getInstance().getAccount());

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
                if (!TextUtils.isEmpty(s.toString()))
                    mPriceType = TYPE_NONE;

                setPriceTypeLayout();
            }
        });
    }

    public void setData(TenSpeedVo tenSpeedVo, String value) {
        if (null == tenSpeedVo)
            return;

        this.value = value;
        mTenSpeedVo = tenSpeedVo;
        mBinding.num.setText("最大交易手数:\n" + value.substring(0, value.lastIndexOf(".")));

        mLowerLimitPrice = tenSpeedVo.getLowerLimitPrice();
        mHighLimitPrice = tenSpeedVo.getHighLimitPrice();


    }

    private void setContractData() {
        if (i == 1) {
            mBinding.title.setText("买入开仓");
            mBinding.businessType.setText("买入开仓");
        } else if (i == 2) {
            mBinding.title.setText("卖出开仓");
            mBinding.businessType.setText("卖出开仓");
        }
        if (null == mContractInfoVo) {
            mPriceMove = 0;
            mMinOrderQty = 0;
            mMaxOrderQty = 0;
        } else {
            mPriceMove = new BigDecimal(mContractInfoVo.getMinPriceMove()).divide(new BigDecimal(100)).floatValue();
            mMinOrderQty = mContractInfoVo.getMinOrderQty();
            mMaxOrderQty = mContractInfoVo.getMaxOrderQty();

            mBinding.etAmount.setText(mMinOrderQty == -1 ? "1" : String.valueOf(mMinOrderQty));
        }
    }

    private void setPriceTypeLayout() {
        mBinding.etPrice.setHint(R.string.market_rival_price);
    }

    private String getPrice() {
        String price = mBinding.etPrice.getText().toString();

        if (!TextUtils.isEmpty(price)) {
            if (price.endsWith("."))
                return price = price.substring(0, price.length() - 1);
//            price = null == mTenSpeedVo ? "" : mTenSpeedVo.getLatestPrice();
        } else{

//        if (TextUtils.isEmpty(price) || price.equals(mContext.getResources().getString(R.string.text_no_data_default)))
//            return "";

            if (i == 1) {
                List<String[]> askLists = mTenSpeedVo.getAskLists();
                price = askLists.get(9)[1];
//            mBinding.etPrice.setText(askLists.get(9)[1]);
            } else {
                List<String[]> bidLists = mTenSpeedVo.getBidLists();
                price = bidLists.get(0)[1];
//            mBinding.etPrice.setText(bidLists.get(0)[1]);
            }

            if (price.endsWith("."))
                return price = price.substring(0, price.length() - 1);
        }

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
        else if (new BigDecimal(amount).compareTo(new BigDecimal(0)) == 0)
            Toast.makeText(mContext, R.string.trade_number_error_zero, Toast.LENGTH_SHORT).show();
        else if (mMinOrderQty != -1 && new BigDecimal(amount).compareTo(new BigDecimal(mMinOrderQty)) == -1)
            Toast.makeText(mContext, R.string.trade_limit_min_amount_error, Toast.LENGTH_SHORT).show();
        else if (mMaxOrderQty != -1 && new BigDecimal(amount).compareTo(new BigDecimal(mMaxOrderQty)) == 1)
            Toast.makeText(mContext, R.string.trade_limit_max_amount_error, Toast.LENGTH_SHORT).show();
        else
            sendData(mContractID, price, amount, bsFlag, ocFlag);
    }

    public class ClickHandlers {

        public void onClickCancel() {
            dismiss();
        }

        public void onClickChange() {

            String price = getPrice();
            if (TextUtils.isEmpty(price)) {
                Toast.makeText(mContext, "请输入金额", Toast.LENGTH_SHORT).show();
                return;
            }
            String amount = mBinding.etAmount.getText().toString().trim();
            if (TextUtils.isEmpty(amount) || "0".equals(amount)) {
                Toast.makeText(mContext, "请输入手数", Toast.LENGTH_SHORT).show();
                return;
            }

            List<String> list = new ArrayList<>();
            list.add(mContractID);
            list.add(price);
            list.add(amount);
            list.add(String.valueOf(i));
            list.add(String.valueOf(0));

            RxBus.getInstance().post(Constants.RxBusConst.RXBUS_MARKETDETAIL_QUICK, list);
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

            if (mMinOrderQty != -1 && new BigDecimal(value).compareTo(new BigDecimal(mMinOrderQty)) == -1)
                Toast.makeText(mContext, R.string.trade_limit_min_amount_error, Toast.LENGTH_SHORT).show();
            else{
                if (value < 0) {
                    mBinding.etAmount.setText("1");
                } else {
                    mBinding.etAmount.setText(String.valueOf(value));
                }
            }
        }

        public void onClickAmountAdd() {
            hiddenKeyBoard();

            String amount = mBinding.etAmount.getText().toString();

            if (TextUtils.isEmpty(amount))
                amount = "0";

            long value = new BigDecimal(amount).add(new BigDecimal(1)).longValue();

            if (mMaxOrderQty != -1 && new BigDecimal(value).compareTo(new BigDecimal(mMaxOrderQty)) == 1)
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
    }

    private void hiddenKeyBoard() {
        ((InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                mBinding.etAmount.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

}
