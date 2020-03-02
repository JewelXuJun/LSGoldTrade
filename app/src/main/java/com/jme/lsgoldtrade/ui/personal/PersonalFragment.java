package com.jme.lsgoldtrade.ui.personal;

import android.os.Build;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.core.hardware.fingerprint.FingerprintManagerCompat;

import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

import com.alibaba.android.arouter.launcher.ARouter;
import com.google.gson.Gson;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.common.util.RxBus;
import com.jme.common.util.StatusBarUtil;
import com.jme.common.util.StringUtils;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseFragment;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.FragmentPersonalBinding;
import com.jme.lsgoldtrade.domain.LoginResponse;
import com.jme.lsgoldtrade.domain.PasswordInfoVo;
import com.jme.lsgoldtrade.domain.SubscribeStateVo;
import com.jme.lsgoldtrade.domain.UserInfoVo;
import com.jme.lsgoldtrade.service.ManagementService;
import com.jme.lsgoldtrade.service.UserService;
import com.jme.lsgoldtrade.view.StockUserDialog;

import java.net.ConnectException;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PersonalFragment extends JMEBaseFragment {

    private FragmentPersonalBinding mBinding;

    private boolean bHidden = false;
    private int mCallEntry = 0;


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
            queryLoginResult();
        }
    }

    private void getUserPasswordSettingInfo() {
        sendRequest(ManagementService.getInstance().getUserPasswordSettingInfo, new HashMap<>(), true);
    }

    private void getListExt() {
        sendRequest(ManagementService.getInstance().getListExt, new HashMap<>(), false, false, false);
    }

    private void queryLoginResult() {
        DTRequest request = new DTRequest(UserService.getInstance().queryLoginResult, new HashMap<>(), false, false);

        Call restResponse = request.getApi().request(request.getParams());

        restResponse.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                Head head = new Head();
                Object body = "";

                if (response.raw().code() != 200) {
                    head.setSuccess(false);
                    head.setCode("" + response.raw().code());
                    head.setMsg("服务器异常");
                } else {
                    if (!request.getApi().isResponseJson()) {
                        body = response.body();
                        head.setSuccess(true);
                        head.setCode("0");
                        head.setMsg("成功");
                    } else {
                        LoginResponse dtResponse = (LoginResponse) response.body();

                        head = new Head();
                        head.setCode(dtResponse.getCode());
                        head.setMsg(dtResponse.getMsg());

                        try {
                            body = new Gson().fromJson(dtResponse.getBodyToString(),
                                    request.getApi().getEntryType());
                        } catch (Exception e) {
                            body = dtResponse.getBodyToString();
                        }
                    }
                }

                OnResult(request, head, body);
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Head head = new Head();
                final Throwable cause = t.getCause() != null ? t.getCause() : t;

                if (cause != null) {
                    if (cause instanceof ConnectException) {
                        head.setSuccess(false);
                        head.setCode("500");
                        head.setMsg(getResources().getString(com.jme.common.R.string.text_error_server));
                    } else {
                        head.setSuccess(false);
                        head.setCode("408");
                        head.setMsg(getResources().getString(com.jme.common.R.string.text_error_timeout));
                    }
                }

                OnResult(request, head, null);
            }
        });
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

                    String hasTimeout = passwordInfoVo.getHasTimeout();
                    String hasSettingDigital = passwordInfoVo.getHasSettingDigital();
                    String hasOpenFingerPrint = passwordInfoVo.getHasOpenFingerPrint();
                    String hasOpenGestures = passwordInfoVo.getHasOpenGestures();
//                    if (TextUtils.isEmpty(hasSettingDigital))
//                        return;

                    if (TextUtils.isEmpty(hasSettingDigital)||hasSettingDigital.equals("N")) {
                        RxBus.getInstance().post(Constants.RxBusConst.RXBUS_TRADING_PASSWORD_SETTING, null);
                    }else {
                        if(mCallEntry == 8){

                            if (TextUtils.isEmpty(hasTimeout) || hasTimeout.equals("N")){
                                ARouter.getInstance().build(Constants.ARouterUriConst.TRADINGBOX).navigation();
                                return;
                            }
                            int type = 1;
                            if (!TextUtils.isEmpty(hasOpenFingerPrint) && hasOpenFingerPrint.equals("Y")) {
                                boolean isCanUseFingerPrint = false;

                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    if (FingerprintManagerCompat.from(mContext).isHardwareDetected()
                                            && FingerprintManagerCompat.from(mContext).hasEnrolledFingerprints())
                                        isCanUseFingerPrint = true;
                                }

                                if (isCanUseFingerPrint) {
                                    type = 2;
                                } else {
                                    if (!TextUtils.isEmpty(hasOpenGestures) && hasOpenGestures.equals("Y"))
                                        type = 3;
                                    else
                                        type = 1;
                                }
                            } else if (!TextUtils.isEmpty(hasOpenGestures) && hasOpenGestures.equals("Y")) {
                                type = 3;
                            } else if (passwordInfoVo.getHasTimeout().equals("Y")) {
                                type = 1;
                            }
                            ARouter.getInstance()
                                    .build(Constants.ARouterUriConst.UNLOCKTRADINGPASSWORD)
                                    .withInt("Type", type)
                                    .withInt("callEntry",mCallEntry)
                                    .navigation();
                        }else {
                            ARouter.getInstance().build(Constants.ARouterUriConst.ACCOUNTSECURITY).navigation();
                        }
                        mCallEntry = 0;
                    }
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
                case "QueryLoginResult":
                    if (head.isSuccess()) {
                        UserInfoVo userInfoVo;

                        try {
                            userInfoVo = (UserInfoVo) response;
                        } catch (Exception e) {
                            userInfoVo = null;

                            e.printStackTrace();
                        }
                       String isOpen = userInfoVo.getIsOpen();
                        if("0".equals(isOpen)||"-2015".equals(isOpen)||"-2019".equals(isOpen)) {
                            //已开通
                            mBinding.tvIncrementState.setText("已开通");
                        }else if("-2013".equals(isOpen)) {
                            //开通中
                            mBinding.tvIncrementState.setText("开通中");
                        }else if("-2012".equals(isOpen)||"-2016".equals(isOpen)||"-2017".equals(isOpen)||"-2018".equals(isOpen)) {
                            //未开通
                            mBinding.tvIncrementState.setText("未开通");
                        }else if("-2014".equals(isOpen)) {
                            //关闭中
                            mBinding.tvIncrementState.setText("关闭审核中");
                        }else {
                            mBinding.tvIncrementState.setText("");
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
//            ARouter.getInstance().build(Constants.ARouterUriConst.OPENINCREMENT).navigation();
            if (null == mUser || !mUser.isLogin()) {
                gotoLogin();
            }else if(TextUtils.isEmpty(mUser.getAccountID())){
                RxBus.getInstance().post(Constants.RxBusConst.RXBUS_TRANSACTION_PLACE_ORDER, null);
            }else {
                ARouter.getInstance().build(Constants.ARouterUriConst.CHECKSERVICE).navigation();
            }
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
            mCallEntry = 8;
            getUserPasswordSettingInfo();

//            ARouter.getInstance().build(Constants.ARouterUriConst.TRADINGBOX).navigation();
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
