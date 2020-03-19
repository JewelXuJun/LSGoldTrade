package com.jme.lsgoldtrade.ui.personal;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.domain.PayIconVo;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PayTypeAdapter extends BaseQuickAdapter<PayIconVo, BaseViewHolder> {

    private Context mContext;

    public PayTypeAdapter(Context context, List<PayIconVo> data) {
        super(R.layout.item_pay_type, data);

        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, PayIconVo item) {
        if (null == item)
            return;

        String iconPath = item.getIconpath();

        if (!TextUtils.isEmpty(iconPath))
            Picasso.with(mContext).load(iconPath).into((ImageView) helper.getView(R.id.img_icon));

        helper.setText(R.id.tv_pay_name, item.getName())
                .setText(R.id.tv_pay_message, item.getDescript())
                .setGone(R.id.img_select, helper.getAdapterPosition() == 0);
    }

}
