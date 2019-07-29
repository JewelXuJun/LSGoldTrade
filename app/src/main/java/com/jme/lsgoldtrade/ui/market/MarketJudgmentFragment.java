package com.jme.lsgoldtrade.ui.market;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.datai.common.charts.kchart.KData;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.common.util.KChartVo;
import com.jme.common.util.NetWorkUtils;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseFragment;
import com.jme.lsgoldtrade.config.AppConfig;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.FragmentMarketJudgmentBinding;
import com.jme.lsgoldtrade.domain.ContractInfoVo;
import com.jme.lsgoldtrade.domain.MarketJudgmentListVo;
import com.jme.lsgoldtrade.domain.TenSpeedVo;
import com.jme.lsgoldtrade.service.ManagementService;
import com.jme.lsgoldtrade.service.MarketService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class MarketJudgmentFragment extends JMEBaseFragment {

    private FragmentMarketJudgmentBinding mBinding;

    private static final int NONE = 0;
    private static final int INIT = 1;
    private static final int NEWEST = 2;
    private static final String DIRECTION_AFTER = "1";
    private static final String DIRECTION_BEFORE = "2";
    private static final String COUNT_KCHART = "200";

    private String mAnalystID;
    private String mContractId;
    private String mHasNext;
    private String mHasPre;
    private int mOffset = 0;
    private int mLastIndex = 0;
    private boolean bFlag = true;
    private int iRequestKDataFlag = NONE;

    private MarketJudgmentListVo mMarketJudgmentListVo;
    private MarketJudgmentListVo.WeekBean mMondayBean;
    private MarketJudgmentListVo.WeekBean mTuesdayBean;
    private MarketJudgmentListVo.WeekBean mWendesdayBean;
    private MarketJudgmentListVo.WeekBean mThursdayBean;
    private MarketJudgmentListVo.WeekBean mFridayBean;
    private TenSpeedVo mTenSpeedVo;

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constants.Msg.MSG_JUDGEMENT_UPDATE_DATA:
                    mHandler.removeMessages(Constants.Msg.MSG_JUDGEMENT_UPDATE_DATA);

                    updateData();

                    mHandler.sendEmptyMessageDelayed(Constants.Msg.MSG_JUDGEMENT_UPDATE_DATA, getTimeInterval());

                    break;
                case Constants.Msg.MSG_JUDGEMENT_RELOAD_DATA:
                    mHandler.removeMessages(Constants.Msg.MSG_JUDGEMENT_RELOAD_DATA);

                    getTenSpeedQuotes();

                    getInitKChartData();

                    break;
            }

            super.handleMessage(msg);
        }
    };

    public static MarketJudgmentFragment newInstance(String analystID, String contractId) {
        MarketJudgmentFragment fragment = new MarketJudgmentFragment();

        Bundle bundle = new Bundle();
        bundle.putString("AnalystID", analystID);
        bundle.putString("ContractID", contractId);

        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_market_judgment;
    }

    @Override
    protected void initView() {
        super.initView();

        mBinding = (FragmentMarketJudgmentBinding) mBindingUtil;

        initKChart();
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);

        mAnalystID = getArguments().getString("AnalystID");
        mContractId = getArguments().getString("ContractID");

        getMarketJudgeList();
    }

    @Override
    protected void initListener() {
        super.initListener();
    }

    @Override
    public void initBinding() {
        super.initBinding();

        mBinding.setHandlers(new ClickHandlers());
    }

    @Override
    public void onResume() {
        super.onResume();

        initRawData();
    }

    @Override
    public void onPause() {
        super.onPause();

        removeMessage();
    }

    private void initKChart() {
        mBinding.kChart.setIsFromServerMAs(false);
        mBinding.kChart.setHasTradeVolume(true);
        mBinding.kChart.setLandscapeButtonVisible(false);
        mBinding.kChart.config();
        mBinding.kChart.setUnit(KData.Unit.MIN30);
        mBinding.kChart.switchKDataToCurrentUnit();
    }

    private void removeMessage() {
        mHandler.removeMessages(Constants.Msg.MSG_JUDGEMENT_UPDATE_DATA);
        mHandler.removeMessages(Constants.Msg.MSG_JUDGEMENT_RELOAD_DATA);
    }

    public void initRawData() {
        if (TextUtils.isEmpty(mContractId))
            return;

        bFlag = true;

        updateData();
    }

    private void updateData() {
        getTenSpeedQuotes();
        getNewestKChartData();
    }

    public String getContractID() {
        return mContractId;
    }

    public ContractInfoVo getContractInfo() {
        if (TextUtils.isEmpty(mContractId) || null == mContract)
            return null;

        return mContract.getContractInfoFromID(mContractId);
    }

    public TenSpeedVo getTenSpeedVo() {
        return mTenSpeedVo;
    }

    private void setDate() {
        mLastIndex = mMarketJudgmentListVo.getLastIndex();
        mHasPre = mMarketJudgmentListVo.getHasPre();
        mHasNext = mMarketJudgmentListVo.getHasNext();
        mMondayBean = mMarketJudgmentListVo.getMONDAY();
        mTuesdayBean = mMarketJudgmentListVo.getTUESDAY();
        mWendesdayBean = mMarketJudgmentListVo.getWEDNESDAY();
        mThursdayBean = mMarketJudgmentListVo.getTHURSDAY();
        mFridayBean = mMarketJudgmentListVo.getFRIDAY();

        setChangeLayout();
        setWeekSelectLayout();
        setMondayData();
        setTuesdayData();
        setWendesdayData();
        setThursdayData();
        setFridayData();
        setInterpretData();
    }

    private void setChangeLayout() {
        mBinding.imgPrevious.setVisibility(TextUtils.isEmpty(mHasPre) ? View.GONE : mHasPre.equals("T") ? View.VISIBLE : View.GONE);
        mBinding.imgNext.setVisibility(TextUtils.isEmpty(mHasNext) ? View.GONE : mHasNext.equals("T") ? View.VISIBLE : View.GONE);
    }

    private void setMondayData() {
        if (null == mMondayBean)
            return;

        mBinding.tvMondayWeek.setText(mMondayBean.getWeek());
        mBinding.tvMondayData.setText(mMondayBean.getDateFormat());

        setJudgmentData(mMondayBean.getJudge(), mBinding.imgMondayJudgment);
        setJudgmentDataResult(mMondayBean.getJudgeResult(), mBinding.imgMondayJudgmentResult);
    }

    private void setTuesdayData() {
        if (null == mTuesdayBean)
            return;

        mBinding.tvTuesdayWeek.setText(mTuesdayBean.getWeek());
        mBinding.tvTuesdayData.setText(mTuesdayBean.getDateFormat());

        setJudgmentData(mTuesdayBean.getJudge(), mBinding.imgTuesdayJudgment);
        setJudgmentDataResult(mTuesdayBean.getJudgeResult(), mBinding.imgTuesdayJudgmentResult);
    }

    private void setWendesdayData() {
        if (null == mWendesdayBean)
            return;

        mBinding.tvWednesdayWeek.setText(mWendesdayBean.getWeek());
        mBinding.tvWednesdayData.setText(mWendesdayBean.getDateFormat());

        setJudgmentData(mWendesdayBean.getJudge(), mBinding.imgWednesdayJudgment);
        setJudgmentDataResult(mWendesdayBean.getJudgeResult(), mBinding.imgWednesdayJudgmentResult);
    }

    private void setThursdayData() {
        if (null == mThursdayBean)
            return;

        mBinding.tvThursdayWeek.setText(mThursdayBean.getWeek());
        mBinding.tvThursdayData.setText(mThursdayBean.getDateFormat());

        setJudgmentData(mThursdayBean.getJudge(), mBinding.imgThursdayJudgment);
        setJudgmentDataResult(mThursdayBean.getJudgeResult(), mBinding.imgThursdayJudgmentResult);
    }

    private void setFridayData() {
        if (null == mFridayBean)
            return;

        mBinding.tvFridayWeek.setText(mFridayBean.getWeek());
        mBinding.tvFridayData.setText(mFridayBean.getDateFormat());

        setJudgmentData(mFridayBean.getJudge(), mBinding.imgFridayJudgment);
        setJudgmentDataResult(mFridayBean.getJudgeResult(), mBinding.imgFridayJudgmentResult);
    }

    private void setJudgmentData(String judgment, ImageView imageView) {
        if (TextUtils.isEmpty(judgment))
            imageView.setImageDrawable(null);
        else if (judgment.equals("S"))
            imageView.setImageDrawable(ContextCompat.getDrawable(mContext, R.mipmap.ic_judment_up));
        else if (judgment.equals("X"))
            imageView.setImageDrawable(ContextCompat.getDrawable(mContext, R.mipmap.ic_judgment_down));
        else if (judgment.equals("H"))
            imageView.setImageDrawable(ContextCompat.getDrawable(mContext, R.mipmap.ic_judgment_transverse));
    }

    private void setJudgmentDataResult(String judgmentResult, ImageView imageView) {
        if (TextUtils.isEmpty(judgmentResult))
            imageView.setImageResource(R.mipmap.ic_judgment_result_transverse);
        else if (judgmentResult.equals("T"))
            imageView.setImageResource(R.mipmap.ic_judgment_result_true);
        else if (judgmentResult.equals("F"))
            imageView.setImageResource(R.mipmap.ic_judgment_result_error);
    }

    private void setInterpretData() {
        if (mLastIndex == 1 && null != mMondayBean)
            mBinding.tvInterpret.setText(mMondayBean.getUnscramble());
        else if (mLastIndex == 2 && null != mTuesdayBean)
            mBinding.tvInterpret.setText(mTuesdayBean.getUnscramble());
        else if (mLastIndex == 3 && null != mWendesdayBean)
            mBinding.tvInterpret.setText(mWendesdayBean.getUnscramble());
        else if (mLastIndex == 4 && null != mThursdayBean)
            mBinding.tvInterpret.setText(mThursdayBean.getUnscramble());
        else if (mLastIndex == 5 && null != mFridayBean)
            mBinding.tvInterpret.setText(mFridayBean.getUnscramble());
    }

    private void setWeekSelectLayout() {
        mBinding.tvMondayWeek.setTextColor(mLastIndex == 1 ? ContextCompat.getColor(mContext, R.color.color_blue_deep)
                : ContextCompat.getColor(mContext, R.color.black_deep));
        mBinding.tvMondayData.setTextColor(mLastIndex == 1 ? ContextCompat.getColor(mContext, R.color.color_blue_deep)
                : ContextCompat.getColor(mContext, R.color.black_deep));
        mBinding.tvTuesdayWeek.setTextColor(mLastIndex == 2 ? ContextCompat.getColor(mContext, R.color.color_blue_deep)
                : ContextCompat.getColor(mContext, R.color.black_deep));
        mBinding.tvTuesdayData.setTextColor(mLastIndex == 2 ? ContextCompat.getColor(mContext, R.color.color_blue_deep)
                : ContextCompat.getColor(mContext, R.color.black_deep));
        mBinding.tvWednesdayWeek.setTextColor(mLastIndex == 3 ? ContextCompat.getColor(mContext, R.color.color_blue_deep)
                : ContextCompat.getColor(mContext, R.color.black_deep));
        mBinding.tvWednesdayData.setTextColor(mLastIndex == 3 ? ContextCompat.getColor(mContext, R.color.color_blue_deep)
                : ContextCompat.getColor(mContext, R.color.black_deep));
        mBinding.tvThursdayWeek.setTextColor(mLastIndex == 4 ? ContextCompat.getColor(mContext, R.color.color_blue_deep)
                : ContextCompat.getColor(mContext, R.color.black_deep));
        mBinding.tvThursdayData.setTextColor(mLastIndex == 4 ? ContextCompat.getColor(mContext, R.color.color_blue_deep)
                : ContextCompat.getColor(mContext, R.color.black_deep));
        mBinding.tvFridayWeek.setTextColor(mLastIndex == 5 ? ContextCompat.getColor(mContext, R.color.color_blue_deep)
                : ContextCompat.getColor(mContext, R.color.black_deep));
        mBinding.tvFridayData.setTextColor(mLastIndex == 5 ? ContextCompat.getColor(mContext, R.color.color_blue_deep)
                : ContextCompat.getColor(mContext, R.color.black_deep));
    }

    public void getInitKChartData() {
        if (iRequestKDataFlag != NONE) {
            mHandler.sendEmptyMessageDelayed(Constants.Msg.MSG_JUDGEMENT_RELOAD_DATA, 100);

            return;
        }

        if (mBinding.kChart.hasKDataInCurrentUnit()) {
            mHandler.sendEmptyMessage(Constants.Msg.MSG_JUDGEMENT_UPDATE_DATA);
        } else {
            iRequestKDataFlag = INIT;

            getKChartQuotes("", DIRECTION_BEFORE);
        }
    }

    public void getNewestKChartData() {
        if (mBinding.kChart.getDataCount() == 0) {
            getInitKChartData();

            return;
        }

        if (iRequestKDataFlag != NONE)
            return;

        if (mHandler.hasMessages(Constants.Msg.MSG_JUDGEMENT_RELOAD_DATA))
            return;

        long tick = mBinding.kChart.getNewestTimeTick(2);
        Date date = new Date(tick);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");

        iRequestKDataFlag = NEWEST;

        getKChartQuotes(sdf.format(date), DIRECTION_AFTER);
    }

    public void updateKChartData(List<KChartVo> list) {
        if (iRequestKDataFlag == INIT) {
            if (null != list && list.size() > 0)
                mBinding.kChart.loadInitialData(list, KData.Unit.MIN30);
        } else if (iRequestKDataFlag == NEWEST) {
            if (null != list && list.size() > 0)
                mBinding.kChart.loadNewestData(list, KData.Unit.MIN30);
        }
    }

    private long getTimeInterval() {
        return NetWorkUtils.isWifiConnected(mContext) ? AppConfig.TimeInterval_WiFi : AppConfig.TimeInterval_NetWork;
    }

    private void getMarketJudgeList() {
        if (TextUtils.isEmpty(mAnalystID))
            return;

        HashMap<String, String> params = new HashMap<>();
        params.put("analystId", mAnalystID);
        params.put("offset", String.valueOf(mOffset));

        sendRequest(ManagementService.getInstance().marketJudgeList, params, true);
    }

    private void getTenSpeedQuotes() {
        if (TextUtils.isEmpty(mContractId))
            return;

        HashMap<String, String> params = new HashMap<>();
        params.put("list", mContractId);

        sendRequest(MarketService.getInstance().getTenSpeedQuotes, params, false, false, false);
    }

    private void getKChartQuotes(String time, String flag) {
        if (TextUtils.isEmpty(mContractId))
            return;

        HashMap<String, String> params = new HashMap<>();
        params.put("type", KData.Unit.MIN30.getCode());
        params.put("contractId", mContractId);
        params.put("quoteTime", time);
        params.put("qryFlag", flag);
        params.put("count", COUNT_KCHART);

        sendRequest(MarketService.getInstance().getKChartQuotes, params, false, false, false);
    }

    @Override
    protected void DataReturn(DTRequest request, Head head, Object response) {
        super.DataReturn(request, head, response);

        switch (request.getApi().getName()) {
            case "MarketJudgeList":
                if (head.isSuccess()) {
                    try {
                        mMarketJudgmentListVo = (MarketJudgmentListVo) response;
                    } catch (Exception e) {
                        mMarketJudgmentListVo = null;

                        e.printStackTrace();
                    }

                    if (null == mMarketJudgmentListVo)
                        return;

                    setDate();
                }

                break;
            case "GetTenSpeedQuotes":
                if (head.isSuccess()) {
                    List<TenSpeedVo> tenSpeedVoList;

                    try {
                        tenSpeedVoList = (List<TenSpeedVo>) response;
                    } catch (Exception e) {
                        tenSpeedVoList = null;

                        e.printStackTrace();
                    }

                    if (null == tenSpeedVoList || 0 == tenSpeedVoList.size())
                        return;

                    mTenSpeedVo = tenSpeedVoList.get(0);
                }

                if (bFlag) {
                    bFlag = false;

                    mHandler.sendEmptyMessageDelayed(Constants.Msg.MSG_JUDGEMENT_UPDATE_DATA, getTimeInterval());
                }

                break;
            case "GetKChartQuotes":
                if (head.isSuccess()) {
                    List<KChartVo> list;

                    try {
                        list = (List<KChartVo>) response;
                    } catch (Exception e) {
                        list = null;

                        e.printStackTrace();
                    }

                    if (null == list || 0 == list.size())
                        return;

                    updateKChartData(list);
                }

                iRequestKDataFlag = NONE;

                break;
        }
    }

    public class ClickHandlers {

        public void onClickLeft() {
            --mOffset;

            getMarketJudgeList();
        }

        public void onClickRight() {
            ++mOffset;

            getMarketJudgeList();
        }

        public void onClickMonday() {
            mLastIndex = 1;

            setInterpretData();
            setWeekSelectLayout();
        }

        public void onClickTuesday() {
            mLastIndex = 2;

            setInterpretData();
            setWeekSelectLayout();
        }

        public void onClickWednesday() {
            mLastIndex = 3;

            setInterpretData();
            setWeekSelectLayout();
        }

        public void onClickThursday() {
            mLastIndex = 4;

            setInterpretData();
            setWeekSelectLayout();
        }

        public void onClickFriday() {
            mLastIndex = 5;

            setInterpretData();
            setWeekSelectLayout();
        }
    }

}
