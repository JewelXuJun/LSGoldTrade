package com.jme.lsgoldtrade.view;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;

import com.jme.common.util.DensityUtil;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBasePopupWindow;
import com.jme.lsgoldtrade.databinding.PopupwindowBindsuccessBinding;

public class BindSuccessPopupWindow extends JMEBasePopupWindow {

    private PopupwindowBindsuccessBinding mBinding;

    public BindSuccessPopupWindow(Context context) {
        super(context);
    }

    @Override
    protected void initPopupWindow() {
        super.initPopupWindow();

        setHeight(DensityUtil.dpTopx(getContext(), 285));
        setWidth(DensityUtil.dpTopx(getContext(), 300));
    }

    @Override
    public void iniWindow() {
        super.iniWindow();

        mBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.popupwindow_bindsuccess, null, false);

        if (null == mBinding)
            return;

        setContentView(mBinding.getRoot());
    }

    public void setData(String time) {
        mBinding.timer.setText(time + "秒后自动关闭");
    }
}
