package com.jme.lsgoldtrade.ui.market;

import android.os.Bundle;
import android.text.TextUtils;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.datai.common.charts.chart.Chart;
import com.datai.common.charts.kchart.KChart;
import com.datai.common.charts.kchart.KData;
import com.datai.common.charts.tchart.TChart;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseActivity;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.ActivityMarketDetailLandscapeBinding;

@Route(path = Constants.ARouterUriConst.MARKETDETAILLANDSCAPE)
public class MarketDetailLandscapeActivity extends JMEBaseActivity {

    private ActivityMarketDetailLandscapeBinding mBinding;

    private Chart mChart;
    private TChart mTChart;
    private KChart mKChart;

    private String mContractId;
    private String mUnitCode;

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

        mChart.setPriceFormatDigit(2);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);

        mContractId = getIntent().getStringExtra("ContractId");
        mUnitCode = getIntent().getStringExtra("ChartUnit");

        setUnit();
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
    }

    private void setUnit() {
        if (TextUtils.isEmpty(mUnitCode))
            return;

        switch (mUnitCode) {
            case "time1":
                mChart.setChartUnit(KData.Unit.TIME);

                break;
            case "dayK":
                mChart.setChartUnit(KData.Unit.DAY);

                break;
            case "weekK":
                mChart.setChartUnit(KData.Unit.WEEK);

                break;
            case "monthK":
                mChart.setChartUnit(KData.Unit.MONTH);

                break;
            case "1minK":
                mChart.setChartUnit(KData.Unit.MIN1);

                break;
            case "5minK":
                mChart.setChartUnit(KData.Unit.MIN5);

                break;
            case "15minK":
                mChart.setChartUnit(KData.Unit.MIN15);

                break;
            case "30minK":
                mChart.setChartUnit(KData.Unit.MIN30);

                break;
            case "60minK":
                mChart.setChartUnit(KData.Unit.MIN60);

                break;
        }
    }

    @Override
    protected void DataReturn(DTRequest request, Head head, Object response) {
        super.DataReturn(request, head, response);
    }

    public class ClickHandlers {

        public void onClickClose() {

        }

    }
}
