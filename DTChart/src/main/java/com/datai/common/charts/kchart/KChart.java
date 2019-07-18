package com.datai.common.charts.kchart;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.datai.common.R;
import com.datai.common.charts.common.Config;
import com.datai.common.charts.common.Descriptor;
import com.datai.common.charts.indicator.Indicator;
import com.datai.common.view.popup.ActionItem;
import com.datai.common.view.popup.QuickAction;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.jme.common.util.KChartVo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Monking on 2016/1/18.
 */
public class KChart extends LinearLayout {
    private Context mContext;
    private CombinedChart mFirstChart;
    private CombinedChart mSecondChart;
    private LinearLayout ll_second_chart_title;
    private TextView tv_first_chart;
    private TextView tv_second_chart;
    private TextView tv_second_chart_title;
    private ImageView iv_landscape;
    private boolean mLandscapeVisible = false;

    private KChartData mKChartData;
    private KChartUIConfig mKConfig;

    private Descriptor mDescriptor = new Descriptor();
    private Indicator mMAIndicator;
    private Indicator mSelIndicator;
    private QuickAction mPopupIndicators;

    private int mMinXRange = Config.MinXRangeDefault;
    private int mMaxXRange = Config.MaxXRangeDefault;
    private Indicator.Type[] mShowIndicators = Config.Indicators;
    private OnKChartSelectedListener mSelectedListener;
    private OnKChartListener mKChartListener;
    private boolean bDefaultScale = true;
    private boolean isValueSelected = false;

    public KChart(Context context) {
        this(context, null);
    }

    public KChart(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        setupLayoutResource(R.layout.view_kchart);
        init();
    }

    public void switchIndicator(Indicator.Type type) {
        mSelIndicator = mKChartData.getIndicator(type);
    }

    public Indicator.Type getIndicatorType() {
        return mSelIndicator.getType();
    }

    public void goBackDefaultScale() {
        bDefaultScale = true;
    }

    public boolean hasKDataInCurrentUnit() {
        return mKChartData.hasKDataInCurrentUnit();
    }

    private float mLastVisibleCount;

    public void switchKDataToCurrentUnit() {
        float oriLowIndex = 0;
        float visibleCount = 0;
        if (bDefaultScale) {
            bDefaultScale = false;
            visibleCount = Config.InitVisibleEntryCount;
        } else {
            int oriCount = mKChartData.getEntryCount();
            float oriScaleX = mFirstChart.getScaleX();
            visibleCount = oriCount / oriScaleX;
            if (visibleCount == 0)
                visibleCount = Config.InitVisibleEntryCount;
        }

        mLastVisibleCount = visibleCount;

        mKChartData.switchKDataToCurrentUnit();
        if (mKChartData.getEntryCount() == 0) {
            clearChartData();
        } else {
            setCandleChartData();
            setSecondChartData();
            int newCount = mKChartData.getEntryCount();
            oriLowIndex = newCount - visibleCount;

            setVisibleEntry(visibleCount, oriLowIndex);
            setTextOnUnHighlight();
        }

        if (mLandscapeVisible) {
            iv_landscape.setVisibility(View.VISIBLE);
        }
    }

    public void loadInitialData(List<KChartVo> list, KData.Unit unit) {
        if (mKChartData.getEntryDataUnit() != unit) {
            // 更新的数据与当前指向数据Unit不相符，故忽略
            return;
        }

        float oriLowIndex = 0;
        float visibleCount = 0;
        if (bDefaultScale) {
            bDefaultScale = false;
            visibleCount = Config.InitVisibleEntryCount;
        } else {
//            int oriCount = mKChartData.getEntryCount();
//            float oriScaleX = mFirstChart.getScaleX();
//            visibleCount = oriCount / oriScaleX;
            visibleCount = mLastVisibleCount;
        }

        mKChartData.loadInitialData(list);
        setCandleChartData();
        setSecondChartData();
        int newCount = mKChartData.getEntryCount();
        oriLowIndex = newCount - visibleCount;

        setVisibleEntry(visibleCount, oriLowIndex);
        setTextOnUnHighlight();
        if (mLandscapeVisible) {
            iv_landscape.setVisibility(View.VISIBLE);
        }
        mFirstChart.animateX(500);
        mSecondChart.animateX(500);
    }

