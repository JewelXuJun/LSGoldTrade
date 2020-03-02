package com.jme.lsgoldtrade.ui.transaction;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;

import com.jme.common.util.DensityUtil;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBasePopupWindow;
import com.jme.lsgoldtrade.databinding.PopwindowIncrementExplainBinding;


public class IncrementExplainPopUpWindow extends JMEBasePopupWindow {

    private PopwindowIncrementExplainBinding mBinding;


    public IncrementExplainPopUpWindow(Context context) {
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

        mBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.popwindow_increment_explain, null, false);

        if (null == mBinding)
            return;

        setContentView(mBinding.getRoot());
        mBinding.setHandlers(new ClickHandlers());

    }


    public void setData(View.OnClickListener agreementIncrement, View.OnClickListener talkTater){
        mBinding.btnAgreementIncrementPop.setOnClickListener(agreementIncrement);
        mBinding.tvTalkLaterPop.setOnClickListener(talkTater);
    }

    public class ClickHandlers {

        public void onClickClose() {
            dismiss();
        }

    }
}
