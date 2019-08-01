package com.jme.lsgoldtrade.wxapi;

import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.umeng.socialize.weixin.view.WXCallbackActivity;

public class WXEntryActivity extends WXCallbackActivity implements IWXAPIEventHandler{

//    private IWXAPI api;
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//    	api = WXAPIFactory.createWXAPI(this, AppConfig.WECHATAPPID, false);
//
//        try {
//            Intent intent = getIntent();
//        	api.handleIntent(intent, this);
//        } catch (Exception e) {
//        	e.printStackTrace();
//        }
//	}
//
//	@Override
//	public void onReq(BaseReq req) {
//
//	}
//
//	@Override
//	public void onResp(BaseResp resp) {
//        finish();
//	}
}