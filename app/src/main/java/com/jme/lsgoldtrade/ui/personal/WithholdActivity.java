package com.jme.lsgoldtrade.ui.personal;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseActivity;
import com.jme.lsgoldtrade.config.AppConfig;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.ActivityWithholdBinding;
import com.jme.lsgoldtrade.service.ManagementService;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.HashMap;

@Route(path = Constants.ARouterUriConst.WITHHOLD)
public class WithholdActivity extends JMEBaseActivity {

    private ActivityWithholdBinding mBinding;

    private WithholdAdapter mAdapter;
    private IWXAPI mWxapi;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_withhold;
    }

    @Override
    protected void initView() {
        super.initView();

        initToolbar(R.string.incrementaccount_pay_title, true);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);

        mAdapter = new WithholdAdapter(null);

        mBinding.recyclerView.setHasFixedSize(false);
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mBinding.recyclerView.setAdapter(mAdapter);

        mWxapi = WXAPIFactory.createWXAPI(this, AppConfig.WECHATAPPID, true);

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
    }

    private void showPaymentBottomDialog(String money) {
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        View paymentView = View.inflate(this, R.layout.bottom_dialog_payment, null);

        ImageView imgCancel = paymentView.findViewById(R.id.iv_cancel);
        ImageView imgWechatSelect = paymentView.findViewById(R.id.iv_wechat_select);
        TextView tvPay = paymentView.findViewById(R.id.tv_pay);

        imgWechatSelect.setVisibility(View.VISIBLE);
        tvPay.setText(String.format(getResources().getString(R.string.incrementaccount_pay_money), money));

        imgCancel.setOnClickListener(v -> dialog.dismiss());

        tvPay.setOnClickListener(v -> {
            dialog.dismiss();

            gotoPayment();
        });

        dialog.setContentView(paymentView);
        dialog.show();
    }

    private void gotoPayment() {
        if (mWxapi.isWXAppInstalled())
            wechatPay();
        else
            showShortToast(R.string.text_wechat_uninstalled);
    }

    private void wechatPay() {

    }

    private void getPayIcon() {
        sendRequest(ManagementService.getInstance().getPayIcon, new HashMap<>(), true);
    }

    @Override
    protected void DataReturn(DTRequest request, Head head, Object response) {
        super.DataReturn(request, head, response);

        switch (request.getApi().getName()) {

        }
    }

}
