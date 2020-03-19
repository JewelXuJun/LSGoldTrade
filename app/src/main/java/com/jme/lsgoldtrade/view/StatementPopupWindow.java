package com.jme.lsgoldtrade.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;

import com.alibaba.android.arouter.launcher.ARouter;
import com.jme.common.util.DensityUtil;
import com.jme.common.util.SharedPreUtils;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBasePopupWindow;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.PopupwindowStatementBinding;

public class StatementPopupWindow extends JMEBasePopupWindow {

    private PopupwindowStatementBinding mBinding;

    private Context mContext;

    public StatementPopupWindow(Context context) {
        super(context);

        mContext = context;
    }

    @Override
    protected void initPopupWindow() {
        super.initPopupWindow();

        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setWidth(DensityUtil.dpTopx(getContext(), 340));
    }

    @Override
    public void iniWindow() {
        super.iniWindow();

        mBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.popupwindow_statement, null, false);

        if (null == mBinding)
            return;

        setContentView(mBinding.getRoot());

        mBinding.setHandlers(new ClickHandlers());
    }

    public class ClickHandlers {

        public void onClickAgreementPrivacy() {
            ARouter.getInstance()
                    .build(Constants.ARouterUriConst.JMEWEBVIEW)
                    .withString("title", mContext.getResources().getString(R.string.main_statement_agreement_privacy))
                    .withString("url", Constants.HttpConst.URL_PRIVACY_POLICY)
                    .navigation();
        }

        public void onClickAgreementUse() {
            ARouter.getInstance()
                    .build(Constants.ARouterUriConst.JMEWEBVIEW)
                    .withString("title", mContext.getResources().getString(R.string.main_statement_agreement_use))
                    .withString("url", Constants.HttpConst.URL_REGISTER_AGGREMENT)
                    .navigation();
        }

        public void onClickAgreementResponsibility() {
            ARouter.getInstance()
                    .build(Constants.ARouterUriConst.JMEWEBVIEW)
                    .withString("title", mContext.getResources().getString(R.string.main_statement_agreement_responsibility))
                    .withString("url", Constants.HttpConst.URL_DISCLAIMER)
                    .navigation();
        }

        public void onClickAgree() {
            SharedPreUtils.setBoolean(mContext, SharedPreUtils.Key_Statement, true);

            dismiss();
        }

    }

}
