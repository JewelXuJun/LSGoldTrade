package com.jme.lsgoldtrade.ui.main;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jme.common.util.DensityUtil;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBasePopupWindow;
import com.jme.lsgoldtrade.databinding.PopupwindowCancelOrderBinding;
import com.jme.lsgoldtrade.databinding.PopupwindowProtocolUpdateBinding;
import com.jme.lsgoldtrade.util.MarketUtil;

public class ProtocolUpdatePopUpWindow extends JMEBasePopupWindow {

    private PopupwindowProtocolUpdateBinding mBinding;

    public ProtocolUpdatePopUpWindow(Context context) {
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

        mBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.popupwindow_protocol_update, null, false);

        if (null == mBinding)
            return;

        setContentView(mBinding.getRoot());

        mBinding.setHandlers(new ClickHandlers());
    }

    public void setData() {

    }

    public class ClickHandlers {

        public void onClickAgree() {

        }

    }

}
