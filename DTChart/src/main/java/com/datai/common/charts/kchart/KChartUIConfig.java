package com.datai.common.charts.kchart;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;

import com.datai.common.charts.common.Config;
import com.datai.common.charts.common.MyValueFormatter;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;

import java.util.ArrayList;

/**
 * Created by Monking on 2016/1/18.
 */
public class KChartUIConfig {
    private Context mContext;
    private CombinedChart mFirstChart;
    private CombinedChart mSecondChart;
    private Typeface mTypeface;
    private int mPriceFormatDigit = 2;
    private MyValueFormatter mValueFormatter;

    public KChartUIConfig(Context context, CombinedChart firstChart, CombinedChart secondChart) {
        mContext = context;
        mFirstChart = firstChart;
        mSecondChart = secondChart;

        mTypeface = Typeface.createFromAsset(mContext.getAssets(), "OpenSans-Regular.ttf");
    }

    public void setChart() {
        mFirstChart.getViewPortHandler().setMajorChart(true);
        ChartTouchListener listener = mFirstChart.getOnTouchListener();
        listener.setSecondChart(mSecondChart);
        mSecondChart.setOnTouchListener(null);

        configFirstChart();
        configSecondChart();
    }

    private void configFirstChart() {
        mFirstChart.setDescription("");
        mFirstChart.getLegend().setEnabled(false);//关闭线条说明
        mFirstChart.setPinchZoom(false);
        mFirstChart.setScaleYEnabled(false);//禁止Y轴方向的缩放
        mFirstChart.setDrawGridBackground(false);//设置背景色（我们在外层Layout设置了）
        mFirstChart.setDrawBorders(true);//画Chart边框（源码改成了只画上下边框）
        mFirstChart.setBorderColor(Config.Border_Color);//设置Chart边框颜色
        mFirstChart.setBorderWidth(Config.Border_Width);//设置Chart边框宽度
        mFirstChart.setDoubleTapToZoomEnabled(false);
        mFirstChart.setHighlightPerDragEnabled(true);//设置高亮十字架可拖拽（源码修改成：长按出现十字架，松手消失）
        mFirstChart.setAutoScaleMinMaxEnabled(true);//设置自动根据可视区域的最大最小值来缩放尺寸
        mFirstChart.setMinOffset(0f);//设置Chart周围不留空白
        mFirstChart.setDragEnabled(false);
        //setMaxVisibleValueCount(60); //当显示的entry数目超过所设值，entry的值就不显示了，此设置目前已无效，源码已改成任何时候都显示最大最小值
        //mFirstChart.zoom(2.0f, 1f, 1, 1);
        //mFirstChart.setScaleMinima(2.0f, 1f);

//        MyMarkerView mv = new MyMarkerView(mContext, R.layout.custom_marker_view);
//        mFirstChart.setMarkerView(mv);

        XAxis xAxis = mFirstChart.getXAxis();
        xAxis.setEnabled(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM_BOTHSIDE);//设置X轴在Chart的底部
        xAxis.setSpaceBetweenLabels(10);//设置X轴值线（值约大线越少，还没看懂原理）
        xAxis.setDrawLabels(false);//不画X轴标尺数值
        xAxis.setXOffset(0);//设置x轴数值X轴偏移
        xAxis.setYOffset(3);//设置x轴数值X轴偏移
        xAxis.setTextSize(10f);
        xAxis.setTypeface(mTypeface);//设置X轴文字的字体
        xAxis.setDrawGridLines(false);//画X轴值线
        xAxis.setGridColor(Config.Grid_Color);//设置X轴值线颜色
        xAxis.setGridLineWidth(Config.Grid_Line_Width);//设置X轴值线宽度
        xAxis.enableGridDashedLine(5.0f, 5.0f, 1);//设置X轴值线成虚线
        xAxis.setDrawAxisLine(false);//不画X轴标尺线
        //xAxis.setTextColor(Config.X_Text_Color);//设置X轴值线的数值的颜色
        xAxis.setAvoidFirstLastClipping(true);//避免第一个和最后一个X轴值显示一半

        YAxis leftAxis = mFirstChart.getAxisLeft();
        leftAxis.setEnabled(true);
        leftAxis.setValueFormatter(mValueFormatter);//设置Y轴值的显示格式
        leftAxis.setLabelCount(5, true);//设置Y-Left轴值线（值约小线越少，还没看懂原理）
        leftAxis.setDrawGridLines(true);//画Y-Left轴值线
        leftAxis.setTypeface(mTypeface);//设置Y-Left轴文字字体
        leftAxis.setGridColor(Config.Grid_Color);//设置Y-Left轴值线颜色
        leftAxis.setGridLineWidth(Config.Grid_Line_Width);//设置Y-Left轴值线宽度
        leftAxis.enableGridDashedLine(5.0f, 5.0f, 1);//设置Y-Left轴值线成虚线
        leftAxis.setDrawAxisLine(false);//不画Y-Left轴标尺线
        leftAxis.setStartAtZero(false);//设置Y-Left值不从0开始
        leftAxis.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);//设置Y轴标尺数字在表内侧显示
        leftAxis.setXOffset(1);//设置Y-Left轴值线的数值X轴偏移
        leftAxis.setYOffset(-5);//设置Y-Left轴值线的数值Y轴偏移
        leftAxis.setTextColor(Config.Y_Text_Color);//设置Y-Left轴值线的数值的颜色
        //leftAxis.setSpaceTop(0f);
        //leftAxis.setSpaceBottom(0f);
        //leftAxis.setShowOnlyMinMax();
        //leftAxis.setAxisMinValue(-20.1f);
        //leftAxis.setAxisMaxValue(60f);
        //leftAxis.setXOffset(-20);

