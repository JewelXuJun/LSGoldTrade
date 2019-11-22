package com.jme.lsgoldtrade.ui.personal;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.google.gson.Gson;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.common.util.BigDecimalUtil;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseActivity;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.ActivityCheckServiceBinding;
import com.jme.lsgoldtrade.domain.LoginResponse;
import com.jme.lsgoldtrade.domain.UserInfoVo;
import com.jme.lsgoldtrade.domain.UsernameVo;
import com.jme.lsgoldtrade.service.AccountService;
import com.jme.lsgoldtrade.service.ManagementService;
import com.jme.lsgoldtrade.service.UserService;
import com.jme.lsgoldtrade.util.MarketUtil;
import com.jme.lsgoldtrade.view.ConfirmPopupwindow;
import com.jme.lsgoldtrade.view.WithholdMessagePopUpWindow;

import java.math.BigDecimal;
import java.net.ConnectException;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Route(path = Constants.ARouterUriConst.CHECKSERVICE)
public class CheckServiceActivity extends JMEBaseActivity {

    private ActivityCheckServiceBinding mBinding;

    private boolean bClickFlag = false;

    private ConfirmPopupwindow mConfirmPopupwindow;
    private WithholdMessagePopUpWindow mWithholdMessagePopUpWindow;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_check_service;
    }

    @Override
    protected void initView() {
        super.initView();

        initToolbar(R.string.increment_mine, true);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);

        mConfirmPopupwindow = new ConfirmPopupwindow(this);
        mWithholdMessagePopUpWindow = new WithholdMessagePopUpWindow(this);

        getRemainTradeDay();
    }

    @Override
    protected void initListener() {
        super.initListener();

        mWithholdMessagePopUpWindow.setOnDismissListener(() -> {
            mBinding.layoutNotSigned.setVisibility(View.GONE);
            mBinding.layoutSigned.setVisibility(View.VISIBLE);
        });
    }

    @Override
    protected void initBinding() {
        super.initBinding();

        mBinding = (ActivityCheckServiceBinding) mBindingUtil;
        mBinding.setHandlers(new ClickHandlers());
    }

    @Override
    protected void onResume() {
        super.onResume();

        String isSign = mUser.getCurrentUser().getIsSign();

        if (TextUtils.isEmpty(isSign) || isSign.equals("N")) {
            queryLoginResult();
        } else {
            mBinding.layoutNotSigned.setVisibility(View.GONE);
            mBinding.layoutSigned.setVisibility(View.VISIBLE);

            getCustomerArrearage();
        }

        getUserInfo();
    }

    private void getRemainTradeDay() {
        sendRequest(ManagementService.getInstance().getRemainTradeDay, new HashMap<>(), false);
    }

    private void queryLoginResult() {
        DTRequest request = new DTRequest(UserService.getInstance().queryLoginResult, new HashMap<>(), true, true);

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

    private void getUserInfo() {
        sendRequest(AccountService.getInstance().getUserInfo, new HashMap<>(), false);
    }

    private void hasWeChatWithdrawAuth() {
        sendRequest(AccountService.getInstance().hasWeChatWithdrawAuth, new HashMap<>(), true);
    }

    private void getCustomerArrearage() {
        sendRequest(ManagementService.getInstance().getCustomerArrearage, new HashMap<>(), true);
    }

    @Override
    protected void DataReturn(DTRequest request, Head head, Object response) {
        super.DataReturn(request, head, response);

        switch (request.getApi().getName()) {
            case "GetRemainTradeDay":
                if (head.isSuccess())
                    mBinding.tvNotSignedMessage.setText(String.format(getResources().getString(R.string.increment_not_signed_message), response.toString()));

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

                    String isSign = userInfoVo.getIsSign();

                    if (TextUtils.isEmpty(isSign) || isSign.equals("N")) {
                        if (bClickFlag) {
                            bClickFlag = false;

                            ARouter.getInstance().build(Constants.ARouterUriConst.WITHHOLDCONTRACT).navigation();
                        } else {
                            mBinding.layoutNotSigned.setVisibility(View.VISIBLE);
                            mBinding.layoutSigned.setVisibility(View.GONE);
                        }
                    } else {
                        if (bClickFlag) {
                            if (null != mWithholdMessagePopUpWindow && !mWithholdMessagePopUpWindow.isShowing()) {
                                bClickFlag = false;

                                mWithholdMessagePopUpWindow.setData(getResources().getString(R.string.increment_account_withhold_signed),
                                        (view) -> mWithholdMessagePopUpWindow.dismiss());
                                mWithholdMessagePopUpWindow.showAtLocation(mBinding.tvAvailableFunds, Gravity.CENTER, 0, 0);
                            }
                        } else {
                            mBinding.layoutNotSigned.setVisibility(View.GONE);
                            mBinding.layoutSigned.setVisibility(View.VISIBLE);
                        }

                        getCustomerArrearage();
                    }
                }

                break;
            case "GetUserInfo":
                if (head.isSuccess()) {
                    UsernameVo usernameVo;

                    try {
                        usernameVo = (UsernameVo) response;
                    } catch (Exception e) {
                        usernameVo = null;
                        e.printStackTrace();
                    }

                    if (null == usernameVo)
                        return;

                    mBinding.tvAvailableFunds.setText(TextUtils.isEmpty(usernameVo.getBalance()) ? getString(R.string.text_no_data_default) :
                            BigDecimalUtil.formatMoney(new BigDecimal(usernameVo.getBalance()).divide(new BigDecimal(100)).toPlainString()));
                    mBinding.tvFrozenFunds.setText(TextUtils.isEmpty(usernameVo.getFrozenBalance()) ? getString(R.string.text_no_data_default) :
                            BigDecimalUtil.formatMoney(new BigDecimal(usernameVo.getFrozenBalance()).divide(new BigDecimal(100)).toPlainString()));
                }

                break;
            case "HasWeChatWithdrawAuth":
                if (head.isSuccess()) {
                    String authFlag;
                    try {
                        authFlag = (String) response;
                    } catch (Exception e) {
                        authFlag = null;
                        e.printStackTrace();
                    }
                    if (TextUtils.isEmpty(authFlag))
                        return;

                    if (authFlag.equals("T"))   // 非首次
                        ARouter.getInstance().build(Constants.ARouterUriConst.WITHDRAW).navigation();
                    else
                        ARouter.getInstance().build(Constants.ARouterUriConst.CHECKUSERINFO).navigation();
                }

                break;
            case "GetCustomerArrearage":
                if (head.isSuccess()) {
                    String money = response.toString();

                    mBinding.layoutPay.setVisibility(TextUtils.isEmpty(money) || new BigDecimal(money).compareTo(BigDecimal.ZERO) == 0 ? View.GONE : View.VISIBLE);
                    mBinding.tvMoney.setText(TextUtils.isEmpty(money) ? "" : MarketUtil.decimalFormatMoney(MarketUtil.getPriceValue(money)));
                }

                break;
        }
    }

    public class ClickHandlers {

        public void onClickRecharge() {
            ARouter.getInstance()
                    .build(Constants.ARouterUriConst.RECHARGE)
                    .navigation();
        }

        public void onClickWithdraw() {
            hasWeChatWithdrawAuth();
        }

        public void onClickThaw() {
            showShortToast(R.string.personal_expect);
//            ARouter.getInstance().build(Constants.ARouterUriConst.THAW).navigation();
        }

        public void onClickDetailed() {
            ARouter.getInstance().build(Constants.ARouterUriConst.DETAILS).navigation();
        }

        public void onClickWithholdContract() {
            bClickFlag = true;

            queryLoginResult();
        }

        public void onClickTradingBox() {
            ARouter.getInstance().build(Constants.ARouterUriConst.TRADINGBOX).navigation();
        }

        public void onClickEntrust() {
            ARouter.getInstance().build(Constants.ARouterUriConst.ENTRUSTRISKMANAGEMENT).navigation();
        }

        public void onClickBankCard() {
            ARouter.getInstance().build(Constants.ARouterUriConst.BANKCARD).navigation();
        }

        public void onClickPaidPrompt() {
            if (null != mConfirmPopupwindow && !mConfirmPopupwindow.isShowing()) {
                mConfirmPopupwindow.setData(getResources().getString(R.string.increment_account_paid_prompt),
                        getResources().getString(R.string.text_confirm), (view) -> mConfirmPopupwindow.dismiss());
                mConfirmPopupwindow.showAtLocation(mBinding.tvAvailableFunds, Gravity.CENTER, 0, 0);
            }
        }

        public void onClickPay() {

        }

        public void onClickService() {

        }
    }
}
