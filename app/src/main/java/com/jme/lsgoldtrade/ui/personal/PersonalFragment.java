package com.jme.lsgoldtrade.ui.personal;

import android.os.Bundle;
import android.view.View;

import com.alibaba.android.arouter.launcher.ARouter;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.common.util.StatusBarUtil;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseFragment;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.FragmentPersonalBinding;

public class PersonalFragment extends JMEBaseFragment {

    private FragmentPersonalBinding mBinding;

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_personal;
    }

    @Override
    protected void initView() {
        super.initView();

        mBinding = (FragmentPersonalBinding) mBindingUtil;

        StatusBarUtil.setStatusBarMode(mActivity, true, R.color.color_blue);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
    }

    @Override
    protected void initListener() {
        super.initListener();
    }

    @Override
    public void initBinding() {
        super.initBinding();

        mBinding.setHandlers(new ClickHandlers());
    }

    @Override
    public void onResume() {
        super.onResume();

        if (null == mUser || !mUser.isLogin()) {
            mBinding.tvAccount.setVisibility(View.GONE);
            mBinding.layoutLoginMessage.setVisibility(View.VISIBLE);
        } else {
            mBinding.tvAccount.setText(mUser.getCurrentUser().getTradeName());
            mBinding.tvAccount.setVisibility(View.VISIBLE);
            mBinding.layoutLoginMessage.setVisibility(View.GONE);
        }
    }

    @Override
    protected void DataReturn(DTRequest request, Head head, Object response) {
        super.DataReturn(request, head, response);
    }

    public class ClickHandlers {

        public void onClickLogin() {
            if (null == mUser || !mUser.isLogin())
                ARouter.getInstance()
                        .build(Constants.ARouterUriConst.ACCOUNTLOGIN)
                        .navigation();
        }

        public void onClickOpenAccountOnline() {

        }

        public void onClickCustomerService() {

        }

        public void onClickMessageCenter() {
            ARouter.getInstance()
                    .build(Constants.ARouterUriConst.NEWSCENTERACTIVITY)
                    .navigation();
        }

        public void onClickShare() {

        }

        public void onClickFeedback() {
            if (null == mUser || !mUser.isLogin())
                showNeedLoginDialog();
            else
                ARouter.getInstance()
                        .build(Constants.ARouterUriConst.FEEDBACK)
                        .navigation();
        }

        public void onClickSeeting() {
            ARouter.getInstance()
                    .build(Constants.ARouterUriConst.SETTING)
                    .navigation();
        }


    }

}
