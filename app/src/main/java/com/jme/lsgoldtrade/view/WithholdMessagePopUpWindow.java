package com.jme.lsgoldtrade.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;

import com.jme.common.util.DensityUtil;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBasePopupWindow;
import com.jme.lsgoldtrade.databinding.PopupwindowWithholdMessageBinding;

public class WithholdMessagePopUpWindow extends JMEBasePopupWindow {

    private PopupwindowWithholdMessageBinding mBinding;

    public WithholdMessagePopUpWindow(Context context) {
        super(context);
    }

    @Override
    protected void initPopupWindow() {
        super.initPopupWindow();

        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setWidth(DensityUtil.dpTopx(getContext(), 320));
    }

    @Override
    public void iniWindow() {
        super.iniWindow();

        mBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.popupwindow_withhold_message, null, false);

        if (null == mBinding)
            return;

        setContentView(mBinding.getRoot());

        mBinding.btnConfirm.setOnClickListener((view) -> dismiss());
    }

    public void setData(String message) {
        mBinding.tvMessage.setText(message);
    }

}
