package com.jme.lsgoldtrade.ui.transaction;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jme.common.util.DensityUtil;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBasePopupWindow;
import com.jme.lsgoldtrade.databinding.PopupwindowGuaranteeFundBinding;

public class GuaranteeFundPopUpWindow extends JMEBasePopupWindow {

    private PopupwindowGuaranteeFundBinding mBinding;

    public GuaranteeFundPopUpWindow(Context context) {
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

        mBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.popupwindow_guarantee_fund, null, false);

        if (null == mBinding)
            return;

        setContentView(mBinding.getRoot());

        mBinding.setHandlers(new ClickHandlers());
    }

    public void setData(String message, View.OnClickListener confirmListener) {
        mBinding.tvMessage.setText(message);
        mBinding.btnConfirm.setOnClickListener(confirmListener);
    }

    public class ClickHandlers {

        public void onClickCancel() {
            dismiss();
        }

    }

}
