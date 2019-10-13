package com.datai.common.charts.fchart;

import android.content.Context;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.datai.common.R;
import com.datai.common.charts.common.Descriptor;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by XuJun on 2016/4/14.
 */
public class FChartAdapter extends BaseAdapter {

    private List<String[]> mList;

    private String mPreClose;

    private Context mContext;
    private Descriptor mDescriptor;
    private LayoutInflater inflater;
    private FData mFData;

    private int mType = FData.TYPE_BUY;
    private int mBigSize = 9;
    private int mSmallSize = 8;

    private String[] mSell = {"卖10", "卖9", "卖8", "卖7", "卖6", "卖5", "卖4", "卖3", "卖2", "卖1"};
    private String[] mBuy = {"买1", "买2", "买3", "买4", "买5", "买6", "买7", "买8", "买9", "买10"};

    public FChartAdapter(Context context, List<String[]> list) {
        mContext = context;
        mList = list;

        mDescriptor = new Descriptor();
        mFData = new FData();
        inflater = LayoutInflater.from(mContext);
    }

    public void setType(int type) {
        mType = type;
    }

    public int getType() {
        return mType;
    }

    public void setPreClose(String preClose) {
        mPreClose = preClose;
    }

    public void addAll(List<String[]> list) {
        if (mList != null) {
            mList.clear();
        }
        mList = list;

        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return null == mList ? 10 : mList.size();
    }

    @Override
    public Object getItem(int position) {
        if (mList == null)
            return 0;

        if (position < mList.size())
            return mList.get(position);
        else
            return new String[3];
    }

    public void setSize(int bigSize, int smallSize) {
        mBigSize = bigSize;
        mSmallSize = smallSize;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();

            convertView = inflater.inflate(R.layout.item_fchart, null);

            holder.tv_item_first = convertView.findViewById(R.id.tv_item_first);
            holder.tv_item_second = convertView.findViewById(R.id.tv_item_second);
            holder.tv_item_third = convertView.findViewById(R.id.tv_item_third);

            holder.tv_item_first.setTextSize(TypedValue.COMPLEX_UNIT_SP, mSmallSize);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        AbsListView.LayoutParams params = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, parent.getHeight() / getCount());
        convertView.setLayoutParams(params);

        if (mList != null && mList.size() > 0) {
            if (position < mList.size()) {
                String[] str = mList.get(position);

                if (str != null && str.length != 0) {
                    String first = str[0];
                    String second = str[1];
                    String third = str[2];

                    if (!TextUtils.isEmpty(first)) {
                        if (mType == FData.TYPE_SELL || mType == FData.TYPE_BUY) {
                            holder.tv_item_first.setText(first);
                        } else if (mType == FData.TYPE_TICK || mType == FData.TYPE_TICK_PART1 || mType == FData.TYPE_TICK_PART2) {
                            holder.tv_item_first.setText(mDescriptor.getTime(Long.parseLong(first)));
                        }
                    }

                    if (!TextUtils.isEmpty(second) && !TextUtils.isEmpty(mPreClose)) {
                        holder.tv_item_second.setText(mDescriptor.setPrice(second, mFData.getType(Float.parseFloat(second),
                                Float.parseFloat(mPreClose))));

                        if (new BigDecimal(second).compareTo(new BigDecimal(1000)) == -1) {
                            holder.tv_item_second.setTextSize(TypedValue.COMPLEX_UNIT_SP, mBigSize);
                        } else {
                            holder.tv_item_second.setTextSize(TypedValue.COMPLEX_UNIT_SP, mSmallSize);
                        }
                    }

                    if (!TextUtils.isEmpty(third)) {
                        if (str.length == 4) {
                            holder.tv_item_third.setTextColor(mDescriptor.getFChartTypeColor(str[3]));
                        }

                        holder.tv_item_third.setText(mDescriptor.floatValue(third, 0));

                        if (new BigDecimal(third).compareTo(new BigDecimal(1000)) == -1) {
                            holder.tv_item_third.setTextSize(TypedValue.COMPLEX_UNIT_SP, mBigSize);
                        } else {
                            holder.tv_item_third.setTextSize(TypedValue.COMPLEX_UNIT_SP, mSmallSize);
                        }
                    }
                }
            }
        } else {
            if (mType == FData.TYPE_SELL) {
                holder.tv_item_first.setText(mSell[position]);
            } else if (mType == FData.TYPE_BUY) {
                holder.tv_item_first.setText(mBuy[position]);
            } else {
                holder.tv_item_first.setText("   - - -   ");
            }

            holder.tv_item_second.setText("   - - -   ");
            holder.tv_item_third.setText("   - - -   ");
        }

        return convertView;
    }

    class ViewHolder {
        TextView tv_item_first;
        TextView tv_item_second;
        TextView tv_item_third;
    }

}

