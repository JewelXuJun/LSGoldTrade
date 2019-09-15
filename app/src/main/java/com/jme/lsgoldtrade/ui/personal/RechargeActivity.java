package com.jme.lsgoldtrade.ui.personal;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.BottomSheetDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alipay.sdk.app.PayTask;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.common.util.BigDecimalUtil;
import com.jme.common.util.DialogHelp;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseActivity;
import com.jme.lsgoldtrade.config.AppConfig;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.ActivityRechargeBinding;
import com.jme.lsgoldtrade.domain.UsernameVo;
import com.jme.lsgoldtrade.domain.WechatPayVo;
import com.jme.lsgoldtrade.service.AccountService;
import com.jme.lsgoldtrade.service.PaymentService;
import com.jme.lsgoldtrade.util.PayResult;
import com.jme.lsgoldtrade.util.PaymentHelper;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * 在线充值
 */
@Route(path = Constants.ARouterUriConst.RECHARGE)
public class RechargeActivity extends JMEBaseActivity {

    private ActivityRechargeBinding mBinding;

    private int payType = 0;    // 支付宝支付-0 微信支付-1

    private PaymentHelper mPaymentHelper;
    private IWXAPI wxapi;

    private static final int SDK_PAY_FLAG = 1;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG:
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     * 对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        DialogHelp.getMessageDialog(RechargeActivity.this, "提示", "充值成功", (dialog, which) -> {
                            finish();
                        }).setCancelable(false).show();
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        DialogHelp.getMessageDialog(RechargeActivity.this, "提示", "充值失败").setCancelable(false).show();
                    }
                    break;
            }
        }
    };

    @Override
    protected int getContentViewId() {
        return R.layout.activity_recharge;
    }

    @Override
    protected void initView() {
        super.initView();
        initToolbar("在线充值", true);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        mPaymentHelper = new PaymentHelper();
        wxapi = WXAPIFactory.createWXAPI(this, AppConfig.WECHATAPPID, true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getUserInfo();
    }

    private void getUserInfo() {
        sendRequest(AccountService.getInstance().getUserInfo, new HashMap<>(), true);
    }

    @Override
    protected void DataReturn(DTRequest request, Head head, Object response) {
        super.DataReturn(request, head, response);
        switch (request.getApi().getName()) {
            case "GetUserInfo":
                if (head.isSuccess()) {
                    UsernameVo value;
                    try {
                        value = (UsernameVo) response;
                    } catch (Exception e) {
                        value = null;
                        e.printStackTrace();
                    }
                    if (value == null)
                        return;

                    mBinding.tvBanlace.setText(TextUtils.isEmpty(value.getBalance()) ? getString(R.string.text_no_data_default) :
                            BigDecimalUtil.formatMoney(new BigDecimal(value.getBalance()).divide(new BigDecimal(100)).toPlainString()) + "元");
                }
                break;
            case "GetTradeAppPayResponse":
                if (head.isSuccess()) {
                    String orderInfo;
                    try {
                        orderInfo = (String) response;
                    } catch (Exception e) {
                        orderInfo = null;
                        e.printStackTrace();
                    }
                    if (TextUtils.isEmpty(orderInfo))
                        return;
                    startAlipay(orderInfo);
                }
                break;
            case "WechatPay":
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

                    mPaymentHelper.startWeChatPay(this, wechatPayVo);
                }
                break;
            default:
                break;
        }
    }

    private void startAlipay(String orderInfo) {
        Runnable payRunnable = () -> {
            PayTask alipay = new PayTask(RechargeActivity.this);
            Map<String, String> result = alipay.payV2(orderInfo, true);

            Message msg = new Message();
            msg.what = SDK_PAY_FLAG;
            msg.obj = result;
            mHandler.sendMessage(msg);
        };

        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    @Override
    protected void initBinding() {
        super.initBinding();

        mBinding = (ActivityRechargeBinding) mBindingUtil;
        mBinding.setHandlers(new ClickHandlers());
    }

    private void gotoPayment() {
        if (payType == 0)
            alipay();
        else {
            if (!wxapi.isWXAppInstalled()) {
                showShortToast("没有安装微信");
                return;
            }
            wechatPay();
        }
    }

    private void alipay() {
        HashMap<String, String> params = new HashMap<>();
        params.put("totalAmount", mBinding.etFunds.getText().toString().trim());
        sendRequest(PaymentService.getInstance().getTradeAppPayResponse, params, true);
    }

    private void wechatPay() {
        HashMap<String, String> params = new HashMap<>();
        params.put("totalFee", mBinding.etFunds.getText().toString().trim());
        sendRequest(PaymentService.getInstance().wechatPay, params, true);
    }

    public class ClickHandlers {
        public void onClickPay() {
            String funds = mBinding.etFunds.getText().toString().trim();
            if (TextUtils.isEmpty(funds)) {
                showShortToast("请输入充值金额");
                return;
            } else {
                int fundsInt = Integer.parseInt(funds);
                if (fundsInt == 0) {
                    showShortToast("请输入充值金额");
                    return;
                }
                if (fundsInt % 100 != 0) {
                    showShortToast("请输入>=100元的整数倍");
                    return;
                }
            }
            showPaymentBottomDialog();
        }
    }

    private void showPaymentBottomDialog() {
        BottomSheetDialog dialog = new BottomSheetDialog(RechargeActivity.this);
        View paymentView = View.inflate(this, R.layout.bottom_dialog_payment, null);

        ImageView ivCancel = paymentView.findViewById(R.id.iv_cancel);
        ImageView ivAlipaySelect = paymentView.findViewById(R.id.iv_alipay_select);
        ImageView ivWechatSelect = paymentView.findViewById(R.id.iv_wechat_select);
        RelativeLayout layoutAlipay = paymentView.findViewById(R.id.layout_alipay);
        RelativeLayout layoutWechat = paymentView.findViewById(R.id.layout_wechat);
        TextView tvPay = paymentView.findViewById(R.id.tv_pay);

        if (payType == 0) {
            ivAlipaySelect.setVisibility(View.VISIBLE);
            ivWechatSelect.setVisibility(View.GONE);
        } else {
            ivAlipaySelect.setVisibility(View.GONE);
            ivWechatSelect.setVisibility(View.VISIBLE);
        }
        ivCancel.setOnClickListener(v -> {
            dialog.dismiss();
        });
        layoutAlipay.setOnClickListener(v -> {
            payType = 0;
            ivAlipaySelect.setVisibility(View.VISIBLE);
            ivWechatSelect.setVisibility(View.GONE);
        });
        layoutWechat.setOnClickListener(v -> {
            payType = 1;
            ivAlipaySelect.setVisibility(View.GONE);
            ivWechatSelect.setVisibility(View.VISIBLE);
        });
        tvPay.setOnClickListener(v -> {
            dialog.dismiss();
            gotoPayment();
        });

        dialog.setContentView(paymentView);
        dialog.show();
    }
}
