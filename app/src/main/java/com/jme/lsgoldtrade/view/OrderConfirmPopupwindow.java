package com.jme.lsgoldtrade.view;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jme.common.util.DensityUtil;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBasePopupWindow;
import com.jme.lsgoldtrade.databinding.PopupwindowOrderConfirmBinding;

public class OrderConfirmPopupwindow extends JMEBasePopupWindow {

    private PopupwindowOrderConfirmBinding mBinding;

    public OrderConfirmPopupwindow(Context context) {
        super(context);
    }

    @Override
    protected void initPopupWindow() {
        super.initPopupWindow();

        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setWidth(DensityUtil.dpTopx(getContext(), 295));
    }

    @Override
    public void iniWindow() {
        super.iniWindow();

        mBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.popupwindow_order_confirm, null, false);

        if (null == mBinding)
            return;

        setContentView(mBinding.getRoot());

        mBinding.btnCancel.setOnClickListener((view) -> dismiss());
    }

    public void setData(String code, View.OnClickListener confirmListener) {
        mBinding.tvContent.setText(code);
        mBinding.btnConfirm.setOnClickListener(confirmListener);
    }
}
