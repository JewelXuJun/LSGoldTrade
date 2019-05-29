package com.jme.lsgoldtrade.ui.market;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.datai.common.charts.chart.Chart;
import com.datai.common.charts.fchart.FChart;
import com.datai.common.charts.indicator.Indicator;
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
import com.jme.common.util.StatusBarUtil;
import com.jme.common.util.TChartVo;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseActivity;
import com.jme.lsgoldtrade.config.AppConfig;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.ActivityMarketDetailBinding;
import com.jme.lsgoldtrade.domain.DetailVo;
import com.jme.lsgoldtrade.domain.SectionVo;
import com.jme.lsgoldtrade.domain.TenSpeedVo;
import com.jme.lsgoldtrade.service.MarketService;
import com.jme.lsgoldtrade.service.TradeService;
import com.jme.lsgoldtrade.ui.trade.OrderPopUpWindow;
import com.jme.lsgoldtrade.util.MarketUtil;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import rx.Subscription;

@Route(path = Constants.ARouterUriConst.MARKETDETAIL)
public class MarketDetailActivity extends JMEBaseActivity implements FChart.OnPriceClickListener, OnKChartSelectedListener {

    private ActivityMarketDetailBinding mBinding;

    private TenSpeedVo mTenSpeedVo;
    private OrderPopUpWindow mWindow;
    private MarketOrderPopUpWindow mPopupWindow;
    private Chart mChart;
    private TChart mTChart;
    private KChart mKChart;
    private Subscription mRxbus;

    private static final int NONE = 0;
    private static final int INIT = 1;
    private static final int NEWEST = 2;
    private static final int MORE = 3;
    private static final String DIRECTION_AFTER = "1";
    private static final String DIRECTION_BEFORE = "2";
    private static final String COUNT_TCHART = "660";
    private static final String COUNT_KCHART = "200";

    private String mContractId;
    private boolean bFlag = true;
    private boolean bHighlight = false;
    private boolean bHasMoreKDataFlag = true;
    private boolean bRequestTDataFlag = false;
    private boolean bGetTradeDateFlag = false;
    private int iRequestKDataFlag = NONE;

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

        mBinding = (ActivityMarketDetailBinding) mBindingUtil;

        mContractId = getIntent().getStringExtra("ContractId");

        initToolbar(MarketUtil.getContractCode(mContractId), true, ContextCompat.getColor(this, R.color.white));
        setBackGroundColor(R.color.common_font_stable);
        setBackNavigation(true, R.mipmap.ic_back_white);

        mChart = mBinding.chart;
        mTChart = mChart.getTChart();
        mKChart = mChart.getKChart();

        mChart.setPriceFormatDigit(2);

