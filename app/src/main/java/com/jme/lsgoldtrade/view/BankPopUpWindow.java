package com.jme.lsgoldtrade.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.jme.common.util.DensityUtil;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBasePopupWindow;
import com.jme.lsgoldtrade.databinding.PopupwindowBankBinding;
import com.jme.lsgoldtrade.ui.personal.BankAdapter;

import java.util.List;

public class BankPopUpWindow extends JMEBasePopupWindow {

    private PopupwindowBankBinding mBinding;

    private BankAdapter mAdapter;

    public BankPopUpWindow(Context context) {
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

        mBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.popupwindow_bank, null, false);

        if (null == mBinding)
            return;

        setContentView(mBinding.getRoot());

        mAdapter = new BankAdapter(mContext, null);

        mBinding.recyclerView.setHasFixedSize(false);
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mBinding.recyclerView.setAdapter(mAdapter);
    }

    public void setData(List<String> list) {

    }

}
