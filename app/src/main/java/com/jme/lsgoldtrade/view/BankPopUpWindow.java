package com.jme.lsgoldtrade.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;

import com.jme.common.util.DensityUtil;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBasePopupWindow;
import com.jme.lsgoldtrade.databinding.PopupwindowBankBinding;
import com.jme.lsgoldtrade.domain.BankVo;
import com.jme.lsgoldtrade.ui.personal.BankAdapter;

import java.util.List;

public class BankPopUpWindow extends JMEBasePopupWindow {

    private PopupwindowBankBinding mBinding;

    private Context mContext;

    private BankAdapter mAdapter;

    public BankPopUpWindow(Context context) {
        super(context);

        mContext = context;
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

        mBinding.setHandlers(new ClickHandlers());

        mAdapter = new BankAdapter(mContext, null);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        mBinding.recyclerView.setLayoutManager(gridLayoutManager);
        mBinding.recyclerView.setAdapter(mAdapter);
        mBinding.recyclerView.setHasFixedSize(true);
        mBinding.recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    public void setData(List<BankVo> bankVoList) {
        mAdapter.setNewData(bankVoList);
    }

    public class ClickHandlers {

        public void onClickClose() {
            dismiss();
        }

    }

}
