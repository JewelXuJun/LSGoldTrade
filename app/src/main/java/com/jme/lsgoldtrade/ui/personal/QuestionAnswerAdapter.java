package com.jme.lsgoldtrade.ui.personal;

import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jme.common.util.RxBus;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.domain.QuestionAnswerVo;

import java.util.List;

public class QuestionAnswerAdapter extends BaseQuickAdapter<QuestionAnswerVo, BaseViewHolder> {

    public QuestionAnswerAdapter(int layoutResId, @Nullable List<QuestionAnswerVo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, QuestionAnswerVo item) {
        if (null == item)
            return;

        String ask = item.getAsk();

        helper.setText(R.id.tv_question, item.getQuestion())
                .setText(R.id.tv_answer, ask.equals(mContext.getResources().getString(R.string.personal_customer_service_cannot_answer))
                        ? callCustomerService(ask, helper) : ask);
    }

    private SpannableString callCustomerService(String value, BaseViewHolder helper) {
        SpannableString spannableString = new SpannableString(value);
        spannableString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.color_blue_deep)),
                value.length() - 4, value.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new TextClick(), value.length() - 4, value.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        ((TextView) helper.getView(R.id.tv_answer)).setMovementMethod(LinkMovementMethod.getInstance());

        return spannableString;
    }

    private class TextClick extends ClickableSpan {

        @Override
        public void onClick(View widget) {
            RxBus.getInstance().post(Constants.RxBusConst.RXBUS_CUSTOMER_SERVICE, null);
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setUnderlineText(false);
        }
    }

}
