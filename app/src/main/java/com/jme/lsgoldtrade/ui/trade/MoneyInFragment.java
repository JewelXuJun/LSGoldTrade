package com.jme.lsgoldtrade.ui.trade;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.alibaba.android.arouter.launcher.ARouter;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.common.ui.base.JMECountDownTimer;
import com.jme.common.util.RxBus;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseFragment;
import com.jme.lsgoldtrade.config.AppConfig;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.FragmentMoneyInBinding;
import com.jme.lsgoldtrade.domain.AccountVo;
import com.jme.lsgoldtrade.domain.ImageVerifyCodeVo;
import com.jme.lsgoldtrade.domain.UserInfoVo;
import com.jme.lsgoldtrade.service.TradeService;
import com.jme.lsgoldtrade.service.UserService;
import com.jme.lsgoldtrade.util.MarketUtil;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.math.BigDecimal;
import java.util.HashMap;

import rx.Subscription;

public class MoneyInFragment extends JMEBaseFragment implements OnRefreshListener {

    private FragmentMoneyInBinding mBinding;

    private boolean bVisibleToUser = false;
    private boolean bFlag = false;
    private boolean bShowImgVerifyCode = false;
    private String mCurAccountBalance;
    private String mKaptchaId;

    private JMECountDownTimer mCountDownTimer;
    private Subscription mRxbus;

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_money_in;
    }

    @Override
    protected void initView() {
        super.initView();

        mBinding = (FragmentMoneyInBinding) mBindingUtil;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);

        mCountDownTimer = new JMECountDownTimer(60000, 1000,
                mBinding.btnVerificationCode, getString(R.string.trade_get_verification_code));

        if (null != mUser) {
            UserInfoVo userInfoVo = mUser.getCurrentUser();

            mBinding.tvMobileNumber.setText(null == userInfoVo ? "" : userInfoVo.getMobile());
        }
    }

    @Override
    protected void initListener() {
        super.initListener();

        initRxBus();

        mBinding.swipeRefreshLayout.setOnRefreshListener(this);

        mBinding.etTransferAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > AppConfig.Length_Limit) {
                        s = s.toString().subSequence(0, s.toString().indexOf(".") + (AppConfig.Length_Limit + 1));

                        mBinding.etTransferAmount.setText(s);
                        mBinding.etTransferAmount.setSelection(s.length());
                    }
                }

                if (s.toString().trim().equals(".")) {
                    s = "0" + s;

                    mBinding.etTransferAmount.setText(s);
                    mBinding.etTransferAmount.setSelection(2);
                }

                if (s.toString().startsWith("0") && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        mBinding.etTransferAmount.setText(s.subSequence(0, 1));
                        mBinding.etTransferAmount.setSelection(1);

                        return;
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                updateUIWithValidation();
            }
        });

        mBinding.etVerificationCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                updateUIWithValidation();
            }
        });

        mBinding.etImgVerifyCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                updateUIWithValidation();
            }
        });
    }

    @Override
    public void initBinding() {
        super.initBinding();

        mBinding.setHandlers(new ClickHandlers());
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        bVisibleToUser = isVisibleToUser;

        if (bVisibleToUser && null != mBinding)
            getAccount(true);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (bVisibleToUser)
            getAccount(true);

        setReserveLayout();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (null != mCountDownTimer)
            mCountDownTimer.cancel();

        if (!mRxbus.isUnsubscribed())
            mRxbus.unsubscribe();
    }

    private void initRxBus() {
        mRxbus = RxBus.getInstance().toObserverable(RxBus.Message.class).subscribe(message -> {
            String callType = message.getObject().toString();

            if (TextUtils.isEmpty(callType))
                return;

            switch (callType) {
                case Constants.RxBusConst.RXBUS_ELECTRONICCARD_UPDATE:
                    getAccount(false);

                    break;
            }
        });
    }

    private void updateUIWithValidation() {
        mBinding.btnSubmit.setEnabled(bShowImgVerifyCode
                ? populated(mBinding.etTransferAmount) && populated(mBinding.etVerificationCode) && populated(mBinding.etImgVerifyCode)
                : populated(mBinding.etTransferAmount) && populated(mBinding.etVerificationCode));
    }

    private boolean populated(final EditText editText) {
        return editText.length() > 0;
    }

    private void setReserveLayout() {
        if (null == mUser || null == mUser.getCurrentUser()) {
            mBinding.btnReserve.setVisibility(View.GONE);
        } else {
            if (!TextUtils.isEmpty(mUser.getIsFromTjs()) && mUser.getIsFromTjs().equals("true")) {
                String cardType = mUser.getCurrentUser().getCardType();
                String reserveFlag = mUser.getCurrentUser().getReserveFlag();

                mBinding.btnReserve.setVisibility(!TextUtils.isEmpty(cardType) && cardType.equals("3") && !TextUtils.isEmpty(reserveFlag) && reserveFlag.equals("N")
                        ? View.VISIBLE : View.GONE);
            } else {
                mBinding.btnReserve.setVisibility(View.GONE);
            }
        }
    }

    private Bitmap getBitmap(String kaptchaImg) {
        Bitmap bitmap = null;

        try {
            byte[] bitmapArray = Base64.decode(kaptchaImg, Base64.DEFAULT);

            bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    private void doSubmit() {
        String mobile = mBinding.tvMobileNumber.getText().toString();
        String amount = mBinding.etTransferAmount.getText().toString();
        String verifyCode = mBinding.etVerificationCode.getText().toString();
        String imgVerifyCode = mBinding.etImgVerifyCode.getText().toString();

        if (amount.endsWith("."))
            amount = amount.substring(0, amount.length() - 1);

        if (TextUtils.isEmpty(mCurAccountBalance))
            showShortToast(R.string.trade_amount_error);
        else if (new BigDecimal(mCurAccountBalance).compareTo(new BigDecimal(0)) != 1)
            showShortToast(R.string.trade_amount_error);
        else if (TextUtils.isEmpty(mobile))
            showShortToast(R.string.trade_mobile_error);
        else if (new BigDecimal(amount).compareTo(new BigDecimal(0)) != 1)
            showShortToast(R.string.trade_money_min_error);
        else if (new BigDecimal(amount).compareTo(new BigDecimal(mCurAccountBalance)) == 1)
            showShortToast(R.string.trade_money_in_max_error);
        else if (!bFlag)
            showShortToast(R.string.login_verification_code_unget);
        else if (verifyCode.length() < 6)
            showShortToast(R.string.login_verification_code_error);
        else if (bShowImgVerifyCode && TextUtils.isEmpty(imgVerifyCode))
            showShortToast(R.string.login_img_verify_code_error);
        else
            inoutMoney(amount, verifyCode, imgVerifyCode);
    }

    private void getAccount(boolean enable) {
        if (null == mUser || !mUser.isLogin())
            return;

        HashMap<String, String> params = new HashMap<>();
        params.put("accountId", mUser.getAccountID());

        sendRequest(TradeService.getInstance().account, params, enable);
    }

    private void fundInoutMsg() {
        bFlag = true;

        sendRequest(UserService.getInstance().fundInoutMsg, new HashMap<>(), true);
    }

    private void inoutMoney(String amount, String verifyCode, String imgVerifyCode) {
        HashMap<String, String> params = new HashMap<>();
        params.put("accountId", mUser.getAccountID());
        params.put("amount", String.valueOf(new BigDecimal(amount).multiply(new BigDecimal(100)).longValue()));
        params.put("direction", "0");
        params.put("smsCode", verifyCode);
        if (bShowImgVerifyCode) {
            params.put("kaptchaId", mKaptchaId);
            params.put("kaptchaCode", imgVerifyCode);
        }

        sendRequest(TradeService.getInstance().inoutmoney, params, true);
    }

    private void kaptcha() {
        sendRequest(UserService.getInstance().kaptcha, new HashMap<>(), true, false, false);
    }

    @Override
    protected void DataReturn(DTRequest request, Head head, Object response) {
        super.DataReturn(request, head, response);

        switch (request.getApi().getName()) {
            case "Account":
                if (head.isSuccess()) {
                    mBinding.swipeRefreshLayout.finishRefresh(true);

                    AccountVo accountVo;

                    try {
                        accountVo = (AccountVo) response;
                    } catch (Exception e) {
                        accountVo = null;

                        e.printStackTrace();
                    }

                    if (null == accountVo)
                        return;

                    mCurAccountBalance = accountVo.getCurAccountBalanceStr();

                    mBinding.tvMoneyInMax.setText(MarketUtil.decimalFormatMoney(mCurAccountBalance));
                } else {
                    mBinding.swipeRefreshLayout.finishRefresh(false);
                }

                break;
            case "FundInoutMsg":
                if (head.isSuccess()) {
                    showShortToast(R.string.login_verification_code_success);

                    if (null != mCountDownTimer)
                        mCountDownTimer.start();
                }

                break;
            case "InoutMoney":
                if (head.isSuccess()) {
                    showShortToast(R.string.trade_money_in_success);

                    mBinding.etTransferAmount.setText("");
                    mBinding.etVerificationCode.setText("");
                    mBinding.etImgVerifyCode.setText("");

                    RxBus.getInstance().post(Constants.RxBusConst.RXBUS_CAPITALTRANSFER_SUCCESS, null);
                } else {
                    kaptcha();
                }

                getAccount(true);

                break;
            case "Kaptcha":
                if (head.isSuccess()) {
                    ImageVerifyCodeVo imageVerifyCodeVo;

                    try {
                        imageVerifyCodeVo = (ImageVerifyCodeVo) response;
                    } catch (Exception e) {
                        imageVerifyCodeVo = null;

                        e.printStackTrace();
                    }

                    if (null == imageVerifyCodeVo)
                        return;

                    String kaptchaImg = imageVerifyCodeVo.getKaptchaImg();

                    if (TextUtils.isEmpty(kaptchaImg))
                        return;

                    if (kaptchaImg.contains(","))
                        kaptchaImg = kaptchaImg.split(",")[1];

                    mBinding.imgVerifyCode.setImageBitmap(getBitmap(kaptchaImg));
                    mBinding.layoutImgVerifyCode.setVisibility(View.VISIBLE);
                    mBinding.etImgVerifyCode.setText("");
                    mBinding.btnSubmit.setEnabled(false);

                    bShowImgVerifyCode = true;

                    mKaptchaId = imageVerifyCodeVo.getKaptchaId();
                }

                break;
        }
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        getAccount(false);
    }

    public class ClickHandlers {

        public void onClickGetVerificationCode() {
            if (TextUtils.isEmpty(mBinding.tvMobileNumber.getText().toString()))
                showShortToast(R.string.trade_mobile_error);
            else
                fundInoutMsg();
        }

        public void onClickLoadImageVerifyCode() {
            kaptcha();
        }

        public void onClickSubmit() {
            doSubmit();
        }

        public void onClickReserve() {
            ARouter.getInstance().build(Constants.ARouterUriConst.BANKRESERVE).navigation();
        }
    }
}
