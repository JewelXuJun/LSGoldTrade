package com.jme.lsgoldtrade.ui.transaction;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jme.common.util.DensityUtil;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBasePopupWindow;
import com.jme.lsgoldtrade.databinding.PopupwindowCancelOrderBinding;
import com.jme.lsgoldtrade.util.MarketUtil;

public class CancelOrderPopUpWindow extends JMEBasePopupWindow {

    private PopupwindowCancelOrderBinding mBinding;

    public CancelOrderPopUpWindow(Context context) {
        super(context);
    }

    @Override
    protected void initPopupWindow() {
        super.initPopupWindow();

        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setWidth(DensityUtil.dpTopx(getContext(), 260));
    }

    @Override
    public void iniWindow() {
        super.iniWindow();

        mBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.popupwindow_cancel_order, null, false);

        if (null == mBinding)
            return;

        setContentView(mBinding.getRoot());
    }

    public void setData(String code, String time, String type, String price, String amount, String surplusAmount, String status,
                        View.OnClickListener cancelListener, View.OnClickListener confirmListener) {
        mBinding.tvCode.setText(code);
        mBinding.tvTime.setText(time);
        mBinding.tvType.setText(type);
        mBinding.tvPrice.setText(MarketUtil.decimalFormatMoney(price) + getContext().getResources().getString(R.string.text_money_unit));
        mBinding.tvAmount.setText(amount + getContext().getResources().getString(R.string.text_amount_unit));
        mBinding.tvSurplusAmount.setText(surplusAmount + getContext().getResources().getString(R.string.text_amount_unit));
        mBinding.tvStatus.setText(status);
        mBinding.btnCancel.setOnClickListener(cancelListener);
        mBinding.btnConfirm.setOnClickListener(confirmListener);
    }

}
