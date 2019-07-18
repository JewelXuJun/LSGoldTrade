package com.jme.lsgoldtrade.view;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.jme.common.util.DensityUtil;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBasePopupWindow;
import com.jme.lsgoldtrade.databinding.PopupwindowHasprofitlossrisksignBinding;

public class HasProfitLossRiskSignPopupWindow extends JMEBasePopupWindow {

    private PopupwindowHasprofitlossrisksignBinding mBinding;

    public int isAgree = 0;

    public HasProfitLossRiskSignPopupWindow(Context context) {
        super(context);
    }

    @Override
    protected void initPopupWindow() {
        super.initPopupWindow();

        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setWidth(DensityUtil.dpTopx(getContext(), 300));
    }

    @Override
    public void iniWindow() {
        super.iniWindow();

        mBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.popupwindow_hasprofitlossrisksign, null, false);

        if (null == mBinding)
            return;

        setContentView(mBinding.getRoot());
    }

    public void setData(View.OnClickListener cancelListener, View.OnClickListener confirmListener) {
        mBinding.cbYingkui.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    isAgree = 1;
                } else {
                    isAgree = 0;
                }
            }
        });
        mBinding.btnCancel.setOnClickListener(cancelListener);
        mBinding.btnConfirm.setOnClickListener(confirmListener);
    }
}
