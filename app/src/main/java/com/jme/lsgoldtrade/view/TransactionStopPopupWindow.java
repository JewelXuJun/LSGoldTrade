package com.jme.lsgoldtrade.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import androidx.databinding.DataBindingUtil;

import com.jme.common.util.DensityUtil;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBasePopupWindow;
import com.jme.lsgoldtrade.databinding.PopupwindowTransactionStopBinding;

public class TransactionStopPopupWindow extends JMEBasePopupWindow {

    private PopupwindowTransactionStopBinding mBinding;

    private Context mContext;

    public TransactionStopPopupWindow(Context context) {
        super(context);

        mContext = context;
    }

    @Override
    protected void initPopupWindow() {
        super.initPopupWindow();

        setHeight(DensityUtil.dpTopx(getContext(), 450));
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
    }

    public class ClickHandlers {

        public void onClickProfitPriceMinus() {

        }

        public void onClickProfitPriceAdd() {

        }

        public void onClickLossPriceMinus() {

        }

        public void onClickLossPriceAdd() {

        }

        public void onClickAmountMinus() {

        }

        public void onClickAmountAdd() {

        }

        public void onClickEntrustTypeTips() {

        }

        public void onCliclStopRiskTips() {

        }

        public void onClickCancel() {

        }

        public void onClickConfirm() {

        }

        public void onClickModify() {

        }

    }

    private void hiddenKeyBoard() {
        ((InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                mBinding.etProfitPrice.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }
}
