package com.datai.common.charts.chart;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.datai.common.R;
import com.datai.common.charts.common.Config;
import com.datai.common.charts.common.Descriptor;
import com.datai.common.charts.kchart.KChart;
import com.datai.common.charts.kchart.KData;
import com.datai.common.charts.tchart.TChart;
import com.datai.common.view.popup.ActionItem;
import com.datai.common.view.popup.QuickAction;

/**
 * Created by XuJun on 2016/1/28.
 */
public class Chart extends LinearLayout {
    public static final String TCHART_MINUTE = "分钟";

    private Context mContext;
    private TChart mTChart;
    private KChart mKChart;
    private RadioGroup mRadioGroup;
    private RadioButton mRadioButton_time;
    private RadioButton mRadioButton_day;
    private RadioButton mRadioButton_week;
    private RadioButton mRadioButton_month;
    private RadioButton mRadioButton_minute;
    private TextView mTime;
    private TextView mDate;
    private TextView mOpen;
    private TextView mClose;
    private TextView mHigh;
    private TextView mLow;
    private TextView mRise_value;
    private TextView mRise_percent;

    private Descriptor mDescriptor;

    private QuickAction mPopupIndicators;

    private OnChartListener mChartListener;

    private boolean mShowTChart = true;

    public Chart(Context context) {
        super(context);
    }

    public Chart(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        setupLayoutResource(R.layout.view_chart);
        init();
        setListener();
    }

    private void setupLayoutResource(int layoutResource) {
        View inflater = LayoutInflater.from(getContext()).inflate(layoutResource, this);

        inflater.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        inflater.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        inflater.layout(0, 0, inflater.getMeasuredWidth(), inflater.getMeasuredHeight());
    }

    private void init() {
        mTChart = findViewById(R.id.view_chart_tchart);
        mKChart = findViewById(R.id.view_chart_kchart);

        mRadioGroup = findViewById(R.id.chart_group);
        mRadioButton_time = findViewById(R.id.chart_time);
        mRadioButton_day = findViewById(R.id.chart_day);
        mRadioButton_week = findViewById(R.id.chart_week);
        mRadioButton_month = findViewById(R.id.chart_month);
        mRadioButton_minute = findViewById(R.id.chart_minute);
        mTime = findViewById(R.id.chart_textitem_time);
        mDate = findViewById(R.id.chart_textitem_date);
        mOpen = findViewById(R.id.chart_textitem_open);
        mClose = findViewById(R.id.chart_textitem_close);
        mHigh = findViewById(R.id.chart_textitem_high);
        mLow = findViewById(R.id.chart_textitem_low);
        mRise_value = findViewById(R.id.chart_textitem_rise_value);
        mRise_percent = findViewById(R.id.chart_textitem_rise_percent);

        mDescriptor = new Descriptor();

//        mKChart.setOnKChartSelectedListener(new OnKChartSelectedListener() {
//            @Override
//            public void onValueSelected(HashMap<String, Object> entry, int index, float prev) {
//                findViewById(R.id.chart_layout).setVisibility(View.GONE);
//                findViewById(R.id.chart_text_layout).setVisibility(View.VISIBLE);
//
//                if (entry != null) {
//                    mTime.setText(mDescriptor.setTime((long) entry.get(Indicator.K_TIME), mKChart.getUnit()));
//                    mDate.setText(mDescriptor.setDate((long) entry.get(Indicator.K_TIME), mKChart.getUnit()));
//                    mOpen.setText(mDescriptor.setOpen((float) entry.get(Indicator.K_OPEN), mKChart.getDataType(index, (float) entry.get(Indicator.K_OPEN), prev)));
//                    mClose.setText(mDescriptor.setClose((float) entry.get(Indicator.K_CLOSE), mKChart.getDataType(index, (float) entry.get(Indicator.K_CLOSE), prev)));
//                    mHigh.setText(mDescriptor.setHigh((float) entry.get(Indicator.K_HIGH), mKChart.getDataType(index, (float) entry.get(Indicator.K_HIGH), prev)));
//                    mLow.setText(mDescriptor.setLow((float) entry.get(Indicator.K_LOW), mKChart.getDataType(index, (float) entry.get(Indicator.K_LOW), prev)));
//
//                    mRise_value.setText(mDescriptor.setRiseValue((float) entry.get(RISE.RISE_VALUE)));
//                    mRise_percent.setText(mDescriptor.setRisePercent((float) entry.get(RISE.RISE_PERCENT)));
//                }
//            }
//
//            @Override
//            public void onNothingSelected() {
//                findViewById(R.id.chart_layout).setVisibility(View.VISIBLE);
//                findViewById(R.id.chart_text_layout).setVisibility(View.GONE);
//            }
//        });

        initPopup();
    }

