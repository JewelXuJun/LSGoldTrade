package com.jme.lsgoldtrade.view;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBasePopupWindow;
import com.jme.lsgoldtrade.config.AppConfig;
import com.jme.lsgoldtrade.databinding.PopupwindowHangqingBinding;
import com.jme.lsgoldtrade.util.MarketUtil;

import java.math.BigDecimal;

public class HangQingWindow extends JMEBasePopupWindow {

    private PopupwindowHangqingBinding mBinding;

    private float mPriceMove = 0.00f;

    public HangQingWindow(Context context) {
        super(context);
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

        mBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.popupwindow_hangqing, null, false);

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

    private String amount;

    public void setData(String account, String code, String price, String amount, String type, String ocFlag, String value, View.OnClickListener confirmListener) {
        this.amount = amount;
        mBinding.backUsername.setText(account);
        mBinding.etPrice.setText(price);
        mBinding.etAmount.setText(amount);

        mBinding.maxAmout.setText("约可交易手数:\n" + value);

        mBinding.btnConfirm.setOnClickListener(confirmListener);

        if ("0".equals(ocFlag)) {
            if ("1".equals(type)) {
                mBinding.title.setText("买多报单确认");
                mBinding.businessType.setText("买入开仓");
            } else if ("2".equals(type)) {
                mBinding.title.setText("卖空报单确认");
                mBinding.businessType.setText("卖出开仓");
            }
            mBinding.maxAmout.setVisibility(View.VISIBLE);
            mBinding.llVariety.setVisibility(View.GONE);
        } else {
            if ("1".equals(type)) {
                mBinding.title.setText("平仓报单确认");
                mBinding.businessType.setText("买多平仓");
            } else if ("2".equals(type)) {
                mBinding.title.setText("平仓报单确认");
                mBinding.businessType.setText("卖空平仓");
            }
            mBinding.maxAmout.setVisibility(View.GONE);
            mBinding.llVariety.setVisibility(View.VISIBLE);
            mBinding.variety.setText(code);
        }

        if ("Ag(T+D)".equals(code)) {
            mPriceMove = new BigDecimal(100).divide(new BigDecimal(100)).floatValue();
        } else {
            mPriceMove = new BigDecimal(1).divide(new BigDecimal(100)).floatValue();
        }
    }

    public class ClickHandlers {

        public void onClickCancel() {
            dismiss();
        }

        public void onClickPriceMinus() {
            //委托开仓价格
            hiddenKeyBoard();

            String price = getPrice(mBinding.etPrice);

            if (TextUtils.isEmpty(price))
                return;

            float value = new BigDecimal(price).subtract(new BigDecimal(mPriceMove)).floatValue();

            if (new BigDecimal(String.valueOf(value)).compareTo(new BigDecimal(0)) == -1) {
                mBinding.etPrice.setText(price);
                mBinding.etPrice.setSelection(price.length());
            } else {
                String valueStr = MarketUtil.formatValue(String.valueOf(value), 2);

                mBinding.etPrice.setText(valueStr);
                mBinding.etPrice.setSelection(valueStr.length());
            }
        }

        public void onClickPriceAdd() {
            //委托开仓价格
            hiddenKeyBoard();

            String price = getPrice(mBinding.etPrice).replace(",", "");

            if (TextUtils.isEmpty(price))
                return;

            float value = new BigDecimal(price).add(new BigDecimal(mPriceMove)).floatValue();

            String valueStr = MarketUtil.formatValue(String.valueOf(value), 2);

            mBinding.etPrice.setText(valueStr);
            mBinding.etPrice.setSelection(valueStr.length());
        }

        public void onClickAmountMinus() {
            //委托开仓手数
            hiddenKeyBoard();

            if (TextUtils.isEmpty(amount))
                amount = "0";

            long value = new BigDecimal(amount).subtract(new BigDecimal(1)).longValue();

            if (value < 1) {
//                showShortToast(R.string.trade_number_error_zero);
            } else {
                mBinding.etAmount.setText(String.valueOf(value));
            }
        }

        public void onClickAmountAdd() {
            //委托开仓手数
            hiddenKeyBoard();

            if (TextUtils.isEmpty(amount))
                amount = "0";

            long value = new BigDecimal(amount).add(new BigDecimal(1)).longValue();

            if (value < 1) {
//                showShortToast(R.string.trade_number_error_zero);
            } else {
                mBinding.etAmount.setText(String.valueOf(value));
            }
        }
    }

    private String getPrice(EditText editText) {
        String price = editText.getText().toString();

        if (TextUtils.isEmpty(price) || price.equals(mContext.getResources().getString(R.string.text_no_data_default)))
            return "";

        if (price.endsWith("."))
            price = price.substring(0, price.length() - 1);

        return price;
    }

    private void hiddenKeyBoard() {
        ((InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                mBinding.etAmount.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }
}
