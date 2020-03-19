package com.jme.lsgoldtrade.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;

import com.jme.common.util.DensityUtil;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBasePopupWindow;
import com.jme.lsgoldtrade.databinding.PopwindowStockUserBinding;

public class StockUserPopUpWindow extends JMEBasePopupWindow {

    private PopwindowStockUserBinding mBinding;



    public StockUserPopUpWindow(Context context) {
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


        mBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.popwindow_stock_user, null, false);

        if (null == mBinding)
            return;

        setContentView(mBinding.getRoot());

        mBinding.popWindowStockUserClose.setOnClickListener((v)->{dismiss();});
        mBinding.tvStockUserLaterPop.setOnClickListener((v)->{dismiss();});

    }

    public void setData(View.OnClickListener clickListener, View.OnClickListener closeClick, View.OnClickListener laterClick){
        mBinding.btnStockUserPop.setOnClickListener(clickListener);
        mBinding.popWindowStockUserClose.setOnClickListener(closeClick);
        mBinding.tvStockUserLaterPop.setOnClickListener(laterClick);
    }

}
