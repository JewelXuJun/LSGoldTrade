package com.jme.lsgoldtrade.ui.market;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import androidx.core.content.ContextCompat;
import androidx.core.hardware.fingerprint.FingerprintManagerCompat;

import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.datai.common.charts.chart.Chart;
import com.datai.common.charts.chart.OnChartListener;
import com.datai.common.charts.fchart.FChart;
import com.datai.common.charts.indicator.Indicator;
import com.datai.common.charts.kchart.KChart;
import com.datai.common.charts.kchart.KData;
import com.datai.common.charts.kchart.OnKChartListener;
import com.datai.common.charts.kchart.OnKChartSelectedListener;
import com.datai.common.charts.tchart.TChart;
import com.google.gson.Gson;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.common.util.DateUtil;
import com.jme.common.util.KChartVo;
import com.jme.common.util.NetWorkUtils;
import com.jme.common.util.RxBus;
import com.jme.common.util.SharedPreUtils;
import com.jme.common.util.StatusBarUtil;
import com.jme.common.util.StringUtils;
import com.jme.common.util.TChartVo;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseActivity;
import com.jme.lsgoldtrade.config.AppConfig;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.ActivityMarketDetailBinding;
import com.jme.lsgoldtrade.domain.AccountVo;
import com.jme.lsgoldtrade.domain.ContractInfoVo;
import com.jme.lsgoldtrade.domain.DetailVo;
import com.jme.lsgoldtrade.domain.LoginResponse;
import com.jme.lsgoldtrade.domain.PasswordInfoVo;
import com.jme.lsgoldtrade.domain.PositionPageVo;
import com.jme.lsgoldtrade.domain.PositionVo;
import com.jme.lsgoldtrade.domain.SectionVo;
import com.jme.lsgoldtrade.domain.TenSpeedVo;
import com.jme.lsgoldtrade.domain.UserInfoVo;
import com.jme.lsgoldtrade.service.ManagementService;
import com.jme.lsgoldtrade.service.MarketService;
import com.jme.lsgoldtrade.service.TradeService;
import com.jme.lsgoldtrade.service.UserService;
import com.jme.lsgoldtrade.view.ConfirmSimplePopupwindow;
import com.jme.lsgoldtrade.view.SignedPopUpWindow;
import com.jme.lsgoldtrade.util.MarketUtil;
import com.jme.lsgoldtrade.view.ConfirmPopupwindow;
import com.jme.lsgoldtrade.view.StockUserDialog;

import java.math.BigDecimal;
import java.net.ConnectException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Subscription;

@Route(path = Constants.ARouterUriConst.MARKETDETAIL)
public class MarketDetailActivity extends JMEBaseActivity implements FChart.OnPriceClickListener, OnKChartSelectedListener {

    private ActivityMarketDetailBinding mBinding;

    private TenSpeedVo mTenSpeedVo;
    private AccountVo mAccountVo;
    private PositionVo mPositionVo;
    private ContractInfoVo mContractInfoVo;
    private MarketTradePopupWindow mMarketTradePopupWindow;
    private ConfirmPopupwindow mConfirmPopupwindow;
    private SignedPopUpWindow mSignedPopUpWindow;
    private Chart mChart;
    private TChart mTChart;
    private KChart mKChart;

    private static final int NONE = 0;
    private static final int INIT = 1;
    private static final int NEWEST = 2;
    private static final int MORE = 3;
    private static final String DIRECTION_AFTER = "1";
    private static final String DIRECTION_BEFORE = "2";
    private static final String COUNT_KCHART = "200";

    private String mContractId;
    private String mPagingKey = "";
    private boolean bFlag = true;
    private boolean bHighlight = false;
    private boolean bHasMoreKDataFlag = true;
    private boolean bRequestTDataFlag = false;
    private boolean bGetTradeDateFlag = false;
    private int iRequestKDataFlag = NONE;
    private int mTChartCount;
    private int mBsFlag = 0;
    private int mOcFlag = 0;
    private long mLongPositionMargin = 0;
    private long mShortPositionMargin = 0;
    private String[] mContractIdList;

    private Subscription mRxbus;
    private StockUserDialog mStockUserDialog;

    private int mCallEntry = 0;
    private ConfirmSimplePopupwindow mTradingPasswordConfirmSimplePopupwindow;

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constants.Msg.MSG_UPDATE_DATA:
                    mHandler.removeMessages(Constants.Msg.MSG_UPDATE_DATA);

                    updateData(false);

                    mHandler.sendEmptyMessageDelayed(Constants.Msg.MSG_UPDATE_DATA, getTimeInterval());

                    break;
                case Constants.Msg.MSG_RELOAD_DATA:
                    mHandler.removeMessages(Constants.Msg.MSG_RELOAD_DATA);

                    getTenSpeedQuotes(false);

                    getInitKChartData();

