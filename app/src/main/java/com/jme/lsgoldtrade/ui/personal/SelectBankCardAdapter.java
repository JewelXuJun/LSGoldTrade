package com.jme.lsgoldtrade.ui.personal;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jme.common.util.DensityUtil;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.domain.BankVo;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SelectBankCardAdapter extends BaseQuickAdapter<BankVo, BaseViewHolder> {


    private Context mContext;
    private View.OnClickListener mClick;

    public SelectBankCardAdapter(Context context, List<BankVo> data, View.OnClickListener click) {
        super(R.layout.item_select_bank_card, data);

        mContext = context;
        mClick = click;
    }

    @Override
    protected void convert(BaseViewHolder helper, BankVo item) {
        if (null == item)
            return;


        Picasso.with(mContext).load(item.getLogoPath()).into((ImageView) helper.getView(R.id.img_select_bank_card_item));
        helper.setText(R.id.tv_select_bank_card_item,item.getBankName());
        ((TextView)helper.getView(R.id.tv_select_bank_card_item)).setTextSize(item.getBankName().length() > 4 ? 14 : 15);
        ((LinearLayout)helper.getView(R.id.ll_select_bank_card_item)).setTag(helper.getAdapterPosition());
        ((LinearLayout)helper.getView(R.id.ll_select_bank_card_item)).setOnClickListener(mClick);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) ((LinearLayout)helper.getView(R.id.ll_select_bank_card_item)).getLayoutParams();
        if(helper.getAdapterPosition()==0){
            params.height = DensityUtil.dpTopx(mContext,50);
            helper.getView(R.id.tv_select_bank_card_flag_item).setVisibility(View.VISIBLE);
        }else{
            params.height = DensityUtil.dpTopx(mContext,40);
            helper.getView(R.id.tv_select_bank_card_flag_item).setVisibility(View.GONE);
        }
        ((LinearLayout)helper.getView(R.id.ll_select_bank_card_item)).setLayoutParams(params);

        if(item.isSelection()){
            ((LinearLayout)helper.getView(R.id.ll_select_bank_card_item)).setBackground(ContextCompat.getDrawable(mContext,R.drawable.btn_select_bank_card_selection));
        }else{
            ((LinearLayout)helper.getView(R.id.ll_select_bank_card_item)).setBackground(ContextCompat.getDrawable(mContext,R.drawable.btn_select_bank_card_default));
        }

    }
}
