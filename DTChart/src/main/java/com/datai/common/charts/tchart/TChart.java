package com.datai.common.charts.tchart;

import android.content.Context;
import android.text.SpannableString;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.datai.common.R;
import com.datai.common.charts.common.Config;
import com.datai.common.charts.common.Descriptor;
import com.datai.common.charts.fchart.FData;
import com.datai.common.charts.fchart.TradeInfoChart;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.gson.JsonArray;
import com.jme.common.util.TChartVo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by XuJun on 2016/1/20.
 */
public class TChart extends LinearLayout {

    private TextView mTChart_data;
    private CombinedChart mFirstChart;
    private CombinedChart mSecondChart;
    private TradeInfoChart mTradeInfoChart;
    private ImageView iv_landscape;
    private boolean mLandscapeVisible = false;
    private OnTChartSelectedListener mSelectedListener;
    private OnTChartListener mTChartListener;

    private TChartUIConfig mTConfig;
    private TChartData mTChartData;
    private FData mFData;

    private Descriptor mDescriptor = new Descriptor();

    private List<long[]> mTimeLists;

    private boolean bFlag = false;
    private boolean bAverageFlag = true;

    public TChart(Context context) {
        super(context);
    }

    public TChart(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupLayoutResource(R.layout.view_tchart);

        init();
    }

    private void setupLayoutResource(int layoutResource) {
        View inflater = LayoutInflater.from(getContext()).inflate(layoutResource, this);

        inflater.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        inflater.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        inflater.layout(0, 0, inflater.getMeasuredWidth(), inflater.getMeasuredHeight());
    }

    private void init() {
        mTChart_data = findViewById(R.id.tchart_data);
        mFirstChart = findViewById(R.id.tchart);
        mSecondChart = findViewById(R.id.vchart);
        mTradeInfoChart = findViewById(R.id.tradeinfochart);
        iv_landscape = findViewById(R.id.iv_landscape);

        mTConfig = new TChartUIConfig(getContext(), mFirstChart, mSecondChart);
        mTChartData = new TChartData();
        mFData = new FData();

        setHighlightListener();
        setChartGestureListener();
    }

    public TradeInfoChart getTradeInfoChart() {
        return mTradeInfoChart;
    }

    public void config() {
        mTConfig.setChart();
        if (mTConfig.isShowSecondChart()) {
            mSecondChart.setVisibility(View.VISIBLE);
            mTradeInfoChart.setVisibility(View.VISIBLE);
        } else {
            mSecondChart.setVisibility(View.GONE);
            mTradeInfoChart.setVisibility(View.GONE);
        }
    }

    public void loadTChartData(JsonArray jsonArray) {
//        mTData.setData(jsonArray);
        /*boolean animate = !mTChartData.hasData();
        mTChartData.loadData(jsonArray);

        setFirstChartData();
        setSecondChartData();

        if (!bFlag)
            setTextOnUnHighlight(mTChartData.getCurrentVals().size() - 1);

        if (mLandscapeVisible) {
            iv_landscape.setVisibility(View.VISIBLE);
        }

        if (animate) {
            mFirstChart.animateX(500);
            mSecondChart.animateX(500);
        }*/
        loadTChartData(jsonArray, false);
    }

    public void loadTChartData(List<TChartVo> list) {
        loadTChartData(list, false, false);
    }

    public void loadTChartData(JsonArray jsonArray, boolean isSingleVol) {
//        mTData.setData(jsonArray);
        boolean animate = !mTChartData.hasData();
        mTChartData.loadData(jsonArray);
        mTChartData.setIsSingleVol(isSingleVol);

        setFirstChartData();
        setSecondChartData();

        if (!bFlag)
            setTextOnUnHighlight(mTChartData.getCurrentVals().size() - 1);

        if (mLandscapeVisible) {
            iv_landscape.setVisibility(View.VISIBLE);
        }

        if (animate) {
            mFirstChart.animateX(500);
            mSecondChart.animateX(500);
        }
    }

    public void loadTChartData(List<TChartVo> list, boolean isSingleVol, boolean isAverage) {
//        mTData.setData(jsonArray);
        bAverageFlag = isAverage;
        boolean animate = !mTChartData.hasData();
        mTChartData.setIsSingleVol(isSingleVol);
        mTChartData.loadData(list);

        setFirstChartData();
        setSecondChartData();

        if (!bFlag)
            setTextOnUnHighlight(mTChartData.getCurrentVals().size() - 1);

        if (mLandscapeVisible) {
            iv_landscape.setVisibility(View.VISIBLE);
        }

        if (animate) {
            mFirstChart.animateX(500);
            mSecondChart.animateX(500);
        }
    }

    public void loadTradeInfoChartData(List<String[]> offerGrpList, List<String[]> bidGrpList,
                                       List<String[]> tickData) {
        mFData.setData(offerGrpList, bidGrpList, tickData);

        setTradeInfoChartData();
    }

