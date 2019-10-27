package com.jme.lsgoldtrade.ui.security;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseActivity;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.ActivityGestureBinding;
import com.jme.lsgoldtrade.domain.PasswordInfoVo;
import com.jme.lsgoldtrade.service.ManagementService;

import java.util.HashMap;

@Route(path = Constants.ARouterUriConst.GESTURE)
public class GestureActivity extends JMEBaseActivity {

    private ActivityGestureBinding mBinding;

    private boolean bFlag = true;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_gesture;
    }

    @Override
    protected void initView() {
        super.initView();

        initToolbar(R.string.security_gesture_setting, true);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
    }

    @Override
    protected void initListener() {
        super.initListener();

        mBinding.switchView.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (bFlag)
                return;
        });
    }

    @Override
    protected void initBinding() {
        super.initBinding();

        mBinding = (ActivityGestureBinding) mBindingUtil;
        mBinding.setHandlers(new ClickHandlers());
    }

    @Override
    protected void onResume() {
        super.onResume();

        bFlag = true;

        getUserPasswordSettingInfo();
    }

    private void getUserPasswordSettingInfo() {
        sendRequest(ManagementService.getInstance().getUserPasswordSettingInfo, new HashMap<>(), bFlag, false, bFlag);
    }

    @Override
    protected void DataReturn(DTRequest request, Head head, Object response) {
        super.DataReturn(request, head, response);

        switch (request.getApi().getName()) {
            case "GetUserPasswordSettingInfo":
                if (head.isSuccess()) {
                    PasswordInfoVo passwordInfoVo;

                    try {
                        passwordInfoVo = (PasswordInfoVo) response;
                    } catch (Exception e) {
                        passwordInfoVo = null;

                        e.printStackTrace();
                    }

                    if (null == passwordInfoVo)
                        return;

                    String hasOpenGestures = passwordInfoVo.getHasOpenGestures();

                    mBinding.switchView.setChecked(TextUtils.isEmpty(hasOpenGestures) ? false : hasOpenGestures.equals("Y"));
                    mBinding.layoutGestureModify.setVisibility(TextUtils.isEmpty(hasOpenGestures) ? View.GONE
                            : hasOpenGestures.equals("Y") ? View.VISIBLE : View.GONE);
                }

                bFlag = false;

                break;
        }
    }

    public class ClickHandlers {

        public void onClickGesturePassword() {

        }

    }

}
