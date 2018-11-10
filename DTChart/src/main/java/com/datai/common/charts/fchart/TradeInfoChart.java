package com.datai.common.charts.fchart;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.datai.common.R;

/**
 * Created by XuJun on 2016/5/4.
 */
public class TradeInfoChart extends LinearLayout {

    private FChart mFChart_buy;
    private FChart mFChart_sell;
    private FChart mFChart_tick;
    private LinearLayout mLayout_fields;
    private RadioGroup mRadioGroup;
    private RadioButton mRadioButton_fields;
    private RadioButton mRadioButton_detail;

    private DoRadioButtonFunction mDoRadioButtonFunction;

    public TradeInfoChart(Context context) {
        super(context);
    }

    public TradeInfoChart(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupLayoutResource(R.layout.view_tradeinfochart);

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
        mFChart_buy = (FChart) findViewById(R.id.fchart_buy);
        mFChart_sell = (FChart) findViewById(R.id.fchart_sell);
        mFChart_tick = (FChart) findViewById(R.id.fchart_tick);
        mLayout_fields = (LinearLayout) findViewById(R.id.layout_fields);
        mRadioGroup = (RadioGroup) findViewById(R.id.chart_group);
        mRadioButton_fields = (RadioButton) findViewById(R.id.radiobutton_fields);
        mRadioButton_detail = (RadioButton) findViewById(R.id.radiobutton_detail);
    }

    private void setListener() {
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
            }
        });

       /* mRadioButton_fields.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mLayout_fields.setVisibility(View.VISIBLE);
                mFChart_tick.setVisibility(View.GONE);
                mRadioButton_fields.setChecked(true);
            }
        });

        mRadioButton_detail.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mLayout_fields.setVisibility(View.GONE);
                mFChart_tick.setVisibility(View.VISIBLE);
                mRadioButton_detail.setChecked(true);
            }
        });*/
    }

    public FChart getFChart(int type) {
        if (type == FData.TYPE_BUY) {
            return mFChart_buy;
        } else if (type == FData.TYPE_SELL) {
            return mFChart_sell;
        } else if (type == FData.TYPE_TICK) {
            return mFChart_tick;
        }

        return mFChart_buy;
    }

    public void setDoRadioButtonFunction(DoRadioButtonFunction function) {
        mDoRadioButtonFunction = function;

        mRadioButton_fields.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mLayout_fields.setVisibility(View.VISIBLE);
                mFChart_tick.setVisibility(View.GONE);
                mRadioButton_fields.setChecked(true);
                mDoRadioButtonFunction.Request();
            }
        });

        mRadioButton_detail.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mLayout_fields.setVisibility(View.GONE);
                mFChart_tick.setVisibility(View.VISIBLE);
                mRadioButton_detail.setChecked(true);
                mDoRadioButtonFunction.Request();
            }
        });
    }

    public interface DoRadioButtonFunction {
         void Request();
    }
}
