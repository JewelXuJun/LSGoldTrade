package com.datai.common.charts.tchart;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;

import com.datai.common.charts.common.Config;
import com.datai.common.charts.common.MyValueFormatter;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;

import java.util.ArrayList;

/**
 * Created by XuJun on 2016/1/20.
 */
public class TChartUIConfig {

    private Context mContext;
    private CombinedChart mFirstChart;
    private CombinedChart mSecondChart;
    private Typeface mTypeface;
    private boolean mShowSecondChart = true;
    private int mPriceFormatDigit = 2;
    private MyValueFormatter mValueFormatter;

    public TChartUIConfig(Context context, CombinedChart firstChart, CombinedChart secondChart) {
        this.mContext = context;
        this.mFirstChart = firstChart;
        this.mSecondChart = secondChart;

        mTypeface = Typeface.createFromAsset(mContext.getAssets(), "OpenSans-Regular.ttf");
    }

    public void setChart() {
        if (mShowSecondChart) {
            mFirstChart.getViewPortHandler().setMajorChart(true);
            ChartTouchListener listener = mFirstChart.getOnTouchListener();
            listener.setSecondChart(mSecondChart);
            mSecondChart.setOnTouchListener(null);
        }

        configFirstChart();
        configSecondChart();
    }

    public void configFirstChart() {
        if (mFirstChart == null)
            return;

        mFirstChart.setDescription("");
        mFirstChart.getLegend().setEnabled(false);//关闭线条说明
        mFirstChart.setPinchZoom(false);
        mFirstChart.setScaleYEnabled(false);
        mFirstChart.setDrawGridBackground(false);//设置背景色（我们在外层Layout设置了）
        mFirstChart.setDrawBorders(true);//画Chart边框（源码改成了只画上下边框）
        mFirstChart.setBorderColor(Config.Border_Color);//设置Chart边框颜色
        mFirstChart.setBorderWidth(Config.Border_Width);//设置Chart边框宽度
        mFirstChart.setDoubleTapToZoomEnabled(false);
        mFirstChart.setHighlightPerDragEnabled(true);//设置高亮十字架可拖拽（源码修改成：长按出现十字架，松手消失）
        mFirstChart.setAutoScaleMinMaxEnabled(true);//设置自动根据可视区域的最大最小值来缩放尺寸
        mFirstChart.setMinOffset(0f);//设置Chart周围不留空白
        mFirstChart.getViewPortHandler().setMaximumScaleX(2);
//        mFirstChart.setDragEnabled(false);

        XAxis xAxis = mFirstChart.getXAxis();
        xAxis.setEnabled(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);//设置X轴在Chart的底部
        xAxis.setSpaceBetweenLabels(1);//设置X轴值线（值越大线越少，还没看懂原理）
        xAxis.setDrawLabels(true);//画X轴标尺数值
        xAxis.setTextColor(Config.X_Text_Color);//设置X轴值线的数值的颜色
        xAxis.setTypeface(mTypeface);//设置X轴文字的字体
        xAxis.setDrawGridLines(true);//画X轴值线
        xAxis.setGridColor(Config.Grid_Color);//设置X轴值线颜色
        xAxis.setGridLineWidth(Config.Grid_Line_Width);//设置X轴值线宽度
        xAxis.enableGridDashedLine(5.0f, 5.0f, 1);//设置X轴值线成虚线
        xAxis.setDrawAxisLine(false);//画X轴标尺线
        xAxis.setAxisLineColor(Config.Border_Color);

        YAxis leftAxis = mFirstChart.getAxisLeft();
        leftAxis.setEnabled(true);
        leftAxis.setValueFormatter(mValueFormatter);//设置Y轴值的显示格式
        leftAxis.setLabelCount(5, true);//设置Y-Left轴值线（值越小线越少，还没看懂原理）
        leftAxis.setDrawGridLines(true);//画Y-Left轴值线
        leftAxis.setTypeface(mTypeface);//设置Y-Left轴文字字体
        leftAxis.setGridColor(Config.Grid_Color);//设置Y-Left轴值线颜色
        leftAxis.setGridLineWidth(Config.Grid_Line_Width);//设置Y-Left轴值线宽度
        leftAxis.enableGridDashedLine(5.0f, 5.0f, 1);//设置Y-Left轴值线成虚线
        leftAxis.setDrawAxisLine(false);//画Y-Left轴标尺线
        leftAxis.setStartAtZero(false);//设置Y-Left值不从0开始
        leftAxis.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);//设置Y轴标尺数字在表外侧显示
        leftAxis.setXOffset(1);//设置Y-Left轴值线的数值X轴偏移
        leftAxis.setYOffset(-5);//设置Y-Left轴值线的数值Y轴偏移
        leftAxis.setTextColor(Config.Y_Text_Color);//设置Y-Left轴值线的数值的颜色
        leftAxis.setSpaceTop(0f);//分时图上下不留空白
        leftAxis.setSpaceBottom(0f);
//        leftAxis.setMedian(0f);