    public void loadMoreData(List<KChartVo> list, KData.Unit unit) {
        if (mKChartData.getEntryDataUnit() != unit) {
            // 更新的数据与当前指向数据Unit不相符，故忽略
            return;
        }

        int oriCount = mKChartData.getEntryCount();
        float oriScaleX = mFirstChart.getScaleX();
        float oriLowIndex = mFirstChart.getLowestVisibleX();
        float visibleCount = oriCount / oriScaleX;

        mKChartData.loadMoreData(list);
        setCandleChartData();
        setSecondChartData();

        int newCount = mKChartData.getEntryCount();
        oriLowIndex += (newCount - oriCount);

        setVisibleMoreEntry(visibleCount, oriLowIndex);
        setTextOnUnHighlight();
    }

    public void loadNewestData(List<KChartVo> list, KData.Unit unit) {
        if (mKChartData.getEntryDataUnit() != unit) {
            // 更新的数据与当前指向数据Unit不相符，故忽略
//            Toast.makeText(mContext, "切换太快", Toast.LENGTH_SHORT).show();
            return;
        }

        int oriCount = mKChartData.getEntryCount();
        float oriScaleX = mFirstChart.getScaleX();
        float oriLowIndex = mFirstChart.getLowestVisibleX();
        float oriHighIndex = mFirstChart.getHighestVisibleX();
        float visibleCount = oriCount / oriScaleX;

        mKChartData.loadNewestData(list);
        setCandleChartData();
        setSecondChartData();

        int newCount = mKChartData.getEntryCount();
        if (oriCount != newCount) {
            if (oriHighIndex <= oriCount - 0.5f && oriHighIndex > oriCount - 1.5f) {
                oriLowIndex += (newCount - oriCount);
            }
            setVisibleEntry(visibleCount, oriLowIndex);
        }
        setTextOnUnHighlight();
    }

    public void setShowIndicators(Indicator.Type[] indicators) {
        if (indicators == null || indicators.length <= 0)
            return;

        mShowIndicators = indicators;
    }

    public void setUnit(KData.Unit unit) {
        mKChartData.setUnit(unit);
    }

    public KData.Unit getUnit() {
        return mKChartData.getUnit();
    }

    public void setIsFromServerMAs(boolean enable) {
        mKChartData.setIsFromServerMAs(enable);
    }

    public void setHasTradeVolume(boolean enable) {
        mKChartData.setHasTradeVolume(enable);
//        if (!enable) {
//            mKChartData.removeIndicator(Indicator.Type.VOL);
//        } else {
//            mKChartData.setIndicator(Indicator.Type.VOL);
//        }
    }

    public void setPriceFormatDigit(int digits) {
        mKConfig.setPriceFormatDigit(digits);
    }

    public int getDataType(int index, float data, float prev) {
        return mKChartData.getType(index, data, prev);
    }

    public long getNewestTimeTick() {
        return mKChartData.getNewestTimeTick(0);
    }

    public long getNewestTimeTick(int offset) {
        return mKChartData.getNewestTimeTick(offset);
    }

    public long getOldestTimeTick() {
        return mKChartData.getOldestTimeTick();
    }

    public int getDataCount() {
        return mKChartData.getEntryCount();
    }

    public void setOnKChartSelectedListener(OnKChartSelectedListener listener) {
        mSelectedListener = listener;
    }

    public void setOnKChartListener(OnKChartListener listener) {
        mKChartListener = listener;
    }

    public void setOnLandscapeListener(OnClickListener listener) {
        iv_landscape.setOnClickListener(listener);
    }

    public void setLandscapeButtonVisible(boolean visible) {
        mLandscapeVisible = visible;
        iv_landscape.setVisibility(mLandscapeVisible ? View.VISIBLE : View.GONE);

        mKConfig.setDragEnable(!visible);// 竖屏时，K线图不能拖拽
    }

    public void setVisibleXRange(int minXRange, int maxXRange) {
        mMinXRange = minXRange;
        mMaxXRange = maxXRange;
    }

