package com.jme.lsgoldtrade.ui.personal;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import androidx.core.content.ContextCompat;

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
import com.jme.lsgoldtrade.view.CloseIncrementPopUpWindow;
import com.jme.lsgoldtrade.view.ConfirmPopupwindow;
import com.jme.lsgoldtrade.view.IncrementAlertDialog;
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
    private String isOpen = "";
    private CloseIncrementPopUpWindow mCloseIncrementPopUpWindow;
    private IncrementAlertDialog mWithholdAlertDialog;
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
        mCloseIncrementPopUpWindow = new CloseIncrementPopUpWindow(this);
        mCloseIncrementPopUpWindow.setOutsideTouchable(false);
        mCloseIncrementPopUpWindow.setFocusable(false);

        gotoDetail();
//        mBinding.tvNotSignedMessage.setText(R.string.increment_account_singed_message);
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

//        String isSign = mUser.getCurrentUser().getIsSign();

//        if (TextUtils.isEmpty(isSign) || isSign.equals("N")) {
            queryLoginResult();
//        } else {
//            mBinding.layoutNotSigned.setVisibility(View.GONE);
//            mBinding.layoutSigned.setVisibility(View.VISIBLE);
//
//            gotoDetail();
//            getCustomerSignBankList();
//            getCustomerArrearage();
//        }
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

    private void checkValueAddedServicesForClose() {
        HashMap<String, String> params = new HashMap<>();
        sendRequest(ManagementService.getInstance().checkValueAddedServicesForClose, params, true,false,false);
    }

    private void closeValueAddedServices() {
        sendRequest(WithholdAccountService.getInstance().closeValueAddedServices, new HashMap<>(), true);
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

//                    String isSign = userInfoVo.getIsSign();
//
//                    if (TextUtils.isEmpty(isSign) || isSign.equals("N")) {
//                        mUser.getCurrentUser().setIsSign("N");
//
//                        if (0 == bClickFlag) {
//                            mBinding.layoutNotSigned.setVisibility(View.VISIBLE);
//                            mBinding.layoutSigned.setVisibility(View.GONE);
//                        } else if (1 == bClickFlag) {
//                            ARouter.getInstance()
//                                    .build(Constants.ARouterUriConst.WITHHOLDCONTRACT)
//                                    .withString("Resource", "Else")
//                                    .navigation();
//                        } else {
//                            mBinding.layoutNotSigned.setVisibility(View.VISIBLE);
//                            mBinding.layoutSigned.setVisibility(View.GONE);
//
//                            if (null != mSignedPopUpWindow && !mSignedPopUpWindow.isShowing())
//                                mSignedPopUpWindow.showAtLocation(mBinding.tvBankcard, Gravity.CENTER, 0, 0);
//                        }
//
//                        bClickFlag = 0;
//                    } else {
//                        mUser.getCurrentUser().setIsSign("Y");
//
//                        if (0 == bClickFlag) {
//                            mBinding.layoutNotSigned.setVisibility(View.GONE);
//                            mBinding.layoutSigned.setVisibility(View.VISIBLE);
//
//                            gotoDetail();
//                            getCustomerSignBankList();
//                            getCustomerArrearage();
//                        } else if (1 == bClickFlag) {
//                            if (null != mWithholdMessagePopUpWindow && !mWithholdMessagePopUpWindow.isShowing()) {
//                                mWithholdMessagePopUpWindow.setData(getResources().getString(R.string.increment_account_withhold_signed),
//                                        (view) -> mWithholdMessagePopUpWindow.dismiss());
//                                mWithholdMessagePopUpWindow.showAtLocation(mBinding.tvBankcard, Gravity.CENTER, 0, 0);
//                            }
//                        } else if (2 == bClickFlag) {
//                            ARouter.getInstance().build(Constants.ARouterUriConst.BANKCARD).navigation();
//                        } else if (3 == bClickFlag) {
//                            ARouter.getInstance().build(Constants.ARouterUriConst.WITHHOLD).navigation();
//                        }
//
//                        bClickFlag = 0;
//                    }

                    // 0:已开通,-2012:首次未开通,-2013:开通中,-2014:已开通关闭审核中,-2015:增值服务已欠费,-2016:极简模式已欠费,-2017:首次未开通未弹框,-2018:已关闭,-2019:过渡期

                       isOpen = userInfoVo.getIsOpen();
                       mBinding.layoutSigned.setVisibility(View.VISIBLE);
                       if("0".equals(isOpen)||"-2015".equals(isOpen)||"-2019".equals(isOpen)){
                           //已开通
                                mBinding.tvIncrementDescribe.setText("更多增值服务，请前往交易使用。");
                                mBinding.tvTrade.setVisibility(View.VISIBLE);
                               getCustomerArrearage();
                               getCustomerSignBankList();
                               mBinding.layoutBank.setVisibility(View.VISIBLE);
                               mBinding.btnIncrementConfirm.setVisibility(View.VISIBLE);
                               setBtnDrawable(false,"关闭增值服务");


                       }else if("-2013".equals(isOpen)){
                           //开通中
                           mBinding.tvIncrementDescribe.setText("您尚未开通增值服务，立即开通即可享用更多增值服务。");
                           mBinding.tvTrade.setVisibility(View.GONE);
                           mBinding.layoutBank.setVisibility(View.GONE);
                           mBinding.layoutPay.setVisibility(View.GONE);
                           mBinding.btnIncrementConfirm.setVisibility(View.VISIBLE);
                           setBtnDrawable(false,"开通中");

                       }else if("-2012".equals(isOpen)||"-2016".equals(isOpen)||"-2017".equals(isOpen)||"-2018".equals(isOpen)){
                           //未开通
                           mBinding.tvIncrementDescribe.setText("您尚未签约增值服务，立即开通即可享用更多增值服务。");
                           mBinding.tvTrade.setVisibility(View.GONE);
                           setBtnDrawable(true,"开通增值服务");
                           if("-2012".equals(isOpen)||"-2017".equals(isOpen)){
                               mBinding.layoutBank.setVisibility(View.GONE);
                               mBinding.btnIncrementConfirm.setVisibility(View.VISIBLE);
                           }else{
                               getCustomerArrearage();
                               getCustomerSignBankList();
                               mBinding.layoutBank.setVisibility(View.VISIBLE);
                               mBinding.btnIncrementConfirm.setVisibility(View.GONE);
                           }
                       }else if("-2014".equals(isOpen)){
                           //关闭中
                           mBinding.tvIncrementDescribe.setText("更多增值服务，请前往交易使用。");
                           mBinding.tvTrade.setVisibility(View.VISIBLE);
                           getCustomerArrearage();
                           getCustomerSignBankList();
                           mBinding.layoutBank.setVisibility(View.VISIBLE);
                           mBinding.btnIncrementConfirm.setVisibility(View.VISIBLE);

                           setBtnDrawable(false,"关闭审核中");
                       }

                } else {
//                    if (head.getCode().equals("-2012")) {
//                        mUser.getCurrentUser().setIsSign("N");
//                    }
                }

                break;
            case "GetCustomerArrearage":
                if (head.isSuccess()) {
                    String money = response.toString();

                    mBinding.layoutPay.setVisibility(TextUtils.isEmpty(money) || new BigDecimal(money).compareTo(BigDecimal.ZERO) == 0 ? View.GONE : View.VISIBLE);
                    mBinding.tvMoney.setText(TextUtils.isEmpty(money) ? "" : MarketUtil.decimalFormatMoney(MarketUtil.getPriceValue(money)));
                    if((TextUtils.isEmpty(money) || new BigDecimal(money).compareTo(BigDecimal.ZERO) == 0)&&"-2018".equals(isOpen)){
                        mBinding.btnIncrementConfirm.setVisibility(View.VISIBLE);
                    }
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
                case "CheckValueAddedServicesForClose":
                        if(head.isSuccess()){
                            closeValueAddedServices();
                        }else{
                            showWithholdAlertDialog("您当前有在运行中的增值服务订单，请等待执行完毕或者主动撤销后再申请关闭增值服务。",(v)->{
                                mWithholdAlertDialog.dismiss();
                            });
                        }
                    break;
            case "CloseValueAddedServices":
                    if(head.isSuccess()){
                        showWithholdAlertDialog("申请已提交,请耐心等待审核",(v)->{
                            queryLoginResult();
                            mWithholdAlertDialog.dismiss();
                        });
                    }
                break;
        }
    }

    public class ClickHandlers {

        public void onClickWithholdContract() {
//            if (TextUtils.isEmpty(mUser.getAccountID())) {
//                RxBus.getInstance().post(Constants.RxBusConst.RXBUS_TRANSACTION_PLACE_ORDER, null);
//                ARouter.getInstance().build(Constants.ARouterUriConst.MAIN).navigation();
//
//                finish();
//            } else {
//                bClickFlag = 1;
//
//                queryLoginResult();
//            }
            if(TextUtils.isEmpty(isOpen)){
                queryLoginResult();
                return;
            }

            if("-2013".equals(isOpen)||"-2014".equals(isOpen)){
                //开通中与关闭中
                return;
            }
            if("-2012".equals(isOpen)||"-2016".equals(isOpen)||"-2017".equals(isOpen)||"-2018".equals(isOpen)){
                if (TextUtils.isEmpty(mUser.getAccountID())) {
                    RxBus.getInstance().post(Constants.RxBusConst.RXBUS_TRANSACTION_PLACE_ORDER, null);
                    ARouter.getInstance().build(Constants.ARouterUriConst.MAIN).navigation();

                    finish();
                }else{
                    ARouter.getInstance().build(Constants.ARouterUriConst.OPENINCREMENT).navigation();
                }
                return;
            }

            if("0".equals(isOpen)||"-2015".equals(isOpen)||"-2019".equals(isOpen)){
                //关闭增值服务
                if (null != mCloseIncrementPopUpWindow && !mCloseIncrementPopUpWindow.isShowing()) {
                    mCloseIncrementPopUpWindow.setData((v)->{
                        checkValueAddedServicesForClose();
                        mCloseIncrementPopUpWindow.dismiss();
                    });
                    mCloseIncrementPopUpWindow.showAtLocation(mBinding.getRoot(), Gravity.CENTER, 0, 0);
                }


                return;
            }



        }

        public void onClickBankCard() {
//            bClickFlag = 2;
//
//            queryLoginResult();
            ARouter.getInstance().build(Constants.ARouterUriConst.BANKCARD).navigation();
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
//            bClickFlag = 3;
//
//            queryLoginResult();
            ARouter.getInstance().build(Constants.ARouterUriConst.WITHHOLD).navigation();
        }

        public void onClickIncrementBtn(){


        }

    }

    private void showWithholdAlertDialog(String tx, View.OnClickListener click) {
        if (isFinishing)
            return;

//        if (null == mWithholdAlertDialog)
            mWithholdAlertDialog = new IncrementAlertDialog(mContext,tx,click);

        if (!mWithholdAlertDialog.isShowing()) {
            mWithholdAlertDialog.show();

            DisplayMetrics dm = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(dm);
            WindowManager.LayoutParams lp = mWithholdAlertDialog.getWindow().getAttributes();
            lp.width = (int) (dm.widthPixels*0.75); //设置宽度
            mWithholdAlertDialog.getWindow().setAttributes(lp);

        }
    }




    private void setBtnDrawable(boolean isBlue,String str){
        if(isBlue) {
            mBinding.btnIncrementConfirm.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_btn_blue_solid));
            mBinding.btnIncrementConfirm.setTextColor(ContextCompat.getColor(this,R.color.white));
        }else{
            mBinding.btnIncrementConfirm.setBackground(ContextCompat.getDrawable(this,R.drawable.shape_increment_btn_gray));
            mBinding.btnIncrementConfirm.setTextColor(ContextCompat.getColor(this,R.color.color_text_gray_hint));
        }
        mBinding.btnIncrementConfirm.setText(str);
    }

    private void setLayoutVisibleOrGone(int flag){
        //全部显示 两种状态 关闭审核中 关闭增值服务
        if(flag == 1){
            mBinding.tvIncrementDescribe.setText("更多增值服务，请前往交易使用。");
            mBinding.layoutSigned.setVisibility(View.VISIBLE);
            mBinding.layoutPay.setVisibility(View.VISIBLE);
            mBinding.btnIncrementConfirm.setVisibility(View.VISIBLE);
            setBtnDrawable(false,"关闭审核中");
            setBtnDrawable(false,"关闭增值服务");
        }else if(flag==2){
            //隐藏去支付layout 3种状态 关闭审核中 关闭增值服务 正常用户关闭生效后

            mBinding.layoutSigned.setVisibility(View.VISIBLE);
            mBinding.layoutPay.setVisibility(View.GONE);
            mBinding.btnIncrementConfirm.setVisibility(View.VISIBLE);

            mBinding.tvIncrementDescribe.setText("更多增值服务，请前往交易使用。");
            setBtnDrawable(false,"关闭审核中");
            setBtnDrawable(false,"关闭增值服务");
            mBinding.tvIncrementDescribe.setText("您尚未开通增值服务，立即开通即可享用更多增值服务。");
            setBtnDrawable(true,"开通增值服务");
        }else if(flag==3){
            //只有按钮隐藏其它 两种状态 增值服务开通中 增值服务未开通
            mBinding.tvIncrementDescribe.setText("您尚未签约增值服务，立即开通即可享用更多增值服务。");
            mBinding.layoutSigned.setVisibility(View.GONE);
            mBinding.layoutPay.setVisibility(View.GONE);
            mBinding.btnIncrementConfirm.setVisibility(View.VISIBLE);
            setBtnDrawable(false,"开通中");
            setBtnDrawable(false,"开通增值服务");
        }else{
            //只隐藏按钮 1种状态
            mBinding.tvIncrementDescribe.setText("您尚未开通增值服务，立即开通即可享用更多增值服务。");
            mBinding.layoutSigned.setVisibility(View.VISIBLE);
            mBinding.layoutPay.setVisibility(View.VISIBLE);
            mBinding.btnIncrementConfirm.setVisibility(View.GONE);
        }
    }
}
