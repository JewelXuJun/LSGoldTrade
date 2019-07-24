package com.jme.lsgoldtrade.ui.personal;

import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.common.util.BigDecimalUtil;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseActivity;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.ActivityRechargeBinding;
import com.jme.lsgoldtrade.domain.UsernameVo;
import com.jme.lsgoldtrade.service.AccountService;
import java.util.HashMap;

/**
 * 在线充值
 */
@Route(path = Constants.ARouterUriConst.RECHARGE)
public class RechargeActivity extends JMEBaseActivity {

    private ActivityRechargeBinding mBinding;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_recharge;
    }

    @Override
    protected void initView() {
        super.initView();
        initToolbar("在线充值", true);
        mBinding = (ActivityRechargeBinding) mBindingUtil;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
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

                    mBinding.tvBanlace.setText(TextUtils.isEmpty(value.getBalance()) ? getString(R.string.text_no_data_default) : BigDecimalUtil.formatMoney(value.getBalance()) + "元");
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void initBinding() {
        super.initBinding();
        mBinding.setHandlers(new ClickHandlers());
    }

    public class ClickHandlers {

        public void onClickPay() {
            String funds = mBinding.etGoldAccount.getText().toString().trim();
            if (TextUtils.isEmpty(funds)) {
                showShortToast("请输入充值金额");
                return;
            }

            showPaymentBottomDialog();
//            PayPopupwindow.popupwindow(RechargeActivity.this, R.id.et_gold_account);
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

        ivAlipaySelect.setVisibility(View.VISIBLE);
        ivWechatSelect.setVisibility(View.GONE);
        ivCancel.setOnClickListener(v -> {
            dialog.dismiss();
        });
        layoutAlipay.setOnClickListener(v -> {
            ivAlipaySelect.setVisibility(View.VISIBLE);
            ivWechatSelect.setVisibility(View.GONE);
        });
        layoutWechat.setOnClickListener(v -> {
            ivAlipaySelect.setVisibility(View.GONE);
            ivWechatSelect.setVisibility(View.VISIBLE);
        });
        tvPay.setOnClickListener(v -> {
            dialog.dismiss();
            showShortToast("开始付款");
        });

        dialog.setContentView(paymentView);
        dialog.show();
    }
}
