package com.jme.lsgoldtrade.ui.personal;

import android.os.Bundle;
import android.view.Gravity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseActivity;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.ActivityAccountSecurityBinding;
import com.jme.lsgoldtrade.view.ConfirmSimplePopupwindow;

@Route(path = Constants.ARouterUriConst.ACCOUNTSECURITY)
public class AccountSecurityActivity extends JMEBaseActivity {

    private ActivityAccountSecurityBinding mBinding;

    private ConfirmSimplePopupwindow mConfirmSimplePopupwindow;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_account_security;
    }

    @Override
    protected void initView() {
        super.initView();

        initToolbar(R.string.personal_account_security, true);

        mConfirmSimplePopupwindow = new ConfirmSimplePopupwindow(this);
        mConfirmSimplePopupwindow.setOutsideTouchable(true);
        mConfirmSimplePopupwindow.setFocusable(true);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);

        mBinding.tvAccount.setText(null == mUser || !mUser.isLogin() ? "" : mUser.getTraderId());
    }

    @Override
    protected void initListener() {
        super.initListener();
    }

    @Override
    protected void initBinding() {
        super.initBinding();

        mBinding = (ActivityAccountSecurityBinding) mBindingUtil;
        mBinding.setHandlers(new ClickHandlers());
    }

    @Override
    protected void DataReturn(DTRequest request, Head head, Object response) {
        super.DataReturn(request, head, response);
    }

    public class ClickHandlers {

        public void onClickTips() {
            if (null != mConfirmSimplePopupwindow && !mConfirmSimplePopupwindow.isShowing()) {
                mConfirmSimplePopupwindow.setData(getResources().getString(R.string.personal_account_security_tips),
                        (view) -> mConfirmSimplePopupwindow.dismiss());
                mConfirmSimplePopupwindow.showAtLocation(mBinding.tvAccount, Gravity.CENTER, 0, 0);
            }
        }

        public void onClickTradingPassword() {

        }

        public void onClickFingerprint() {

        }

        public void onClickGesture() {

        }

        public void onClickOnlineDuration() {

        }

    }

}
