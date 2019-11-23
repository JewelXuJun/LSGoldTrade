package com.jme.lsgoldtrade.ui.personal;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseActivity;
import com.jme.lsgoldtrade.config.AppConfig;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.ActivityWithholdBinding;
import com.jme.lsgoldtrade.domain.PayIconVo;
import com.jme.lsgoldtrade.service.ManagementService;
import com.jme.lsgoldtrade.service.PaymentService;
import com.jme.lsgoldtrade.util.MarketUtil;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.HashMap;
import java.util.List;

@Route(path = Constants.ARouterUriConst.WITHHOLD)
public class WithholdActivity extends JMEBaseActivity {

    private ActivityWithholdBinding mBinding;

    private String mMoney;

    private List<PayIconVo> mPayIconVoList;

    private IWXAPI mWxapi;

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

    private void serviceFeePay(String money) {
        HashMap<String, String> params = new HashMap<>();
        params.put("", money);

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
            case "ServiceFeePay":
                if (head.isSuccess()) {

                }

                break;
        }
    }

    public class ClickHandlers {

        public void onClickPay() {
            if (!TextUtils.isEmpty(mMoney))
                showPaymentBottomDialog(mMoney);
        }
    }

}
