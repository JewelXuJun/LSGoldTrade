package com.jme.lsgoldtrade.ui.tradingbox;

import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.StyleSpan;
import android.view.Gravity;

import com.jme.common.util.DateUtil;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseFragment;
import com.jme.lsgoldtrade.databinding.FragmentTradingBoxBinding;
import com.jme.lsgoldtrade.domain.TradingBoxDataInfoVo;
import com.jme.lsgoldtrade.util.PicassoUtils;
import com.jme.lsgoldtrade.view.TradingBoxPopupwindow;

public class TradingBoxFragment extends JMEBaseFragment {

    private FragmentTradingBoxBinding mBinding;

    private TradingBoxDataInfoVo.HistoryVoBean mHistoryVoBean;

    private TradingBoxPopupwindow mWindow;

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_trading_box;
    }

    @Override
    protected void initView() {
        super.initView();

        mWindow = new TradingBoxPopupwindow(mContext);
        mWindow.setOutsideTouchable(true);
        mWindow.setFocusable(true);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);

        setValue();
    }

    @Override
    protected void initListener() {
        super.initListener();
    }

    @Override
    public void initBinding() {
        super.initBinding();

        mBinding = (FragmentTradingBoxBinding) mBindingUtil;
        mBinding.setHandlers(new ClickHandlers());
    }

    public void setData(TradingBoxDataInfoVo.HistoryVoBean historyVoBean) {
        mHistoryVoBean = historyVoBean;
    }

    private void setValue() {
        if (null == mHistoryVoBean)
            return;

        String chance = mHistoryVoBean.getChance();
        String analystOpinion = mHistoryVoBean.getAnalystOpinion();
        String direction = mHistoryVoBean.getDirection();
        String etfUrl = mHistoryVoBean.getEtfUrl();
        String moodUrl = mHistoryVoBean.getMoodUrl();
        String pushTime = mHistoryVoBean.getPushTime();
        String variety = mHistoryVoBean.getVariety();

        mBinding.tvAbstract.setText(chance);
        mBinding.tvContract.setText(String.format(mContext.getResources().getString(R.string.trading_box_variety), variety,
                direction.equals("0") ? mContext.getResources().getString(R.string.text_more) : mContext.getResources().getString(R.string.text_empty)));
        mBinding.tvPublishTime.setText(TextUtils.isEmpty(pushTime) ? "" : String.format(mContext.getResources().getString(R.string.trading_box_publist_time),
                DateUtil.dataToStringWithData2(DateUtil.dateToLong(pushTime))));
        mBinding.tvAnalyst.setText(analystOpinion);

        PicassoUtils.getInstance().loadImg(mContext, moodUrl, mBinding.imgSpeculation);
        PicassoUtils.getInstance().loadImg(mContext, etfUrl, mBinding.imgEtf);
    }

    public class ClickHandlers {

        public void onClickSpeculation() {
            if (null != mWindow && !mWindow.isShowing()) {
                String value = mContext.getResources().getString(R.string.trading_box_speculation_message);
                int firstPosition = value.indexOf("数据来源：");
                int secondPostion = value.indexOf("统计方法：");
                int thirdPostion = value.indexOf("数据释义：");
                int fourthPostion = value.indexOf("数据解读：");

                SpannableString spannableString = new SpannableString(value);
                spannableString.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), firstPosition, firstPosition + 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                spannableString.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), secondPostion, secondPostion + 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                spannableString.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), thirdPostion, thirdPostion + 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                spannableString.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), fourthPostion, fourthPostion + 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                mWindow.setData(mContext.getResources().getString(R.string.trading_box_speculation), spannableString);
                mWindow.showAtLocation(mBinding.tvAbstract, Gravity.CENTER, 0, 0);
            }
        }

        public void onClickETF() {
            if (null != mWindow && !mWindow.isShowing()) {
                String value = mContext.getResources().getString(R.string.trading_box_gold_etf_message);
                int firstPosition = value.indexOf("公布机构：");
                int secondPostion = value.indexOf("发布频率：");
                int thirdPostion = value.indexOf("统计方法：");
                int fourthPostion = value.indexOf("数据释义：");
                int fifthPostion = value.indexOf("数据解读：");

                SpannableString spannableString = new SpannableString(value);
                spannableString.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), firstPosition, firstPosition + 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                spannableString.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), secondPostion, secondPostion + 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                spannableString.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), thirdPostion, thirdPostion + 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                spannableString.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), fourthPostion, fourthPostion + 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                spannableString.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), fifthPostion, fifthPostion + 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                mWindow.setData(mContext.getResources().getString(R.string.trading_box_gold_etf), spannableString);
                mWindow.showAtLocation(mBinding.tvAbstract, Gravity.CENTER, 0, 0);
            }
        }
    }
}
