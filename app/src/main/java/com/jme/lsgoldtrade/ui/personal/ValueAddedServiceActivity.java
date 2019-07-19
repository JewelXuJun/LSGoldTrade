package com.jme.lsgoldtrade.ui.personal;

import android.os.Bundle;
import android.view.Gravity;
import android.widget.CompoundButton;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseActivity;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.ActivityValueAddedServiceBinding;
import com.jme.lsgoldtrade.service.AccountService;
import com.jme.lsgoldtrade.view.OpenServicePopupWindow;

import java.util.HashMap;

/**
 * 我的增值业务
 */
@Route(path = Constants.ARouterUriConst.VALUEADDEDSERVICE)
public class ValueAddedServiceActivity extends JMEBaseActivity {

    private ActivityValueAddedServiceBinding mBinding;

    private int isAgree = 0;

    private OpenServicePopupWindow mWindow;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_value_added_service;
    }

    @Override
    protected void initView() {
        super.initView();
        mBinding = (ActivityValueAddedServiceBinding) mBindingUtil;
        initToolbar("开通增值服务", true);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        mWindow = new OpenServicePopupWindow(mContext);
        mWindow.setOutsideTouchable(true);
        mWindow.setFocusable(true);
    }

    @Override
    protected void initListener() {
        super.initListener();
        mBinding.cbAgree.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    isAgree = 1;
                } else {
                    isAgree = 0;
                }
            }
        });
    }

    @Override
    protected void initBinding() {
        super.initBinding();
        mBinding.setHandlers(new ClickHandlers());
    }

    public class ClickHandlers {

        public void onClickValueAddServiceAgreement() {
            String url = "http://www.taijs.com/upload/fwxy.htm";
            ARouter.getInstance()
                    .build(Constants.ARouterUriConst.JMEWEBVIEW)
                    .withString("title", "增值服务协议")
                    .withString("url", url)
                    .navigation();
        }

        public void onClickOpenService() {
            if (isAgree == 0)
                showShortToast("请阅读并同意以下协议");
            else
                sendRequest(AccountService.getInstance().openValueAddedServices, new HashMap<>(), false);
        }
    }

    @Override
    protected void DataReturn(DTRequest request, Head head, Object response) {
        super.DataReturn(request, head, response);
        switch (request.getApi().getName()) {
            case "OpenValueAddedServices":
                if (head.isSuccess()) {
                    ARouter.getInstance()
                            .build(Constants.ARouterUriConst.VALUESERVICESUCCESS)
                            .navigation();
                } else {
                    if (head.getMsg().contains("需要在泰金所完成2笔以上的交易才能开通增值服务！")) {
                        if (null != mWindow) {
                            mWindow.setData(head.getMsg());
                            mWindow.showAtLocation(mBinding.lltitle, Gravity.CENTER, 0, 0);
                        }
                    } else {
                        showShortToast(head.getMsg());
                    }
                }
                break;
        }
    }
}
