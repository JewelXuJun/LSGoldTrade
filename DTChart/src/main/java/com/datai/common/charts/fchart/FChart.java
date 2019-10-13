package com.datai.common.charts.fchart;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.datai.common.R;
import com.datai.common.charts.common.Descriptor;

import java.math.BigDecimal;
import java.util.List;

public class FChart extends LinearLayout implements AdapterView.OnItemClickListener {

    private Context mContext;

    private ListView mListView;
    private FChartAdapter mFChartAdapter;
    private Descriptor mDescriptor;
    private OnPriceClickListener mClickListener;

    public FChart(Context context) {
        super(context);
    }

    public FChart(Context context, AttributeSet attrs) {
        super(context, attrs);

        setupLayoutResource(R.layout.view_fchart);

        mContext = context;

        init();
    }

    private void setupLayoutResource(int layoutResource) {
        View inflater = LayoutInflater.from(getContext()).inflate(layoutResource, this);
        inflater.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        inflater.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        inflater.layout(0, 0, inflater.getMeasuredWidth(), inflater.getMeasuredHeight());
    }

    private void init() {
        mDescriptor = new Descriptor();

        mListView = findViewById(R.id.listview);

        mFChartAdapter = new FChartAdapter(mContext, null);
        mListView.setAdapter(mFChartAdapter);
    }

    public void setSize(int bigSize, int smallSize) {
        mFChartAdapter.setSize(bigSize, smallSize);
    }

    public void setData(List<String[]> list, int type, String preClose) {
        mFChartAdapter.setType(type);
        mFChartAdapter.setPreClose(preClose);
        mFChartAdapter.addAll(list);
        mFChartAdapter.notifyDataSetChanged();

        if (type != FData.TYPE_BUY && type != FData.TYPE_SELL)
            mListView.setEnabled(false);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (mFChartAdapter.getType() == FData.TYPE_BUY || mFChartAdapter.getType() == FData.TYPE_SELL) {
            String[] data = (String[]) mFChartAdapter.getItem(position);
            if (data != null && !TextUtils.isEmpty(data[1]) && !data[1].equals("   - -   ") && new BigDecimal(data[1]).compareTo(new BigDecimal("0")) != 0)
                mClickListener.OnPriceClick("" + mDescriptor.setPrice(data[1], FData.STABLE), data[0]);
        }
    }

    public void setOnPriceClickListener(OnPriceClickListener onPriceClickListener) {
        mClickListener = onPriceClickListener;
        mListView.setOnItemClickListener(this);
    }

    public interface OnPriceClickListener {
        void OnPriceClick(String price, String title);
    }
}