    public void loadTradeInfoChartData(List<String[]> offerGrpList, List<String[]> bidGrpList) {
        mFData.setTradeData(offerGrpList, bidGrpList);

        setTradeChartData();
    }

    public void loadDealChartData(List<String[]> tickData) {
        mFData.setDealData(tickData);

        setDealData();
    }

    public void setPriceFormatDigit(int digits) {
        mTConfig.setPriceFormatDigit(digits);
    }

    public void setPreClose(String preClose) {
        mTChartData.setPreclose(preClose);
        float f = Float.parseFloat(preClose);
        mTConfig.setPreClose(f);
    }

    public void setIsNeedSupplement(boolean enable) {
        mTChartData.setIsNeedSupplement(enable);
    }

    public void setIsStartFromBeginning(boolean enable) {
        mTChartData.setIsStartFromBeginning(enable);
    }

    public void setScaleXEnabled(boolean enabled) {
        mTConfig.setScaleXEnabled(enabled);
    }

    public void setTChartXAxisTime(List<long[]> list, long timeInterval) {
        if (list != null && list.size() != 0) {
            boolean bHaveCloseTime;

            mTimeLists = list;

            int size = list.size();

            if (size > 1) {
                bHaveCloseTime = true;

                if (size > 2)
                    mTConfig.setXAxisTime(true, bHaveCloseTime, true, list);
                else
                    mTConfig.setXAxisTime(bHaveCloseTime, list.get(0)[0], list.get(list.size() - 1)[1], list.get(list.size() - 1)[0] - list.get(0)[1]);
            } else {
                mTConfig.setXAxisTime(false, true, true, list);
            }

            mTChartData.setTradeTime(list, timeInterval);
        }
    }

    public void setShowVolChart(boolean showVolChart) {
        mTConfig.setShowSecondChart(showVolChart);
    }

    public void setOnTChartListener(OnTChartListener listener) {
        mTChartListener = listener;
    }

    public void setOnLandscapeListener(OnClickListener listener) {
        iv_landscape.setOnClickListener(listener);
    }

    public void setLandscapeButtonVisible(boolean visible) {
        mLandscapeVisible = visible;
        iv_landscape.setVisibility(mLandscapeVisible ? View.VISIBLE : View.GONE);

        //mTConfig.setDragEnable(!visible);// 竖屏时，K线图不能拖拽
    }

    public void setOnTChartSelectedListener(OnTChartSelectedListener listener) {
        mSelectedListener = listener;
    }

    private void setHighlightListener() {
        mFirstChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
                if (h == null)
                    return;

                bFlag = true;
                int index = h.getXIndex();
                setTextOnUnHighlight(index);

                if (mSelectedListener != null) {
                    float prev = -1;
                    if (index > 0) {
                        prev = mTChartData.getEmptyVals().get(index - 1).getVal();
                    } else {
                        prev = mTChartData.getEmptyVals().get(0).getVal();
                    }
                    mSelectedListener.onTChartValueSelected(mTChartData.getEmptyVals().get(index).getVal(), index, prev);
                }
            }