        initTChart();
        initKChart();
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);

        mWindow = new OrderPopUpWindow(this);
        mWindow.setOutsideTouchable(true);
        mWindow.setFocusable(true);
    }

    @Override
    protected void initListener() {
        super.initListener();

        initRxBus();

        mChart.setOnLandscapeListener((view) -> gotoMarketDetailLandscapeActivity());

        mChart.setOnChartListener((showTChart, unit) -> {
            iRequestKDataFlag = NONE;

            sendChartRefreshMessage(showTChart);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (!mRxbus.isUnsubscribed())
            mRxbus.unsubscribe();
    }

    private void setBackGroundColor(int color) {
        StatusBarUtil.setStatusBarMode(this, true, color);
        mToolbarHelper.setBackgroundColor(ContextCompat.getColor(this, color));
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

                    if (!mContractId.equals(list.get(0)))
                        return;

                    showPopupWindow(list.get(0), list.get(1), list.get(2), list.get(3), list.get(4));

                    break;
            }
        });
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

            getTChartData();
        }
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
            mBinding.tvTurnVolume.setText(MarketUtil.getVolumeValue(String.valueOf(tenSpeedVo.getTurnover()), false));
            mBinding.tvVolume.setText(MarketUtil.getVolumeValue(String.valueOf(tenSpeedVo.getTurnVolume()), false));
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

        mTChart.loadTradeInfoChartData(tenSpeedVo.getAskLists(), tenSpeedVo.getBidLists());

        if (null != mPopupWindow)
            mPopupWindow.setData(tenSpeedVo);
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

        setBackGroundColor(MarketUtil.getMarketStateColor(rateType));

        KData.Unit unit = mKChart.getUnit();

        mBinding.tvLastPrice.setText(MarketUtil.formatValue(String.valueOf(laststPirce), 2));
        mBinding.tvRange.setText(MarketUtil.getMarketRangeValue(rateType, MarketUtil.formatValue(String.valueOf(updown), 2)));
        mBinding.tvRate.setText(MarketUtil.getMarketRateValue(rateType, MarketUtil.formatValue(String.valueOf(upDownRate), 2)));
        mBinding.tvOpen.setText(MarketUtil.formatValue(String.valueOf(openPrice), 2));
        mBinding.tvPreclose.setText(MarketUtil.formatValue(String.valueOf(preClose), 2));
        mBinding.tvTurnVolume.setText(MarketUtil.getVolumeValue(String.valueOf(turnover), false));
        mBinding.tvVolume.setText(MarketUtil.getVolumeValue(String.valueOf(turnVolume), false));
        mBinding.tvStateTime.setText(unit == KData.Unit.DAY || unit == KData.Unit.WEEK || unit == KData.Unit.MONTH
                ? DateUtil.dataToStringWithData(time) : DateUtil.dataToStringWithDataTime2(time));
        mBinding.tvHigh.setText(MarketUtil.formatValue(String.valueOf(highesPrice), 2));
        mBinding.tvHigh.setTextColor(ContextCompat.getColor(this,
                MarketUtil.getMarketStateColor(new BigDecimal(highesPrice).compareTo(new BigDecimal(preClose)))));
        mBinding.tvLow.setText(MarketUtil.formatValue(String.valueOf(lowestPrice), 2));
        mBinding.tvLow.setTextColor(ContextCompat.getColor(this, MarketUtil.getMarketStateColor(new BigDecimal(lowestPrice).compareTo(new BigDecimal(preClose)))));
    }

    private void showPopupWindow(String contractID, String price, String amount, String bsFlag, String ocFlag) {
        if (null == mWindow)
            return;

        mWindow.setData(mUser.getAccount(), contractID, price, amount,
                Integer.parseInt(bsFlag) == 1 ? getString(R.string.trade_buy) : getString(R.string.trade_sale), (view) -> {
                    limitOrder(contractID, price, amount, bsFlag, ocFlag);

                    mWindow.dismiss();
                });
        mWindow.showAtLocation(mBinding.tvHigh, Gravity.CENTER, 0, 0);
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
        params.put("count", AppConfig.PageSize_10);

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
        params.put("count", COUNT_TCHART);

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
            case "LimitOrder":
                if (head.isSuccess())
                    showShortToast(R.string.trade_success);

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

        public void onClickDeclarationForm() {
            if (null == mUser || !mUser.isLogin()) {
                ARouter.getInstance()
                        .build(Constants.ARouterUriConst.ACCOUNTLOGIN)
                        .navigation();
            } else {
                AppConfig.Select_ContractId = mContractId;

                RxBus.getInstance().post(Constants.RxBusConst.RXBUS_TRADEFRAGMENT, mContractId);

                ARouter.getInstance()
                        .build(Constants.ARouterUriConst.MAIN)
                        .navigation();

                finish();
            }
        }

        public void onClickOneKeyOrder() {
            if (null == mUser || !mUser.isLogin()) {
                ARouter.getInstance()
                        .build(Constants.ARouterUriConst.ACCOUNTLOGIN)
                        .navigation();
            } else {
                mPopupWindow = new MarketOrderPopUpWindow(MarketDetailActivity.this, mContractId);
                mPopupWindow.setOutsideTouchable(true);
                mPopupWindow.setFocusable(true);
                mPopupWindow.setData(mTenSpeedVo);
                mPopupWindow.showAtLocation(mBinding.layoutFooterview, Gravity.BOTTOM, 0, 0);
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

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (null != mPopupWindow && mPopupWindow.isShowing())
            return false;

        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_BACK == keyCode) {
            if (null != mPopupWindow && mPopupWindow.isShowing())
                mPopupWindow.dismiss();
            else
                finish();

            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}
