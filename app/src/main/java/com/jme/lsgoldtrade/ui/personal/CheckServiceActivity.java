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
import com.jme.common.util.RxBus;
import com.jme.common.util.StringUtils;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseActivity;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.ActivityCheckServiceBinding;
import com.jme.lsgoldtrade.domain.BankVo;
import com.jme.lsgoldtrade.domain.LoginResponse;
import com.jme.lsgoldtrade.domain.UserInfoVo;
import com.jme.lsgoldtrade.service.ManagementService;
import com.jme.lsgoldtrade.service.UserService;
import com.jme.lsgoldtrade.service.WithholdAccountService;
import com.jme.lsgoldtrade.util.MarketUtil;
import com.jme.lsgoldtrade.view.ConfirmPopupwindow;
import com.jme.lsgoldtrade.view.SignedPopUpWindow;
import com.jme.lsgoldtrade.view.WithholdMessagePopUpWindow;

import java.math.BigDecimal;
import java.net.ConnectException;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Route(path = Constants.ARouterUriConst.CHECKSERVICE)
public class CheckServiceActivity extends JMEBaseActivity {

    private ActivityCheckServiceBinding mBinding;

    private int bClickFlag = 0;

    private SignedPopUpWindow mSignedPopUpWindow;
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

        mSignedPopUpWindow = new SignedPopUpWindow(this);
        mConfirmPopupwindow = new ConfirmPopupwindow(this);
        mWithholdMessagePopUpWindow = new WithholdMessagePopUpWindow(this);

        mBinding.tvNotSignedMessage.setText(R.string.increment_account_singed_message);
    }

    @Override
    protected void initListener() {
        super.initListener();

        mWithholdMessagePopUpWindow.setOnDismissListener(() -> {
            mBinding.layoutNotSigned.setVisibility(View.GONE);
            mBinding.layoutSigned.setVisibility(View.VISIBLE);

            gotoDetail();
            getCustomerSignBankList();
            getCustomerArrearage();
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

            gotoDetail();
            getCustomerSignBankList();
            getCustomerArrearage();
        }

    }

    private void gotoDetail() {
        setRightNavigation(getResources().getString(R.string.transaction_transfer_icbc_electronic_card_detail), 0, R.style.ToolbarThemeBlue,
                () -> ARouter.getInstance().build(Constants.ARouterUriConst.DETAILS).navigation());
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

    private void getCustomerSignBankList() {
        sendRequest(WithholdAccountService.getInstance().getCustomerSignBankList, new HashMap<>(), true);
    }

    private void getCustomerArrearage() {
        sendRequest(ManagementService.getInstance().getCustomerArrearage, new HashMap<>(), true);
    }

    @Override
    protected void DataReturn(DTRequest request, Head head, Object response) {
        super.DataReturn(request, head, response);

        switch (request.getApi().getName()) {
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
                        mUser.getCurrentUser().setIsSign("N");

                        if (0 == bClickFlag) {
                            mBinding.layoutNotSigned.setVisibility(View.VISIBLE);
                            mBinding.layoutSigned.setVisibility(View.GONE);
                        } else if (1 == bClickFlag) {
                            ARouter.getInstance()
                                    .build(Constants.ARouterUriConst.WITHHOLDCONTRACT)
                                    .withString("Resource", "Else")
                                    .navigation();
                        } else {
                            mBinding.layoutNotSigned.setVisibility(View.VISIBLE);
                            mBinding.layoutSigned.setVisibility(View.GONE);

                            if (null != mSignedPopUpWindow && !mSignedPopUpWindow.isShowing())
                                mSignedPopUpWindow.showAtLocation(mBinding.tvBankcard, Gravity.CENTER, 0, 0);
                        }

                        bClickFlag = 0;
                    } else {
                        mUser.getCurrentUser().setIsSign("Y");

                        if (0 == bClickFlag) {
                            mBinding.layoutNotSigned.setVisibility(View.GONE);
                            mBinding.layoutSigned.setVisibility(View.VISIBLE);

                            gotoDetail();
                            getCustomerSignBankList();
                            getCustomerArrearage();
                        } else if (1 == bClickFlag) {
                            if (null != mWithholdMessagePopUpWindow && !mWithholdMessagePopUpWindow.isShowing()) {
                                mWithholdMessagePopUpWindow.setData(getResources().getString(R.string.increment_account_withhold_signed),
                                        (view) -> mWithholdMessagePopUpWindow.dismiss());
                                mWithholdMessagePopUpWindow.showAtLocation(mBinding.tvBankcard, Gravity.CENTER, 0, 0);
                            }
                        } else if (2 == bClickFlag) {
                            ARouter.getInstance().build(Constants.ARouterUriConst.BANKCARD).navigation();
                        } else if (3 == bClickFlag) {
                            ARouter.getInstance().build(Constants.ARouterUriConst.WITHHOLD).navigation();
                        }

                        bClickFlag = 0;
                    }
                } else {
                    if (head.getCode().equals("-2012")) {
                        mUser.getCurrentUser().setIsSign("N");
                    }
                }

                break;
            case "GetCustomerArrearage":
                if (head.isSuccess()) {
                    String money = response.toString();

                    mBinding.layoutPay.setVisibility(TextUtils.isEmpty(money) || new BigDecimal(money).compareTo(BigDecimal.ZERO) == 0 ? View.GONE : View.VISIBLE);
                    mBinding.tvMoney.setText(TextUtils.isEmpty(money) ? "" : MarketUtil.decimalFormatMoney(MarketUtil.getPriceValue(money)));
                } else {
                    mBinding.layoutPay.setVisibility(View.GONE);
                }

                break;
            case "GetCustomerSignBankList":
                if (head.isSuccess()) {
                    List<BankVo> bankVoList;

                    try {
                        bankVoList = (List<BankVo>) response;
                    } catch (Exception e) {
                        bankVoList = null;

                        e.printStackTrace();
                    }

                    if (null == bankVoList || 0 == bankVoList.size())
                        return;

                    BankVo bankVo = bankVoList.get(0);

                    if (null == bankVo)
                        return;

                    mBinding.tvBankcard.setText(StringUtils.formatBankCardDefault(bankVo.getBankNo()));
                }

                break;
        }
    }

    public class ClickHandlers {

        public void onClickWithholdContract() {
            if (TextUtils.isEmpty(mUser.getAccountID())) {
                RxBus.getInstance().post(Constants.RxBusConst.RXBUS_TRANSACTION_PLACE_ORDER, null);
                ARouter.getInstance().build(Constants.ARouterUriConst.MAIN).navigation();

                finish();
            } else {
                bClickFlag = 1;

                queryLoginResult();
            }
        }

        public void onClickBankCard() {
            bClickFlag = 2;

            queryLoginResult();
        }

        public void onClickPaidPrompt() {
            if (null != mConfirmPopupwindow && !mConfirmPopupwindow.isShowing()) {
                mConfirmPopupwindow.setData(getResources().getString(R.string.increment_account_paid_prompt),
                        getResources().getString(R.string.text_confirm), (view) -> mConfirmPopupwindow.dismiss());
                mConfirmPopupwindow.showAtLocation(mBinding.tvBankcard, Gravity.CENTER, 0, 0);
            }
        }

        public void onClickTrde() {
            RxBus.getInstance().post(Constants.RxBusConst.RXBUS_TRANSACTION_PLACE_ORDER, null);
            ARouter.getInstance().build(Constants.ARouterUriConst.MAIN).navigation();

            finish();
        }

        public void onClickPay() {
            bClickFlag = 3;

            queryLoginResult();
        }

    }
}
