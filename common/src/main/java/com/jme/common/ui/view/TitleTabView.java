package com.jme.common.ui.view;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jme.common.R;


/**
 * Created by Yanmin on 2016/3/23.
 */
public class TitleTabView extends LinearLayout implements View.OnClickListener {

    // 上一次选中的index
    private int previousIndex = -1;

    public interface OnTabClickListener {
        public void OnTabClick(int index);
    }

    private Context mContext;

    private TextView tv_tab_0;
    private TextView tv_tab_1;
    private TextView tv_tab_2;
    private View line_1;
    private View line_2;
    private TextView[] tv_tabs = new TextView[3];

    private OnTabClickListener mListener;

    public TitleTabView(Context context) {
        super(context);
        mContext = context;
        initView();
    }

    public TitleTabView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView();
    }

    private void initView() {
        setupLayoutResource(R.layout.layout_title_tabview);

        tv_tab_0 = (TextView) findViewById(R.id.tv_tab_0);
        tv_tab_1 = (TextView) findViewById(R.id.tv_tab_1);
        tv_tab_2 = (TextView) findViewById(R.id.tv_tab_2);
        line_1 = (View) findViewById(R.id.line_1);
        line_2 = (View) findViewById(R.id.line_2);

        tv_tabs[0] = tv_tab_0;
        tv_tabs[1] = tv_tab_1;
        tv_tabs[2] = tv_tab_2;

        for (int i = 0; i < tv_tabs.length; i++) {
            tv_tabs[i].setTag(i);
            tv_tabs[i].setOnClickListener(this);
        }
    }

    private void setupLayoutResource(int layoutResource) {
        View inflater = LayoutInflater.from(getContext()).inflate(layoutResource, this);

        inflater.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        inflater.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        inflater.layout(0, 0, inflater.getMeasuredWidth(), inflater.getMeasuredHeight());
    }

    public void init(String[] item, int defaultIndex, OnTabClickListener listener) {
        if (item == null)
            return;

        if (item.length >= 2) {
            tv_tab_0.setText(item[0]);
            tv_tab_1.setText(item[1]);
            if (item.length >= 3) {
                tv_tab_2.setText(item[2]);
            } else {
                tv_tab_2.setVisibility(View.GONE);
                line_2.setVisibility(View.GONE);
            }
        }
        mListener = listener;
        switchTab(defaultIndex);
    }

    public void switchTab(int index) {
        for (int i = 0; i < tv_tabs.length; i++) {
            tv_tabs[i].setTextColor(ContextCompat.getColor(mContext, R.color.common_font_title_unchecked));
            tv_tabs[i].setTextSize(18);
        }

        tv_tabs[index].setTextColor(ContextCompat.getColor(mContext, R.color.common_font_title));
        tv_tabs[index].setTextSize(20);

        if (mListener != null && previousIndex != index)
            mListener.OnTabClick(index);
        previousIndex = index;
    }

    @Override
    public void onClick(View v) {
        switchTab((int) v.getTag());
    }


    public void upDateTitleText(int index, String text){
        if (index < 0 || index > tv_tabs.length - 1)
            return;

        if (TextUtils.isEmpty(text))
            return;

        tv_tabs[index].setText(text);

    }
}
