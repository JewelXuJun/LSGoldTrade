package com.jme.lsgoldtrade.ui.order;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.domain.MyOrderVo;

import java.util.List;

public class MyOrderAdapter extends BaseQuickAdapter<MyOrderVo, BaseViewHolder> {

    private Context context;

    public MyOrderAdapter(Context mContext, int resId, @Nullable List<MyOrderVo> data) {
        super(resId, data);
        context = mContext;
    }

    @Override
    protected void convert(BaseViewHolder helper, MyOrderVo item) {
        if ("0".equals(item.getEntrustTheDirection())) {
            helper.setText(R.id.fangxiang, "多");
            helper.setTextColor(R.id.fangxiang, context.getResources().getColor(R.color.color_red));
        } else {
            helper.setText(R.id.fangxiang, "空");
            helper.setTextColor(R.id.fangxiang, context.getResources().getColor(R.color.color_green));
        }
        helper.setText(R.id.pingzhong, item.getOrders());
        helper.setText(R.id.time, item.getCratedTime());
        helper.setText(R.id.num, item.getEntrustTheHandCount());
        String status = item.getStatus();
        if ("0".equals(status) || "1".equals(status) || "2".equals(status) || "3".equals(status)) {
//            helper.getView(R.id.chedan).setBackground(mContext.getResources().getDrawable(R.drawable.bg_btn_blue_solid));
            helper.getView(R.id.chedan).setVisibility(View.VISIBLE);
        } else {
//            helper.getView(R.id.chedan).setBackground(mContext.getResources().getDrawable(R.drawable.exampe_bg));
            helper.getView(R.id.chedan).setVisibility(View.GONE);
        }
        TextView mStatus = helper.getView(R.id.status);
        if ("0".equals(status)) {
            mStatus.setText("委托中");
        } else if ("1".equals(status)) {
            mStatus.setText("建仓");
        } else if ("2".equals(status)) {
            mStatus.setText("建仓中");
        } else if ("3".equals(status)) {
            mStatus.setText("平仓");
        } else if ("4".equals(status)) {
            mStatus.setText("平仓中");
        } else if ("5".equals(status)) {
            mStatus.setText("委托完成");
        } else if ("6".equals(status)) {
            mStatus.setText("撤销中");
        } else if ("7".equals(status)) {
            mStatus.setText("已撤销");
        } else if ("8".equals(status)) {
            mStatus.setText("建仓完成");
        }
        helper.getView(R.id.details).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance()
                        .build(Constants.ARouterUriConst.ORDERDETAILS)
                        .withString("id", item.getId())
                        .navigation();
            }
        });
        helper.getView(R.id.chedan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.cheDanListener(helper.getLayoutPosition());
                }
            }
        });
    }

    public OnCheDanListener listener;

    public interface OnCheDanListener {
        void cheDanListener(int position);
    }

    public void setOnCheDanListener(OnCheDanListener listener) {
        this.listener = listener;
    }
}