        YAxis rightAxis = mFirstChart.getAxisRight();
        rightAxis.setEnabled(true);
        rightAxis.setValueFormatter(new MyValueFormatter(2, "%"));//设置Y轴值的显示格式
        rightAxis.setLabelCount(5, true);//设置Y-Left轴值线（值越小线越少，还没看懂原理）
        rightAxis.setDrawGridLines(false);//画Y-Left轴值线
        rightAxis.setTypeface(mTypeface);//设置Y-Left轴文字字体
        rightAxis.setGridColor(Config.Grid_Color);//设置Y-Left轴值线颜色
        rightAxis.setGridLineWidth(Config.Grid_Line_Width);//设置Y-Left轴值线宽度
        rightAxis.enableGridDashedLine(5.0f, 5.0f, 1);//设置Y-Left轴值线成虚线
        rightAxis.setDrawAxisLine(false);//画Y-Left轴标尺线
        rightAxis.setStartAtZero(false);//设置Y-Left值不从0开始
        rightAxis.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);//设置Y轴标尺数字在表外侧显示
        rightAxis.setXOffset(1);//设置Y-Left轴值线的数值X轴偏移
        rightAxis.setYOffset(-5);//设置Y-Left轴值线的数值Y轴偏移
        rightAxis.setTextColor(Config.Y_Text_Color);//设置Y-Left轴值线的数值的颜色
        rightAxis.setSpaceTop(0f);//分时图上下不留空白
        rightAxis.setSpaceBottom(0f);
        rightAxis.setMedian(0f, Config.Increasing_Color, Config.Decreasing_Color);
    }

    public void configSecondChart() {
        if (mSecondChart == null)
            return;

        mSecondChart.setDescription("");
        mSecondChart.getLegend().setEnabled(false);//关闭线条说明
        mSecondChart.setPinchZoom(false);// scaling can now only be done on x- and y-axis separately
        mSecondChart.setScaleYEnabled(false);//禁止Y轴方向的缩放
        mSecondChart.setDrawGridBackground(false);//设置背景色（我们在外层Layout设置了）
        mSecondChart.setDrawBorders(true);//画Chart边框（源码改成了只画上下边框）
        mSecondChart.setBorderColor(Config.Border_Color);//设置Chart边框颜色
        mSecondChart.setBorderWidth(Config.Border_Width);//设置Chart边框宽度
        mSecondChart.setMinOffset(0f);//设置Chart周围不留空白
        mSecondChart.setAutoScaleMinMaxEnabled(true);
//        mSecondChart.setDragEnabled(false);
        //setDoubleTapToZoomEnabled(false);//副Chart没有焦点，不需要设置
        //setHighlightPerDragEnabled(true);//副Chart没有焦点，不需要设置
        //mSecondChart.zoom(2.0f, 1f, 1, 1);
        //mSecondChart.setScaleMinima(2.0f, 1f);
        //setDrawBarShadow(false);
        //setDrawValueAboveBar(true);

        XAxis xAxis = mSecondChart.getXAxis();
        xAxis.setEnabled(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);//设置X轴在Chart的底部
        xAxis.setSpaceBetweenLabels(10);//设置X轴值线（值约大线越少，还没看懂原理）
        xAxis.setDrawLabels(false);//画X轴标尺数值
        xAxis.setXOffset(10);//设置x轴数值X轴偏移
        xAxis.setTypeface(mTypeface);//设置X轴文字的字体
        xAxis.setDrawGridLines(true);//画X轴值线
        xAxis.setTextColor(Config.X_Text_Color);//设置X轴值线的数值的颜色
        xAxis.setGridColor(Config.Grid_Color);//设置X轴值线颜色
        xAxis.setGridLineWidth(Config.Grid_Line_Width);//设置X轴值线宽度
        xAxis.enableGridDashedLine(5.0f, 5.0f, 1);//设置X轴值线成虚线
        xAxis.setDrawAxisLine(false);//画X轴标尺线
        //xAxis.setAxisLineColor(Color.RED);

        YAxis leftAxis = mSecondChart.getAxisLeft();
        leftAxis.setEnabled(true);
        leftAxis.setValueFormatter(new MyValueFormatter(0));//设置Y轴值的显示格式
        leftAxis.setLabelCount(3, true);//设置Y-Left轴值线（值约小线越少，还没看懂原理）
        leftAxis.setTypeface(mTypeface);//设置Y-Left轴文字字体
        leftAxis.setDrawGridLines(true);//不画Y-Left轴值线
        leftAxis.setGridColor(Config.Grid_Color);//设置Y-Left轴值线颜色
        leftAxis.setGridLineWidth(Config.Grid_Line_Width);//设置Y-Left轴值线宽度
        leftAxis.enableGridDashedLine(5.0f, 5.0f, 1);//设置Y-Left轴值线成虚线
        leftAxis.setDrawAxisLine(false);//不画Y-Left轴标尺线
        leftAxis.setStartAtZero(false);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);//设置Y轴标尺数字在表内侧显示
        leftAxis.setXOffset(1);//设置Y-Left轴值线的数值X轴偏移
        leftAxis.setYOffset(-5);//设置Y-Left轴值线的数值Y轴偏移
        leftAxis.setTextColor(Config.Y_Text_Color);//设置Y-Left轴值线的数值的颜色
        //leftAxis.setAxisMinValue(-2.01f);
        //leftAxis.setAxisMaxValue(2.01f);
        leftAxis.setStartAtZero(true);

        YAxis rightAxis = mSecondChart.getAxisRight();
        rightAxis.setEnabled(false);
    }

    public void setPreClose(float f) {
        //更新分时图的中间值（昨收价）
        YAxis leftAxis = mFirstChart.getAxisLeft();
        leftAxis.setMedian(f, Config.Increasing_Color, Config.Decreasing_Color);
    }

    public void setPriceFormatDigit(int digits) {
        mPriceFormatDigit = digits;
        mValueFormatter = new MyValueFormatter(mPriceFormatDigit);
    }

    public int getPriceFormatDigit() {
        return mPriceFormatDigit;
    }

    public void setShowSecondChart(boolean showSecondChart) {
        mShowSecondChart = showSecondChart;
    }

    public boolean isShowSecondChart() {
        return mShowSecondChart;
    }

    public void setScaleXEnabled(boolean enable) {
        mFirstChart.setScaleXEnabled(enable);
    }

    public void setDragEnable(boolean enable) {
//        mFirstChart.setDragEnabled(enable);
//        mSecondChart.setDragEnabled(enable);
    }

    public void setXAxisTime(boolean enable, long startTime, long endTime, long closeTime) {
        mFirstChart.getXAxis().setOnlyShowFirstLastValue(true, enable, startTime, endTime, closeTime);
    }

    public void setLineDataSet(LineDataSet set) {
        set.setDrawCircles(false);//画节点圆心
        set.setCircleSize(4f);//设置节点圆心Size
        set.setDrawValues(false);
        set.setFillAlpha(100);

        set.setHighlightEnabled(false);
        set.setHighLightColor(Config.HighLight_Color);
        set.setDrawHorizontalHighlightIndicator(true);
        set.setAxisDependency(YAxis.AxisDependency.LEFT);

        set.setValueTextColor(Color.WHITE);
        set.setValueTextSize(Config.VALUE_TEXT_SIZE);
    }

    public void setCurrentLineDataset(LineDataSet set) {
        setLineDataSet(set);
        set.setLineWidth(Config.Line_Width);
        set.setColor(Config.Current_Color);
        set.setDrawFilled(true);
        set.setFillColor(Config.Current_Fill_Color);

        set.setHighlightEnabled(true);
        set.setDrawHorizontalHighlightInTouchPoint(false);
        set.setHighLightColor(Config.HighLight_Color);
        set.setHighlightValueFormatter(mValueFormatter);
        set.setDrawHorizontalHighlightIndicator(true);
    }

    public void setRightAxisDataset(LineDataSet set) {
        setEmptyLineDataset(set);
        set.setLineWidth(0f);
        set.setAxisDependency(YAxis.AxisDependency.RIGHT);
    }

    public void setAverageLineDataset(LineDataSet set) {
        setLineDataSet(set);
        set.setLineWidth(Config.Line_Width);
        set.setColor(Config.Average_Color);
    }

    public void setVolBarDataset(BarDataSet set) {
        set.setBarSpacePercent(10f);
        ArrayList colors = new ArrayList<Integer>();
        colors.add(Config.Increasing_Color);
        colors.add(Config.Decreasing_Color);
        set.setColors(colors);
        set.setDrawValues(false);

        set.setHighlightEnabled(true);
        set.setDrawHorizontalHighlightInTouchPoint(false);
        set.setHighlightValueFormatter(mValueFormatter);
        set.setHighLightColor(Config.HighLight_Color);
        set.setDrawHorizontalHighlightIndicator(false);
    }

    public void setEmptyLineDataset(LineDataSet set) {
        setLineDataSet(set);
        set.setLineWidth(0f);

        set.setHighlightEnabled(false);
        set.setDrawHorizontalHighlightInTouchPoint(false);
        set.setHighlightValueFormatter(mValueFormatter);
        set.setDrawHorizontalHighlightIndicator(false);//副表不显示水平Highlight线
    }

    public void setEmptyBarDataset(BarDataSet set) {
        setVolBarDataset(set);
        set.setBarSpacePercent(100f);

        set.setHighlightEnabled(false);
        set.setDrawHorizontalHighlightInTouchPoint(false);
        set.setHighlightValueFormatter(mValueFormatter);
        set.setDrawHorizontalHighlightIndicator(true);
    }
}
