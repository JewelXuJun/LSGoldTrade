package com.jme.lsgoldtrade.ui.mainpage;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.domain.FiveSpeedVo;
import com.jme.lsgoldtrade.util.MarketUtil;

import java.math.BigDecimal;
import java.util.List;


public class RateMarketAdapter extends RecyclerView.Adapter<RateMarketAdapter.ViewHolder> implements View.OnClickListener {

    private Context mContext;
    private List<FiveSpeedVo> mList;

    private OnItemClickListener mItemClickListener;

    private final int mItemWidth;
    private final LayoutInflater mLayoutInflater;

    public RateMarketAdapter(Context context, List<FiveSpeedVo> list, int itemWidth) {
        mContext = context;
        mList = list;
        mItemWidth = itemWidth;
        mLayoutInflater = LayoutInflater.from(context);
    }

    public void setDataList(List<FiveSpeedVo> list) {
        mList = list;

        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.item_rate_market_adapter, parent, false);
        ViewHolder holder = new ViewHolder(view, mItemWidth);
        view.setOnClickListener(this);

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (null != mList && 0 != mList.size()) {
            holder.bindData(mContext, mList.get(position));
            holder.itemView.setTag(position);
        }
    }

    @Override
    public int getItemCount() {
        return null == mList ? 0 : mList.size() < 3 ? mList.size() : 3;
    }

    @Override
    public void onClick(View view) {
        if (null != mItemClickListener) {
            Object tag = view.getTag();

            if (null != tag)
                mItemClickListener.onItemClick((Integer) tag);
        }
    }

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_contract;
        private TextView tv_last_price;
        private TextView tv_range;
        private TextView tv_rate;

        public ViewHolder(View itemView, int itemWidth) {
            super(itemView);

            ViewGroup.LayoutParams layoutParams = itemView.getLayoutParams();
            layoutParams.width = itemWidth;

            tv_contract = itemView.findViewById(R.id.tv_contract);
            tv_last_price = itemView.findViewById(R.id.tv_last_price);
            tv_range = itemView.findViewById(R.id.tv_range);
            tv_rate = itemView.findViewById(R.id.tv_rate);
        }

        public void bindData(Context context, FiveSpeedVo fiveSpeedVo) {
            if (null == fiveSpeedVo)
                return;

            String upDownRate = fiveSpeedVo.getUpDownRateValue();

            int rateType;

            if (TextUtils.isEmpty(upDownRate))
                rateType = 0;
            else
                rateType = new BigDecimal(upDownRate).compareTo(new BigDecimal(0));

            tv_contract.setText(MarketUtil.getContractNameEN(fiveSpeedVo.getContractId()));
            tv_last_price.setText(fiveSpeedVo.getLatestPriceValue());
            tv_last_price.setTextColor(ContextCompat.getColor(context, MarketUtil.getMarketStateColor(rateType)));
            tv_range.setText(MarketUtil.getMarketRangeValue(rateType, fiveSpeedVo.getUpDownValue()));
            tv_range.setTextColor(ContextCompat.getColor(context, MarketUtil.getMarketStateColor(rateType)));
            tv_rate.setText(MarketUtil.getMarketRateValue(rateType, upDownRate));
            tv_rate.setTextColor(ContextCompat.getColor(context, MarketUtil.getMarketStateColor(rateType)));
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}
