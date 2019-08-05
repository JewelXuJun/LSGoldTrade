package com.jme.lsgoldtrade.ui.personal;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.common.util.BigDecimalUtil;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseActivity;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.ActivityWithdrawBinding;
import com.jme.lsgoldtrade.domain.UsernameVo;
import com.jme.lsgoldtrade.domain.WithdrawApplyVo;
import com.jme.lsgoldtrade.service.AccountService;
import com.jme.lsgoldtrade.service.PaymentService;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareConfig;
import com.umeng.socialize.bean.SHARE_MEDIA;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Android Studio.
 * Author : zhangzhongqiang
 * Email  : betterzhang.dev@gmail.com
 * Time   : 2019/07/31 18:20
 * Desc   : 提现页面
 */
@Route(path = Constants.ARouterUriConst.WITHDRAW)
public class WithdrawActivity extends JMEBaseActivity {

    private ActivityWithdrawBinding mBinding;

    private BigDecimal balance;

    private String openid = "";
    private String nickName = "";

    @Override
    protected int getContentViewId() {
        return R.layout.activity_withdraw;
    }

    @Override
    protected void initView() {
        super.initView();
        initToolbar("提现", true);
        mBinding = (ActivityWithdrawBinding) mBindingUtil;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        getUserInfo();
        getWithdrawFeeRate();
    }

    @Override
    protected void initBinding() {
        super.initBinding();
        mBinding.setHandlers(new ClickHandlers());
    }

    private void getUserInfo() {
        sendRequest(AccountService.getInstance().getUserInfo, new HashMap<>(), true);
    }

    private void getWithdrawFeeRate() {
        sendRequest(AccountService.getInstance().getWithdrawFeeRate, new HashMap<>(), false);
    }

    private void hasWeChatWithdrawAuth() {
        sendRequest(AccountService.getInstance().hasWeChatWithdrawAuth, new HashMap<>(), false);
    }

    private void applyWithdrawByWeChat() {
        // 首次提现的话，需要传openid nickName 非首次的话就传空就可以了
        HashMap<String, String> params = new HashMap<>();
        params.put("amount", mBinding.etFunds.getText().toString().trim());
        params.put("openid", openid);
        params.put("nickName", nickName);
        sendRequest(PaymentService.getInstance().withdrawApply, params, true);
    }

    private void gotoWeChatAuth() {
//        boolean isAuth = UMShareAPI.get(this).isAuthorize(WithdrawActivity.this, SHARE_MEDIA.WEIXIN);
//        Log.e("-----", "" + isAuth);
//        if (isAuth)
//            UMShareAPI.get(this).deleteOauth(WithdrawActivity.this, SHARE_MEDIA.WEIXIN, umAuthListener);
        UMShareConfig config = new UMShareConfig();
        config.isNeedAuthOnGetUserInfo(true);
        UMShareAPI.get(this).setShareConfig(config);
        UMShareAPI.get(this).getPlatformInfo(WithdrawActivity.this, SHARE_MEDIA.WEIXIN, umAuthListener);
    }

    UMAuthListener umAuthListener = new UMAuthListener() {
        @Override
        public void onStart(SHARE_MEDIA media) {

        }

        @Override
        public void onComplete(SHARE_MEDIA media, int i, Map<String, String> map) {
//            showShortToast("授权成功");
            Log.e("------", map.toString());
            openid = map.get("openid");
            nickName = map.get("name");
            applyWithdrawByWeChat();
        }

        @Override
        public void onError(SHARE_MEDIA media, int i, Throwable throwable) {
            showShortToast("授权失败：" + throwable.getMessage());
        }

        @Override
        public void onCancel(SHARE_MEDIA media, int i) {
            showShortToast("授权取消");
        }
    };

    @Override
    protected void DataReturn(DTRequest request, Head head, Object response) {
        super.DataReturn(request, head, response);
        switch (request.getApi().getName()) {
            case "GetUserInfo":
                if (head.isSuccess()) {
                    UsernameVo value;
                    try {
                        value = (UsernameVo) response;
                    } catch (Exception e) {
                        value = null;
                        e.printStackTrace();
                    }
                    if (value == null)
                        return;

                    mBinding.tvBanlace.setText(TextUtils.isEmpty(value.getBalance()) ? getString(R.string.text_no_data_default) :
                            BigDecimalUtil.formatMoney(new BigDecimal(value.getBalance()).divide(new BigDecimal(100)).toPlainString()) + "元");

                    balance = new BigDecimal(value.getBalance()).divide(new BigDecimal(100));
                }
                break;
            case "GetWithdrawFeeRate":
                if (head.isSuccess()) {
                    String rate;
                    try {
                        rate = (String) response;
                    } catch (Exception e) {
                        rate = null;
                        e.printStackTrace();
                    }
                    if (TextUtils.isEmpty(rate))
                        return;
                    mBinding.tvWithdrawRule.setText(String.format(getString(R.string.text_withdraw_rule), rate + "%"));
                }
                break;
            case "HasWeChatWithdrawAuth":
                if (head.isSuccess()) {
                    String authFlag;
                    try {
                        authFlag = (String) response;
                    } catch (Exception e) {
                        authFlag = null;
                        e.printStackTrace();
                    }
                    if (TextUtils.isEmpty(authFlag))
                        return;

                    if (authFlag.equals("T"))   // 非首次
                        applyWithdrawByWeChat();
                    else    // 微信授权
                        gotoWeChatAuth();
                }
                break;
            case "WithdrawApply":
                if (head.isSuccess()) {
                    WithdrawApplyVo withdrawApplyVo;
                    try {
                        withdrawApplyVo = (WithdrawApplyVo) response;
                    } catch (Exception e) {
                        withdrawApplyVo = null;
                        e.printStackTrace();
                    }
                    if (withdrawApplyVo == null)
                        return;
                    ARouter.getInstance().build(Constants.ARouterUriConst.WITHDRAWRESULT)
                            .withSerializable("WithdrawApplyVo", withdrawApplyVo)
                            .navigation();
                    this.finish();
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void initListener() {
        super.initListener();
        mBinding.etFunds.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 删除.后面超过两位的数字
                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                        s = s.toString().subSequence(0,
                                s.toString().indexOf(".") + 3);
                        mBinding.etFunds.setText(s);
                        mBinding.etFunds.setSelection(s.length());
                    }
                }

                // 如果.在起始位置,则起始位置自动补0
                if (s.toString().trim().substring(0).equals(".")) {
                    s = "0" + s;
                    mBinding.etFunds.setText(s);
                    mBinding.etFunds.setSelection(2);
                }

                // 如果起始位置为0并且第二位跟的不是".",则无法后续输入
                if (s.toString().startsWith("0")
                        && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        mBinding.etFunds.setText(s.subSequence(0, 1));
                        mBinding.etFunds.setSelection(1);
                        return;
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public class ClickHandlers {

        public void onClickWithdraw() {
            String funds = mBinding.etFunds.getText().toString().trim();
            if (TextUtils.isEmpty(funds)) {
                showShortToast("请输入提现金额");
                return;
            } else if (new BigDecimal(funds).compareTo(new BigDecimal(0)) == 0) {
                showShortToast("请输入正确的提现金额");
                return;
            } else if (new BigDecimal(funds).compareTo(new BigDecimal(1)) < 0) {
                showShortToast("提现金额至少1元");
                return;
            } else if (new BigDecimal(funds).compareTo(balance) > 0) {
                showShortToast("输入的提现金额大于可提金额");
                return;
            }
            hasWeChatWithdrawAuth();
        }

    }
}