        YAxis rightAxis = mFirstChart.getAxisRight();
        rightAxis.setEnabled(false);
    }

    private void configSecondChart() {
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
        mSecondChart.setDragEnabled(false);
        //setDoubleTapToZoomEnabled(false);//副Chart没有焦点，不需要设置
        //setHighlightPerDragEnabled(true);//副Chart没有焦点，不需要设置
        //mSecondChart.zoom(2.0f, 1f, 1, 1);
        //mSecondChart.setScaleMinima(2.0f, 1f);
        //setDrawBarShadow(false);
        //setDrawValueAboveBar(true);

        XAxis xAxis = mSecondChart.getXAxis();
        xAxis.setEnabled(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM_BOTHSIDE);//设置X轴在Chart的底部
        xAxis.setSpaceBetweenLabels(10);//设置X轴值线（值约大线越少，还没看懂原理）
        xAxis.setDrawLabels(true);//画X轴标尺数值
        xAxis.setXOffset(0);//设置x轴数值X轴偏移
        xAxis.setYOffset(3);//设置x轴数值X轴偏移
        xAxis.setTextSize(10f);
        xAxis.setTypeface(mTypeface);//设置X轴文字的字体
        xAxis.setDrawGridLines(false);//画X轴值线
        xAxis.setTextColor(Config.X_Text_Color);//设置X轴值线的数值的颜色
        xAxis.setGridColor(Config.Grid_Color);//设置X轴值线颜色
        xAxis.setGridLineWidth(Config.Grid_Line_Width);//设置X轴值线宽度
        xAxis.enableGridDashedLine(5.0f, 5.0f, 1);//设置X轴值线成虚线
        xAxis.setDrawAxisLine(false);//画X轴标尺线
        //xAxis.setAxisLineColor(Color.RED);
        xAxis.setAvoidFirstLastClipping(true);//避免第一个和最后一个X轴值显示一半

        YAxis leftAxis = mSecondChart.getAxisLeft();
        leftAxis.setEnabled(true);
        leftAxis.setValueFormatter(new MyValueFormatter(2));//设置Y轴值的显示格式
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
        leftAxis.setStartAtZero(true);//当BarChart的值接近0时，图表仍然以0为底线

        YAxis rightAxis = mSecondChart.getAxisRight();
        rightAxis.setEnabled(false);
    }

    public void setPriceFormatDigit(int digits) {
        mPriceFormatDigit = digits;
        mValueFormatter = new MyValueFormatter(mPriceFormatDigit);
    }

    public MyValueFormatter getValueFormatter() {
        return mValueFormatter;
    }

    public void setStartAtZero(boolean enable) {
        YAxis leftAxis = mSecondChart.getAxisLeft();
        leftAxis.setStartAtZero(enable);
    }

    public void setDragEnable(boolean enable) {
        mFirstChart.setDragEnabled(enable);
        mSecondChart.setDragEnabled(enable);
    }

    public void setCommonLineDataSet(LineDataSet set) {
        set.setDrawCubic(false); // 平滑曲线
        set.setDrawCircles(false);//画节点圆心
        set.setCircleSize(4f);//设置节点圆心Size
        //set1.setDrawFilled(true);
        set.setLineWidth(Config.Line_Width);
        set.setColor(Color.WHITE);
        set.setFillAlpha(100);
        set.setDrawValues(false);

        set.setHighlightEnabled(false);
        set.setHighlightValueFormatter(mValueFormatter);
        set.setHighLightColor(Config.HighLight_Color);
        set.setDrawHorizontalHighlightIndicator(false);
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
    }

    public void setCubicLineDataset(LineDataSet set) {
        setCommonLineDataSet(set);
        set.setDrawCubic(true); // 平滑曲线
        set.setCubicIntensity(0.2f);//平滑程度
    }

    public void setCommonCandleDateSet(CandleDataSet set) {
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setShadowColorSameAsCandle(true);
        set.setShadowWidth(1f);

        set.setDecreasingPaintStyle(Paint.Style.FILL);
        set.setIncreasingPaintStyle(Paint.Style.FILL);

        set.setUseDataSetColor(true);
        set.setShadowColor(Config.Candle_Shadow_Color);
        set.setDecreasingColor(Config.Decreasing_Color);
        set.setIncreasingColor(Config.Increasing_Color);//即使setUseDataSetColor设为true，但是这三个值也要设置，用于显示最大最小值的颜色

        set.setHighlightEnabled(false);
        set.setDrawHorizontalHighlightInTouchPoint(true);
        set.setHighlightValueFormatter(mValueFormatter);
        set.setHighLightColor(Config.HighLight_Color);
        set.setDrawValues(true);//设置绘画节点的值（源码修改成：只绘画当前可视区域的最大最小值）
        //set1.setValueTextSize(0f);
        set.setValueTextColor(Color.WHITE);//设置绘画节点的颜色（源码修改：最大值显示Decreasing）
        set.setValueTextSize(12f);
        //set.setHighlightLineWidth(1f);
    }

    public void setBollCandleDateSet(CandleDataSet set) {
        setCommonCandleDateSet(set);
        set.setDrawHorizontalHighlightIndicator(false);
        set.setDrawValues(false);
    }

    public void setCommonBarDateSet(BarDataSet set) {
        set.setBarSpacePercent(70f);
        ArrayList colors = new ArrayList<Integer>();
        colors.add(Config.Increasing_Color);
        colors.add(Config.Decreasing_Color);
        set.setColors(colors);
        set.setDrawValues(false);

        set.setHighlightEnabled(false);
        set.setDrawHorizontalHighlightInTouchPoint(true);
        set.setHighlightValueFormatter(mValueFormatter);
        set.setHighLightColor(Config.HighLight_Color);
        set.setDrawHorizontalHighlightIndicator(false);
    }

    public void setVOLBarDateSet(BarDataSet set) {
        setCommonBarDateSet(set);
        set.setBarSpacePercent(10f);
    }

    public void setHighlightEnabled(DataSet set, boolean enable) {
        set.setHighlightEnabled(enable);
    }
}