            @Override
            public void onNothingSelected() {
                bFlag = false;
                setTextOnUnHighlight(mTChartData.getCurrentVals().size() - 1);

                if (mSelectedListener != null) {
                    mSelectedListener.onTChartNothingSelected();
                }
            }
        });
    }

    private void setChartGestureListener() {
        mFirstChart.setOnChartGestureListener(new OnChartGestureListener() {
            @Override
            public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

            }

            @Override
            public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
            }

            @Override
            public void onChartLongPressed(MotionEvent me) {

            }

            @Override
            public void onChartDoubleTapped(MotionEvent me) {

            }

            @Override
            public void onChartSingleTapped(MotionEvent me) {
                if (mTChartListener != null) {
                    mTChartListener.onChartSingleTapped(me);
                }
            }

            @Override
            public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {

            }

            @Override
            public void onChartScale(MotionEvent me, float scaleX, float scaleY) {

            }

            @Override
            public void onChartTranslate(MotionEvent me, float dX, float dY) {

            }
        });
    }

    private void setTextOnUnHighlight(int index) {
        if (index < 0) {
            return;
        }

        List<Object> list = mTChartData.compriseData(index);

        if (list != null) {
            SpannableString message = mDescriptor.getTchartDetail(list,
                    mTChartData.isRise((float) list.get(3)), mTConfig.getPriceFormatDigit());

            if (TextUtils.isEmpty(message)) {
                mTChart_data.setText("No Data");
            } else {
                mTChart_data.setText(message);
            }
        }
    }

    private void setFirstChartData() {
        List<String> xValsList = mTChartData.getXVals();

        // 添加 当前价Line 和 平均价Line
        LineDataSet currentLineDataSet = new LineDataSet(mTChartData.getCurrentVals(), "CurrentData");
        mTConfig.setCurrentLineDataset(currentLineDataSet);

        LineDataSet averageLineDataSet = new LineDataSet(mTChartData.getAverageVals(), "AverageData");
        mTConfig.setAverageLineDataset(averageLineDataSet);

        LineDataSet riseRangeSet = new LineDataSet(mTChartData.getRiseRangeVals(), "RiseData");
        mTConfig.setRightAxisDataset(riseRangeSet);

        ArrayList<LineDataSet> lineDateSet = new ArrayList<LineDataSet>();
        lineDateSet.add(currentLineDataSet);
        lineDateSet.add(averageLineDataSet);
        lineDateSet.add(riseRangeSet);
        LineData lineData = new LineData(xValsList, lineDateSet);

        // 添加 Empty Bar
        ArrayList<BarEntry> emptyBarList = new ArrayList<BarEntry>();
        for (int i = 0; i < mTChartData.getEmptyVals().size(); i++) {
            Entry entry = mTChartData.getEmptyVals().get(i);
            BarEntry barEntry = new BarEntry(entry.getVal(), entry.getXIndex());
            emptyBarList.add(barEntry);
        }
        BarDataSet emptyBarDataSet = new BarDataSet(emptyBarList, "EmptyBarData");
        mTConfig.setEmptyBarDataset(emptyBarDataSet);
        BarData barData = new BarData(xValsList, emptyBarDataSet);

        CombinedData combinedData = new CombinedData(xValsList);
        combinedData.setData(barData);
        combinedData.setData(lineData);

        mFirstChart.setData(combinedData);
        if (bAverageFlag) {
            mFirstChart.getXAxis().setLabelsToSkip(mFirstChart.getXValCount() / 4);
        } else {
            long[] timeValues = new long[mTimeLists.size()];

            for (int i = 0; i < mTimeLists.size(); i++) {
                timeValues[i] = (mTimeLists.get(i)[1] - mTimeLists.get(i)[0]) / Config.TimeInterval_OneMinute;
            }

            mFirstChart.getXAxis().setLabelsToSkip(timeValues, true);
        }

        mFirstChart.invalidate();
    }

    private void setSecondChartData() {
        List<String> xValsList = mTChartData.getXVals();

        BarDataSet volBarDataSet = new BarDataSet(mTChartData.getVolVals(), "VolData");
        mTConfig.setVolBarDataset(volBarDataSet);
        BarData barData = new BarData(xValsList, volBarDataSet);

        List<Integer> list_color = mTChartData.getRiseVals();
        int[] color = new int[list_color.size()];
        for (int j = 0; j < list_color.size(); j++) {
            switch (list_color.get(j)) {
                case TChartData.DECREASING:
                    color[j] = Config.Decreasing_Color;
                    break;
                case TChartData.INCREASING:
                case TChartData.STABLE:
                default:
                    color[j] = Config.Increasing_Color;
                    break;
            }
        }
        volBarDataSet.setColors(color);

        LineDataSet emptyLineDataSet = new LineDataSet(mTChartData.getEmptyVals(), "EmptyLineData");
        mTConfig.setEmptyLineDataset(emptyLineDataSet);
        LineData emptyLineData = new LineData(xValsList, emptyLineDataSet);

        CombinedData combinedData = new CombinedData(xValsList);
        combinedData.setData(emptyLineData);
        combinedData.setData(barData);

        mSecondChart.setData(combinedData);
        mSecondChart.getXAxis().setLabelsToSkip(mSecondChart.getXValCount() / 4);
        mSecondChart.invalidate();
    }

    private void setTradeInfoChartData() {
        mTradeInfoChart.getFChart(FData.TYPE_BUY).setData(mFData.getBidGrp(), FData.TYPE_BUY, mTChartData.getPreclose());
        mTradeInfoChart.getFChart(FData.TYPE_SELL).setData(mFData.getOfferGrp(), FData.TYPE_SELL, mTChartData.getPreclose());
        mTradeInfoChart.getFChart(FData.TYPE_TICK).setData(mFData.getTickData(), FData.TYPE_TICK, mTChartData.getPreclose());
    }

    private void setTradeChartData() {
        mTradeInfoChart.getFChart(FData.TYPE_BUY).setData(mFData.getBidGrp(), FData.TYPE_BUY, mTChartData.getPreclose());
        mTradeInfoChart.getFChart(FData.TYPE_SELL).setData(mFData.getOfferGrp(), FData.TYPE_SELL, mTChartData.getPreclose());
    }

    private void setDealData() {
        mTradeInfoChart.getFChart(FData.TYPE_TICK).setData(mFData.getTickData(), FData.TYPE_TICK, mTChartData.getPreclose());
    }
}
