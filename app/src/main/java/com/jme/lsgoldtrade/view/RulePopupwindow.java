package com.jme.lsgoldtrade.view;

import android.content.Context;
import androidx.databinding.DataBindingUtil;

import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBasePopupWindow;
import com.jme.lsgoldtrade.databinding.PopupwindowRuleBinding;

public class RulePopupwindow extends JMEBasePopupWindow {

    private PopupwindowRuleBinding mBinding;

    public RulePopupwindow(Context context) {
        super(context);
    }

    public RulePopupwindow(Context context, int type) {
        super(context, type);
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

        mBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.popupwindow_rule, null, false);

        if (null == mBinding)
            return;

        setContentView(mBinding.getRoot());

        mBinding.layoutCancel.setOnClickListener((view) -> dismiss());
    }

    public void setData(String title, SpannableString content) {
        mBinding.tvTitle.setText(title);
        mBinding.tvContent.setText(content);
    }
}
