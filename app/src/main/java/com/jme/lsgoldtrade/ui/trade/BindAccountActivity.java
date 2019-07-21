package com.jme.lsgoldtrade.ui.trade;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.widget.CompoundButton;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.common.ui.base.JMECountDownTimer;
import com.jme.common.util.AppManager;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseActivity;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.ActivityBindAccountBinding;
import com.jme.lsgoldtrade.domain.BindUserNameVo;
import com.jme.lsgoldtrade.service.TradeService;
import com.jme.lsgoldtrade.util.NormalUtils;
import com.jme.lsgoldtrade.view.BindSuccessPopupWindow;

import java.util.HashMap;

/**
 * 账号绑定
 */
@Route(path = Constants.ARouterUriConst.BINDACCOUNT)
public class BindAccountActivity extends JMEBaseActivity {

    private ActivityBindAccountBinding mBinding;

    private JMECountDownTimer mCountDownTimer;

    private BindSuccessPopupWindow mWindow;

    private int isAgree = 0;

    private int time = 3;

    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (mWindow != null) {
                if (time == 0) {
                    mWindow.setData(time + "");
                    handler.removeCallbacksAndMessages(null);
                    ARouter.getInstance()
                            .build(Constants.ARouterUriConst.LOGINPWD)
                            .navigation();
                    mWindow.dismiss();
                    AppManager.getAppManager().finishActivity();
                } else {
                    mWindow.setData(time + "");
                    handler.removeCallbacksAndMessages(null);
                    handler.sendEmptyMessageDelayed(1, 1000);
                    --time;
                }
            }
        }
    };
    private String name, card;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_bind_account;
    }

    @Override
    protected void initView() {
        super.initView();
        mBinding = (ActivityBindAccountBinding) mBindingUtil;
        initToolbar("账号绑定", true);
        name = getIntent().getStringExtra("name");
        card = getIntent().getStringExtra("card");

        mBinding.etName.setText(name);
        mBinding.etNameCard.setText(card);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        mWindow = new BindSuccessPopupWindow(mContext);
        mWindow.setOutsideTouchable(true);
        mWindow.setFocusable(true);
        mCountDownTimer = new JMECountDownTimer(60000, 1000,
                mBinding.btnVerificationCode, getString(R.string.trade_get_verification_code));
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
    protected void onDestroy() {
        super.onDestroy();

        if (null != mCountDownTimer)
            mCountDownTimer.cancel();
        if (null != handler) {
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }
    }

    @Override
    protected void initBinding() {
        super.initBinding();
        mBinding.setHandlers(new ClickHandlers());
    }

    public class ClickHandlers {

        public void onClickSoftWareAgreement() {
            String url = "http://www.taijs.com/upload/fwxy.htm" + "?name" + name + "&cardNo=" + card;
            ARouter.getInstance()
                    .build(Constants.ARouterUriConst.JMEWEBVIEW)
                    .withString("title", "软件服务协议")
                    .withString("url", url)
                    .navigation();
        }

        public void onClickBusinessAgreement() {
            String url = "http://www.taijs.com/upload/dljj.htm" + "?name" + name + "&cardNo=" + card;
            ARouter.getInstance()
                    .build(Constants.ARouterUriConst.JMEWEBVIEW)
                    .withString("title", "业务服务协议")
                    .withString("url", url)
                    .navigation();
        }

        public void onClickGetVerificationCode() {
            String name = mBinding.etName.getText().toString().trim();
            String nameCard = mBinding.etNameCard.getText().toString().trim();
            String goldAccount = mBinding.etGoldAccount.getText().toString().trim();

            if (TextUtils.isEmpty(name)) {
                showShortToast("请输入您的姓名");
                return;
            }
            if (TextUtils.isEmpty(nameCard)) {
                showShortToast("请输入您的身份证号码");
                return;
            }
            if (!NormalUtils.isIdCardNum(nameCard)) {
                showShortToast("请输入有效身份证号");
                return;
            }
            if (TextUtils.isEmpty(goldAccount)) {
                showShortToast("请输入您的黄金账号");
                return;
            }

            HashMap<String, String> params = new HashMap<>();
            params.put("name", name);
            params.put("idCard", nameCard);
            params.put("account", goldAccount);
            sendRequest(TradeService.getInstance().icbcMsg, params, false);
        }

        public void onClickBind() {
            String name = mBinding.etName.getText().toString();
            String verifyCode = mBinding.etVerifyCode.getText().toString();
            String namecard = mBinding.etNameCard.getText().toString();
            String glodaccount = mBinding.etGoldAccount.getText().toString().trim();

            if (TextUtils.isEmpty(name))
                showShortToast("请输入姓名");
            else if (TextUtils.isEmpty(namecard))
                showShortToast("请输入身份证号码");
            else if (TextUtils.isEmpty(glodaccount))
                showShortToast("请输入黄金账号");
            else if (TextUtils.isEmpty(verifyCode))
                showShortToast("请输入验证码");
            else if (isAgree == 0)
                showShortToast("请阅读并同意以下协议");
            else
                bindAccount(name, namecard, glodaccount, verifyCode);

            mBinding.btnBind.setClickable(false);
        }
    }

    private void bindAccount(String name, String namecard, String glodaccount, String verifyCode) {
        HashMap<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("idCard", namecard);
        params.put("account", glodaccount);
        params.put("smsCode", verifyCode);
        sendRequest(TradeService.getInstance().bindaccount, params, true);
    }

    @Override
    protected void DataReturn(DTRequest request, Head head, Object response) {
        super.DataReturn(request, head, response);
        switch (request.getApi().getName()) {
            case "IcbcMsg":
                if (head.isSuccess()) {
                    showShortToast(R.string.login_verification_code_success);
                    if (null != mCountDownTimer)
                        mCountDownTimer.start();
                } else {
                    showShortToast(head.getMsg());
                }
                break;
            case "Bindaccount":
                mBinding.btnBind.setClickable(true);
                if (head.isSuccess()) {
                    BindUserNameVo bindUserName;

                    try {
                        bindUserName = (BindUserNameVo) response;
                    } catch (Exception e) {
                        bindUserName = null;

                        e.printStackTrace();
                    }

                    if (null == bindUserName)
                        return;

                    if (null != mWindow) {
                        mWindow.setData(time + "");
                        mWindow.showAtLocation(mBinding.btnBind, Gravity.CENTER, 0, 0);
                        handler.sendEmptyMessageDelayed(1, 1000);
                        --time;
                    }
                } else {
                    showShortToast(head.getMsg());
                }
                break;
        }
    }
}
