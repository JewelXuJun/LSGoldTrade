package com.jme.lsgoldtrade.ui.mainpage;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MotionEvent;
import android.widget.ImageView;

import com.datai.common.charts.kchart.KData;
import com.datai.common.charts.kchart.OnKChartListener;
import com.datai.common.charts.kchart.OnKChartSelectedListener;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.common.util.DateUtil;
import com.jme.common.util.KChartVo;
import com.jme.common.util.NetWorkUtils;
import com.jme.common.util.RxBus;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseFragment;
import com.jme.lsgoldtrade.config.AppConfig;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.FragmentHangqingyepanBinding;
import com.jme.lsgoldtrade.domain.DetailVo;
import com.jme.lsgoldtrade.domain.FenXiShiListVo;
import com.jme.lsgoldtrade.domain.SectionVo;
import com.jme.lsgoldtrade.domain.TenSpeedVo;
import com.jme.lsgoldtrade.service.ManagementService;
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

public class HangQingYanPanFragment extends JMEBaseFragment implements OnKChartSelectedListener {

    private FragmentHangqingyepanBinding mBinding;

    private static final int NONE = 0;
    private static final int INIT = 1;
    private static final int NEWEST = 2;
    private static final int MORE = 3;
    private static final String DIRECTION_AFTER = "1";
    private static final String DIRECTION_BEFORE = "2";
    private static final String COUNT_TCHART = "660";
    private static final String COUNT_KCHART = "200";

    private TenSpeedVo mTenSpeedVo;
    private String mContractId = "Au(T+D)";
    private boolean bFlag = true;
    private boolean bHasMoreKDataFlag = true;
    private int iRequestKDataFlag = NONE;
    private Subscription mRxbus;

    public String id;
    private int lastIndex;
    private String hasNext;
    private String hasPre;
    private int offset = 0;
    private FenXiShiListVo fenXiShiVoList;
    private OrderPopUpWindow mWindow;

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

    public void getanalystId(String id) {
        this.id = id;
    }

