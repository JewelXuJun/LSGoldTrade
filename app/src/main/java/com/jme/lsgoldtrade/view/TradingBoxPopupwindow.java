package com.jme.lsgoldtrade.view;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBasePopupWindow;
import com.jme.lsgoldtrade.databinding.PopupwindowTradingboxBinding;

public class TradingBoxPopupwindow extends JMEBasePopupWindow {

    private PopupwindowTradingboxBinding mBinding;

    public TradingBoxPopupwindow(Context context) {
        super(context);
    }

    @Override
    protected void initPopupWindow() {
        super.initPopupWindow();

        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void iniWindow() {
        super.iniWindow();

        mBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.popupwindow_tradingbox, null, false);

        if (null == mBinding)
            return;

        setContentView(mBinding.getRoot());
    }

    public void setData(int res, View.OnClickListener cancelListener) {
        mBinding.content.setImageResource(res);
        mBinding.cancel.setOnClickListener(cancelListener);
    }
}
