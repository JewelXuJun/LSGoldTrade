package com.jme.lsgoldtrade.ui.personal;

import android.app.Activity;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
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
import com.jme.common.util.SharedPreUtils;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.config.User;
import com.jme.lsgoldtrade.domain.QuestionAnswerVo;

import java.util.List;

public class QuestionAnswerAdapter extends BaseQuickAdapter<List<QuestionAnswerVo>, BaseViewHolder> {

    private Activity mActivity;

    public QuestionAnswerAdapter(Activity activity, int layoutResId, @Nullable List<List<QuestionAnswerVo>> data) {
        super(layoutResId, data);

        mActivity = activity;
    }

    @Override
    protected void convert(BaseViewHolder helper, List<QuestionAnswerVo> questionAnswerVoList) {
        if (null == questionAnswerVoList)
            return;

        int size = questionAnswerVoList.size();

        if (size == 0)
            return;

        if (size == 1) {
            QuestionAnswerVo questionAnswerVo = questionAnswerVoList.get(0);

            String ask = questionAnswerVo.getAsk();
            String jumpTxt = questionAnswerVo.getJumpTxt();
            String answer = ask + jumpTxt;

            helper.setText(R.id.tv_question, questionAnswerVo.getQuestion())
                    .setText(R.id.tv_answer, TextUtils.isEmpty(jumpTxt) ? ask : doShortcutKeys(answer, jumpTxt, helper))
                    .setGone(R.id.tv_answer, true)
                    .setGone(R.id.layout_answer_list, false);
        } else {
            helper.setText(R.id.tv_question, questionAnswerVoList.get(0).getInputQuestion())
                    .setGone(R.id.tv_answer, false)
                    .setGone(R.id.layout_answer_list, true)
                    .addOnClickListener(R.id.tv_change_group)
                    .addOnClickListener(R.id.layout_question_first)
                    .addOnClickListener(R.id.layout_question_second)
                    .addOnClickListener(R.id.layout_question_third)
                    .addOnClickListener(R.id.layout_question_fourth);

            for (int i = 0; i < questionAnswerVoList.size(); i++) {
                QuestionAnswerVo questionAnswerVo = questionAnswerVoList.get(i);

                if (null != questionAnswerVo) {
                    if (i == 0) {
                        helper.setText(R.id.tv_question_first, "1." + questionAnswerVo.getQuestion())
                                .setGone(R.id.layout_question_first, true);
                    } else if (i == 1) {
                        helper.setText(R.id.tv_question_second, "2." + questionAnswerVo.getQuestion())
                                .setGone(R.id.layout_question_second, true);
                    } else if (i == 2) {
                        helper.setText(R.id.tv_question_third, "3." + questionAnswerVo.getQuestion())
                                .setGone(R.id.layout_question_third, true);
                    } else if (i == 3) {
                        helper.setText(R.id.tv_question_fourth, "4." + questionAnswerVo.getQuestion())
                                .setGone(R.id.layout_question_fourth, true);
                    }
                }
            }
        }

    }

    private SpannableString doShortcutKeys(String value, String jumpTxt, BaseViewHolder helper) {
        SpannableString spannableString = new SpannableString(value);
        spannableString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.color_blue_deep)),
                value.length() - jumpTxt.length(), value.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new TextClick(), value.length() - jumpTxt.length(), value.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        ((TextView) helper.getView(R.id.tv_answer)).setMovementMethod(LinkMovementMethod.getInstance());

        return spannableString;
    }

    private void gotoLogin() {
        String loginType = SharedPreUtils.getString(mContext, SharedPreUtils.Login_Type);

        if (TextUtils.isEmpty(loginType)) {
            ARouter.getInstance().build(Constants.ARouterUriConst.ACCOUNTLOGIN).navigation();
        } else {
            if (loginType.equals("Account"))
                ARouter.getInstance().build(Constants.ARouterUriConst.ACCOUNTLOGIN).navigation();
            else if (loginType.equals("Mobile"))
                ARouter.getInstance().build(Constants.ARouterUriConst.MOBILELOGIN).navigation();
        }
    }

    private class TextClick extends ClickableSpan {

        @Override
        public void onClick(View widget) {
            String value = ((TextView) widget).getText().toString();
            User user = User.getInstance();

            if (value.endsWith("立即开户")) {
                if (null == user || !user.isLogin()) {
                    gotoLogin();
                } else {
                    RxBus.getInstance().post(Constants.RxBusConst.RXBUS_LOGOUT_SUCCESS, null);
                    ARouter.getInstance().build(Constants.ARouterUriConst.MAIN).navigation();

                    mActivity.finish();
                }
            } else if (value.endsWith("去绑定")) {
                if (null == user || !user.isLogin()) {
                    gotoLogin();
                } else {
                    ARouter.getInstance()
                            .build(Constants.ARouterUriConst.AUTHENTICATION)
                            .withString("Type", "2")
                            .navigation();

                    mActivity.finish();
                }
            } else if (value.endsWith("去下单")) {
                if (null == user || !user.isLogin()) {
                    gotoLogin();
                } else {
                    RxBus.getInstance().post(Constants.RxBusConst.RXBUS_TRANSACTION_PLACE_ORDER, null);
                    ARouter.getInstance().build(Constants.ARouterUriConst.MAIN).navigation();

                    mActivity.finish();
                }
            } else if (value.endsWith("去撤单")) {
                if (null == user || !user.isLogin()) {
                    gotoLogin();
                } else {
                    RxBus.getInstance().post(Constants.RxBusConst.RXBUS_TRANSACTION_CANCEL_ORDER, null);
                    ARouter.getInstance().build(Constants.ARouterUriConst.MAIN).navigation();

                    mActivity.finish();
                }
            } else if (value.endsWith("查看当前持仓")) {
                if (null == user || !user.isLogin()) {
                    gotoLogin();
                } else {
                    RxBus.getInstance().post(Constants.RxBusConst.RXBUS_TRANSACTION_HOLD_POSITIONS, null);
                    ARouter.getInstance().build(Constants.ARouterUriConst.MAIN).navigation();

                    mActivity.finish();
                }
            } else if (value.endsWith("立即入金")) {
                if (null == user || !user.isLogin()) {
                    gotoLogin();
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
