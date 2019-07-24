package com.jme.lsgoldtrade.ui.tradingbox;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;

import com.alibaba.android.arouter.launcher.ARouter;
import com.google.gson.Gson;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseFragment;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.FragmentTradingBoxHistoryBinding;
import com.jme.lsgoldtrade.domain.TradingBoxDetailsVo;
import com.jme.lsgoldtrade.service.ManagementService;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

public class TradingBoxHistoryFragment extends JMEBaseFragment {

    private FragmentTradingBoxHistoryBinding mBinding;

    private String mTradeId;
    private String mContract;
    private String mDirection;
    private String mType;
    private long mTime = 0;

    private CountDownTimer mCountDownTimer;

    private List<TradingBoxDetailsVo.RelevantInfoListVosBean> mRelevantInfoListVosBeanList;

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_trading_box_history;
    }

    @Override
    protected void initView() {
        super.initView();
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);

        mBinding.layoutDate.setVisibility(mType.equals("1") ? View.GONE : View.VISIBLE);
    }

    @Override
    protected void initListener() {
        super.initListener();
    }

    @Override
    public void initBinding() {
        super.initBinding();

        mBinding = (FragmentTradingBoxHistoryBinding) mBindingUtil;
        mBinding.setHandlers(new ClickHandlers());
    }

    public void setData(String tradeId, String type) {
        mTradeId = tradeId;
        mType = type;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser) {
            if (TextUtils.isEmpty(mTradeId) || null == mBinding)
                return;

            getTradeBoxByTradeId();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (TextUtils.isEmpty(mTradeId))
            return;

        getTradeBoxByTradeId();
    }

    private void initTimer() {
        mCountDownTimer = new CountDownTimer(mTime * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                String value = formatLongToTimeStr(millisUntilFinished / 1000);
                String[] values = value.split(":");

                mBinding.tvDay.setText(values[0]);
                mBinding.tvHour.setText(values[1]);
                mBinding.tvMinute.setText(values[2]);
                mBinding.tvSecond.setText(values[3]);
            }

            @Override
            public void onFinish() {
                mBinding.tvAgree.setBackground(ContextCompat.getDrawable(mContext, R.drawable.bg_click_not));
                mBinding.tvOpposition.setBackground(ContextCompat.getDrawable(mContext, R.drawable.bg_click_not));
                mBinding.tvAgree.setEnabled(false);
                mBinding.tvOpposition.setEnabled(false);
            }

        }.start();
    }

    private void setProgressData(String directionUpRate, String directionDownRate) {
        if (TextUtils.isEmpty(directionUpRate) || TextUtils.isEmpty(directionDownRate)) {
            mBinding.progressDefault.setVisibility(View.VISIBLE);
            mBinding.progress.setVisibility(View.GONE);
        } else {
            if (new BigDecimal(directionUpRate).compareTo(new BigDecimal(0)) == 0
                    && new BigDecimal(directionDownRate).compareTo(new BigDecimal(0)) == 0) {
                mBinding.progressDefault.setVisibility(View.VISIBLE);
                mBinding.progress.setVisibility(View.GONE);
            } else {
                mBinding.progressDefault.setVisibility(View.GONE);
                mBinding.progress.setVisibility(View.VISIBLE);

                if (directionUpRate.contains("."))
                    directionUpRate = directionUpRate.substring(0, directionUpRate.indexOf("."));

                mBinding.progress.setProgress(Integer.parseInt(directionUpRate));
            }
        }
    }

    public String formatLongToTimeStr(Long time) {
        int day = 0;
        int hour = 0;
        int minute = 0;
        int second = time.intValue();
        String dayStr;
        String hourStr;
        String minuteStr;
        String secondStr;

        if (time > 60) {
            minute = second / 60;
            second = second % 60;
        }

        if (minute > 60) {
            hour = minute / 60;
            minute = minute % 60;
        }

        if (hour > 24) {
            day = hour / 24;
            hour = hour % 24;
        }

        if (day < 10) {
            dayStr = "0" + day;
        } else {
            dayStr = String.valueOf(day);
        }

        if (hour < 10) {
            hourStr = "0" + hour;
        } else {
            hourStr = String.valueOf(hour);
        }

        if (minute < 10) {
            minuteStr = "0" + minute;
        } else {
            minuteStr = String.valueOf(minute);
        }

        if (second < 10) {
            secondStr = "0" + second;
        } else {
            secondStr = String.valueOf(second);
        }

        return dayStr + ":" + hourStr + ":" + minuteStr + ":" + secondStr;
    }

    private void getTradeBoxByTradeId() {
        HashMap<String, String> params = new HashMap<>();
        params.put("tradeId", mTradeId);

        sendRequest(ManagementService.getInstance().tradeBoxByTradeId, params, true);
    }

    public void add(String type) {
        if (TextUtils.isEmpty(mTradeId) || TextUtils.isEmpty(mContract))
            return;

        HashMap<String, String> params = new HashMap<>();
        params.put("boxId", mTradeId);
        params.put("direction", type);
        params.put("variety", mContract);

        sendRequest(ManagementService.getInstance().add, params, true);
    }

    @Override
    protected void DataReturn(DTRequest request, Head head, Object response) {
        super.DataReturn(request, head, response);

        switch (request.getApi().getName()) {
            case "TradeBoxByTradeId":
                if (head.isSuccess()) {
                    TradingBoxDetailsVo tradingBoxDetailsVo;

                    try {
                        tradingBoxDetailsVo = (TradingBoxDetailsVo) response;
                    } catch (Exception e) {
                        tradingBoxDetailsVo = null;

                        e.printStackTrace();
                    }

                    if (null == tradingBoxDetailsVo)
                        return;

                    mContract = tradingBoxDetailsVo.getVariety();
                    mDirection = tradingBoxDetailsVo.getDirection();
                    mTime = tradingBoxDetailsVo.getCloseTime();
                    mRelevantInfoListVosBeanList = tradingBoxDetailsVo.getRelevantInfoListVos();
                    String directionUpRate = tradingBoxDetailsVo.getDirectionUpRate();
                    String directionDownRate = tradingBoxDetailsVo.getDirectionDownRate();
                    String openPositionsTimeBegin = tradingBoxDetailsVo.getOpenPositionsTimeBegin();
                    String openPositionsTimeEnd = tradingBoxDetailsVo.getOpenPositionsTimeEnd();
                    String closePositionsTimeBegin = tradingBoxDetailsVo.getClosePositionsTimeBegin();
                    String closePositionsTimeEnd = tradingBoxDetailsVo.getClosePositionsTimeEnd();
                    String[] openTimeStart = openPositionsTimeBegin.split(" ");
                    String[] openTimeEnd = openPositionsTimeEnd.split(" ");
                    String[] closeTimeStart = closePositionsTimeBegin.split(" ");
                    String[] closeTimeEnd = closePositionsTimeEnd.split(" ");

                    mBinding.tvAbstract.setText(tradingBoxDetailsVo.getChance());
                    mBinding.tvFundamentalAnalysis.setText(tradingBoxDetailsVo.getFundamentalAnalysis());
                    mBinding.tvAnalyst.setText(tradingBoxDetailsVo.getAnalystOpinion());
                    mBinding.tvContract.setText(mContract);
                    mBinding.tvDirection.setText(mDirection.equals("0") ? R.string.text_more : R.string.text_empty);
                    mBinding.layoutTime.setVisibility(mTime <= 0 ? View.GONE : View.VISIBLE);
                    mBinding.tvAgreePercent.setText(directionUpRate + "%");
                    mBinding.tvOppositionPercent.setText(directionDownRate + "%");
                    mBinding.tvAgreeNumber.setText(String.format(mContext.getResources().getString(R.string.trading_box_ticket), String.valueOf(tradingBoxDetailsVo.getDirectionUpNum())));
                    mBinding.tvOppositionNumber.setText(String.format(mContext.getResources().getString(R.string.trading_box_ticket), String.valueOf(tradingBoxDetailsVo.getDirectionDownNum())));
                    mBinding.tvAgree.setBackground(mTime <= 0 ? ContextCompat.getDrawable(mContext, R.drawable.bg_click_not)
                            : ContextCompat.getDrawable(mContext, R.drawable.bg_btn_blue_solid));
                    mBinding.tvOpposition.setBackground(mTime <= 0 ? ContextCompat.getDrawable(mContext, R.drawable.bg_click_not)
                            : ContextCompat.getDrawable(mContext, R.drawable.bg_btn_blue_solid));
                    mBinding.tvAgree.setEnabled(mTime <= 0 ? false : true);
                    mBinding.tvOpposition.setEnabled(mTime <= 0 ? false : true);

                    initTimer();
                    setProgressData(directionUpRate, directionDownRate);

                    mBinding.tvOpenTimeStartDate.setText(openTimeStart[0].replace("-", "/"));
                    mBinding.tvOpenTimeStartHour.setText(openTimeStart[1]);
                    mBinding.tvOpenTimeEndDate.setText(openTimeEnd[0].replace("-", "/"));
                    mBinding.tvOpenTimeEndHour.setText(openTimeEnd[1]);
                    mBinding.tvOpenTimeEqualStartDate.setText(closeTimeStart[0].replace("-", "/"));
                    mBinding.tvOpenTimeEqualStartHour.setText(closeTimeStart[1]);
                    mBinding.tvOpenTimeEqualEndDate.setText(closeTimeEnd[0].replace("-", "/"));
                    mBinding.tvOpenTimeEqualEndHour.setText(closeTimeEnd[1]);
                }

                break;
            case "Add":
                if (head.isSuccess()) {
                    ARouter.getInstance()
                            .build(Constants.ARouterUriConst.PLACEORDER)
                            .withString("Type", "Vote")
                            .withString("Direction", request.getParams().get("direction").equals("0") ? mDirection
                                    : mDirection.equals("0") ? "1" : "0")
                            .withString("TradeId", mTradeId)
                            .navigation();
                }

                break;
        }
    }

    public class ClickHandlers {

        public void onClickInfo() {
            if (null == mRelevantInfoListVosBeanList || 0 == mRelevantInfoListVosBeanList.size())
                return;

            ARouter.getInstance()
                    .build(Constants.ARouterUriConst.RELEVANTINFO)
                    .withString("InfoList", new Gson().toJson(mRelevantInfoListVosBeanList))
                    .navigation();
        }

        public void onClickAgree() {
            if (null == mUser || !mUser.isLogin())
                ARouter.getInstance().build(Constants.ARouterUriConst.ACCOUNTLOGIN).navigation();
            else
                add("0");
        }

        public void onClickOpposition() {
            if (null == mUser || !mUser.isLogin())
                ARouter.getInstance().build(Constants.ARouterUriConst.ACCOUNTLOGIN).navigation();
            else
                add("1");
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (null != mCountDownTimer)
            mCountDownTimer.cancel();
    }
}
