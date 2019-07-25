package com.jme.lsgoldtrade.wxapi;

import android.content.Intent;
import android.util.Log;
import com.jme.common.util.DialogHelp;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseActivity;
import com.jme.lsgoldtrade.config.AppConfig;
import com.jme.lsgoldtrade.ui.personal.CheckServiceActivity;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

public class WXPayEntryActivity extends JMEBaseActivity implements IWXAPIEventHandler {

    private static final String TAG = "WXPayEntryActivity";

    private IWXAPI iwxapi;

    @Override
    protected int getContentViewId() {
        return R.layout.pay_result;
    }

    @Override
    protected void initView() {
        super.initView();
        iwxapi = WXAPIFactory.createWXAPI(this, AppConfig.WECHATAPPID);
        iwxapi.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        iwxapi.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {

    }

    @Override
    public void onResp(BaseResp resp) {
        Log.d(TAG, "onPayFinish, errCode = " + resp.errCode);
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            int code = resp.errCode;
            String result = "";
            switch (code) {
                case 0:
                    result = "支付成功";
                    break;
                case -1:
                    result = "支付失败";
                    break;
                case -2:
                    result = "支付取消";
                    break;
            }
            DialogHelp.getMessageDialog(this, "提示", result, (dialog, which) -> {
                Intent intent = new Intent(this, CheckServiceActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }).show();
        }
    }
}