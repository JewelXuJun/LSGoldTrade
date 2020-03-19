package com.jme.lsgoldtrade.ui.transaction;

import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.google.gson.Gson;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.common.ui.base.JMECountDownTimer;
import com.jme.common.util.RxBus;
import com.jme.common.util.StringUtils;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseActivity;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.ActivityBindAccountBinding;
import com.jme.lsgoldtrade.domain.BindAccountResponse;
import com.jme.lsgoldtrade.domain.BindAccountVo;
import com.jme.lsgoldtrade.domain.PasswordSettingVo;
import com.jme.lsgoldtrade.service.TradeService;

import java.net.ConnectException;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Route(path = Constants.ARouterUriConst.BINDACCOUNT)
public class BindAccountActivity extends JMEBaseActivity {

    private ActivityBindAccountBinding mBinding;

    private JMECountDownTimer mCountDownTimer;

    private String mName;
    private String mIDCard;
    private boolean bFlag = false;
    private boolean bAgreeFlag = true;
//    private boolean isbAgreeIncrementFlag = true;

    private IncrementExplainPopUpWindow mIncrementExplainPopUpWindow;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_bind_account;
    }

    @Override
    protected void initView() {
        super.initView();

        initToolbar(R.string.transaction_bind_account, true);

        mBinding.checkboxAgree.setChecked(true);
//        mBinding.checkboxIncrementAgree.setChecked(true);

//        setAggrementMessage();
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);

        mName = getIntent().getStringExtra("Name");
        mIDCard = getIntent().getStringExtra("IDCard");

        mBinding.tvName.setText(mName);
        mBinding.tvIdCard.setText(StringUtils.formatIDCardNumber(mIDCard));

        mCountDownTimer = new JMECountDownTimer(60000, 1000,
                mBinding.btnVerificationCode, getString(R.string.transaction_get_verification_code));

        mIncrementExplainPopUpWindow = new IncrementExplainPopUpWindow(this);
        mIncrementExplainPopUpWindow.setOutsideTouchable(false);
        mIncrementExplainPopUpWindow.setFocusable(false);
    }

    @Override
    protected void initListener() {
        super.initListener();

        mBinding.checkboxAgree.setOnCheckedChangeListener((compoundButton, isChecked) -> bAgreeFlag = isChecked);
//        mBinding.checkboxIncrementAgree.setOnCheckedChangeListener((compoundButton, isChecked) -> isbAgreeIncrementFlag = isChecked);
    }

    @Override
    protected void initBinding() {
        super.initBinding();

        mBinding = (ActivityBindAccountBinding) mBindingUtil;
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

    private void setAggrementMessage() {
        SpannableString spannableString = new SpannableString(getResources().getString(R.string.transaction_bind_account_increment_agreement));
        spannableString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.color_blue_deep)),
                4, 12, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new TextClick1(), 4, 12, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new AbsoluteSizeSpan(10, true), 4, 12, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        spannableString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.color_blue_deep)),
                17, 21, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new TextClick2(), 17, 21, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        spannableString.setSpan(new AbsoluteSizeSpan(10, true), 17, 21, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mBinding.tvIncrementAgree.setMovementMethod(LinkMovementMethod.getInstance());
        mBinding.tvIncrementAgree.setText(spannableString);
    }


    private void bindAccount(String name, String namecard, String glodaccount, String verifyCode) {
        HashMap<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("idCard", namecard);
        params.put("account", glodaccount);
        params.put("smsCode", verifyCode);

        showLoadingDialog("");

        DTRequest request = new DTRequest(TradeService.getInstance().bindAccount, params, false, true);

        Call restResponse = request.getApi().request(request.getParams());

        restResponse.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                Head head = new Head();
                Object body = "";

                if (response.raw().code() != 200) {
                    head.setSuccess(false);
                    head.setCode("" + response.raw().code());
                    head.setMsg("服务器异常");
                } else {
                    if (!request.getApi().isResponseJson()) {
                        body = response.body();
                        head.setSuccess(true);
                        head.setCode("0");
                        head.setMsg("成功");
                    } else {
                        BindAccountResponse dtResponse = (BindAccountResponse) response.body();

                        head = new Head();
                        head.setCode(dtResponse.getCode());
                        head.setMsg(dtResponse.getMsg());

                        try {
                            body = new Gson().fromJson(dtResponse.getBodyToString(),
                                    request.getApi().getEntryType());
                        } catch (Exception e) {
                            body = dtResponse.getBodyToString();
                        }
                    }
                }

                OnResult(request, head, body);
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Head head = new Head();
                final Throwable cause = t.getCause() != null ? t.getCause() : t;

                if (cause != null) {
                    if (cause instanceof ConnectException) {
                        head.setSuccess(false);
                        head.setCode("500");
                        head.setMsg(getResources().getString(com.jme.common.R.string.text_error_server));
                    } else {
                        head.setSuccess(false);
                        head.setCode("408");
                        head.setMsg(getResources().getString(com.jme.common.R.string.text_error_timeout));
                    }
                }

                OnResult(request, head, null);
            }
        });
    }

    private void whetherChangeLoginPwd() {
        sendRequest(TradeService.getInstance().whetherChangeLoginPwd, new HashMap<>(), true);
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
                        mUser.setAccountID(bindAccountVo.getAccountId());
                        mUser.setAccount(bindAccountVo.getAccount());
                    }

                    RxBus.getInstance().post(Constants.RxBusConst.RXBUS_BIND_SUCCESS, null);

                   /* if (isbAgreeIncrementFlag) {
                        ARouter.getInstance()
                                .build(Constants.ARouterUriConst.WITHHOLDCONTRACT)
                                .withString("Resource", "Trade")
                                .withBoolean("isBindAccount", true)
                                .navigation();
                    }*/

                    finish();

                }

                break;
            case "WhetherChangeLoginPwd":
                if (head.isSuccess()) {
                    PasswordSettingVo passwordSettingVo;

                    try {
                        passwordSettingVo = (PasswordSettingVo) response;
                    } catch (Exception e) {
                        passwordSettingVo = null;

                        e.printStackTrace();
                    }

                    String flag = passwordSettingVo.getFlag();

                    if (TextUtils.isEmpty(flag) || flag.equals("N"))
                        ARouter.getInstance().build(Constants.ARouterUriConst.SETLOGINPASSWORD).navigation();
                    else
                        ARouter.getInstance()
                                .build(Constants.ARouterUriConst.WITHHOLDCONTRACT)
                                .withString("Resource", "Trade")
                                .navigation();

                    finish();
                }

                break;
        }
    }

    public class ClickHandlers {

        public void onClickSoftWareAgreement() {
            ARouter.getInstance()
                    .build(Constants.ARouterUriConst.JMEWEBVIEW)
                    .withString("title", getString(R.string.transaction_soft_aggrement_title))
                    .withString("url", "http://www.taijs.com/upload/fwxy.htm" + "?name=" + mName + "&cardNo=" + StringUtils.formatIDCardNumber(mIDCard))
                    .navigation();
        }

        public void onClickBusinessAgreement() {
            ARouter.getInstance()
                    .build(Constants.ARouterUriConst.JMEWEBVIEW)
                    .withString("title", getString(R.string.transaction_business_aggrement_title))
                    .withString("url", "http://www.taijs.com/upload/dljj.htm" + "?name=" + mName + "&cardNo=" + StringUtils.formatIDCardNumber(mIDCard))
                    .navigation();
        }

        public void onClickGetVerificationCode() {
            String goldAccount = mBinding.etGoldAccount.getText().toString().trim();

            if (TextUtils.isEmpty(goldAccount))
                showShortToast(R.string.transaction_gold_account_hint);
            else if (!goldAccount.startsWith("000131"))
                showShortToast(R.string.transaction_gold_account_hint);
            else if (goldAccount.length() < 16)
                showShortToast(R.string.transaction_gold_account_hint);
            else
                getIcbcMsg(mBinding.tvName.getText().toString().trim(), mIDCard, goldAccount);
        }

        public void onClickBind() {

            String goldAccount = mBinding.etGoldAccount.getText().toString().trim();
            String verifyCode = mBinding.etVerifyCode.getText().toString();

            if (TextUtils.isEmpty(goldAccount))
                showShortToast(R.string.transaction_gold_account_hint);
            else if (!goldAccount.startsWith("000131"))
                showShortToast(R.string.transaction_gold_account_hint);
            else if (goldAccount.length() != 16)
                showShortToast(R.string.transaction_gold_account_hint);
            else if (!bFlag)
                showShortToast(R.string.login_verification_code_unget);
            else if (TextUtils.isEmpty(verifyCode))
                showShortToast(R.string.login_verification_code_error);
            else if (verifyCode.length() < 6)
                showShortToast(R.string.login_verification_code_error);
            else if (!bAgreeFlag)
                showShortToast(R.string.transaction_bind_aggrement_message);
            else
                bindAccount(mBinding.tvName.getText().toString().trim(), mIDCard, goldAccount, verifyCode);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (null != mCountDownTimer)
            mCountDownTimer.cancel();
    }

    private class TextClick1 extends ClickableSpan {

        @Override
        public void onClick(View widget) {

            avoidHintColor(widget);
            ARouter.getInstance()
                    .build(Constants.ARouterUriConst.JMEWEBVIEW)
                    .withString("title", getString(R.string.increment_agreement))
                    .withString("url", "http://www.taijs.com/upload/zzfwxy.htm")
                    .navigation();
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setUnderlineText(false);
            ds.clearShadowLayer();
        }
    }

    private class TextClick2 extends ClickableSpan {

        @Override
        public void onClick(View widget) {
            avoidHintColor(widget);

            if (null != mIncrementExplainPopUpWindow && !mIncrementExplainPopUpWindow.isShowing()) {
                mIncrementExplainPopUpWindow.setData((agreement) -> {
                    mBinding.checkboxIncrementAgree.setChecked(true);
                    mIncrementExplainPopUpWindow.dismiss();
                }, (later) -> {
                    mBinding.checkboxIncrementAgree.setChecked(false);
                    mIncrementExplainPopUpWindow.dismiss();
                });

                mIncrementExplainPopUpWindow.showAtLocation(mBinding.getRoot(), Gravity.CENTER, 0, 0);
            }
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setUnderlineText(false);
            ds.clearShadowLayer();
        }
    }

    private void avoidHintColor(View view) {
        if (view instanceof TextView)
            ((TextView) view).setHighlightColor(getResources().getColor(android.R.color.transparent));
    }

}
