package com.jme.lsgoldtrade.ui.personal;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jme.common.util.RxBus;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.config.User;
import com.jme.lsgoldtrade.domain.QuestionAnswerVo;

import java.util.List;

public class QuestionAnswerAdapter extends BaseQuickAdapter<QuestionAnswerVo, BaseViewHolder> {

    private Activity mActivity;

    public QuestionAnswerAdapter(Activity activity, int layoutResId, @Nullable List<QuestionAnswerVo> data) {
        super(layoutResId, data);

        mActivity = activity;
    }

    @Override
    protected void convert(BaseViewHolder helper, QuestionAnswerVo item) {
        if (null == item)
            return;

        String ask = item.getAsk();
        String jumpTxt = item.getJumpTxt();
        String answer = ask + jumpTxt;

        helper.setText(R.id.tv_question, item.getQuestion())
                .setText(R.id.tv_answer, TextUtils.isEmpty(jumpTxt) ? ask : doShortcutKeys(answer, jumpTxt, helper));
    }

    private SpannableString doShortcutKeys(String value, String jumpTxt, BaseViewHolder helper) {
        SpannableString spannableString = new SpannableString(value);
        spannableString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.color_blue_deep)),
                value.length() - jumpTxt.length(), value.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new TextClick(), value.length() - jumpTxt.length(), value.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        ((TextView) helper.getView(R.id.tv_answer)).setMovementMethod(LinkMovementMethod.getInstance());

        return spannableString;
    }

    private class TextClick extends ClickableSpan {

        @Override
        public void onClick(View widget) {
            String value = ((TextView) widget).getText().toString();
            User user = User.getInstance();

            if (value.endsWith("立即开户")) {
                if (null == user || !user.isLogin()) {
                    ARouter.getInstance().build(Constants.ARouterUriConst.ACCOUNTLOGIN).navigation();
                } else {
                    RxBus.getInstance().post(Constants.RxBusConst.RXBUS_CANCEL, null);
                    ARouter.getInstance().build(Constants.ARouterUriConst.MAIN).navigation();

                    mActivity.finish();
                }
            } else if (value.endsWith("去绑定")) {
                if (null == user || !user.isLogin()) {
                    ARouter.getInstance().build(Constants.ARouterUriConst.ACCOUNTLOGIN).navigation();
                } else {
                    ARouter.getInstance()
                            .build(Constants.ARouterUriConst.AUTHENTICATION)
                            .withString("Type", "2")
                            .navigation();

                    mActivity.finish();
                }
            } else if (value.endsWith("去下单")) {
                if (null == user || !user.isLogin()) {
                    ARouter.getInstance().build(Constants.ARouterUriConst.ACCOUNTLOGIN).navigation();
                } else {
                    RxBus.getInstance().post(Constants.RxBusConst.RXBUS_TRADE, null);
                    ARouter.getInstance().build(Constants.ARouterUriConst.MAIN).navigation();

                    mActivity.finish();
                }
            } else if (value.endsWith("去撤单")) {
                if (null == user || !user.isLogin()) {
                    ARouter.getInstance().build(Constants.ARouterUriConst.ACCOUNTLOGIN).navigation();
                } else {
                    RxBus.getInstance().post(Constants.RxBusConst.RXBUS_CANCELORDER, null);
                    ARouter.getInstance().build(Constants.ARouterUriConst.MAIN).navigation();

                    mActivity.finish();
                }
            } else if (value.endsWith("查看当前持仓")) {
                if (null == user || !user.isLogin()) {
                    ARouter.getInstance().build(Constants.ARouterUriConst.ACCOUNTLOGIN).navigation();
                } else {
                    RxBus.getInstance().post(Constants.RxBusConst.RXBUS_TRADEFRAGMENT_HOLD, null);
                    ARouter.getInstance().build(Constants.ARouterUriConst.MAIN).navigation();

                    mActivity.finish();
                }
            } else if (value.endsWith("立即入金")) {
                if (null == user || !user.isLogin()) {
                    ARouter.getInstance().build(Constants.ARouterUriConst.ACCOUNTLOGIN).navigation();
                } else {
                    ARouter.getInstance().build(Constants.ARouterUriConst.CAPITALTRANSFER).navigation();

                    mActivity.finish();
                }
            } else if (value.endsWith("找回密码")) {
                ARouter.getInstance().build(Constants.ARouterUriConst.FORGETPASSWORD).navigation();

                mActivity.finish();
            } else if (value.endsWith("人工客服")) {
                RxBus.getInstance().post(Constants.RxBusConst.RXBUS_CUSTOMER_SERVICE, null);
            }
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setUnderlineText(false);
        }
    }

}