                    break;
            }

            super.handleMessage(msg);
        }
    };

    @Override
    protected int getContentViewId() {
        return R.layout.activity_market_detail;
    }

    @Override
    protected void initView() {
        super.initView();

        mContractId = getIntent().getStringExtra("ContractId");
        mContractIdList = StringUtils.getStringArray(getIntent().getStringExtra("ContractListValue"));

        setChangeLayout(getPosition(mContractId));
        setBackGroundColor(R.color.common_font_stable);

        mChart = mBinding.chart;
        mTChart = mChart.getTChart();
        mKChart = mChart.getKChart();

        mMarketTradePopupWindow = new MarketTradePopupWindow(this);
        mConfirmPopupwindow = new ConfirmPopupwindow(this);
        mSignedPopUpWindow = new SignedPopUpWindow(this);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);

        mTradingPasswordConfirmSimplePopupwindow = new ConfirmSimplePopupwindow(this);
        mTradingPasswordConfirmSimplePopupwindow.setOutsideTouchable(false);
        mTradingPasswordConfirmSimplePopupwindow.setFocusable(false);

        mChart.initChartSort(mUser.isLogin() ? SharedPreUtils.getString(this, SharedPreUtils.MARKET_SORT_LOGIN)
                : SharedPreUtils.getString(this, SharedPreUtils.MARKET_SORT_UNLOGIN));
        mChart.setPriceFormatDigit(2);

        initTChart();
        initKChart();
    }

    @Override
    protected void initListener() {
        super.initListener();

        initRxBus();

        mChart.setOnLandscapeListener((view) -> gotoMarketDetailLandscapeActivity());

        mChart.setOnChartListener(new OnChartListener() {
            @Override
            public void onSwitchUnit(boolean showTChart, KData.Unit unit) {
                iRequestKDataFlag = NONE;

                sendChartRefreshMessage(showTChart);
            }

            @Override
            public void onEditSort() {
                ARouter.getInstance().build(Constants.ARouterUriConst.MARKETEDITSORT).navigation();
            }
        });

        mKChart.setOnKChartListener(new OnKChartListener() {
            @Override
            public void onEnding(long oldestTime, KData.Unit unit) {
                if (mKChart.getDataCount() >= 200)
                    getMoreKChartData(oldestTime, unit);
            }

            @Override
            public void onChartSingleTapped(MotionEvent me) {

            }
        });

        mTChart.getTradeInfoChart().setDoRadioButtonFunction(() -> {
        });
    }

    @Override
    protected void initBinding() {
        super.initBinding();

        mBinding = (ActivityMarketDetailBinding) mBindingUtil;
        mBinding.setHandlers(new ClickHandlers());
    }

    @Override
    protected void onResume() {
        super.onResume();

        initRawData();
    }

    @Override
    protected void onPause() {
        super.onPause();

        removeMessage();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (!mRxbus.isUnsubscribed())
            mRxbus.unsubscribe();
    }

    private void setBackGroundColor(int color) {
        StatusBarUtil.setStatusBarMode(this, true, color);
        mBinding.layoutTitle.setBackgroundColor(ContextCompat.getColor(this, color));
        mBinding.layoutMarketDetail.setBackgroundColor(ContextCompat.getColor(this, color));
    }

    private void initTChart() {
        mTChart.setScaleXEnabled(false);
        mTChart.setIsNeedSupplement(false);
        mTChart.setIsStartFromBeginning(true);
        mTChart.setLandscapeButtonVisible(true);
        mTChart.config();
    }

    private void initKChart() {
        mKChart.setIsFromServerMAs(false);
        mKChart.setHasTradeVolume(true);
        mKChart.setLandscapeButtonVisible(true);
        mKChart.setOnKChartSelectedListener(this);
        mKChart.config();
    }

    public void initRawData() {
        if (TextUtils.isEmpty(mContractId))
            return;

        bFlag = true;
        bGetTradeDateFlag = false;

        mChart.setChartUnit(mChart.getChartUnit());

        mKChart.clearData();

        updateData(true);
    }

    private void initRxBus() {
        mRxbus = RxBus.getInstance().toObserverable(RxBus.Message.class).subscribe(message -> {
            String callType = message.getObject().toString();

            if (TextUtils.isEmpty(callType))
                return;

            switch (callType) {
                case Constants.RxBusConst.RXBUS_MARKETDETAIL_QUICK:
                    Object object = message.getObject2();

                    if (null == object)
                        return;

                    List<String> list = (List<String>) object;

                    if (null == list || 5 != list.size())
                        return;

                    limitOrder(list.get(0), list.get(1), list.get(2), list.get(3), list.get(4));

                    break;

                case Constants.RxBusConst.RXBUS_MARKET_UNIT_SORT_SUCCESS:
                    getTimeLineList();

                    break;

                case Constants.RxBusConst.RXBUS_LOGIN_SUCCESS:
                    if(isOpenStockUser())
                        showStockUserDialog();
                    getTimeLineList();
                    break;
                case Constants.RxBusConst.RXBUS_BUY_MORE_SETPASSWORD_SUCCESS:
                    if (TextUtils.isEmpty(mUser.getAccountID())) {
                        RxBus.getInstance().post(Constants.RxBusConst.RXBUS_TRANSACTION_PLACE_ORDER, mContractId);
                        ARouter.getInstance().build(Constants.ARouterUriConst.MAIN).navigation();
                    } else {
                        if (TextUtils.isEmpty(mContractId) || null == mTenSpeedVo || null == mContract)
                            return;

                        mContractInfoVo = mContract.getContractInfoFromID(mContractId);

                        if (null == mContractInfoVo)
                            return;

                        mBsFlag = 1;
                        mOcFlag = 0;
                        mPagingKey = "";

                        queryLoginResult();
                    }
                    break;
                case Constants.RxBusConst.RXBUS_SALE_EMPTY_SETPASSWORD_SUCCESS:
                    if (TextUtils.isEmpty(mUser.getAccountID())) {
                        RxBus.getInstance().post(Constants.RxBusConst.RXBUS_TRANSACTION_PLACE_ORDER, mContractId);
                        ARouter.getInstance().build(Constants.ARouterUriConst.MAIN).navigation();
                    } else {
                        if (TextUtils.isEmpty(mContractId) || null == mTenSpeedVo || null == mContract)
                            return;

                        mContractInfoVo = mContract.getContractInfoFromID(mContractId);

                        if (null == mContractInfoVo)
                            return;

                        mBsFlag = 2;
                        mOcFlag = 0;
                        mPagingKey = "";

                        queryLoginResult();
                    }
                    break;
                case Constants.RxBusConst.RXBUS_DECLARATION_FORM_SETPASSWORD_SUCCESS:
                    AppConfig.Select_ContractId = mContractId;

                    RxBus.getInstance().post(Constants.RxBusConst.RXBUS_TRANSACTION_PLACE_ORDER, mContractId);
                    ARouter.getInstance().build(Constants.ARouterUriConst.MAIN).navigation();

                    finish();
                    break;
            }
        });
    }

    private int getPosition(String contractId) {
        int position = -1;

        if (!TextUtils.isEmpty(contractId) && null != mContractIdList) {
            for (int i = 0; i < mContractIdList.length; i++) {
                String value = mContractIdList[i];

                if (!TextUtils.isEmpty(value) && value.equals(contractId))
                    position = i;
            }
        }

        return position;
    }

    private void setChangeLayout(int position) {
        mBinding.tvContract.setText(mContractId);

        if (position == -1)
            return;

        mBinding.imgPrevious.setBackground(position == 0 ? ContextCompat.getDrawable(this, R.mipmap.ic_market_previous_prohibit)
                : ContextCompat.getDrawable(this, R.mipmap.ic_market_previous));
        mBinding.imgNext.setBackground(position == mContractIdList.length - 1 ? ContextCompat.getDrawable(this, R.mipmap.ic_market_next_prohibit)
                : ContextCompat.getDrawable(this, R.mipmap.ic_market_next));
        mBinding.layoutPrevious.setVisibility(position == -1 ? View.GONE : View.VISIBLE);
        mBinding.layoutNext.setVisibility(position == -1 ? View.GONE : View.VISIBLE);
    }

    private void updateData(boolean enable) {
        getTenSpeedQuotes(enable);
        getDetail();

        if (mChart.isShowTChart())
            getTChartData();
        else
            getNewestKChartData();
    }

    private void gotoMarketDetailLandscapeActivity() {
        ARouter.getInstance()
                .build(Constants.ARouterUriConst.MARKETDETAILLANDSCAPE)
                .withString("ContractId", mContractId)
                .withString("ChartUnit", mChart.getChartUnit().getCode())
                .navigation(this, Constants.IntentConst.CODE_REQUEST_LANDSCAPE);
    }

    private long getTimeInterval() {
        return NetWorkUtils.isWifiConnected(mContext) ? AppConfig.TimeInterval_WiFi : AppConfig.TimeInterval_NetWork;
    }

    public void sendChartRefreshMessage(boolean showTChart) {
        if (showTChart) {
            bGetTradeDateFlag = false;

            mHandler.sendEmptyMessage(Constants.Msg.MSG_UPDATE_DATA);
        } else {
            mHandler.sendEmptyMessage(Constants.Msg.MSG_RELOAD_DATA);
        }
    }

    private void removeMessage() {
        mHandler.removeMessages(Constants.Msg.MSG_UPDATE_DATA);
        mHandler.removeMessages(Constants.Msg.MSG_RELOAD_DATA);
    }

    public void getTChartData() {
        if (bRequestTDataFlag)
            return;

        if (!bGetTradeDateFlag) {
            getContractSection();

            return;
        }

        bRequestTDataFlag = true;
        bFlag = false;

        getTChartQuotes();
    }

    public void getInitKChartData() {
        if (iRequestKDataFlag != NONE) {
            mHandler.sendEmptyMessageDelayed(Constants.Msg.MSG_RELOAD_DATA, 100);

            return;
        }

        KData.Unit unit = mKChart.getUnit();

        if (null == unit)
            return;

        mKChart.switchKDataToCurrentUnit();

        if (mKChart.hasKDataInCurrentUnit()) {
            mHandler.sendEmptyMessage(Constants.Msg.MSG_UPDATE_DATA);
        } else {
            bHasMoreKDataFlag = true;
            iRequestKDataFlag = INIT;

            getKChartQuotes(unit.getCode(), "", DIRECTION_BEFORE);
        }
    }

    public void getNewestKChartData() {
        if (mKChart.getDataCount() == 0) {
            getInitKChartData();

            return;
        }

        if (iRequestKDataFlag != NONE)
            return;

        if (mHandler.hasMessages(Constants.Msg.MSG_RELOAD_DATA))
            return;

        KData.Unit unit = mKChart.getUnit();

        if (null == unit)
            return;

        long tick = mKChart.getNewestTimeTick(2);
        Date date = new Date(tick);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");

        iRequestKDataFlag = NEWEST;

        getKChartQuotes(unit.getCode(), sdf.format(date), DIRECTION_AFTER);
    }

    public void getMoreKChartData(long oldestTime, KData.Unit unit) {
        if (iRequestKDataFlag != NONE)
            return;

        if (bHasMoreKDataFlag == false)
            return;

        Date date = new Date(oldestTime - 1);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");

        iRequestKDataFlag = MORE;

        getKChartQuotes(unit.getCode(), sdf.format(date), DIRECTION_BEFORE);
    }

    private void getTradeTime(List<SectionVo> list) {
        List<long[]> timeLists = new ArrayList<>();

        for (SectionVo sectionVo : list) {
            if (null != sectionVo && sectionVo.getSectionType() == 2) {
                String startTime = sectionVo.getStartTime();
                String endTime = sectionVo.getEndTime();

                if (!TextUtils.isEmpty(startTime) && !TextUtils.isEmpty(endTime)) {
                    long[] timeValue = new long[2];
                    timeValue[0] = DateUtil.dateToLong(sectionVo.getStartTime()).longValue();
                    timeValue[1] = DateUtil.dateToLong(sectionVo.getEndTime()).longValue();

                    timeLists.add(timeValue);
                }
            }
        }

        if (null != timeLists && timeLists.size() > 0) {
            bGetTradeDateFlag = true;

            mTChart.setTChartXAxisTime(timeLists, AppConfig.Minute);

            calculateTChartCount(timeLists);

            getTChartData();
        }
    }

    private void calculateTChartCount(List<long[]> timeLists) {
        long timeTotal = 0;

        for (long[] times : timeLists) {
            timeTotal = timeTotal + (times[1] - times[0]);
        }

        mTChartCount = new BigDecimal(timeTotal).divide(new BigDecimal(AppConfig.Minute)).intValue() + 1;
    }

    private void updateMarketData(TenSpeedVo tenSpeedVo) {
        mTenSpeedVo = tenSpeedVo;

        String lastSettlePrice = tenSpeedVo.getLastSettlePrice();

        if (TextUtils.isEmpty(lastSettlePrice))
            return;

        if (!bHighlight) {
            String upDown = tenSpeedVo.getUpDown();
            String highestPrice = tenSpeedVo.getHighestPrice();
            String lowestPrice = tenSpeedVo.getLowestPrice();

            int rateType;

            if (TextUtils.isEmpty(upDown))
                rateType = 0;
            else
                rateType = new BigDecimal(upDown).compareTo(new BigDecimal(0));

            setBackGroundColor(MarketUtil.getMarketStateColor(rateType));

            mBinding.tvLastPrice.setText(MarketUtil.getValue(tenSpeedVo.getLatestPrice()));
            mBinding.tvRange.setText(MarketUtil.getMarketRangeValue(rateType, upDown));
            mBinding.tvRate.setText(MarketUtil.getMarketRateValue(rateType, tenSpeedVo.getUpDownRate()));
            mBinding.tvOpen.setText(MarketUtil.getValue(tenSpeedVo.getOpenPrice()));
            mBinding.tvPreclose.setText(MarketUtil.getValue(tenSpeedVo.getLastClosePrice()));
            mBinding.tvTurnVolume.setText(MarketUtil.getVolumeValue2(String.valueOf(new BigDecimal(tenSpeedVo.getTurnover()).divide(new BigDecimal(100))), false));
            mBinding.tvVolume.setText(MarketUtil.getVolumeValue(String.valueOf(tenSpeedVo.getTurnVolume()), false) + "手");
            mBinding.tvStateTime.setText(MarketUtil.getValue(DateUtil.stringToAllTime(tenSpeedVo.getQuoteTime())));
            mBinding.tvHigh.setText(MarketUtil.getValue(highestPrice));
            mBinding.tvHigh.setTextColor(ContextCompat.getColor(this, MarketUtil.getMarketStateColor(
                    TextUtils.isEmpty(highestPrice) ? -2 : new BigDecimal(highestPrice).compareTo(new BigDecimal(lastSettlePrice)))));
            mBinding.tvLow.setText(MarketUtil.getValue(lowestPrice));
            mBinding.tvLow.setTextColor(ContextCompat.getColor(this, MarketUtil.getMarketStateColor(
                    TextUtils.isEmpty(lowestPrice) ? -2 : new BigDecimal(lowestPrice).compareTo(new BigDecimal(lastSettlePrice)))));
        }

        if (!TextUtils.isEmpty(lastSettlePrice))
            mTChart.setPreClose(lastSettlePrice);

        mTChart.loadTradeInfoChartData(tenSpeedVo.getTenAskLists(), tenSpeedVo.getTenBidLists());
    }

    public void updateKChartData(List<KChartVo> list, KData.Unit unit) {
        if (iRequestKDataFlag == INIT) {
            if (null != list && list.size() > 0)
                mKChart.loadInitialData(list, unit);
        } else if (iRequestKDataFlag == MORE) {
            if (null != list && list.size() > 0)
                mKChart.loadMoreData(list, unit);
            else
                bHasMoreKDataFlag = false;
        } else if (iRequestKDataFlag == NEWEST) {
            if (null != list && list.size() > 0)
                mKChart.loadNewestData(list, unit);
        }
    }

    private void setDetailData(List<DetailVo> list) {
        List<String[]> listStr = new ArrayList<>();

        for (DetailVo detailVo : list) {
            if (null != detailVo) {
                String[] values = new String[3];
                values[0] = String.valueOf(DateUtil.dateToLong(detailVo.getQuoteTime()));
                values[1] = MarketUtil.getPriceValue(detailVo.getLatestPrice());
                values[2] = String.valueOf(detailVo.getTurnVolume());

                listStr.add(values);
            }
        }

        mTChart.loadDealChartData(listStr);
    }

    private void setDataFromKChart(HashMap<String, Object> entry, float preClose) {
        if (null == entry || 0 == preClose)
            return;

        float laststPirce = (float) entry.get(Indicator.K_CLOSE);
        float updown = new BigDecimal(laststPirce).subtract(new BigDecimal(preClose)).setScale(2, BigDecimal.ROUND_DOWN).floatValue();
        float upDownRate = new BigDecimal(updown).divide(new BigDecimal(preClose), 4, BigDecimal.ROUND_UP).multiply(new BigDecimal(100)).setScale(2).floatValue();
        float openPrice = (float) entry.get(Indicator.K_OPEN);
        float highesPrice = (float) entry.get(Indicator.K_HIGH);
        float lowestPrice = (float) entry.get(Indicator.K_LOW);
        float turnVolume = (float) entry.get(Indicator.K_VOL);
        float turnover = (float) entry.get(Indicator.K_VOL_MONEY);
        long time = (long) entry.get(Indicator.K_TIME);
        int rateType = new BigDecimal(upDownRate).compareTo(new BigDecimal(0));

        KData.Unit unit = mKChart.getUnit();

        mBinding.tvLastPrice.setText(MarketUtil.formatValue(String.valueOf(laststPirce), 2));
        mBinding.tvRange.setText(MarketUtil.getMarketRangeValue(rateType, MarketUtil.formatValue(String.valueOf(updown), 2)));
        mBinding.tvRate.setText(MarketUtil.getMarketRateValue(rateType, MarketUtil.formatValue(String.valueOf(upDownRate), 2)));
        mBinding.tvOpen.setText(MarketUtil.formatValue(String.valueOf(openPrice), 2));
        mBinding.tvPreclose.setText(MarketUtil.formatValue(String.valueOf(preClose), 2));
        mBinding.tvTurnVolume.setText(MarketUtil.getVolumeValue(String.valueOf(new BigDecimal(turnover).divide(new BigDecimal(100))), false));
        mBinding.tvVolume.setText(MarketUtil.getVolumeValue(String.valueOf(turnVolume), false));
        mBinding.tvStateTime.setText(unit == KData.Unit.DAY || unit == KData.Unit.WEEK || unit == KData.Unit.MONTH
                ? DateUtil.dataToStringWithData(time) : DateUtil.dataToStringWithDataTime2(time));
        mBinding.tvHigh.setText(MarketUtil.formatValue(String.valueOf(highesPrice), 2));
        mBinding.tvHigh.setTextColor(ContextCompat.getColor(this,
                MarketUtil.getMarketStateColor(new BigDecimal(highesPrice).compareTo(new BigDecimal(preClose)))));
        mBinding.tvLow.setText(MarketUtil.formatValue(String.valueOf(lowestPrice), 2));
        mBinding.tvLow.setTextColor(ContextCompat.getColor(this, MarketUtil.getMarketStateColor(new BigDecimal(lowestPrice).compareTo(new BigDecimal(preClose)))));
    }

    private void getTenSpeedQuotes(boolean enable) {
        if (TextUtils.isEmpty(mContractId))
            return;

        HashMap<String, String> params = new HashMap<>();
        params.put("list", mContractId);

        sendRequest(MarketService.getInstance().getTenSpeedQuotes, params, enable, false, false);
    }

    private void getDetail() {
        if (TextUtils.isEmpty(mContractId))
            return;

        HashMap<String, String> params = new HashMap<>();
        params.put("contractId", mContractId);
        params.put("detailId", "");
        params.put("qryFlag", DIRECTION_BEFORE);
        params.put("count", AppConfig.PageSize_20);

        sendRequest(MarketService.getInstance().getDetail, params, false, false, false);
    }

    private void getContractSection() {
        if (TextUtils.isEmpty(mContractId))
            return;

        HashMap<String, String> params = new HashMap<>();
        params.put("contractId", mContractId);

        sendRequest(MarketService.getInstance().getContractSection, params, false, false, false);
    }

    private void getTChartQuotes() {
        if (TextUtils.isEmpty(mContractId))
            return;

        HashMap<String, String> params = new HashMap<>();
        params.put("contractId", mContractId);
        params.put("qryFlag", DIRECTION_AFTER);
        params.put("count", mTChartCount == 0 ? "1440" : String.valueOf(mTChartCount));

        sendRequest(MarketService.getInstance().getTChartQuotes, params, false, false, false);
    }

    private void getKChartQuotes(String type, String time, String flag) {
        if (TextUtils.isEmpty(mContractId))
            return;

        HashMap<String, String> params = new HashMap<>();
        params.put("type", type);
        params.put("contractId", mContractId);
        params.put("quoteTime", time);
        params.put("qryFlag", flag);
        params.put("count", COUNT_KCHART);

        sendRequest(MarketService.getInstance().getKChartQuotes, params, false, false, false);
    }

    private void getUserPasswordSettingInfo() {
        sendRequest(ManagementService.getInstance().getUserPasswordSettingInfo, new HashMap<>(), true, false, false);
    }

    private void queryLoginResult() {
        DTRequest request = new DTRequest(UserService.getInstance().queryLoginResult, new HashMap<>(), true, true);

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
                        LoginResponse dtResponse = (LoginResponse) response.body();

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

    private void getAccount() {
        if (null == mUser || !mUser.isLogin())
            return;

        String accountID = mUser.getAccountID();

        if (TextUtils.isEmpty(accountID))
            return;

        HashMap<String, String> params = new HashMap<>();
        params.put("accountId", accountID);

        sendRequest(TradeService.getInstance().account, params, true);
    }

    private void position() {
        if (null == mUser || !mUser.isLogin())
            return;

        String accountID = mUser.getAccountID();

        if (TextUtils.isEmpty(accountID))
            return;

        HashMap<String, String> params = new HashMap<>();
        params.put("accountId", accountID);
        params.put("pagingKey", mPagingKey);

        sendRequest(TradeService.getInstance().position, params, false);
    }

    private void getTimeLineList() {
        HashMap<String, String> params = new HashMap<>();
        params.put("uuid", AppConfig.UUID);
        params.put("token", mUser.getToken());

        sendRequest(ManagementService.getInstance().timeLineList, params, false, false, false);
    }

    private void limitOrder(String contractId, String price, String amount, String bsFlag, String ocFlag) {
        HashMap<String, String> params = new HashMap<>();
        params.put("contractId", contractId);
        params.put("accountId", mUser.getAccountID());
        params.put("entrustPrice", String.valueOf(new BigDecimal(price).multiply(new BigDecimal(100)).longValue()));
        params.put("entrustNumber", amount);
        params.put("bsFlag", bsFlag);
        params.put("ocFlag", ocFlag);
        params.put("tradingType", "0");

        sendRequest(TradeService.getInstance().limitOrder, params, true);
    }

    @Override
    protected void DataReturn(DTRequest request, Head head, Object response) {
        super.DataReturn(request, head, response);

        switch (request.getApi().getName()) {
            case "GetTenSpeedQuotes":
                if (head.isSuccess()) {
                    List<TenSpeedVo> list;

                    try {
                        list = (List<TenSpeedVo>) response;
                    } catch (Exception e) {
                        list = null;

                        e.printStackTrace();
                    }

                    if (null == list || 0 == list.size())
                        return;

                    TenSpeedVo tenSpeedVo = list.get(0);

                    if (null == tenSpeedVo)
                        return;

                    updateMarketData(tenSpeedVo);
                }

                if (bFlag) {
                    bFlag = false;

                    mHandler.sendEmptyMessageDelayed(Constants.Msg.MSG_UPDATE_DATA, getTimeInterval());
                }

                break;
            case "GetDetail":
                if (head.isSuccess()) {
                    List<DetailVo> list;

                    try {
                        list = (List<DetailVo>) response;
                    } catch (Exception e) {
                        list = null;

                        e.printStackTrace();
                    }

                    if (null == list || 0 == list.size())
                        return;

                    setDetailData(list);
                }

                break;
            case "GetContractSection":
                if (head.isSuccess()) {
                    List<SectionVo> list;

                    try {
                        list = (List<SectionVo>) response;
                    } catch (Exception e) {
                        list = null;

                        e.printStackTrace();
                    }

                    if (null == list || 0 == list.size())
                        return;

                    getTradeTime(list);
                }

                break;
            case "GetTChartQuotes":
                if (head.isSuccess()) {
                    List<TChartVo> list;

                    try {
                        list = (List<TChartVo>) response;
                    } catch (Exception e) {
                        list = null;

                        e.printStackTrace();
                    }

                    if (null == list || 0 == list.size())
                        return;

                    mTChart.loadTChartData(list);
                }

                bRequestTDataFlag = false;

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

                    KData.Unit unit = mKChart.getUnit();

                    if (null == unit)
                        return;

                    if (unit.getCode().equals(request.getParams().get("type")))
                        updateKChartData(list, unit);
                }

                iRequestKDataFlag = NONE;

                break;
            case "QueryLoginResult":
                if (head.isSuccess()) {
                    UserInfoVo userInfoVo;

                    try {
                        userInfoVo = (UserInfoVo) response;
                    } catch (Exception e) {
                        userInfoVo = null;

                        e.printStackTrace();
                    }

//                    String isSign = userInfoVo.getIsSign();
//
//                    if (TextUtils.isEmpty(isSign) || isSign.equals("N")) {
//                        mUser.getCurrentUser().setIsSign("N");
//
//                        if (null != mSignedPopUpWindow && !mSignedPopUpWindow.isShowing())
//                            mSignedPopUpWindow.showAtLocation(mBinding.tvHigh, Gravity.CENTER, 0, 0);
//                    } else {
                        getAccount();
//                    }
                } else {
//                    if (head.getCode().equals("-2012"))
//                        mUser.getCurrentUser().setIsSign("N");
                }

                break;
            case "Account":
                if (head.isSuccess()) {
                    try {
                        mAccountVo = (AccountVo) response;
                    } catch (Exception e) {
                        mAccountVo = null;

                        e.printStackTrace();
                    }

                    position();
                }

                break;
            case "Position":
                if (head.isSuccess()) {
                    PositionPageVo positionPageVo;

                    try {
                        positionPageVo = (PositionPageVo) response;
                    } catch (Exception e) {
                        positionPageVo = null;

                        e.printStackTrace();
                    }

                    if (null != positionPageVo) {
                        List<PositionVo> positionVoList = positionPageVo.getPositionList();

                        if (null != positionVoList && 0 != positionVoList.size()) {
                            for (PositionVo positionVo : positionVoList) {
                                if (null != positionVo && positionVo.getContractId().equals(mContractId)) {
                                    if (positionVo.getType().equals("多")) {
                                        if (mBsFlag == 1)
                                            mPositionVo = positionVo;

                                        mLongPositionMargin = positionVo.getPositionMargin();
                                    } else if (positionVo.getType().equals("空")) {
                                        if (mBsFlag == 2)
                                            mPositionVo = positionVo;

                                        mShortPositionMargin = positionVo.getPositionMargin();
                                    }
                                }
                            }
                        }

                        boolean hasNext = positionPageVo.isHasNext();
                        mPagingKey = positionPageVo.getPagingKey();

                        if (hasNext) {
                            position();
                        } else {
                            if (null != mMarketTradePopupWindow && !mMarketTradePopupWindow.isShowing()) {
                                mMarketTradePopupWindow.setData(mTenSpeedVo, mAccountVo, mPositionVo, mContractInfoVo,
                                        mUser.getAccount(), Math.abs(mLongPositionMargin - mShortPositionMargin), mBsFlag, mOcFlag);
                                mMarketTradePopupWindow.showAtLocation(mBinding.tvHigh, Gravity.BOTTOM, 0, 0);
                            }
                        }
                    }
                }

                break;
            case "TimeLineList":
                if (head.isSuccess()) {
                    String value = "";

                    if (null != response)
                        value = response.toString();

                    SharedPreUtils.setString(this, mUser.isLogin() ? SharedPreUtils.MARKET_SORT_LOGIN : SharedPreUtils.MARKET_SORT_UNLOGIN, value);

                    mChart.initChartSort(mUser.isLogin() ? SharedPreUtils.getString(this, SharedPreUtils.MARKET_SORT_LOGIN)
                            : SharedPreUtils.getString(this, SharedPreUtils.MARKET_SORT_UNLOGIN));

                    removeMessage();
                    initRawData();
                }

                break;
            case "LimitOrder":
                if (head.isSuccess()) {
                    showShortToast(R.string.transaction_success);
                } else {
                    if (head.getMsg().contains("可用资金不足")) {
                        if (null != mConfirmPopupwindow && !mConfirmPopupwindow.isShowing()) {
                            mConfirmPopupwindow.setData(mContext.getResources().getString(R.string.transaction_money_error),
                                    mContext.getResources().getString(R.string.transaction_money_in),
                                    (view) -> ARouter.getInstance().build(Constants.ARouterUriConst.CAPITALTRANSFER).navigation());
                            mConfirmPopupwindow.showAtLocation(mBinding.tvHigh, Gravity.CENTER, 0, 0);
                        }
                    }
                }

                break;
            case "GetUserPasswordSettingInfo":
                if (head.isSuccess()) {
                    PasswordInfoVo passwordInfoVo;

                    try {
                        passwordInfoVo = (PasswordInfoVo) response;
                    } catch (Exception e) {
                        passwordInfoVo = null;

                        e.printStackTrace();
                    }

                    if (null == passwordInfoVo)
                        return;

                    String hasTimeout = passwordInfoVo.getHasTimeout();
                    String hasSettingDigital = passwordInfoVo.getHasSettingDigital();
                    String hasOpenFingerPrint = passwordInfoVo.getHasOpenFingerPrint();
                    String hasOpenGestures = passwordInfoVo.getHasOpenGestures();

                    if (TextUtils.isEmpty(hasSettingDigital) || hasSettingDigital.equals("N")) {
                        if (null != mTradingPasswordConfirmSimplePopupwindow && !mTradingPasswordConfirmSimplePopupwindow.isShowing()) {
                            mTradingPasswordConfirmSimplePopupwindow.setData(mContext.getResources().getString(R.string.security_setting_tips),
                                    mContext.getResources().getString(R.string.personal_setting),
                                    (view) -> {
                                        ARouter.getInstance().build(Constants.ARouterUriConst.TRADINGPASSWORDSETTING).navigation();

                                        mTradingPasswordConfirmSimplePopupwindow.dismiss();
                                    });
                            mTradingPasswordConfirmSimplePopupwindow.showAtLocation(mBinding.getRoot(), Gravity.CENTER, 0, 0);
                        }
                    } else {
                        if (TextUtils.isEmpty(hasTimeout) || hasTimeout.equals("N")) {
                            if(mCallEntry == 3){
                                //买多
                                RxBus.getInstance().post(Constants.RxBusConst.RXBUS_BUY_MORE_SETPASSWORD_SUCCESS, null);
                            }else if(mCallEntry == 4){
                                //卖空
                                RxBus.getInstance().post(Constants.RxBusConst.RXBUS_SALE_EMPTY_SETPASSWORD_SUCCESS, null);
                            }else if(mCallEntry == 9){
                                //报单
                                RxBus.getInstance().post(Constants.RxBusConst.RXBUS_DECLARATION_FORM_SETPASSWORD_SUCCESS, null);
                            }

                            return;
                        }

                        int type = 1;

                        if (!TextUtils.isEmpty(hasOpenFingerPrint) && hasOpenFingerPrint.equals("Y")) {
                            boolean isCanUseFingerPrint = false;

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                if (FingerprintManagerCompat.from(mContext).isHardwareDetected()
                                        && FingerprintManagerCompat.from(mContext).hasEnrolledFingerprints())
                                    isCanUseFingerPrint = true;
                            }

                            if (isCanUseFingerPrint) {
                                type = 2;
                            } else {
                                if (!TextUtils.isEmpty(hasOpenGestures) && hasOpenGestures.equals("Y"))
                                    type = 3;
                                else
                                    type = 1;
                            }
                        } else if (!TextUtils.isEmpty(hasOpenGestures) && hasOpenGestures.equals("Y")) {
                            type = 3;
                        } else if (passwordInfoVo.getHasTimeout().equals("Y")) {
                            type = 1;
                        }

                        ARouter.getInstance()
                                .build(Constants.ARouterUriConst.UNLOCKTRADINGPASSWORD)
                                .withInt("Type", type)
                                .withInt("callEntry",mCallEntry)
                                .navigation();
                    }
                }

                break;

        }
    }

    @Override
    public void OnPriceClick(String price, String title) {

    }

    @Override
    public void onValueSelected(HashMap<String, Object> entry, int index, float preClose) {
        bHighlight = true;

        setDataFromKChart(entry, preClose);
    }

    @Override
    public void onNothingSelected() {
        bHighlight = false;

        updateMarketData(mTenSpeedVo);
    }

    public class ClickHandlers {

        public void onClickBack() {
            finish();
        }

        public void onClickPrevious() {
            int position = getPosition(mContractId);

            if (position == -1 || position == 0 || null == mContractIdList)
                return;

            position = position - 1;

            if (position > mContractIdList.length)
                return;

            mContractId = mContractIdList[position];

            setChangeLayout(position);
            removeMessage();
            initRawData();
        }

        public void onClickNext() {
            int position = getPosition(mContractId);

            if (null == mContractIdList)
                return;

            if (position == -1 || position == mContractIdList.length - 1)
                return;

            position = position + 1;

            if (position > mContractIdList.length)
                return;

            mContractId = mContractIdList[position];

            setChangeLayout(position);
            removeMessage();
            initRawData();
        }

        public void onClickWarning() {
            if (null == mUser || !mUser.isLogin()) {
                gotoLogin();
            } else {
                ARouter.getInstance()
                        .build(Constants.ARouterUriConst.WARNING)
                        .withString("ContractID", MarketUtil.getContractCode(mContractId))
                        .navigation();
            }
        }

        public void onClickBuyMore() {
            if (null == mUser || !mUser.isLogin()) {
                gotoLogin();
            } else {
                //买多
                mCallEntry = 3;
                getUserPasswordSettingInfo();

            }
        }

        public void onClickSaleEmpty() {
            if (null == mUser || !mUser.isLogin()) {
                gotoLogin();
            } else {
                //卖空
                mCallEntry = 4;
                getUserPasswordSettingInfo();

            }
        }

        public void onClickDeclarationForm() {
            if (null == mUser || !mUser.isLogin()) {
                gotoLogin();
            } else {
                //报单
                mCallEntry = 9;
                getUserPasswordSettingInfo();

            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (null == intent)
            return;

        switch (requestCode) {
            case Constants.IntentConst.CODE_REQUEST_LANDSCAPE:
                if (resultCode == Activity.RESULT_OK) {
                    String contractId = intent.getStringExtra("ContractId");
                    String chartUnit = intent.getStringExtra("ChartUnit");

                    if (contractId.equals(mContractId))
                        mChart.setUnit(chartUnit);
                }

                break;
        }
    }

    private void showStockUserDialog() {
        if (isFinishing)
            return;

        if (!isForeground())
            return;

        if (null == mStockUserDialog)
            mStockUserDialog = new StockUserDialog(mContext);

        if (!mStockUserDialog.isShowing()) {
            mStockUserDialog.show();
            DisplayMetrics dm = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(dm);
            WindowManager.LayoutParams lp = mStockUserDialog.getWindow().getAttributes();
            lp.width = (int) (dm.widthPixels*0.75); //设置宽度
            mStockUserDialog.getWindow().setAttributes(lp);

        }
    }
    private boolean isOpenStockUser(){
        if(!TextUtils.isEmpty(mUser.getAccountID())&&mUser.getCurrentUser()!=null&&mUser.getCurrentUser().getIsOpen()!=null&&"-2017".equals(mUser.getCurrentUser().getIsOpen())){
            return true;
        }else{
            return false;
        }
    }

}
