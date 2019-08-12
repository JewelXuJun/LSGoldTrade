package com.jme.lsgoldtrade.ui.market;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.MotionEvent;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.datai.common.charts.chart.Chart;
import com.datai.common.charts.chart.OnChartListener;
import com.datai.common.charts.fchart.FChart;
import com.datai.common.charts.kchart.KChart;
import com.datai.common.charts.kchart.KData;
import com.datai.common.charts.kchart.OnKChartListener;
import com.datai.common.charts.kchart.OnKChartSelectedListener;
import com.datai.common.charts.tchart.TChart;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.common.util.DateUtil;
import com.jme.common.util.KChartVo;
import com.jme.common.util.NetWorkUtils;
import com.jme.common.util.RxBus;
import com.jme.common.util.SharedPreUtils;
import com.jme.common.util.TChartVo;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseActivity;
import com.jme.lsgoldtrade.config.AppConfig;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.ActivityMarketDetailLandscapeBinding;
import com.jme.lsgoldtrade.domain.DetailVo;
import com.jme.lsgoldtrade.domain.SectionVo;
import com.jme.lsgoldtrade.domain.TenSpeedVo;
import com.jme.lsgoldtrade.service.ManagementService;
import com.jme.lsgoldtrade.service.MarketService;
import com.jme.lsgoldtrade.util.MarketUtil;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import rx.Subscription;

@Route(path = Constants.ARouterUriConst.MARKETDETAILLANDSCAPE)
public class MarketDetailLandscapeActivity extends JMEBaseActivity implements FChart.OnPriceClickListener, OnKChartSelectedListener {

    private ActivityMarketDetailLandscapeBinding mBinding;

    private TenSpeedVo mTenSpeedVo;
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
    private String mUnitCode;
    private boolean bFlag = true;
    private boolean bHasMoreKDataFlag = true;
    private boolean bRequestTDataFlag = false;
    private boolean bGetTradeDateFlag = false;
    private int iRequestKDataFlag = NONE;
    private int mTChartCount;

    private Subscription mRxbus;

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constants.Msg.MSG_UPDATE_DATA_LANDSCAPE:
                    mHandler.removeMessages(Constants.Msg.MSG_UPDATE_DATA_LANDSCAPE);

                    updateData(false);

                    mHandler.sendEmptyMessageDelayed(Constants.Msg.MSG_UPDATE_DATA_LANDSCAPE, getTimeInterval());

                    break;
                case Constants.Msg.MSG_RELOAD_DATA_LANDSCAPE:
                    mHandler.removeMessages(Constants.Msg.MSG_RELOAD_DATA_LANDSCAPE);

                    getTenSpeedQuotes(false);

                    getInitKChartData();