    private void updateData(boolean enable) {
        getTenSpeedQuotes(enable);
        getDetail();
        getNewestKChartData();
    }

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_hangqingyepan;
    }

    @Override
    protected void initView() {
        super.initView();
        mBinding = (FragmentHangqingyepanBinding) mBindingUtil;
        initKChart();
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        mWindow = new OrderPopUpWindow(mContext);
        mWindow.setOutsideTouchable(true);
        mWindow.setFocusable(true);
        getDateFromNet();
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

    private void removeMessage() {
        mHandler.removeMessages(Constants.Msg.MSG_UPDATE_DATA);
        mHandler.removeMessages(Constants.Msg.MSG_RELOAD_DATA);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (!mRxbus.isUnsubscribed())
            mRxbus.unsubscribe();
    }

    public void initRawData() {
        if (TextUtils.isEmpty(mContractId))
            return;

        bFlag = true;
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

    private void showPopupWindow(String contractID, String price, String amount, String bsFlag, String ocFlag) {
        if (null == mWindow)
            return;

        mWindow.setData(mUser.getAccount(), contractID, price, amount,
                bsFlag, ocFlag, (view) -> {
                    limitOrder(contractID, price, amount, bsFlag, ocFlag);

                    mWindow.dismiss();
                });
        mWindow.showAtLocation(mBinding.fenxi, Gravity.BOTTOM, 0, 0);
    }

    private void initKChart() {
        mBinding.mKChart.setIsFromServerMAs(false);
        mBinding.mKChart.setHasTradeVolume(true);
        mBinding.mKChart.setLandscapeButtonVisible(false);
        mBinding.mKChart.setOnKChartSelectedListener(this);
        mBinding.mKChart.config();
    }

    private void getDateFromNet() {
        HashMap<String, String> map = new HashMap<>();
        map.put("analystId", id);
        map.put("offset", offset + "");
        sendRequest(ManagementService.getInstance().fenxishiList, map, false);
    }

    @Override
    protected void initListener() {
        super.initListener();
        initRxBus();
        mBinding.mKChart.setOnKChartListener(new OnKChartListener() {
            @Override
            public void onEnding(long oldestTime, KData.Unit unit) {
                if (mBinding.mKChart.getDataCount() >= 200)
                    getMoreKChartData(oldestTime, unit);
            }

            @Override
            public void onChartSingleTapped(MotionEvent me) {
                //切换VOL等选项

            }
        });
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

    private void getKChartQuotes(String type, String time, String flag) {
        if (TextUtils.isEmpty(mContractId))
            return;

        HashMap<String, String> params = new HashMap<>();
        params.put("type", "30minK");
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

                    KData.Unit unit = KData.Unit.MIN30;

                    mBinding.mKChart.setUnit(unit);

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
                else
                    showShortToast(head.getMsg());

                break;
            case "FenXiList":
                if (head.isSuccess()) {
                    try {
                        fenXiShiVoList = (FenXiShiListVo) response;
                    } catch (Exception e) {
                        fenXiShiVoList = null;

                        e.printStackTrace();
                    }
                    if (null == fenXiShiVoList)
                        return;

                    setDate();
                }
                break;
        }
    }

    @Override
    public void initBinding() {
        super.initBinding();
        mBinding.setHandlers(new ClickHandlers());
    }

    private void setDate() {
        lastIndex = fenXiShiVoList.getLastIndex();
        hasNext = fenXiShiVoList.getHasNext();
        hasPre = fenXiShiVoList.getHasPre();

        FenXiShiListVo.MONDAYBean monday = fenXiShiVoList.getMONDAY();
        FenXiShiListVo.TUESDAYBean tuesday = fenXiShiVoList.getTUESDAY();
        FenXiShiListVo.WEDNESDAYBean wednesday = fenXiShiVoList.getWEDNESDAY();
        FenXiShiListVo.THURSDAYBean thursday = fenXiShiVoList.getTHURSDAY();
        FenXiShiListVo.FRIDAYBean friday = fenXiShiVoList.getFRIDAY();

        mBinding.monday.setText(monday.getWeek() + "\n" + monday.getDateFormat());
        mBinding.tuesday.setText(tuesday.getWeek() + "\n" + tuesday.getDateFormat());
        mBinding.wednesday.setText(wednesday.getWeek() + "\n" + wednesday.getDateFormat());
        mBinding.thursday.setText(thursday.getWeek() + "\n" + thursday.getDateFormat());
        mBinding.friday.setText(friday.getWeek() + "\n" + friday.getDateFormat());

        String monJudge = monday.getJudge();
        String tuesJudge = tuesday.getJudge();
        String wedJudge = wednesday.getJudge();
        String thursJudge = thursday.getJudge();
        String friJudge = friday.getJudge();

        String mondayJudgeResult = monday.getJudgeResult();
        String tuesdayJudgeResult = tuesday.getJudgeResult();
        String wednesdayJudgeResult = wednesday.getJudgeResult();
        String thursdayJudgeResult = thursday.getJudgeResult();
        String fridayJudgeResult = friday.getJudgeResult();


        String mondayUnscramble = monday.getUnscramble();
        String tuesdayUnscramble = tuesday.getUnscramble();
        String wednesdayUnscramble = wednesday.getUnscramble();
        String thursdayUnscramble = thursday.getUnscramble();
        String fridayUnscramble = friday.getUnscramble();

        hangQingYanPan(monJudge, mBinding.mondayYanpan);
        hangQingYanPan(tuesJudge, mBinding.tuesdayYanpan);
        hangQingYanPan(wedJudge, mBinding.wednesdayPanyan);
        hangQingYanPan(thursJudge, mBinding.thursdayYanpan);
        hangQingYanPan(friJudge, mBinding.fridayYanpan);

        yanPanJieGuo(mondayJudgeResult, mBinding.mondayJieguo);
        yanPanJieGuo(tuesdayJudgeResult, mBinding.tuesdayJieguo);
        yanPanJieGuo(wednesdayJudgeResult, mBinding.wednesdayJieguo);
        yanPanJieGuo(thursdayJudgeResult, mBinding.thursdayJieguo);
        yanPanJieGuo(fridayJudgeResult, mBinding.fridayJieguo);

        yanPanJieDu(mondayUnscramble);
        yanPanJieDu(tuesdayUnscramble);
        yanPanJieDu(wednesdayUnscramble);
        yanPanJieDu(thursdayUnscramble);
        yanPanJieDu(fridayUnscramble);
    }

    private void yanPanJieDu(String mondayUnscramble) {
        if (lastIndex == 1) {
            mBinding.fenxi.setText(mondayUnscramble);
            mondayShow();
        } else if (lastIndex == 2) {
            mBinding.fenxi.setText(mondayUnscramble);
            TuesdayShow();
        } else if (lastIndex == 3){
            mBinding.fenxi.setText(mondayUnscramble);
            WednesdayShow();
        } else if (lastIndex == 4) {
            mBinding.fenxi.setText(mondayUnscramble);
            ThursdayShow();
        } else if (lastIndex == 5){
            mBinding.fenxi.setText(mondayUnscramble);
            FridayShow();
        }
    }

    private void yanPanJieGuo(String mondayJudgeResult, ImageView imageView) {
        if ("T".equals(mondayJudgeResult)) {
            imageView.setImageResource(R.mipmap.result_true);
        } else if ("F".equals(mondayJudgeResult)) {
            imageView.setImageResource(R.mipmap.result_false);
        } else {
            imageView.setImageResource(R.mipmap.result_ping);
        }
    }

    private void hangQingYanPan(String judge, ImageView imageView) {
        if ("S".equals(judge)) { //上涨
            imageView.setImageResource(R.mipmap.hangqing_up);
        } else if ("X".equals(judge)) { //下跌
            imageView.setImageResource(R.mipmap.hangqing_down);
        } else { //横盘
            imageView.setImageResource(R.mipmap.hangqing_heng);
        }
    }

    @Override
    public void onValueSelected(HashMap<String, Object> entry, int index, float preClose) {
    }

    @Override
    public void onNothingSelected() {
        updateMarketData(mTenSpeedVo);
    }

    public class ClickHandlers {

        public void onClickLeft() {
            if ("T".equals(hasPre)) {
                --offset;
                getDateFromNet();
            }
        }

        public void onClickRight() {
            if ("T".equals(hasNext)) {
                ++offset;
                getDateFromNet();
            }
        }

        public void onClickMonday() {
            mondayShow();
            lastIndex = 1;
            yanPanJieDu(fenXiShiVoList.getMONDAY().getUnscramble());
        }

        public void onClickTuesday() {
            TuesdayShow();
            lastIndex = 2;
            yanPanJieDu(fenXiShiVoList.getTUESDAY().getUnscramble());
        }

        public void onClickWednesday() {
            WednesdayShow();
            lastIndex = 3;
            yanPanJieDu(fenXiShiVoList.getWEDNESDAY().getUnscramble());
        }

        public void onClickThursday() {
            ThursdayShow();
            lastIndex = 4;
            yanPanJieDu(fenXiShiVoList.getTHURSDAY().getUnscramble());
        }

        public void onClickFriday() {
            FridayShow();
            lastIndex = 5;
            yanPanJieDu(fenXiShiVoList.getFRIDAY().getUnscramble());
        }
    }

    private void FridayShow() {
        mBinding.monday.setTextColor(getResources().getColor(R.color.color_333));
        mBinding.tuesday.setTextColor(getResources().getColor(R.color.color_333));
        mBinding.wednesday.setTextColor(getResources().getColor(R.color.color_333));
        mBinding.thursday.setTextColor(getResources().getColor(R.color.color_333));
        mBinding.friday.setTextColor(getResources().getColor(R.color.color_0080ff));
    }

    private void ThursdayShow() {
        mBinding.monday.setTextColor(getResources().getColor(R.color.color_333));
        mBinding.tuesday.setTextColor(getResources().getColor(R.color.color_333));
        mBinding.wednesday.setTextColor(getResources().getColor(R.color.color_333));
        mBinding.thursday.setTextColor(getResources().getColor(R.color.color_0080ff));
        mBinding.friday.setTextColor(getResources().getColor(R.color.color_333));
    }

    private void WednesdayShow() {
        mBinding.monday.setTextColor(getResources().getColor(R.color.color_333));
        mBinding.tuesday.setTextColor(getResources().getColor(R.color.color_333));
        mBinding.wednesday.setTextColor(getResources().getColor(R.color.color_0080ff));
        mBinding.thursday.setTextColor(getResources().getColor(R.color.color_333));
        mBinding.friday.setTextColor(getResources().getColor(R.color.color_333));
    }

    private void TuesdayShow() {
        mBinding.monday.setTextColor(getResources().getColor(R.color.color_333));
        mBinding.tuesday.setTextColor(getResources().getColor(R.color.color_0080ff));
        mBinding.wednesday.setTextColor(getResources().getColor(R.color.color_333));
        mBinding.thursday.setTextColor(getResources().getColor(R.color.color_333));
        mBinding.friday.setTextColor(getResources().getColor(R.color.color_333));
    }

    private void mondayShow() {
        mBinding.monday.setTextColor(getResources().getColor(R.color.color_0080ff));
        mBinding.tuesday.setTextColor(getResources().getColor(R.color.color_333));
        mBinding.wednesday.setTextColor(getResources().getColor(R.color.color_333));
        mBinding.thursday.setTextColor(getResources().getColor(R.color.color_333));
        mBinding.friday.setTextColor(getResources().getColor(R.color.color_333));
    }

    public void getInitKChartData() {
        if (iRequestKDataFlag != NONE) {
            mHandler.sendEmptyMessageDelayed(Constants.Msg.MSG_RELOAD_DATA, 100);

            return;
        }

        KData.Unit unit = KData.Unit.MIN30;

        if (null == unit)
            return;

        mBinding.mKChart.switchKDataToCurrentUnit();

        if (mBinding.mKChart.hasKDataInCurrentUnit()) {
            mHandler.sendEmptyMessage(Constants.Msg.MSG_UPDATE_DATA);
        } else {
            bHasMoreKDataFlag = true;
            iRequestKDataFlag = INIT;

            getKChartQuotes(unit.getCode(), "", DIRECTION_BEFORE);
        }
    }

    public void getNewestKChartData() {
        if (mBinding.mKChart.getDataCount() == 0) {
            getInitKChartData();

            return;
        }

        if (iRequestKDataFlag != NONE)
            return;

        if (mHandler.hasMessages(Constants.Msg.MSG_RELOAD_DATA))
            return;

        KData.Unit unit = KData.Unit.MIN30;

        if (null == unit)
            return;

        long tick = mBinding.mKChart.getNewestTimeTick(2);
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
            calculateTChartCount(timeLists);
        }
    }

    private void calculateTChartCount(List<long[]> timeLists) {
        long timeTotal = 0;

        for (long[] times : timeLists) {
            timeTotal = timeTotal + (times[1] - times[0]);
        }
    }

    private void updateMarketData(TenSpeedVo tenSpeedVo) {
        mTenSpeedVo = tenSpeedVo;
    }

    public void updateKChartData(List<KChartVo> list, KData.Unit unit) {
        if (iRequestKDataFlag == INIT) {
            if (null != list && list.size() > 0)
                mBinding.mKChart.loadInitialData(list, unit);
        } else if (iRequestKDataFlag == MORE) {
            if (null != list && list.size() > 0)
                mBinding.mKChart.loadMoreData(list, unit);
            else
                bHasMoreKDataFlag = false;
        } else if (iRequestKDataFlag == NEWEST) {
            if (null != list && list.size() > 0)
                mBinding.mKChart.loadNewestData(list, unit);
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
    }

    private long getTimeInterval() {
        return NetWorkUtils.isWifiConnected(mContext) ? AppConfig.TimeInterval_WiFi : AppConfig.TimeInterval_NetWork;
    }
}
