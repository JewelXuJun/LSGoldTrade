package com.jme.lsgoldtrade.view;

import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;

import com.alibaba.android.arouter.launcher.ARouter;
import com.jme.common.util.DensityUtil;
import com.jme.common.util.RxBus;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBasePopupWindow;
import com.jme.lsgoldtrade.config.AppConfig;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.PopupwindowTransactionStopBinding;
import com.jme.lsgoldtrade.domain.ConditionOrderInfoVo;
import com.jme.lsgoldtrade.domain.ContractInfoVo;
import com.jme.lsgoldtrade.domain.FiveSpeedVo;
import com.jme.lsgoldtrade.domain.PositionVo;
import com.jme.lsgoldtrade.util.MarketUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class TransactionStopPopupWindow extends JMEBasePopupWindow {

    private PopupwindowTransactionStopBinding mBinding;

    private Context mContext;

    private long mPosition;
    private float mPriceMove = 0.01f;
    private int mLength = 2;
    private String mContractID;
    private String mType = "";

    private View mView;
    private FiveSpeedVo mFiveSpeedVo;
    private PositionVo mPositionVo;
    private ContractInfoVo mContractInfoVo;
    private ConditionOrderInfoVo mConditionOrderInfoVo;
    private RulePopupwindow mRulePopupwindow;

    public TransactionStopPopupWindow(Context context, View view) {
        super(context);

        mContext = context;
        mView = view;
        mRulePopupwindow = new RulePopupwindow(mContext, 1);
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

        mBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.popupwindow_transaction_stop, null, false);

        if (null == mBinding)
            return;

        setContentView(mBinding.getRoot());

        mBinding.setHandlers(new ClickHandlers());

        initListener();
    }

    private void initListener() {
        mBinding.etProfitPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > mLength) {
                        s = s.toString().subSequence(0, s.toString().indexOf(".") + (mLength + 1));

                        mBinding.etProfitPrice.setText(s);
                        mBinding.etProfitPrice.setSelection(s.length());
                    }
                }

                if (s.toString().trim().equals(".")) {
                    s = "0" + s;

                    mBinding.etProfitPrice.setText(s);
                    mBinding.etProfitPrice.setSelection(2);
                }

                if (s.toString().startsWith("0") && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        mBinding.etProfitPrice.setText(s.subSequence(0, 1));
                        mBinding.etProfitPrice.setSelection(1);

                        return;
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mBinding.etLossPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > mLength) {
                        s = s.toString().subSequence(0, s.toString().indexOf(".") + (mLength + 1));

                        mBinding.etLossPrice.setText(s);
                        mBinding.etLossPrice.setSelection(s.length());
                    }
                }

                if (s.toString().trim().equals(".")) {
                    s = "0" + s;

                    mBinding.etLossPrice.setText(s);
                    mBinding.etLossPrice.setSelection(2);
                }

                if (s.toString().startsWith("0") && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        mBinding.etLossPrice.setText(s.subSequence(0, 1));
                        mBinding.etLossPrice.setSelection(1);

                        return;
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

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

    public void setData(boolean stopOrderFlag, String contractId, FiveSpeedVo fiveSpeedVo, PositionVo positionVo,
                        ContractInfoVo contractInfoVo, ConditionOrderInfoVo conditionOrderInfoVo) {
        mContractID = contractId;
        mFiveSpeedVo = fiveSpeedVo;
        mPositionVo = positionVo;
        mContractInfoVo = contractInfoVo;
        mConditionOrderInfoVo = conditionOrderInfoVo;
        mLength = mContractID.equals("Ag(T+D)") ? 0 : 2;

        if (stopOrderFlag) {
            mBinding.layoutNotSetting.setVisibility(View.GONE);
            mBinding.layoutAlreadySetting.setVisibility(View.VISIBLE);
            mBinding.btnCancel.setVisibility(View.GONE);
            mBinding.btnConfirm.setVisibility(View.GONE);
            mBinding.btnTransactionCancel.setVisibility(View.VISIBLE);
            mBinding.btnModify.setVisibility(View.VISIBLE);
            mBinding.btnModifySetting.setVisibility(View.GONE);

            long stopProfitPrice = mConditionOrderInfoVo.getStopProfitPrice();
            long stopLossPrice = mConditionOrderInfoVo.getStopLossPrice();

            mBinding.tvStopProfitPrice.setText(1 == stopProfitPrice ? mContext.getResources().getString(R.string.transaction_not_setting)
                    : MarketUtil.formatValue(MarketUtil.getPriceValue(stopProfitPrice), 2));
            mBinding.tvStopLossPrice.setText(1 == stopLossPrice ? mContext.getResources().getString(R.string.transaction_not_setting)
                    : MarketUtil.formatValue(MarketUtil.getPriceValue(stopLossPrice), 2));
            mBinding.tvAmount.setText(String.valueOf(mConditionOrderInfoVo.getEntrustNumber()));
            mBinding.tvEntrustType.setText(R.string.transaction_market_price_fak);
            mBinding.tvSettingType.setText(String.format(mContext.getResources().getString(R.string.transaction_stop_current_setting_type),
                    mConditionOrderInfoVo.getEffectiveTimeFlag() == 0 ? mContext.getResources().getString(R.string.transaction_effective_on_that_day)
                            : mContext.getResources().getString(R.string.transaction_effective_before_cancel)));
        } else {
            mType = null == mPositionVo ? "" : mPositionVo.getType();
            mPosition = null == mPositionVo ? 0 : mPositionVo.getPosition();
            mPriceMove = null == mContractInfoVo ? 0.01f : new BigDecimal(mContractInfoVo.getMinPriceMove()).divide(new BigDecimal(100)).floatValue();

            mBinding.layoutNotSetting.setVisibility(View.VISIBLE);
            mBinding.layoutAlreadySetting.setVisibility(View.GONE);
            mBinding.btnCancel.setVisibility(View.VISIBLE);
            mBinding.btnConfirm.setVisibility(View.VISIBLE);
            mBinding.btnTransactionCancel.setVisibility(View.GONE);
            mBinding.btnModify.setVisibility(View.GONE);
            mBinding.btnModifySetting.setVisibility(View.GONE);
            mBinding.etProfitPrice.setText("");
            mBinding.etProfitPrice.setInputType(mContractID.equals("Ag(T+D)") ? InputType.TYPE_CLASS_NUMBER : EditorInfo.TYPE_CLASS_NUMBER | EditorInfo.TYPE_NUMBER_FLAG_DECIMAL);
            mBinding.etLossPrice.setText("");
            mBinding.etLossPrice.setInputType(mContractID.equals("Ag(T+D)") ? InputType.TYPE_CLASS_NUMBER : EditorInfo.TYPE_CLASS_NUMBER | EditorInfo.TYPE_NUMBER_FLAG_DECIMAL);
            mBinding.etAmount.setText(String.valueOf(mPosition));
            mBinding.checkboxEffectiveOnThatDay.setChecked(true);
            mBinding.checkboxEffectiveBeforeCancel.setChecked(false);
            mBinding.checkboxAgree.setChecked(true);

            setStopPriceTitle();
        }
    }

    public void setFiveSpeedVo(List<FiveSpeedVo> fiveSpeedVoList) {
        if (null != fiveSpeedVoList && 0 != fiveSpeedVoList.size()) {
            for (FiveSpeedVo fiveSpeedVo : fiveSpeedVoList) {
                if (null != fiveSpeedVo && fiveSpeedVo.getContractId().equals(mContractID))
                    mFiveSpeedVo = fiveSpeedVo;
            }
        }

        setStopPriceTitle();
    }

    private void setStopPriceTitle() {
        mBinding.tvStopProfitPriceTitle.setText(TextUtils.isEmpty(mType) ? mContext.getResources().getString(R.string.transaction_stop_profit_price_title)
                : mType.equals("多") ? String.format(mContext.getResources().getString(R.string.transaction_stop_profit_price), ">=", mFiveSpeedVo.getLatestPriceValue())
                : String.format(mContext.getResources().getString(R.string.transaction_stop_profit_price), "<=", mFiveSpeedVo.getLatestPriceValue()));
        mBinding.tvStopLossPriceTitle.setText(TextUtils.isEmpty(mType) ? mContext.getResources().getString(R.string.transaction_stop_loss_price_title)
                : mType.equals("多") ? String.format(mContext.getResources().getString(R.string.transaction_stop_loss_price), "<=", mFiveSpeedVo.getLatestPriceValue())
                : String.format(mContext.getResources().getString(R.string.transaction_stop_loss_price), ">=", mFiveSpeedVo.getLatestPriceValue()));
    }

    private String getProfitPrice() {
        String profitPrice = mBinding.etProfitPrice.getText().toString();

        if (TextUtils.isEmpty(profitPrice))
            return mFiveSpeedVo.getLatestPriceValue();

        if (profitPrice.endsWith("."))
            profitPrice = profitPrice.substring(0, profitPrice.length() - 1);

        return profitPrice;
    }

    private String getLossPrice() {
        String lossPrice = mBinding.etLossPrice.getText().toString();

        if (TextUtils.isEmpty(lossPrice))
            return mFiveSpeedVo.getLatestPriceValue();

        if (lossPrice.endsWith("."))
            lossPrice = lossPrice.substring(0, lossPrice.length() - 1);

        return lossPrice;
    }

    private void sendConfirmData(String stopProfitPrice, String stopLossPrice, String entrustNumber) {
        dismiss();

        List<String> list = new ArrayList<>();
        list.add(mType.equals("多") ? "2" : "1");
        list.add("1");
        list.add(mContractID);
        list.add(mBinding.checkboxEffectiveOnThatDay.isChecked() ? "0" : "1");
        list.add(stopProfitPrice);
        list.add(stopLossPrice);
        list.add(entrustNumber);

        RxBus.getInstance().post(Constants.RxBusConst.RXBUS_TRANSACTION_STOP_SHEET_UPDATE, list);
    }


    private void sendModifyData(String profitPrice, String lossPrice, String amount) {
        dismiss();

        List<String> list = new ArrayList<>();
        list.add(String.valueOf(mConditionOrderInfoVo.getId()));
        list.add(mBinding.checkboxEffectiveOnThatDay.isChecked() ? "0" : "1");
        list.add(profitPrice);
        list.add(lossPrice);
        list.add(amount);

        RxBus.getInstance().post(Constants.RxBusConst.RXBUS_TRANSACTION_STOP_SHEET_MODIFY_ORDER, list);
    }

    public class ClickHandlers {

        public void onClickProfitPriceMinus() {
            hiddenKeyBoard();

            String price = getProfitPrice();

            if (TextUtils.isEmpty(price))
                return;

            float value = new BigDecimal(price).subtract(new BigDecimal(mPriceMove)).floatValue();
            String minPrice = mType.equals("多") ? mFiveSpeedVo.getLatestPriceValue() : mFiveSpeedVo.getLowerLimitPrice();

            if (new BigDecimal(value).compareTo(new BigDecimal(minPrice)) == -1) {
                Toast.makeText(mContext, mType.equals("多") ? R.string.transaction_sheet_limit_down_price_error2
                        : R.string.transaction_sheet_limit_down_price_error3, Toast.LENGTH_SHORT).show();

                mBinding.etProfitPrice.setText(price);
                mBinding.etProfitPrice.setSelection(price.length());
            } else {
                String valueStr = MarketUtil.formatValue(String.valueOf(value), mLength);

                mBinding.etProfitPrice.setText(valueStr);
                mBinding.etProfitPrice.setSelection(valueStr.length());
            }
        }

        public void onClickProfitPriceAdd() {
            hiddenKeyBoard();

            String price = getProfitPrice();

            if (TextUtils.isEmpty(price))
                return;

            float value = new BigDecimal(price).add(new BigDecimal(mPriceMove)).floatValue();
            String maxPrice = mType.equals("多") ? mFiveSpeedVo.getHighLimitPrice() : mFiveSpeedVo.getLatestPriceValue();

            if (new BigDecimal(value).compareTo(new BigDecimal(maxPrice)) == 1) {
                Toast.makeText(mContext, mType.equals("多") ? R.string.transaction_sheet_limit_up_price_error2
                        : R.string.transaction_sheet_limit_up_price_error3, Toast.LENGTH_SHORT).show();

                mBinding.etProfitPrice.setText(price);
                mBinding.etProfitPrice.setSelection(price.length());
            } else {
                String valueStr = MarketUtil.formatValue(String.valueOf(value), mLength);

                mBinding.etProfitPrice.setText(valueStr);
                mBinding.etProfitPrice.setSelection(valueStr.length());
            }
        }

        public void onClickLossPriceMinus() {
            hiddenKeyBoard();

            String price = getLossPrice();

            if (TextUtils.isEmpty(price))
                return;

            float value = new BigDecimal(price).subtract(new BigDecimal(mPriceMove)).floatValue();
            String minPrice = mType.equals("多") ? mFiveSpeedVo.getLowerLimitPrice() : mFiveSpeedVo.getLatestPriceValue();

            if (new BigDecimal(value).compareTo(new BigDecimal(minPrice)) == -1) {
                Toast.makeText(mContext, mType.equals("多") ? R.string.transaction_sheet_limit_down_price_error3
                        : R.string.transaction_sheet_limit_down_price_error2, Toast.LENGTH_SHORT).show();

                mBinding.etLossPrice.setText(price);
                mBinding.etLossPrice.setSelection(price.length());
            } else {
                String valueStr = MarketUtil.formatValue(String.valueOf(value), mLength);

                mBinding.etLossPrice.setText(valueStr);
                mBinding.etLossPrice.setSelection(valueStr.length());
            }
        }

        public void onClickLossPriceAdd() {
            hiddenKeyBoard();

            String price = getLossPrice();

            if (TextUtils.isEmpty(price))
                return;

            float value = new BigDecimal(price).add(new BigDecimal(mPriceMove)).floatValue();
            String maxPrice = mType.equals("多") ? mFiveSpeedVo.getLatestPriceValue() : mFiveSpeedVo.getHighLimitPrice();

            if (new BigDecimal(value).compareTo(new BigDecimal(maxPrice)) == 1) {
                Toast.makeText(mContext, mType.equals("多") ? R.string.transaction_sheet_limit_up_price_error3
                        : R.string.transaction_sheet_limit_up_price_error2, Toast.LENGTH_SHORT).show();

                mBinding.etLossPrice.setText(price);
                mBinding.etLossPrice.setSelection(price.length());
            } else {
                String valueStr = MarketUtil.formatValue(String.valueOf(value), mLength);

                mBinding.etLossPrice.setText(valueStr);
                mBinding.etLossPrice.setSelection(valueStr.length());
            }
        }

        public void onClickAmountMinus() {
            hiddenKeyBoard();

            String amount = mBinding.etAmount.getText().toString();

            if (TextUtils.isEmpty(amount))
                amount = "0";

            long value = new BigDecimal(amount).subtract(new BigDecimal(1)).longValue();

            if (new BigDecimal(value).compareTo(new BigDecimal(0)) == 1) {
                mBinding.etAmount.setText(String.valueOf(value));
                mBinding.etAmount.setSelection(String.valueOf(value).length());
            } else {
                Toast.makeText(mContext, String.format(mContext.getResources().getString(R.string.transaction_entrust_les2), 1), Toast.LENGTH_SHORT).show();
            }
        }

        public void onClickAmountAdd() {
            hiddenKeyBoard();

            String amount = mBinding.etAmount.getText().toString();

            if (TextUtils.isEmpty(amount))
                amount = "0";

            long value = new BigDecimal(amount).add(new BigDecimal(1)).longValue();

            if (new BigDecimal(value).compareTo(new BigDecimal(mPosition)) == 1) {
                Toast.makeText(mContext, R.string.transaction_entrust_larger3, Toast.LENGTH_SHORT).show();
            } else {
                mBinding.etAmount.setText(String.valueOf(value));
                mBinding.etAmount.setSelection(String.valueOf(value).length());
            }
        }

        public void onClickEntrustTypeTips() {
            if (null != mRulePopupwindow && !mRulePopupwindow.isShowing()) {
                mRulePopupwindow.setData(mContext.getResources().getString(R.string.transaction_market_price_fak),
                        new SpannableString(mContext.getResources().getString(R.string.transaction_market_price_fak_rule)));
                mRulePopupwindow.showAtLocation(mView, Gravity.CENTER, 0, 0);
            }
        }

        public void onCliclStopRiskTips() {
            ARouter.getInstance()
                    .build(Constants.ARouterUriConst.JMEWEBVIEW)
                    .withString("title", mContext.getResources().getString(R.string.transaction_stop_risk_tips_title))
                    .withString("url", Constants.HttpConst.URL_TRANSACTION_STOP)
                    .navigation();
        }

        public void onClickCancel() {
            dismiss();
        }

        public void onClickTransactionCancel() {
            RxBus.getInstance().post(Constants.RxBusConst.RXBUS_TRANSACTION_STOP_SHEET_CANCEL_ORDER, mConditionOrderInfoVo.getId());
        }

        public void onClickConfirm() {
            String profitPrice = mBinding.etProfitPrice.getText().toString();
            String lossPrice = mBinding.etLossPrice.getText().toString();
            String amount = mBinding.etAmount.getText().toString();

            if (TextUtils.isEmpty(profitPrice) && TextUtils.isEmpty(lossPrice))
                Toast.makeText(mContext, R.string.transaction_stop_price_empty, Toast.LENGTH_SHORT).show();
            else if (!TextUtils.isEmpty(profitPrice) && mType.equals("多") && new BigDecimal(profitPrice).compareTo(new BigDecimal(mFiveSpeedVo.getLatestPriceValue())) == -1)
                Toast.makeText(mContext, String.format(mContext.getResources().getString(R.string.transaction_stop_profit_price_range),
                        mFiveSpeedVo.getLatestPriceValue(), mFiveSpeedVo.getHighLimitPrice()), Toast.LENGTH_SHORT).show();
            else if (!TextUtils.isEmpty(profitPrice) && mType.equals("多") && new BigDecimal(profitPrice).compareTo(new BigDecimal(mFiveSpeedVo.getHighLimitPrice())) == 1)
                Toast.makeText(mContext, String.format(mContext.getResources().getString(R.string.transaction_stop_profit_price_range),
                        mFiveSpeedVo.getLatestPriceValue(), mFiveSpeedVo.getHighLimitPrice()), Toast.LENGTH_SHORT).show();
            else if (!TextUtils.isEmpty(profitPrice) && mType.equals("空") && new BigDecimal(profitPrice).compareTo(new BigDecimal(mFiveSpeedVo.getLowerLimitPrice())) == -1)
                Toast.makeText(mContext, String.format(mContext.getResources().getString(R.string.transaction_stop_profit_price_range),
                        mFiveSpeedVo.getLowerLimitPrice(), mFiveSpeedVo.getLatestPriceValue()), Toast.LENGTH_SHORT).show();
            else if (!TextUtils.isEmpty(profitPrice) && mType.equals("空") && new BigDecimal(profitPrice).compareTo(new BigDecimal(mFiveSpeedVo.getLatestPriceValue())) == 1)
                Toast.makeText(mContext, String.format(mContext.getResources().getString(R.string.transaction_stop_profit_price_range),
                        mFiveSpeedVo.getLowerLimitPrice(), mFiveSpeedVo.getLatestPriceValue()), Toast.LENGTH_SHORT).show();
            else if (!TextUtils.isEmpty(lossPrice) && mType.equals("多") && new BigDecimal(lossPrice).compareTo(new BigDecimal(mFiveSpeedVo.getLowerLimitPrice())) == -1)
                Toast.makeText(mContext, String.format(mContext.getResources().getString(R.string.transaction_stop_loss_price_range),
                        mFiveSpeedVo.getLowerLimitPrice(), mFiveSpeedVo.getLatestPriceValue()), Toast.LENGTH_SHORT).show();
            else if (!TextUtils.isEmpty(lossPrice) && mType.equals("多") && new BigDecimal(lossPrice).compareTo(new BigDecimal(mFiveSpeedVo.getLatestPriceValue())) == 1)
                Toast.makeText(mContext, String.format(mContext.getResources().getString(R.string.transaction_stop_loss_price_range),
                        mFiveSpeedVo.getLowerLimitPrice(), mFiveSpeedVo.getLatestPriceValue()), Toast.LENGTH_SHORT).show();
            else if (!TextUtils.isEmpty(lossPrice) && mType.equals("空") && new BigDecimal(lossPrice).compareTo(new BigDecimal(mFiveSpeedVo.getLatestPriceValue())) == -1)
                Toast.makeText(mContext, String.format(mContext.getResources().getString(R.string.transaction_stop_loss_price_range),
                        mFiveSpeedVo.getLatestPriceValue(), mFiveSpeedVo.getHighLimitPrice()), Toast.LENGTH_SHORT).show();
            else if (!TextUtils.isEmpty(lossPrice) && mType.equals("空") && new BigDecimal(lossPrice).compareTo(new BigDecimal(mFiveSpeedVo.getHighLimitPrice())) == 1)
                Toast.makeText(mContext, String.format(mContext.getResources().getString(R.string.transaction_stop_loss_price_range),
                        mFiveSpeedVo.getLatestPriceValue(), mFiveSpeedVo.getHighLimitPrice()), Toast.LENGTH_SHORT).show();
            else if (TextUtils.isEmpty(amount))
                Toast.makeText(mContext, R.string.transaction_number_error, Toast.LENGTH_SHORT).show();
            else if (new BigDecimal(amount).compareTo(new BigDecimal(0)) == 0)
                Toast.makeText(mContext, String.format(mContext.getResources().getString(R.string.transaction_entrust_les2), 1), Toast.LENGTH_SHORT).show();
            else if (new BigDecimal(amount).compareTo(new BigDecimal(mPosition)) == 1)
                Toast.makeText(mContext, R.string.transaction_entrust_larger3, Toast.LENGTH_SHORT).show();
            else if (!mBinding.checkboxAgree.isChecked())
                Toast.makeText(mContext, R.string.transaction_condition_sheet_risk_agree, Toast.LENGTH_SHORT).show();
            else
                sendConfirmData(profitPrice, lossPrice, amount);
        }

        public void onClickModify() {
            mType = null == mPositionVo ? "" : mPositionVo.getType();
            mPosition = null == mPositionVo ? 0 : mPositionVo.getPosition();
            mPriceMove = null == mContractInfoVo ? 0.01f : new BigDecimal(mContractInfoVo.getMinPriceMove()).divide(new BigDecimal(100)).floatValue();
            long stopProfitPrice = mConditionOrderInfoVo.getStopProfitPrice();
            long stopLossPrice = mConditionOrderInfoVo.getStopLossPrice();

            mBinding.layoutNotSetting.setVisibility(View.VISIBLE);
            mBinding.layoutAlreadySetting.setVisibility(View.GONE);
            mBinding.btnCancel.setVisibility(View.VISIBLE);
            mBinding.btnConfirm.setVisibility(View.GONE);
            mBinding.btnTransactionCancel.setVisibility(View.GONE);
            mBinding.btnModify.setVisibility(View.GONE);
            mBinding.btnModifySetting.setVisibility(View.VISIBLE);
            mBinding.etProfitPrice.setText(1 == stopProfitPrice ? "" : MarketUtil.formatValue(MarketUtil.getPriceValue(stopProfitPrice), 2));
            mBinding.etProfitPrice.setInputType(mContractID.equals("Ag(T+D)") ? InputType.TYPE_CLASS_NUMBER : EditorInfo.TYPE_CLASS_NUMBER | EditorInfo.TYPE_NUMBER_FLAG_DECIMAL);
            mBinding.etLossPrice.setText(1 == stopLossPrice ? "" : MarketUtil.formatValue(MarketUtil.getPriceValue(stopLossPrice), 2));
            mBinding.etLossPrice.setInputType(mContractID.equals("Ag(T+D)") ? InputType.TYPE_CLASS_NUMBER : EditorInfo.TYPE_CLASS_NUMBER | EditorInfo.TYPE_NUMBER_FLAG_DECIMAL);
            mBinding.etAmount.setText(String.valueOf(mPosition));
            mBinding.checkboxEffectiveOnThatDay.setChecked(mConditionOrderInfoVo.getEffectiveTimeFlag() == 0);
            mBinding.checkboxEffectiveBeforeCancel.setChecked(mConditionOrderInfoVo.getEffectiveTimeFlag() == 1);
            mBinding.checkboxAgree.setChecked(true);

            setStopPriceTitle();
        }

        public void onClickModifySetting() {
            String profitPrice = mBinding.etProfitPrice.getText().toString();
            String lossPrice = mBinding.etLossPrice.getText().toString();
            String amount = mBinding.etAmount.getText().toString();

            if (TextUtils.isEmpty(profitPrice) && TextUtils.isEmpty(lossPrice))
                Toast.makeText(mContext, R.string.transaction_stop_price_empty, Toast.LENGTH_SHORT).show();
            else if (!TextUtils.isEmpty(profitPrice) && mType.equals("多") && new BigDecimal(profitPrice).compareTo(new BigDecimal(mFiveSpeedVo.getLatestPriceValue())) == -1)
                Toast.makeText(mContext, String.format(mContext.getResources().getString(R.string.transaction_stop_profit_price_range),
                        mFiveSpeedVo.getLatestPriceValue(), mFiveSpeedVo.getHighLimitPrice()), Toast.LENGTH_SHORT).show();
            else if (!TextUtils.isEmpty(profitPrice) && mType.equals("多") && new BigDecimal(profitPrice).compareTo(new BigDecimal(mFiveSpeedVo.getHighLimitPrice())) == 1)
                Toast.makeText(mContext, String.format(mContext.getResources().getString(R.string.transaction_stop_profit_price_range),
                        mFiveSpeedVo.getLatestPriceValue(), mFiveSpeedVo.getHighLimitPrice()), Toast.LENGTH_SHORT).show();
            else if (!TextUtils.isEmpty(profitPrice) && mType.equals("空") && new BigDecimal(profitPrice).compareTo(new BigDecimal(mFiveSpeedVo.getLowerLimitPrice())) == -1)
                Toast.makeText(mContext, String.format(mContext.getResources().getString(R.string.transaction_stop_profit_price_range),
                        mFiveSpeedVo.getLowerLimitPrice(), mFiveSpeedVo.getLatestPriceValue()), Toast.LENGTH_SHORT).show();
            else if (!TextUtils.isEmpty(profitPrice) && mType.equals("空") && new BigDecimal(profitPrice).compareTo(new BigDecimal(mFiveSpeedVo.getLatestPriceValue())) == 1)
                Toast.makeText(mContext, String.format(mContext.getResources().getString(R.string.transaction_stop_profit_price_range),
                        mFiveSpeedVo.getLowerLimitPrice(), mFiveSpeedVo.getLatestPriceValue()), Toast.LENGTH_SHORT).show();
            else if (!TextUtils.isEmpty(lossPrice) && mType.equals("多") && new BigDecimal(lossPrice).compareTo(new BigDecimal(mFiveSpeedVo.getLowerLimitPrice())) == -1)
                Toast.makeText(mContext, String.format(mContext.getResources().getString(R.string.transaction_stop_loss_price_range),
                        mFiveSpeedVo.getLowerLimitPrice(), mFiveSpeedVo.getLatestPriceValue()), Toast.LENGTH_SHORT).show();
            else if (!TextUtils.isEmpty(lossPrice) && mType.equals("多") && new BigDecimal(lossPrice).compareTo(new BigDecimal(mFiveSpeedVo.getLatestPriceValue())) == 1)
                Toast.makeText(mContext, String.format(mContext.getResources().getString(R.string.transaction_stop_loss_price_range),
                        mFiveSpeedVo.getLowerLimitPrice(), mFiveSpeedVo.getLatestPriceValue()), Toast.LENGTH_SHORT).show();
            else if (!TextUtils.isEmpty(lossPrice) && mType.equals("空") && new BigDecimal(lossPrice).compareTo(new BigDecimal(mFiveSpeedVo.getLatestPriceValue())) == -1)
                Toast.makeText(mContext, String.format(mContext.getResources().getString(R.string.transaction_stop_loss_price_range),
                        mFiveSpeedVo.getLatestPriceValue(), mFiveSpeedVo.getHighLimitPrice()), Toast.LENGTH_SHORT).show();
            else if (!TextUtils.isEmpty(lossPrice) && mType.equals("空") && new BigDecimal(lossPrice).compareTo(new BigDecimal(mFiveSpeedVo.getHighLimitPrice())) == 1)
                Toast.makeText(mContext, String.format(mContext.getResources().getString(R.string.transaction_stop_loss_price_range),
                        mFiveSpeedVo.getLatestPriceValue(), mFiveSpeedVo.getHighLimitPrice()), Toast.LENGTH_SHORT).show();
            else if (TextUtils.isEmpty(amount))
                Toast.makeText(mContext, R.string.transaction_number_error, Toast.LENGTH_SHORT).show();
            else if (new BigDecimal(amount).compareTo(new BigDecimal(0)) == 0)
                Toast.makeText(mContext, String.format(mContext.getResources().getString(R.string.transaction_entrust_les2), 1), Toast.LENGTH_SHORT).show();
            else if (new BigDecimal(amount).compareTo(new BigDecimal(mPosition)) == 1)
                Toast.makeText(mContext, R.string.transaction_entrust_larger3, Toast.LENGTH_SHORT).show();
            else if (!mBinding.checkboxAgree.isChecked())
                Toast.makeText(mContext, R.string.transaction_condition_sheet_risk_agree, Toast.LENGTH_SHORT).show();
            else
                sendModifyData(profitPrice, lossPrice, amount);
        }

    }

    private void hiddenKeyBoard() {
        ((InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                mBinding.etProfitPrice.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }
}
