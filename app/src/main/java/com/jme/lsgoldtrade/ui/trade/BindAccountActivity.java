package com.jme.lsgoldtrade.ui.trade;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.common.ui.base.JMECountDownTimer;
import com.jme.common.util.RxBus;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseActivity;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.config.User;
import com.jme.lsgoldtrade.databinding.ActivityBindAccountBinding;
import com.jme.lsgoldtrade.domain.BindAccountVo;
import com.jme.lsgoldtrade.service.TradeService;
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

    private String mName;
    private String mIDCard;
    private boolean bFlag = false;
    private boolean bAgreeFlag = true;
    private int mTime = 3;

    public Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case Constants.Msg.MSG_BING_ACCOUNT_SUCCESS:
                    if (null != mWindow) {
                        if (mTime == 0) {
                            mWindow.setData(String.valueOf(mTime));
                            mHandler.removeCallbacksAndMessages(null);
                            mWindow.dismiss();

                            RxBus.getInstance().post(Constants.RxBusConst.RXBUS_BIND_SUCCESS, null);
                            ARouter.getInstance().build(Constants.ARouterUriConst.SETLOGINPASSWORD).navigation();

                            finish();
                        } else {
                            mWindow.setData(String.valueOf(mTime));
                            mHandler.removeCallbacksAndMessages(null);
                            mHandler.sendEmptyMessageDelayed(Constants.Msg.MSG_BING_ACCOUNT_SUCCESS, 1000);

                            --mTime;
                        }
                    }

                    break;
            }
        }
    };

    @Override
    protected int getContentViewId() {
        return R.layout.activity_bind_account;
    }

    @Override
    protected void initView() {
        super.initView();

        initToolbar(R.string.trade_bind_account, true);

        mBinding = (ActivityBindAccountBinding) mBindingUtil;

        mBinding.checkboxAgree.setChecked(true);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);

        mName = getIntent().getStringExtra("Name");
        mIDCard = getIntent().getStringExtra("IDCard");

        mBinding.tvName.setText(mName);
        mBinding.tvIdCard.setText(mIDCard);

        mWindow = new BindSuccessPopupWindow(mContext);
        mWindow.setOutsideTouchable(true);
        mWindow.setFocusable(true);

        mCountDownTimer = new JMECountDownTimer(60000, 1000,
                mBinding.btnVerificationCode, getString(R.string.trade_get_verification_code));
    }

    @Override
    protected void initListener() {
        super.initListener();

        mBinding.checkboxAgree.setOnCheckedChangeListener((compoundButton, isChecked) -> bAgreeFlag = isChecked);
    }

    @Override
    protected void initBinding() {
        super.initBinding();

        mBinding.setHandlers(new ClickHandlers());
    }

    private void getIcbcMsg(String name, String idCard, String goldAccount) {
        bFlag = true;

        HashMap<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("idCard", idCard);
        params.put("account", goldAccount);

        sendRequest(TradeService.getInstance().icbcMsg, params, true);
    }

    private void bindAccount(String name, String namecard, String glodaccount, String verifyCode) {
        HashMap<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("idCard", namecard);
        params.put("account", glodaccount);
        params.put("smsCode", verifyCode);

        sendRequest(TradeService.getInstance().bindAccount, params, true);
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
                }

                break;
            case "BindAccount":
                if (head.isSuccess()) {
                    BindAccountVo bindAccountVo;

                    try {
                        bindAccountVo = (BindAccountVo) response;
                    } catch (Exception e) {
                        bindAccountVo = null;

                        e.printStackTrace();
                    }

                    if (null != bindAccountVo && mUser.isLogin()) {
                        mUser.setAccountID(bindAccountVo.getAccountId() == null ? "" : String.valueOf(bindAccountVo.getAccountId().longValue()));
                        mUser.setAccount(bindAccountVo.getAccount());
                    }

                    if (null != mWindow && !mWindow.isShowing()) {
                        mWindow.setData(String.valueOf(mTime));
                        mWindow.showAtLocation(mBinding.btnBind, Gravity.CENTER, 0, 0);

                        mHandler.sendEmptyMessageDelayed(Constants.Msg.MSG_BING_ACCOUNT_SUCCESS, 1000);

                        --mTime;
                    }
                }

                break;
        }
    }

    public class ClickHandlers {

        public void onClickSoftWareAgreement() {
            ARouter.getInstance()
                    .build(Constants.ARouterUriConst.JMEWEBVIEW)
                    .withString("title", getString(R.string.trade_soft_aggrement_title))
                    .withString("url", "http://www.taijs.com/upload/fwxy.htm" + "?name" + mName + "&cardNo=" + mIDCard)
                    .navigation();
        }

        public void onClickBusinessAgreement() {
            ARouter.getInstance()
                    .build(Constants.ARouterUriConst.JMEWEBVIEW)
                    .withString("title", getString(R.string.trade_business_aggrement_title))
                    .withString("url", "http://www.taijs.com/upload/dljj.htm" + "?name" + mName + "&cardNo=" + mIDCard)
                    .navigation();
        }

        public void onClickGetVerificationCode() {
            String goldAccount = mBinding.etGoldAccount.getText().toString().trim();

            if (TextUtils.isEmpty(goldAccount))
                showShortToast(R.string.trade_gold_account_hint);
            else if (!goldAccount.startsWith("000131"))
                showShortToast(R.string.trade_gold_account_hint);
            else if (goldAccount.length() < 16)
                showShortToast(R.string.trade_gold_account_hint);
            else
                getIcbcMsg(mBinding.tvName.getText().toString().trim(), mBinding.tvIdCard.getText().toString().trim(), goldAccount);
        }

        public void onClickBind() {
            String goldAccount = mBinding.etGoldAccount.getText().toString().trim();
            String verifyCode = mBinding.etVerifyCode.getText().toString();

            if (TextUtils.isEmpty(goldAccount))
                showShortToast(R.string.trade_gold_account_hint);
            else if (!goldAccount.startsWith("000131"))
                showShortToast(R.string.trade_gold_account_hint);
            else if (goldAccount.length() < 16)
                showShortToast(R.string.trade_gold_account_hint);
            else if (!bFlag)
                showShortToast(R.string.login_verification_code_unget);
            else if (TextUtils.isEmpty(verifyCode))
                showShortToast(R.string.login_verification_code_error);
            else if (verifyCode.length() < 6)
                showShortToast(R.string.login_verification_code_error);
            else if (!bAgreeFlag)
                showShortToast(R.string.register_aggrement_unread);
            else
                bindAccount(mBinding.tvName.getText().toString().trim(), mBinding.tvIdCard.getText().toString().trim(), goldAccount, verifyCode);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (null != mCountDownTimer)
            mCountDownTimer.cancel();

        if (null != mHandler) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
        }
    }

}
