package com.jme.lsgoldtrade.ui.personal;

import android.os.Bundle;

import androidx.core.content.ContextCompat;

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
import com.jme.lsgoldtrade.domain.PasswordInfoVo;
import com.jme.lsgoldtrade.domain.SubscribeStateVo;
import com.jme.lsgoldtrade.service.ManagementService;

import java.util.HashMap;
import java.util.List;

public class PersonalFragment extends JMEBaseFragment {

    private FragmentPersonalBinding mBinding;

    private boolean bHidden = false;

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_personal;
    }

    @Override
    protected void initView() {
        super.initView();

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

        mBinding = (FragmentPersonalBinding) mBindingUtil;
        mBinding.setHandlers(new ClickHandlers());
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

        bHidden = hidden;

        if (!bHidden)
            StatusBarUtil.setStatusBarMode(mActivity, true, R.color.color_blue);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (!bHidden)
            StatusBarUtil.setStatusBarMode(mActivity, true, R.color.color_blue);

        if (null == mUser || !mUser.isLogin()) {
            mBinding.tvAccount.setVisibility(View.GONE);
            mBinding.layoutAccountSecurity.setVisibility(View.GONE);
            mBinding.layoutLoginMessage.setVisibility(View.VISIBLE);
            mBinding.layoutOpenAccount.setVisibility(View.VISIBLE);
            mBinding.layoutSubscribe.setVisibility(View.GONE);
            mBinding.tvIncrementState.setText("");
        } else {
            mBinding.tvAccount.setText(StringUtils.phoneInvisibleMiddle(mUser.getCurrentUser().getMobile()));
            mBinding.tvAccount.setVisibility(View.VISIBLE);
            mBinding.layoutAccountSecurity.setVisibility(TextUtils.isEmpty(mUser.getAccountID()) ? View.GONE : View.VISIBLE);
            mBinding.layoutLoginMessage.setVisibility(View.GONE);
            mBinding.layoutOpenAccount.setVisibility(TextUtils.isEmpty(mUser.getAccountID()) ? View.VISIBLE : View.GONE);

            getListExt();
        }
    }

    private void getUserPasswordSettingInfo() {
        sendRequest(ManagementService.getInstance().getUserPasswordSettingInfo, new HashMap<>(), true);
    }

    private void getListExt() {
        sendRequest(ManagementService.getInstance().getListExt, new HashMap<>(), false, false, false);
    }

    @Override
    protected void DataReturn(DTRequest request, Head head, Object response) {
        super.DataReturn(request, head, response);

        switch (request.getApi().getName()) {
            case "GetUserPasswordSettingInfo":
                if (head.isSuccess()) {
                    PasswordInfoVo passwordInfoVo;

                    try {
                        passwordInfoVo = (PasswordInfoVo) response;
                    } catch (Exception e) {
                        passwordInfoVo = null;

                        e.printStackTrace();
                    }

                    if (null == passwordInfoVo)
                        return;

                    String hasSettingDigital = passwordInfoVo.getHasSettingDigital();

                    if (TextUtils.isEmpty(hasSettingDigital))
                        return;

                    if (hasSettingDigital.equals("N"))
                        RxBus.getInstance().post(Constants.RxBusConst.RXBUS_TRADING_PASSWORD_SETTING, null);
                    else
                        ARouter.getInstance().build(Constants.ARouterUriConst.ACCOUNTSECURITY).navigation();
                }

                break;
            case "GetListExt":
                if (head.isSuccess()) {
                    SubscribeStateVo subscribeStateVo;

                    try {
                        subscribeStateVo = (SubscribeStateVo) response;
                    } catch (Exception e) {
                        subscribeStateVo = null;

                        e.printStackTrace();
                    }

                    if (null == subscribeStateVo) {
                        mBinding.layoutSubscribe.setVisibility(View.GONE);
                    } else {
                        List<SubscribeStateVo.SubscribeBean> subscribeBeanList = subscribeStateVo.getList();

                        if (null == subscribeBeanList || 0 == subscribeBeanList.size()) {
                            mBinding.layoutSubscribe.setVisibility(View.GONE);
                        } else {
                            mBinding.layoutSubscribe.setVisibility(View.VISIBLE);

                            int num = subscribeStateVo.getNum();

                            mBinding.tvTradingBox.setText(0 == num ? R.string.personal_trading_box_new : R.string.personal_trading_box_publish);
                            mBinding.tvTradingBox.setTextColor(0 == num ? ContextCompat.getColor(mContext, R.color.color_text_normal)
                                    : ContextCompat.getColor(mContext, R.color.color_red));
                        }

                    }
                }

                break;
        }
    }

    public class ClickHandlers {

        public void onClickLogin() {
            if (null == mUser || !mUser.isLogin())
                gotoLogin();
        }

        public void onClickAccountSecurity() {
            if (null == mUser || !mUser.isLogin())
                gotoLogin();
            else
                getUserPasswordSettingInfo();
        }

        public void onClickOpenAccount() {
            if (null == mUser || !mUser.isLogin())
                gotoLogin();
            else if (TextUtils.isEmpty(mUser.getAccountID()))
                RxBus.getInstance().post(Constants.RxBusConst.RXBUS_TRANSACTION_PLACE_ORDER, null);
        }

        public void onClickIncrement() {
            if (null == mUser || !mUser.isLogin())
                gotoLogin();
            else
                ARouter.getInstance().build(Constants.ARouterUriConst.CHECKSERVICE).navigation();
        }

        public void onClickCustomerService() {
            ARouter.getInstance().build(Constants.ARouterUriConst.CUSTOMSERVICE).navigation();
        }

        public void onClickMessageCenter() {
            if (null == mUser || !mUser.isLogin())
                gotoLogin();
            else
                ARouter.getInstance().build(Constants.ARouterUriConst.NEWSCENTERACTIVITY).navigation();
        }

        public void onClickFastManagement() {
            ARouter.getInstance().build(Constants.ARouterUriConst.FASTMANAGEMENT).navigation();
        }

        public void onClickSubscribe() {
            ARouter.getInstance().build(Constants.ARouterUriConst.TRADINGBOX).navigation();
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
