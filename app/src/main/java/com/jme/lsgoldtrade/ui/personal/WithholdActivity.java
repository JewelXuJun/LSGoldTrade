package com.jme.lsgoldtrade.ui.personal;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseActivity;
import com.jme.lsgoldtrade.config.AppConfig;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.ActivityWithholdBinding;
import com.jme.lsgoldtrade.domain.LoginResponse;
import com.jme.lsgoldtrade.domain.PayIconVo;
import com.jme.lsgoldtrade.domain.UserInfoVo;
import com.jme.lsgoldtrade.domain.WechatPayVo;
import com.jme.lsgoldtrade.service.ManagementService;
import com.jme.lsgoldtrade.service.PaymentService;
import com.jme.lsgoldtrade.service.UserService;
import com.jme.lsgoldtrade.util.MarketUtil;
import com.jme.lsgoldtrade.util.PaymentHelper;
import com.jme.lsgoldtrade.view.SignedPopUpWindow;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.net.ConnectException;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Route(path = Constants.ARouterUriConst.WITHHOLD)
public class WithholdActivity extends JMEBaseActivity {

    private ActivityWithholdBinding mBinding;

    private String mMoney;

    private List<PayIconVo> mPayIconVoList;

    private IWXAPI mWxapi;
    private PaymentHelper mPaymentHelper;
    private SignedPopUpWindow mSignedPopUpWindow;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_withhold;
    }

    @Override
    protected void initView() {
        super.initView();

        initToolbar(R.string.increment_account_pay_title, true);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);

        mWxapi = WXAPIFactory.createWXAPI(this, AppConfig.WECHATAPPID, true);
        mPaymentHelper = new PaymentHelper();
        mSignedPopUpWindow = new SignedPopUpWindow(this);

        getCustomerArrearage();
        getPayIcon();
    }

    @Override
    protected void initListener() {
        super.initListener();
    }

    @Override
    protected void initBinding() {
        super.initBinding();

        mBinding = (ActivityWithholdBinding) mBindingUtil;
        mBinding.setHandlers(new ClickHandlers());
    }

    private void showPaymentBottomDialog(String money) {
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        View paymentView = View.inflate(this, R.layout.bottom_dialog_pay_list, null);

        ImageView imgCancel = paymentView.findViewById(R.id.img_cancel);
        RecyclerView recyclerView = paymentView.findViewById(R.id.recyclerView);
        TextView tvPay = paymentView.findViewById(R.id.tv_pay);

        PayTypeAdapter adapter = new PayTypeAdapter(this, null);

        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        tvPay.setText(String.format(getResources().getString(R.string.increment_account_pay_money),
                MarketUtil.decimalFormatMoney(MarketUtil.getPriceValue(money))));
        adapter.setNewData(mPayIconVoList);

        imgCancel.setOnClickListener(v -> dialog.dismiss());

        tvPay.setOnClickListener(v -> {
            dialog.dismiss();

            gotoPayment(money);
        });

        dialog.setContentView(paymentView);
        dialog.show();
    }

    private void gotoPayment(String money) {
        if (mWxapi.isWXAppInstalled())
            serviceFeePay(money);
        else
            showShortToast(R.string.text_wechat_uninstalled);
    }

    private void getCustomerArrearage() {
        sendRequest(ManagementService.getInstance().getCustomerArrearage, new HashMap<>(), true);
    }

    private void getPayIcon() {
        sendRequest(ManagementService.getInstance().getPayIcon, new HashMap<>(), false);
    }

    private void queryLoginResult(boolean enable) {
        DTRequest request = new DTRequest(UserService.getInstance().queryLoginResult, new HashMap<>(), enable, true);

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

    private void serviceFeePay(String money) {
        HashMap<String, String> params = new HashMap<>();
        params.put("totalFee", money);

        sendRequest(PaymentService.getInstance().serviceFeePay, params, true);
    }

    @Override
    protected void DataReturn(DTRequest request, Head head, Object response) {
        super.DataReturn(request, head, response);

        switch (request.getApi().getName()) {
            case "GetCustomerArrearage":
                if (head.isSuccess()) {
                    mMoney = response.toString();

                    mBinding.tvMoney.setText(TextUtils.isEmpty(mMoney) ? "" :
                            MarketUtil.decimalFormatMoney(MarketUtil.getPriceValue(mMoney)) + getResources().getString(R.string.text_money_unit));

                    if (!TextUtils.isEmpty(mMoney))
                        queryLoginResult(false);
                }

                break;
            case "GetPayIcon":
                if (head.isSuccess()) {
                    try {
                        mPayIconVoList = (List<PayIconVo>) response;
                    } catch (Exception e) {
                        mPayIconVoList = null;

                        e.printStackTrace();
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

                    String isSign = userInfoVo.getIsSign();

                    if (TextUtils.isEmpty(isSign) || isSign.equals("N")) {
                        mUser.getCurrentUser().setIsSign("N");

                        if (null != mSignedPopUpWindow && !mSignedPopUpWindow.isShowing())
                            mSignedPopUpWindow.showAtLocation(mBinding.tvMoney, Gravity.CENTER, 0, 0);
                    } else {
                        showPaymentBottomDialog(mMoney);
                    }
                } else {
                    if (head.getCode().equals("-2012"))
                        mUser.getCurrentUser().setIsSign("N");
                }

                break;
            case "ServiceFeePay":
                if (head.isSuccess()) {
                    WechatPayVo wechatPayVo;

                    try {
                        wechatPayVo = (WechatPayVo) response;
                    } catch (Exception e) {
                        wechatPayVo = null;
                        e.printStackTrace();
                    }

                    if (wechatPayVo == null)
                        return;

                    mPaymentHelper.startWeChatPay(this, wechatPayVo, "");
                }

                break;
        }
    }

    public class ClickHandlers {

        public void onClickPay() {
            if (!TextUtils.isEmpty(mMoney))
                queryLoginResult(true);
        }
    }

}