    private void setupLayoutResource(int layoutResource) {
        View inflated = LayoutInflater.from(getContext()).inflate(layoutResource, this);
        inflated.setLayoutParams(new LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        inflated.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        inflated.layout(0, 0, inflated.getMeasuredWidth(), inflated.getMeasuredHeight());
    }

    private void init() {
        mFirstChart = findViewById(R.id.chart1);
        mSecondChart = findViewById(R.id.chart2);
        ll_second_chart_title = findViewById(R.id.ll_second_chart_title);
        tv_first_chart = findViewById(R.id.tv_first_chart);
        tv_second_chart = findViewById(R.id.tv_second_chart);
        tv_second_chart_title = findViewById(R.id.tv_second_chart_title);
        iv_landscape = findViewById(R.id.iv_landscape);

        mKConfig = new KChartUIConfig(getContext(), mFirstChart, mSecondChart);
        mKChartData = new KChartData();

        setHighlightListener();
        setChartGestureListener();

        ll_second_chart_title.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    public void config() {
        mKConfig.setChart();

        mMAIndicator = mKChartData.setMARanks(Config.MARanks);
        mKChartData.setIndicator(mShowIndicators);
        mSelIndicator = mKChartData.getIndicator(mShowIndicators[0]);

        initPopup();
    }

    private void initPopup() {
        if (mShowIndicators == null || mShowIndicators.length <= 0)
            return;

        mPopupIndicators = new QuickAction(mContext, QuickAction.VERTICAL);

        mPopupIndicators.setActionItemTitleSize(12);

        for (int i = 0; i < mShowIndicators.length; i++) {
            String title = mShowIndicators[i].toString();
            ActionItem item = new ActionItem(i, title, null);
            mPopupIndicators.addActionItem(item);
        }

        mPopupIndicators.setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {
            @Override
            public void onItemClick(QuickAction source, int pos,
                                    int actionId) {
                ActionItem item = mPopupIndicators.getActionItem(pos);
                switchIndicator(mShowIndicators[actionId]);
                setSecondChartData();
            }
        });

        tv_second_chart_title.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupIndicators.show(v);
            }
        });
    }

    private void setHighlightListener() {
        mFirstChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
                if (h == null)
                    return;

                int index = h.getXIndex();
                HashMap<String, Object> entryData = mKChartData.getEntryData(index);
                tv_first_chart.setText(mDescriptor.getDetail(mMAIndicator, entryData, false, true));
                tv_second_chart.setText(mDescriptor.getDetail(mSelIndicator, entryData, false, true));
                isValueSelected = true;

                if (mSelectedListener != null) {
                    float prev = -1;
                    if (index > 0) {
                        prev = (float) mKChartData.getEntryData(index - 1).get(Indicator.K_CLOSE);
                    } else {
                        prev = (float) mKChartData.getEntryData(0).get(Indicator.K_OPEN);
                    }
                    mSelectedListener.onValueSelected(mKChartData.getEntryData(index), index, prev);
                }
            }

            @Override
            public void onNothingSelected() {
                isValueSelected = false;
                setTextOnUnHighlight();
                if (mSelectedListener != null) {
                    mSelectedListener.onNothingSelected();
                }
            }
        });
    }

    private boolean isInit = false;

    private void setChartGestureListener() {
        mFirstChart.setOnChartGestureListener(new OnChartGestureListener() {
            @Override
            public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

            }

            @Override
            public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
                if (mFirstChart.getLowestVisibleXIndex() < 20) {
                    if (mKChartListener != null) {
                        mKChartListener.onEnding(mKChartData.getOldestTimeTick(), mKChartData.getUnit());
                    }
                }
            }

            @Override
            public void onChartLongPressed(MotionEvent me) {

            }

            @Override
            public void onChartDoubleTapped(MotionEvent me) {

            }

            @Override
            public void onChartSingleTapped(MotionEvent me) {
                if (me.getY() <= mFirstChart.getHeight() * 2 / 3) {
                    if (mKChartListener != null) {
                        mKChartListener.onChartSingleTapped(me);
                    }

                    if (isInit == false) {
                        isInit = true;
                        HashMap<String, Object> entryData = mKChartData.getEntryData(mKChartData.getEntryCount() - 1);
                        tv_first_chart.setText(mDescriptor.getDetail(mMAIndicator, entryData, false, true));
                        setCandleChartData();
                    } else {
                        isInit = false;
                        switchIndicator(mShowIndicators[6]);
                        setFirstBOLLChartData();

                        HashMap<String, Object> entryData = mKChartData.getEntryData(mKChartData.getEntryCount() - 1);
                        tv_first_chart.setText(mDescriptor.getDetail(mMAIndicator, entryData, false, true));

                        setTextOnUnHighlight();
                    }
                } else {
                    //点击chart2，切换到下一个item
                    Indicator.Type type = mSelIndicator.getType();
                    int index = -1;
                    for (int i = 0; i < mShowIndicators.length; i++) {
                        if (type == mShowIndicators[i]) {
                            index = i;
                            break;
                        }
                    }
                    if (index == -1) return;
                    index = (index + 1) % mShowIndicators.length;
                    switchIndicator(mShowIndicators[index]);
                    setSecondChartData();
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

    private void setTextOnUnHighlight() {
        if (isValueSelected)
            return;

        HashMap<String, Object> entryData = mKChartData.getEntryData(mKChartData.getEntryCount() - 1);
        tv_first_chart.setText(mDescriptor.getDetail(mMAIndicator, entryData, false, true));
        tv_second_chart.setText(mDescriptor.getDetail(mSelIndicator, entryData, false, true));
    }

    private void setVisibleEntry(float visibleCount, float xIndex) {
        mFirstChart.setVisibleXRange(mMinXRange, mMaxXRange);
        mSecondChart.setVisibleXRange(mMinXRange, mMaxXRange);
        float originalScaleX = mFirstChart.getScaleX();

        float scaleX = mKChartData.getEntryCount() / visibleCount;
        mFirstChart.zoom((1 / originalScaleX) * scaleX, 1f, 1, 1);
        mSecondChart.zoom((1 / originalScaleX) * scaleX, 1f, 1, 1);

        mFirstChart.moveViewToXSync(xIndex / (1 / originalScaleX) * scaleX);
        mSecondChart.moveViewToXSync(xIndex / (1 / originalScaleX) * scaleX);
    }

    private void setVisibleMoreEntry(float visibleCount, float xIndex) {
        mFirstChart.setVisibleXRange(mMinXRange, mMaxXRange);
        mSecondChart.setVisibleXRange(mMinXRange, mMaxXRange);
        float originalScaleX = mFirstChart.getScaleX();

        float scaleX = mKChartData.getEntryCount() / visibleCount;
        mFirstChart.zoom((1 / originalScaleX) * scaleX, 1f, 1, 1);
        mSecondChart.zoom((1 / originalScaleX) * scaleX, 1f, 1, 1);

        mFirstChart.moveViewToXSync(xIndex);
        mSecondChart.moveViewToXSync(xIndex);
    }

    private void clearChartData() {
        mFirstChart.clear();
        mSecondChart.clear();
        tv_first_chart.setText("");
        tv_second_chart.setText("");
    }

    //Candle(Main) Chart
    private void setCandleChartData() {
        //mFirstChart.resetTracking();

        // Candle
        CandleDataSet candleDataSet = new CandleDataSet(mKChartData.getCandleEntry(), "Candle");
        mKConfig.setCommonCandleDateSet(candleDataSet);
        mKConfig.setHighlightEnabled(candleDataSet, true);//让Candle显示HighLight
        candleDataSet.setColors(mKChartData.getColorList());

        CandleData candleData = new CandleData(mKChartData.getXVals(), candleDataSet);
        candleData.setValueFormatter(mKConfig.getValueFormatter());//new MyValueFormatter(2)

        // MA Lines
        ArrayList<LineDataSet> lineDateSet = new ArrayList<LineDataSet>();
        String lineKeys[] = mMAIndicator.getKeys();
        List<ArrayList> lists = mKChartData.getEntryList(mMAIndicator);
        for (int i = 0; i < lineKeys.length; i++) {
            LineDataSet MAset = new LineDataSet(lists.get(i), lineKeys[i]);
            mKConfig.setCommonLineDataSet(MAset);
            MAset.setColor(Config.LineColorGroup[i]);
            lineDateSet.add(MAset);
        }
        LineData lineData = new LineData(mKChartData.getXVals(), lineDateSet);

        CombinedData combinedData = new CombinedData(mKChartData.getXVals());
        combinedData.setData(candleData);
        combinedData.setData(lineData);

        mFirstChart.setData(combinedData);
        mFirstChart.invalidate();
    }

    //MACD Chart
    private void setMACDChartData() {
        String lineKeys[] = mSelIndicator.getKeys();
        List<ArrayList> lists = mKChartData.getEntryList(mSelIndicator);

        //Line: DIFF DEA
        ArrayList<LineDataSet> lineDateSet = new ArrayList<LineDataSet>();
        int i;
        for (i = 0; i < lineKeys.length - 1; i++) {
            LineDataSet MAset = new LineDataSet(lists.get(i), lineKeys[i]);
            mKConfig.setCommonLineDataSet(MAset);
            MAset.setColor(Config.LineColorGroup[i]);
            lineDateSet.add(MAset);
        }

        LineData lineData = new LineData(mKChartData.getXVals(), lineDateSet);

        //bar: STICK
        BarDataSet barSet = new BarDataSet(lists.get(i), lineKeys[i]);
        mKConfig.setCommonBarDateSet(barSet);
        mKConfig.setHighlightEnabled(barSet, true);//让MACD Bar显示HighLight
        ArrayList<BarEntry> stickList = lists.get(i);
        int[] color = new int[stickList.size()];
        for (int j = 0; j < stickList.size(); j++) {
            if (stickList.get(j).getVal() >= 0)
                color[j] = Config.Increasing_Color;
            else
                color[j] = Config.Decreasing_Color;
        }
        barSet.setColors(color);

        BarData barData = new BarData(mKChartData.getXVals(), barSet);

        CombinedData combinedData = new CombinedData(mKChartData.getXVals());
        combinedData.setData(barData);
        combinedData.setData(lineData);

        mSecondChart.setData(combinedData);
        mKConfig.setStartAtZero(false);
        mSecondChart.invalidate();
    }

    //BOLL Chart
    private void setBOLLChartData() {
        String lineKeys[] = mSelIndicator.getKeys();
        List<ArrayList> lists = mKChartData.getEntryList(mSelIndicator);

        //Lines
        ArrayList<LineDataSet> lineDateSet = new ArrayList<LineDataSet>();
        for (int i = 0; i < lineKeys.length; i++) {
            LineDataSet set = new LineDataSet(lists.get(i), lineKeys[i]);
            mKConfig.setCommonLineDataSet(set);
            set.setColor(Config.LineColorGroup[i]);
            lineDateSet.add(set);
        }
        LineData lineData = new LineData(mKChartData.getXVals(), lineDateSet);

        // Candle
        CandleDataSet candleDataSet = new CandleDataSet(mKChartData.getCandleEntry(), "Candle");
        mKConfig.setBollCandleDateSet(candleDataSet);
        mKConfig.setHighlightEnabled(candleDataSet, true);//让Candle显示HighLight
        candleDataSet.setColors(mKChartData.getColorList());

        CandleData candleData = new CandleData(mKChartData.getXVals(), candleDataSet);
        candleData.setValueFormatter(mKConfig.getValueFormatter());//new MyValueFormatter(2)

        CombinedData combinedData = new CombinedData(mKChartData.getXVals());
        combinedData.setData(candleData);
        combinedData.setData(lineData);

        mSecondChart.setData(combinedData);
        mKConfig.setStartAtZero(false);
        mSecondChart.invalidate();
    }

    //BOLL Chart
    private void setFirstBOLLChartData() {
        String lineKeys[] = mSelIndicator.getKeys();
        List<ArrayList> lists = mKChartData.getEntryList(mSelIndicator);

        //Lines
        ArrayList<LineDataSet> lineDateSet = new ArrayList<LineDataSet>();
        for (int i = 0; i < lineKeys.length; i++) {
            LineDataSet set = new LineDataSet(lists.get(i), lineKeys[i]);
            mKConfig.setCommonLineDataSet(set);
            set.setColor(Config.LineColorGroup[i]);
            lineDateSet.add(set);
        }
        LineData lineData = new LineData(mKChartData.getXVals(), lineDateSet);

        // Candle
        CandleDataSet candleDataSet = new CandleDataSet(mKChartData.getCandleEntry(), "Candle");
        mKConfig.setBollCandleDateSet(candleDataSet);
        mKConfig.setHighlightEnabled(candleDataSet, true);//让Candle显示HighLight
        candleDataSet.setColors(mKChartData.getColorList());

        CandleData candleData = new CandleData(mKChartData.getXVals(), candleDataSet);
        candleData.setValueFormatter(mKConfig.getValueFormatter());//new MyValueFormatter(2)

        CombinedData combinedData = new CombinedData(mKChartData.getXVals());
        combinedData.setData(candleData);
        combinedData.setData(lineData);

        mFirstChart.setData(combinedData);
        mKConfig.setStartAtZero(false);
        mFirstChart.invalidate();
    }

    //VOL Chart
    private void setVOLChartData() {
        String lineKeys[] = mSelIndicator.getKeys();
        List<ArrayList> lists = mKChartData.getEntryList(mSelIndicator);

        //Line: MA5, MA10
        ArrayList<LineDataSet> lineDateSet = new ArrayList<LineDataSet>();
        int i;
        for (i = 1; i < lineKeys.length; i++) {
            LineDataSet MAset = new LineDataSet(lists.get(i), lineKeys[i]);
            mKConfig.setCommonLineDataSet(MAset);
            MAset.setColor(Config.LineColorGroup[i]);
            lineDateSet.add(MAset);
        }

        LineData lineData = new LineData(mKChartData.getXVals(), lineDateSet);

        //bar: VOL
        BarDataSet barSet = new BarDataSet(lists.get(0), lineKeys[0]);
        mKConfig.setVOLBarDateSet(barSet);
        mKConfig.setHighlightEnabled(barSet, true);//让Bar显示HighLight
        barSet.setColors(mKChartData.getColorList());

        BarData barData = new BarData(mKChartData.getXVals(), barSet);

        CombinedData combinedData = new CombinedData(mKChartData.getXVals());
        combinedData.setData(barData);
        combinedData.setData(lineData);

        mSecondChart.setDrawValueAboveBar(true);
        mKConfig.setStartAtZero(true);
        mSecondChart.setData(combinedData);
        mSecondChart.invalidate();
    }

    //Other Chart
    private void setOtherChartData() {
        String lineKeys[] = mSelIndicator.getKeys();
        List<ArrayList> lists = mKChartData.getEntryList(mSelIndicator);

        //Lines
        ArrayList<LineDataSet> lineDateSet = new ArrayList<LineDataSet>();
        for (int i = 0; i < lineKeys.length; i++) {
            LineDataSet set = new LineDataSet(lists.get(i), lineKeys[i]);
            mKConfig.setCommonLineDataSet(set);
            set.setColor(Config.LineColorGroup[i]);
            lineDateSet.add(set);
        }
        LineData lineData = new LineData(mKChartData.getXVals(), lineDateSet);

        //Empty BarChart:为了解决只有Line的时候，上下表不对其的问题，从而添加一个不可见的BarLine，这是一种权宜的修改办法
        ArrayList<BarEntry> emptyList = new ArrayList<BarEntry>();
        for (int i = 0; i < lists.get(0).size(); i++) {
            Entry entry = (Entry) lists.get(0).get(i);
            BarEntry barEntry = new BarEntry(entry.getVal(), entry.getXIndex());
            emptyList.add(barEntry);
        }
        if (emptyList.size() > 0) {
            Entry firstEntry = emptyList.get(0);
            for (int i = 0; i < firstEntry.getXIndex(); i++) {
                emptyList.add(i, new BarEntry(firstEntry.getVal(), i));
            }
        }

        BarDataSet emptyBarSet = new BarDataSet(emptyList, "EmptyBar");
        mKConfig.setCommonBarDateSet(emptyBarSet);
        emptyBarSet.setBarSpacePercent(100f);//此BarLine不可见
        mKConfig.setHighlightEnabled(emptyBarSet, true);//让EmptyBar来显示HighLight，可以解决当数据为空的Entry，同样也能显示HighLight的问题
        BarData emptyBarData = new BarData(mKChartData.getXVals(), emptyBarSet);

        CombinedData combinedData = new CombinedData(mKChartData.getXVals());
        combinedData.setData(emptyBarData);//emptyBarData 必须先set，否则会影响到HighLight的显示
        combinedData.setData(lineData);

        mSecondChart.setData(combinedData);
        mKConfig.setStartAtZero(false);
        mSecondChart.invalidate();
    }

    //Second Chart
    private void setSecondChartData() {
        if (mSelIndicator.getType() == Indicator.Type.MACD) {
            setMACDChartData();
        } else if (mSelIndicator.getType() == Indicator.Type.BOLL) {
            setBOLLChartData();
        } else if (mSelIndicator.getType() == Indicator.Type.VOL) {
            setVOLChartData();
        } else {
            setOtherChartData();
        }

        String title = mDescriptor.getTitle(mSelIndicator);
        tv_second_chart_title.setVisibility(View.VISIBLE);
        tv_second_chart_title.setText(title);
        setTextOnUnHighlight();
    }
}
