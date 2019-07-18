package com.jme.lsgoldtrade.view;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBasePopupWindow;
import com.jme.lsgoldtrade.databinding.PopupwindowFudongjizhiBinding;

public class FuDongJiZhiPopupWindow extends JMEBasePopupWindow {

    private PopupwindowFudongjizhiBinding mBinding;

    public FuDongJiZhiPopupWindow(Context context) {
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

        mBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.popupwindow_fudongjizhi, null, false);

        if (null == mBinding)
            return;

        setContentView(mBinding.getRoot());
    }

    public void setData(String title, String content, View.OnClickListener cancelListener) {
        mBinding.title.setText(title);
        mBinding.content.setText(content);
        mBinding.cancel.setOnClickListener(cancelListener);
    }
}