    private void setListener() {
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
            }
        });

        mRadioButton_time.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setShowTChart(true);
            }
        });

        mRadioButton_day.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setShowKChart(KData.Unit.DAY);
            }
        });

        mRadioButton_week.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setShowKChart(KData.Unit.WEEK);
            }
        });

        mRadioButton_month.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setShowKChart(KData.Unit.MONTH);
            }
        });

        mRadioButton_minute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRadioButton_minute.setChecked(true);
                mPopupIndicators.show(v);
            }
        });
    }

    private void initPopup() {
        if (Config.MinuteType == null)
            return;

        mPopupIndicators = new QuickAction(mContext, QuickAction.VERTICAL);

        mPopupIndicators.setActionItemTitleSize(12);

        for (int i = 0; i < Config.MinuteType.length; i++) {
            String title = Config.MinuteType[i].getCHDescribe();
            ActionItem item = new ActionItem(i, title, null);
            mPopupIndicators.addActionItem(item);
        }

        mPopupIndicators.setOnDismissListener(new QuickAction.OnDismissListener() {
            @Override
            public void onDismiss() {
                setRadioButtonEnable();
            }
        });

        mPopupIndicators
                .setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {
                    @Override
                    public void onItemClick(QuickAction source, int pos,
                                            int actionId) {
                        setShowKChart(Config.MinuteType[pos]);
                    }
                });
    }

    public KChart getKChart() {
        return mKChart;
    }

    public TChart getTChart() {
        return mTChart;
    }

    public void setOnChartListener(OnChartListener listener) {
        mChartListener = listener;
    }

    public void setOnLandscapeListener(OnClickListener listener) {
        mTChart.setOnLandscapeListener(listener);
        mKChart.setOnLandscapeListener(listener);
    }

    public void setLandscapeButtonVisible(boolean visible) {
        mTChart.setLandscapeButtonVisible(visible);
        mKChart.setLandscapeButtonVisible(visible);
    }

    private void setRadioButtonEnable() {
        if (mShowTChart) {
            mRadioButton_time.setChecked(true);
            mRadioButton_minute.setText(TCHART_MINUTE);
        } else {
            KData.Unit unit = mKChart.getUnit();
            switch (unit) {
                case DAY:
                    mRadioButton_day.setChecked(true);
                    mRadioButton_minute.setText(TCHART_MINUTE);
                    break;
                case WEEK:
                    mRadioButton_week.setChecked(true);
                    mRadioButton_minute.setText(TCHART_MINUTE);
                    break;
                case MONTH:
                    mRadioButton_month.setChecked(true);
                    mRadioButton_minute.setText(TCHART_MINUTE);
                    break;
                default:
                    mRadioButton_minute.setChecked(true);
                    mRadioButton_minute.setText(unit.getCHDescribe());
                    break;
            }
        }
    }

    public KData.Unit getChartUnit() {
        if (mShowTChart)
            return KData.Unit.TIME;
        else
            return mKChart.getUnit();
    }

    public void setChartUnit(KData.Unit unit) {
        if (unit == KData.Unit.TIME)
            setShowTChart(true);
        else
            setShowKChart(unit);
    }

    private void setShowKChart(KData.Unit unit) {
        mKChart.setUnit(unit);
        setShowTChart(false);
    }

    private void setShowTChart(boolean bEnable) {
        mShowTChart = bEnable;
        mTChart.setVisibility(bEnable ? View.VISIBLE : View.GONE);
        mKChart.setVisibility(bEnable ? View.GONE : View.VISIBLE);
        setRadioButtonEnable();

        notifySwitchUnit();
    }

    public void notifySwitchUnit() {
        if (mChartListener != null) {
            if (mShowTChart) {
                mChartListener.onSwitchUnit(mShowTChart, null);
            } else {
                mChartListener.onSwitchUnit(mShowTChart, mKChart.getUnit());
            }
        }
    }

    public boolean isShowTChart() {
        return mShowTChart;
    }

    public void setPriceFormatDigit(int digits) {
        mTChart.setPriceFormatDigit(digits);//用于显示价格
        mKChart.setPriceFormatDigit(digits);//用于显示价格
    }
}