                    break;
            }

            super.handleMessage(msg);
        }
    };

    @Override
    protected int getContentViewId() {
        return R.layout.activity_market_detail_landscape;
    }

    @Override
    protected void initView() {
        super.initView();

        mBinding = (ActivityMarketDetailLandscapeBinding) mBindingUtil;

        mChart = mBinding.chart;
        mTChart = mChart.getTChart();
        mKChart = mChart.getKChart();
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);

        mContractId = getIntent().getStringExtra("ContractId");
        mUnitCode = getIntent().getStringExtra("ChartUnit");

        mChart.initChartSort(mUser.isLogin() ? SharedPreUtils.getString(this, SharedPreUtils.MARKET_SORT_LOGIN)
                : SharedPreUtils.getString(this, SharedPreUtils.MARKET_SORT_UNLOGIN));
        mChart.setPriceFormatDigit(2);

        initTChart();
        initKChart();

        mBinding.tvName.setText(mContractId);

        mChart.setUnit(mUnitCode);
    }

    @Override
    protected void initListener() {
        super.initListener();

        initRxBus();

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
                //切换VOL等选项
            }
        });

        mTChart.getTradeInfoChart().setDoRadioButtonFunction(() -> {
        });
    }

    @Override
    protected void initBinding() {
        super.initBinding();

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

    private void initTChart() {
        mTChart.config();
        mTChart.setScaleXEnabled(true);
        mTChart.setIsNeedSupplement(false);
        mTChart.setIsStartFromBeginning(true);
        mTChart.setLandscapeButtonVisible(false);
    }

    private void initKChart() {
        mKChart.config();
        mKChart.setIsFromServerMAs(false);
        mKChart.setHasTradeVolume(true);
        mKChart.setLandscapeButtonVisible(false);
        mKChart.setOnKChartSelectedListener(this);
    }

    private void initRxBus() {
        mRxbus = RxBus.getInstance().toObserverable(RxBus.Message.class).subscribe(message -> {
            String callType = message.getObject().toString();

            if (TextUtils.isEmpty(callType))
                return;

            switch (callType) {
                case Constants.RxBusConst.RXBUS_MARKET_UNIT_SORT_SUCCESS:
                    getTimeLineList();

                    break;
            }
        });
    }

    public void initRawData() {
        if (TextUtils.isEmpty(mContractId))
            return;

        bFlag = true;
        bGetTradeDateFlag = false;

        updateData(true);
    }

    private void updateData(boolean enable) {
        getTenSpeedQuotes(enable);
        getDetail();

        if (mChart.isShowTChart())
            getTChartData();
        else
            getNewestKChartData();
    }

    private void doBack() {
        Intent intent = new Intent();
        intent.putExtra("ContractId", mContractId);
        intent.putExtra("ChartUnit", mChart.getChartUnit().getCode());

        setResult(Activity.RESULT_OK, intent);

        finish();
    }

    private long getTimeInterval() {
        return NetWorkUtils.isWifiConnected(mContext) ? AppConfig.TimeInterval_WiFi : AppConfig.TimeInterval_NetWork;
    }

    public void sendChartRefreshMessage(boolean showTChart) {
        if (showTChart) {
            bGetTradeDateFlag = false;

            mHandler.sendEmptyMessage(Constants.Msg.MSG_UPDATE_DATA_LANDSCAPE);
        } else {
            mHandler.sendEmptyMessage(Constants.Msg.MSG_RELOAD_DATA_LANDSCAPE);
        }
    }

    private void removeMessage() {
        mHandler.removeMessages(Constants.Msg.MSG_UPDATE_DATA_LANDSCAPE);
        mHandler.removeMessages(Constants.Msg.MSG_RELOAD_DATA_LANDSCAPE);
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
            mHandler.sendEmptyMessageDelayed(Constants.Msg.MSG_RELOAD_DATA_LANDSCAPE, 100);

            return;
        }

        KData.Unit unit = mKChart.getUnit();

        if (null == unit)
            return;

        mKChart.switchKDataToCurrentUnit();

        if (mKChart.hasKDataInCurrentUnit()) {
            mHandler.sendEmptyMessage(Constants.Msg.MSG_UPDATE_DATA_LANDSCAPE);
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

        if (mHandler.hasMessages(Constants.Msg.MSG_RELOAD_DATA_LANDSCAPE))
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
        if (bHasMoreKDataFlag == false)
            return;

        Date date = new Date(oldestTime - 1);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

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

        String upDown = tenSpeedVo.getUpDown();
        String highestPrice = tenSpeedVo.getHighestPrice();
        String lowestPrice = tenSpeedVo.getLowestPrice();
        String openPrice = tenSpeedVo.getOpenPrice();

        int rateType;

        if (TextUtils.isEmpty(upDown))
            rateType = 0;
        else
            rateType = new BigDecimal(upDown).compareTo(new BigDecimal(0));

        mBinding.layoutLastPrice.setBackground(ContextCompat.getDrawable(this, MarketUtil.getMarketStateBackgroundColor(rateType)));
        mBinding.tvLastPrice.setText(MarketUtil.getValue(tenSpeedVo.getLatestPrice()));
        mBinding.tvRange.setText(MarketUtil.getMarketRangeValue(rateType, upDown));
        mBinding.tvRate.setText(MarketUtil.getMarketRateValue(rateType, tenSpeedVo.getUpDownRate()));
        mBinding.tvOpen.setText(MarketUtil.getValue(openPrice));
        mBinding.tvOpen.setTextColor(ContextCompat.getColor(this,
                MarketUtil.getMarketStateColor(TextUtils.isEmpty(openPrice) ? -2 : new BigDecimal(openPrice).compareTo(new BigDecimal(lastSettlePrice)))));
        mBinding.tvPreclose.setText(MarketUtil.getValue(tenSpeedVo.getLastClosePrice()));
        mBinding.tvTurnVolume.setText(MarketUtil.getVolumeValue(String.valueOf(new BigDecimal(tenSpeedVo.getTurnover()).divide(new BigDecimal(100))), false));
        mBinding.tvVolume.setText(MarketUtil.getVolumeValue(String.valueOf(tenSpeedVo.getTurnVolume()), false));
        mBinding.tvStateTime.setText(MarketUtil.getValue(DateUtil.stringToAllTime(tenSpeedVo.getQuoteTime())));
        mBinding.tvHigh.setText(MarketUtil.getValue(highestPrice));
        mBinding.tvHigh.setTextColor(ContextCompat.getColor(this,
                MarketUtil.getMarketStateColor(TextUtils.isEmpty(highestPrice) ? -2 : new BigDecimal(highestPrice).compareTo(new BigDecimal(lastSettlePrice)))));
        mBinding.tvLow.setText(MarketUtil.getValue(lowestPrice));
        mBinding.tvLow.setTextColor(ContextCompat.getColor(this,
                MarketUtil.getMarketStateColor(TextUtils.isEmpty(lowestPrice) ? -2 : new BigDecimal(lowestPrice).compareTo(new BigDecimal(lastSettlePrice)))));

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

    private void getTimeLineList() {
        HashMap<String, String> params = new HashMap<>();
        params.put("uuid", AppConfig.UUID);
        params.put("token", mUser.getToken());

        sendRequest(ManagementService.getInstance().timeLineList, params, false, false, false);
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

                    mHandler.sendEmptyMessageDelayed(Constants.Msg.MSG_UPDATE_DATA_LANDSCAPE, getTimeInterval());
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
        }
    }

    @Override
    public void OnPriceClick(String price, String title) {

    }

    @Override
    public void onValueSelected(HashMap<String, Object> entry, int index, float preClose) {

    }

    @Override
    public void onNothingSelected() {
        updateMarketData(mTenSpeedVo);
    }

    public class ClickHandlers {

        public void onClickClose() {
            doBack();
        }

    }

    @Override
    public void onBackPressed() {
        doBack();

        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (!mRxbus.isUnsubscribed())
            mRxbus.unsubscribe();
    }

}
