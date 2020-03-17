package com.jme.lsgoldtrade.view;

import android.content.Context;

import androidx.databinding.DataBindingUtil;

import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBasePopupWindow;
import com.jme.lsgoldtrade.config.AppConfig;
import com.jme.lsgoldtrade.databinding.PopupwindowEveningUpBinding;
import com.jme.lsgoldtrade.domain.FiveSpeedVo;
import com.jme.lsgoldtrade.util.MarketUtil;

import java.math.BigDecimal;

public class EveningUpPopupWindow extends JMEBasePopupWindow {

    private PopupwindowEveningUpBinding mBinding;

    private Context mContext;

    private int mLength = 2;
    private float mPriceMove = 0.00f;
    private String mType;
    private String mLowerLimitPrice;
    private String mHighLimitPrice;
    private long mMinOrderQty = 0;
    private long mMaxOrderQty = 0;
    private long mMaxHoldQty = 0;
    private long mMaxAmount = 0;

    private FiveSpeedVo mFiveSpeedVo;

    public EveningUpPopupWindow(Context context) {
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

        mBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.popupwindow_evening_up, null, false);

        if (null == mBinding)
            return;

        setContentView(mBinding.getRoot());

        mBinding.setHandlers(new ClickHandlers());

        initListener();
    }

    private void initListener() {
        mBinding.btnCancel.setOnClickListener((view) -> dismiss());

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
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void setData(FiveSpeedVo fiveSpeedVo, String contractID, String type, float priceMove, String lowerLimitPrice,
                        String highLimitPrice, long minOrderQty, long maxOrderQty, long maxHoldQty, long maxAmount, long frozenAmount,
                        View.OnClickListener confirmListener) {
        mFiveSpeedVo = fiveSpeedVo;
        mLength = contractID.equals("Ag(T+D)") ? 0 : 2;
        mType = type;

        mBinding.tvBusinessType.setText(mType.equals("多") ? mContext.getString(R.string.transaction_buy_evening)
                : mContext.getString(R.string.transaction_sale_evening));
        mBinding.tvBusinessVarieties.setText(contractID);
        mBinding.etPrice.setText("");
        mBinding.etPrice.setInputType(contractID.equals("Ag(T+D)") ? InputType.TYPE_CLASS_NUMBER : EditorInfo.TYPE_CLASS_NUMBER | EditorInfo.TYPE_NUMBER_FLAG_DECIMAL);
        mBinding.etAmount.setText(maxAmount - frozenAmount == 0 ? "0" : String.valueOf(maxAmount));

        mPriceMove = priceMove;
        mLowerLimitPrice = lowerLimitPrice;
        mHighLimitPrice = highLimitPrice;
        mMinOrderQty = minOrderQty;
        mMaxOrderQty = maxOrderQty;
        mMaxHoldQty = maxHoldQty;
        mMaxAmount = maxAmount;

        mBinding.btnConfirm.setOnClickListener(confirmListener);
    }

    public String getAmount() {
        return mBinding.etAmount.getText().toString();
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
                    Toast.makeText(mContext, R.string.transaction_limit_max_amount_error_canbuy2, Toast.LENGTH_SHORT).show();
                else
                    mBinding.etAmount.setText(String.valueOf(value));
            } else {
                if (new BigDecimal(value).compareTo(new BigDecimal(Math.min(mMaxAmount, mMaxOrderQty))) == 1)
                    Toast.makeText(mContext, R.string.transaction_limit_max_amount_error_canbuy2, Toast.LENGTH_SHORT).show();
                else
                    mBinding.etAmount.setText(String.valueOf(value));
            }
        }
    }

    public String getPrice() {
        String price = mBinding.etPrice.getText().toString();

        if (TextUtils.isEmpty(price))
            price = mType.equals("多") ? mFiveSpeedVo.getFiveBidLists().get(0)[1] : mFiveSpeedVo.getFiveAskLists().get(0)[1];

        if (price.endsWith("."))
            price = price.substring(0, price.length() - 1);

        return price;
    }

    private void hiddenKeyBoard() {
        ((InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                mBinding.etAmount.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }
}
