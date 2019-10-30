package com.jme.lsgoldtrade.ui.transaction;

import android.app.Service;
import android.content.Context;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import androidx.databinding.DataBindingUtil;

import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBasePopupWindow;
import com.jme.lsgoldtrade.config.AppConfig;
import com.jme.lsgoldtrade.databinding.PopupwindowGuaranteeFundSettingBinding;

public class GuaranteeFundSettingPopUpWindow extends JMEBasePopupWindow {

    private PopupwindowGuaranteeFundSettingBinding mBinding;

    public GuaranteeFundSettingPopUpWindow(Context context) {
        super(context);
    }

    @Override
    protected void initPopupWindow() {
        super.initPopupWindow();

        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
    }

    @Override
    public void iniWindow() {
        super.iniWindow();

        mBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.popupwindow_guarantee_fund_setting, null, false);

        if (null == mBinding)
            return;

        setContentView(mBinding.getRoot());

        mBinding.etGuaranteeFund.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > AppConfig.Length_Limit) {
                        s = s.toString().subSequence(0, s.toString().indexOf(".") + (AppConfig.Length_Limit + 1));

                        mBinding.etGuaranteeFund.setText(s);
                        mBinding.etGuaranteeFund.setSelection(s.length());
                    }
                }

                if (s.toString().trim().equals(".")) {
                    s = "0" + s;

                    mBinding.etGuaranteeFund.setText(s);
                    mBinding.etGuaranteeFund.setSelection(2);
                }

                if (s.toString().startsWith("0") && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        mBinding.etGuaranteeFund.setText(s.subSequence(0, 1));
                        mBinding.etGuaranteeFund.setSelection(1);

                        return;
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }

        });

        mBinding.setHandlers(new ClickHandlers());
    }

    public void setData(View.OnClickListener confirmListener) {
        mBinding.btnConfirm.setOnClickListener(confirmListener);

        showSoftKeyboard();
    }

    public String getGuaranteeFund() {
        return mBinding.etGuaranteeFund.getText().toString();
    }

    private void showSoftKeyboard() {
        mBinding.etGuaranteeFund.setText("")  ;
        mBinding.etGuaranteeFund.requestFocus();

        new Handler().postDelayed(() -> {
            InputMethodManager inputMethodManager = (InputMethodManager) mBinding.etGuaranteeFund.getContext().getSystemService(Service.INPUT_METHOD_SERVICE);

            if (null != inputMethodManager)
                inputMethodManager.showSoftInput(mBinding.etGuaranteeFund, 0);
        }, 50);
    }

    public void hiddenSoftKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) mBinding.etGuaranteeFund.getContext().getSystemService(Service.INPUT_METHOD_SERVICE);

        if (null != inputMethodManager)
            inputMethodManager.hideSoftInputFromWindow(mBinding.etGuaranteeFund.getWindowToken(), 0);
    }

    public class ClickHandlers {

        public void onClickCancel() {
            dismiss();
        }

    }

}
