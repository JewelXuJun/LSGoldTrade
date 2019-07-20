package com.jme.lsgoldtrade.ui.personal;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;

import com.alibaba.android.arouter.launcher.ARouter;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.common.util.RxBus;
import com.jme.common.util.StatusBarUtil;
import com.jme.common.util.StringUtils;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseFragment;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.FragmentPersonalBinding;
import com.jme.lsgoldtrade.service.ManagementService;

import java.util.HashMap;

/**
 * 我的
 */
public class PersonalFragment extends JMEBaseFragment {

    private FragmentPersonalBinding mBinding;

    private String mIncrementState;

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
            mBinding.layoutOpenAccount.setVisibility(View.VISIBLE);
            mBinding.layoutSubscribe.setVisibility(View.GONE);
            mBinding.tvIncrementState.setText("");
        } else {
            mBinding.tvAccount.setText(StringUtils.phoneInvisibleMiddle(mUser.getCurrentUser().getMobile()));
            mBinding.tvAccount.setVisibility(View.VISIBLE);
            mBinding.layoutLoginMessage.setVisibility(View.GONE);
            mBinding.layoutOpenAccount.setVisibility(TextUtils.isEmpty(mUser.getAccountID()) ? View.VISIBLE : View.GONE);
            mBinding.layoutSubscribe.setVisibility(View.VISIBLE);

            getUserAddedServicesStatus();
        }
    }

    private void getUserAddedServicesStatus() {
        sendRequest(ManagementService.getInstance().getUserAddedServicesStatus, new HashMap<>(), false, false, false);
    }

    @Override
    protected void DataReturn(DTRequest request, Head head, Object response) {
        super.DataReturn(request, head, response);

        switch (request.getApi().getName()) {
            case "GetUserAddedServicesStatus":
                if (null == response)
                    mIncrementState = "";
                else
                    mIncrementState = response.toString();

                mBinding.tvIncrementState.setText(mIncrementState.equals("T") ? R.string.personal_increment_state_open
                        : R.string.personal_increment_state_unopen);
                mBinding.tvIncrementState.setTextColor(mIncrementState.equals("T") ? ContextCompat.getColor(mContext, R.color.color_text_black)
                        : ContextCompat.getColor(mContext, R.color.color_ff3300));

                break;
        }
    }

    public class ClickHandlers {

        public void onClickLogin() {
            if (null == mUser || !mUser.isLogin())
                ARouter.getInstance().build(Constants.ARouterUriConst.ACCOUNTLOGIN).navigation();
        }

        public void onClickOpenAccountOnline() {
            if (null == mUser || !mUser.isLogin())
                ARouter.getInstance().build(Constants.ARouterUriConst.ACCOUNTLOGIN).navigation();
            else if (TextUtils.isEmpty(mUser.getAccountID()))
                RxBus.getInstance().post(Constants.RxBusConst.RXBUS_TRADE, null);
        }

        public void onClickIncrement() {
            if (null == mUser || !mUser.isLogin()) {
                ARouter.getInstance().build(Constants.ARouterUriConst.ACCOUNTLOGIN).navigation();
            } else {
                if (mIncrementState.equals("T"))
                    ARouter.getInstance().build(Constants.ARouterUriConst.CHECKSERVICE).navigation();
                else
                    ARouter.getInstance().build(Constants.ARouterUriConst.VALUEADDEDSERVICE).navigation();
            }
        }

        public void onClickCustomerService() {
            ARouter.getInstance().build(Constants.ARouterUriConst.CUSTOMSERVICE).navigation();
        }

        public void onClickMessageCenter() {
            if (null == mUser || !mUser.isLogin())
                ARouter.getInstance().build(Constants.ARouterUriConst.ACCOUNTLOGIN).navigation();
            else
                ARouter.getInstance().build(Constants.ARouterUriConst.NEWSCENTERACTIVITY).navigation();
        }

        public void onClickFastManagement() {
            ARouter.getInstance()
                    .build(Constants.ARouterUriConst.FASTENTRY)
                    .navigation();
        }

        public void onClickSubscribe() {
            ARouter.getInstance()
                    .build(Constants.ARouterUriConst.TRADINGBOX)
                    .navigation();
        }

        public void onClickShare() {
            showShortToast(R.string.personal_expect);
        }

        public void onClickAbout() {
            ARouter.getInstance().build(Constants.ARouterUriConst.ABOUT).navigation();
        }

        public void onClickSeeting() {
            ARouter.getInstance().build(Constants.ARouterUriConst.SETTING).navigation();
        }

    }

}
