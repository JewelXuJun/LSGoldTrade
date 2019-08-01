package com.datai.common.charts.chart;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.datai.common.R;
import com.datai.common.charts.kchart.KChart;
import com.datai.common.charts.kchart.KData;
import com.datai.common.charts.tchart.TChart;
import com.datai.common.view.popup.ActionItem;
import com.datai.common.view.popup.QuickAction;

/**
 * Created by XuJun on 2016/1/28.
 */
public class Chart extends LinearLayout {
    public static final String TCHART_ELSE = "其他";

    private Context mContext;
    private TChart mTChart;
    private KChart mKChart;
    private RadioGroup mRadioGroup;
    private RadioButton mRadioButton_time;
    private RadioButton mRadioButton_day;
    private RadioButton mRadioButton_week;
    private RadioButton mRadioButton_month;
    private RadioButton mRadioButton_minute;

    private QuickAction mPopupIndicators;

    private OnChartListener mChartListener;

    public KData.Unit[] mChartType;

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
    }

    public void initChartSort(String chartSortValue) {
        if (TextUtils.isEmpty(chartSortValue)) {
            mChartType = new KData.Unit[]{KData.Unit.DAY, KData.Unit.WEEK, KData.Unit.MONTH, KData.Unit.MIN1, KData.Unit.MIN5,
                    KData.Unit.MIN15, KData.Unit.MIN30, KData.Unit.MIN60, KData.Unit.MIN240, KData.Unit.EDITSORT};
        } else {
            if (chartSortValue.contains("分时"))
                chartSortValue = chartSortValue.replace("分时,", "");

            String[] chartSortValueArray = chartSortValue.split(",");

            if (chartSortValueArray == null || chartSortValueArray.length == 0) {
                mChartType = new KData.Unit[]{KData.Unit.DAY, KData.Unit.WEEK, KData.Unit.MONTH, KData.Unit.MIN1, KData.Unit.MIN5,
                        KData.Unit.MIN15, KData.Unit.MIN30, KData.Unit.MIN60, KData.Unit.MIN240, KData.Unit.EDITSORT};
            } else {
                mChartType = new KData.Unit[chartSortValueArray.length + 1];

                for (int i = 0; i < chartSortValueArray.length; i++) {
                    String value = chartSortValueArray[i];

                    if (value.equals("日线"))
                        mChartType[i] = KData.Unit.DAY;
                    else if (value.equals("周线"))
                        mChartType[i] = KData.Unit.WEEK;
                    else if (value.equals("月线"))
                        mChartType[i] = KData.Unit.MONTH;
                    else if (value.equals("1分钟"))
                        mChartType[i] = KData.Unit.MIN1;
                    else if (value.equals("5分钟"))
                        mChartType[i] = KData.Unit.MIN5;
                    else if (value.equals("15分钟"))
                        mChartType[i] = KData.Unit.MIN15;
                    else if (value.equals("30分钟"))
                        mChartType[i] = KData.Unit.MIN30;
                    else if (value.equals("1小时"))
                        mChartType[i] = KData.Unit.MIN60;
                    else if (value.equals("4小时"))
                        mChartType[i] = KData.Unit.MIN240;
                }

                mChartType[chartSortValueArray.length] = KData.Unit.EDITSORT;
            }
        }

        setChartUnit(KData.Unit.TIME);

        mRadioButton_day.setText(mChartType[0].getCHDescribe());
        mRadioButton_week.setText(mChartType[1].getCHDescribe());
        mRadioButton_month.setText(mChartType[2].getCHDescribe());

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
                setShowKChart(mChartType[0]);
            }
        });

        mRadioButton_week.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setShowKChart(mChartType[1]);
            }
        });

        mRadioButton_month.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setShowKChart(mChartType[2]);
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
        mPopupIndicators = new QuickAction(mContext, QuickAction.VERTICAL);
        mPopupIndicators.setActionItemTitleSize(12);

        for (int i = 3; i < mChartType.length; i++) {
            String title = mChartType[i].getCHDescribe();
            ActionItem item = new ActionItem(i, title, null);

            mPopupIndicators.addActionItem(item);
        }

        mPopupIndicators.setOnDismissListener(new QuickAction.OnDismissListener() {
            @Override
            public void onDismiss() {
                setRadioButtonEnable();
            }
        });

        mPopupIndicators.setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {
            @Override
            public void onItemClick(QuickAction source, int position, int actionId) {
                int index = position + 3;

                if (index >= mChartType.length)
                    return;

                if (index == mChartType.length - 1) {
                    if (null != mChartListener)
                        mChartListener.onEditSort();
                } else {
                    setShowKChart(mChartType[index]);
                }
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

    public void setUnit(String unitCode) {
        if (TextUtils.isEmpty(unitCode))
            return;

        switch (unitCode) {
            case "time1":
                setChartUnit(KData.Unit.TIME);

                break;
            case "dayK":
                setChartUnit(KData.Unit.DAY);

                break;
            case "weekK":
                setChartUnit(KData.Unit.WEEK);

                break;
            case "monthK":
                setChartUnit(KData.Unit.MONTH);

                break;
            case "1minK":
                setChartUnit(KData.Unit.MIN1);

                break;
            case "5minK":
                setChartUnit(KData.Unit.MIN5);

                break;
            case "15minK":
                setChartUnit(KData.Unit.MIN15);

                break;
            case "30minK":
                setChartUnit(KData.Unit.MIN30);

                break;
            case "60minK":
                setChartUnit(KData.Unit.MIN60);

                break;
            case "240minK":
                setChartUnit(KData.Unit.MIN240);

                break;
            default:
                setChartUnit(KData.Unit.TIME);

                break;
        }
    }

    private void setRadioButtonEnable() {
        if (mShowTChart) {
            mRadioButton_time.setChecked(true);
            mRadioButton_minute.setText(TCHART_ELSE);
        } else {
            KData.Unit unit = mKChart.getUnit();
            int position = -1;

            for (int i = 0; i < mChartType.length; i++) {
                if (mChartType[i].getCHDescribe().equals(unit.getCHDescribe()))
                    position = i;
            }

            switch (position) {
                case 0:
                    mRadioButton_day.setChecked(true);
                    mRadioButton_minute.setText(TCHART_ELSE);

                    break;
                case 1:
                    mRadioButton_week.setChecked(true);
                    mRadioButton_minute.setText(TCHART_ELSE);

                    break;
                case 2:
                    mRadioButton_month.setChecked(true);
                    mRadioButton_minute.setText(TCHART_ELSE);

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
        mTChart.setPriceFormatDigit(digits);
        mKChart.setPriceFormatDigit(digits);
    }
}
