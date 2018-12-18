package com.jme.lsgoldtrade.ui.market;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.datai.common.charts.fchart.FChart;
import com.datai.common.charts.tchart.TChart;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.common.util.DateUtil;
import com.jme.common.util.NetWorkUtils;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseActivity;
import com.jme.lsgoldtrade.config.AppConfig;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.ActivityMarketDetailBinding;
import com.jme.lsgoldtrade.domain.TenSpeedVo;
import com.jme.lsgoldtrade.service.MarketService;
import com.jme.lsgoldtrade.util.MarketUtil;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

@Route(path = Constants.ARouterUriConst.MARKETDETAIL)
public class MarketDetailActivity extends JMEBaseActivity implements FChart.OnPriceClickListener {

    private ActivityMarketDetailBinding mBinding;

    private TenSpeedVo mTenSpeedVo;
    private MarketOrderPopUpWindow mPopupWindow;
    private TChart mTChart;

    private String mContractId;
    private boolean bFlag = true;
    private boolean bHighlight = false;

    private static final String DIRECTION_AFTER = "1";
    private static final String COUNT_TCHART = "660";
    private static final String COUNT_KCHART = "200";

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constants.Msg.MSG_UPDATE_DATA:
                    mHandler.removeMessages(Constants.Msg.MSG_UPDATE_DATA);

                    getTenSpeedQuotes(false);

                    mHandler.sendEmptyMessageDelayed(Constants.Msg.MSG_UPDATE_DATA, getTimeInterval());

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

        initToolbar(MarketUtil.getContractNameEN(mContractId), true, ContextCompat.getColor(this, R.color.white));
        setBackGroundColor(R.color.common_font_stable);
        setBackNavigation(true, R.mipmap.ic_back_white);

        mPopupWindow = new MarketOrderPopUpWindow(this);
        mTChart = mBinding.chart.getTChart();

        mBinding.chart.setPriceFormatDigit(2);

        initTChart();
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
    }

    @Override
    protected void initListener() {
        super.initListener();
    }

    @Override
    protected void initBinding() {
        super.initBinding();

        mBinding.setHandlers(new ClickHandlers());
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (TextUtils.isEmpty(mContractId))
            return;

        getTenSpeedQuotes(true);
    }

    @Override
    protected void onPause() {
        super.onPause();

        bFlag = true;

        removeMessage();
    }

    private void setBackGroundColor(int color) {
//        StatusBarUtil.setStatusBarMode(this, true, color);
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

    private long getTimeInterval() {
        return NetWorkUtils.isWifiConnected(mContext) ? AppConfig.TimeInterval_WiFi : AppConfig.TimeInterval_NetWork;
    }

    private void removeMessage() {
        mHandler.removeMessages(Constants.Msg.MSG_UPDATE_DATA);
    }

    private void updateMarketData(TenSpeedVo tenSpeedVo) {
        if (null == tenSpeedVo)
            return;

        mTenSpeedVo = tenSpeedVo;

        String lastSettlePrice = tenSpeedVo.getLastSettlePrice();

        if (TextUtils.isEmpty(lastSettlePrice))
            return;

        if (!bHighlight) {
            String upDownRate = tenSpeedVo.getUpDownRate();
            String highestPrice = tenSpeedVo.getHighestPrice();
            String lowestPrice = tenSpeedVo.getLowestPrice();

            int rateType;

            if (TextUtils.isEmpty(upDownRate))
                rateType = 0;
            else
                rateType = new BigDecimal(upDownRate).compareTo(new BigDecimal(0));

            setBackGroundColor(MarketUtil.getMarketStateColor(rateType));

            mBinding.tvLastPrice.setText(MarketUtil.getValue(tenSpeedVo.getLatestPrice()));
            mBinding.tvRange.setText(MarketUtil.getMarketRangeValue(rateType, tenSpeedVo.getUpDown()));
            mBinding.tvRate.setText(MarketUtil.getMarketRateValue(rateType, upDownRate));
            mBinding.tvOpen.setText(MarketUtil.getValue(tenSpeedVo.getOpenPrice()));
            mBinding.tvPreclose.setText(MarketUtil.getValue(tenSpeedVo.getLastClosePrice()));
            mBinding.tvTurnVolume.setText(MarketUtil.getVolumeValue(String.valueOf(tenSpeedVo.getTurnover()), false));
            mBinding.tvVolume.setText(MarketUtil.getVolumeValue(String.valueOf(tenSpeedVo.getTurnVolume()), false));
            mBinding.tvTime.setText(MarketUtil.getValue(DateUtil.stringToAllTime(tenSpeedVo.getQuoteTime())));
            mBinding.tvHigh.setText(MarketUtil.getValue(highestPrice));
            mBinding.tvHigh.setTextColor(ContextCompat.getColor(this, MarketUtil.getMarketStateColor(
                    TextUtils.isEmpty(highestPrice) || TextUtils.isEmpty(lastSettlePrice) ? -2 : new BigDecimal(highestPrice).compareTo(new BigDecimal(lastSettlePrice)))));
            mBinding.tvLow.setText(MarketUtil.getValue(lowestPrice));
            mBinding.tvLow.setTextColor(ContextCompat.getColor(this, MarketUtil.getMarketStateColor(
                    TextUtils.isEmpty(lowestPrice) || TextUtils.isEmpty(lastSettlePrice) ? -2 : new BigDecimal(lowestPrice).compareTo(new BigDecimal(lastSettlePrice)))));
        }

        mTChart.setPreClose(lastSettlePrice);
        mTChart.loadTradeInfoChartData(tenSpeedVo.getAskLists(), tenSpeedVo.getBidLists());
    }

    private void getTenSpeedQuotes(boolean enable) {
        HashMap<String, String> params = new HashMap<>();
        params.put("list", mContractId);

        sendRequest(MarketService.getInstance().getTenSpeedQuotes, params, enable, false, false);
    }

    private void getTChartQuotes() {
        HashMap<String, String> params = new HashMap<>();
        params.put("contractId", mContractId);
        params.put("qryFlag", DIRECTION_AFTER);
        params.put("count", COUNT_TCHART);

        sendRequest(MarketService.getInstance().getTChartQuotes, params, false, false, false);
    }

    private void getKChartQuotes(String type, String time, String flag) {
        HashMap<String, String> params = new HashMap<>();
        params.put("type", type);
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

                    updateMarketData(list.get(0));
                }

                if (bFlag) {
                    bFlag = false;

                    mHandler.sendEmptyMessageDelayed(Constants.Msg.MSG_UPDATE_DATA, getTimeInterval());
                }

                break;
            case "GetTChartQuotes":
                if (head.isSuccess()) {

                } else {

                }

                break;
            case "GetKChartQuotes":
                if (head.isSuccess()) {

                } else {

                }

                break;
        }
    }

    @Override
    public void OnPriceClick(String price, String title) {

    }

    public class ClickHandlers {

        public void onClickBack() {
            finish();
        }

        public void onClickDeclarationForm() {

        }

        public void onClickOneKeyOrder() {
            mPopupWindow.showAtLocation(mBinding.layoutFooterview, Gravity.BOTTOM, 0, 0);
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
