package com.jme.lsgoldtrade.ui.personal;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.domain.BankVo;
import com.squareup.picasso.Picasso;

import java.util.List;

public class BankAdapter extends BaseQuickAdapter<BankVo, BaseViewHolder> {

    private Context mContext;

    public BankAdapter(Context context, List<BankVo> data) {
        super(R.layout.item_bank, data);

        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, BankVo item) {
        if (null == item)
            return;

        String logoPath = item.getLogoPath();

        if (!TextUtils.isEmpty(logoPath))
            Picasso.with(mContext).load(logoPath).into((ImageView) helper.getView(R.id.img_bank_logo));

        helper.setText(R.id.tv_bank, item.getBankName());
    }

}
