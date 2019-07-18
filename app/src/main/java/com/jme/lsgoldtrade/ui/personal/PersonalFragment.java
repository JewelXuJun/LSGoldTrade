package com.jme.lsgoldtrade.ui.personal;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.alibaba.android.arouter.launcher.ARouter;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.common.util.StatusBarUtil;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseFragment;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.config.User;
import com.jme.lsgoldtrade.databinding.FragmentPersonalBinding;
import com.jme.lsgoldtrade.service.ManagementService;
import com.jme.lsgoldtrade.util.JumpActivity;
import com.jme.lsgoldtrade.util.NormalUtils;

import java.util.HashMap;

/**
 * 我的
 */
public class PersonalFragment extends JMEBaseFragment {

    private FragmentPersonalBinding mBinding;

    private static final int REQUEST_CODE_ASK_CALL_PHONE = 0x126;

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
            String phone = NormalUtils.settingphone(mUser.getCurrentUser().getMobile());
            mBinding.tvAccount.setText(phone);
            mBinding.tvAccount.setVisibility(View.VISIBLE);
            mBinding.layoutLoginMessage.setVisibility(View.GONE);
        }
    }

    private void callCustomer() {
        if (Build.VERSION.SDK_INT >= 23) {
            int checkCallPhonePermission = ContextCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE);

            if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(mActivity, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CODE_ASK_CALL_PHONE);
                return;
            } else {
                callPhone();
            }
        } else {
            callPhone();
        }
    }

    private void callPhone() {
        Intent intent;
        intent = new Intent("android.intent.action.CALL", Uri.parse("tel:4008276006"));

        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_CALL_PHONE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    callCustomer();

                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);

                break;
        }
    }

    public class ClickHandlers {

        public void onClickLogin() {
            if (null == mUser || !mUser.isLogin())
                ARouter.getInstance()
                        .build(Constants.ARouterUriConst.ACCOUNTLOGIN)
                        .navigation();
        }

        public void onClickOpenAccountOnline() {
            JumpActivity.jumpSmall(mContext);
        }

        public void onClickCustomerService() {
//            callCustomer();
            ARouter.getInstance()
                    .build(Constants.ARouterUriConst.CUSTOMSERVICE)
                    .navigation();
        }

        public void onClickMessageCenter() {
            if (null == mUser || !mUser.isLogin())
                showNeedLoginDialog();
            else
                ARouter.getInstance()
                        .build(Constants.ARouterUriConst.NEWSCENTERACTIVITY)
                        .navigation();
        }

        public void onClickShare() {
            showShortToast(R.string.personal_expect);
        }

        public void onClickAbout() {
            ARouter.getInstance()
                    .build(Constants.ARouterUriConst.ABOUT)
                    .navigation();
        }

        public void onClickSeeting() {
            ARouter.getInstance()
                    .build(Constants.ARouterUriConst.SETTING)
                    .navigation();
        }

        public void onClickSubscribe() {
            ARouter.getInstance()
                    .build(Constants.ARouterUriConst.TRADINGBOX)
                    .navigation();
        }

        public void onClickHomeTab() {
            ARouter.getInstance()
                    .build(Constants.ARouterUriConst.FASTENTRY)
                    .navigation();
        }

        public void onClickValueAddedService() {
            if (User.getInstance().isLogin()) {
                sendRequest(ManagementService.getInstance().getUserAddedServicesStatus, new HashMap<>(), false);
            } else {
                showNeedLoginDialog();
            }
        }
    }

    @Override
    protected void DataReturn(DTRequest request, Head head, Object response) {
        super.DataReturn(request, head, response);
        switch (request.getApi().getName()) {
            case "GetUserAddedServicesStatus":
                if (head.isSuccess()) {
                    ARouter.getInstance()
                            .build(Constants.ARouterUriConst.CHECKSERVICE)
                            .navigation();
                } else {
                    ARouter.getInstance()
                            .build(Constants.ARouterUriConst.VALUEADDEDSERVICE)
                            .navigation();
                }
                break;
        }
    }
}
