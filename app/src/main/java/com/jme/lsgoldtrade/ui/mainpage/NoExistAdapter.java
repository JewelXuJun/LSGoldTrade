package com.jme.lsgoldtrade.ui.mainpage;

import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.domain.NavigatorVo;
import com.squareup.picasso.Picasso;

import java.util.List;

public class NoExistAdapter extends BaseQuickAdapter<NavigatorVo.UsedModulesBean, BaseViewHolder> {

    private int bw = 0;

    public OnChooseTabListener listener;

    public interface OnChooseTabListener {
        void add(NavigatorVo.UsedModulesBean item);
    }

    public void setOnChooseTabListener(OnChooseTabListener listener) {
        this.listener = listener;
    }

    public void showChoose(int bw) {
        this.bw = bw;
    }

    public NoExistAdapter(int resId, List<NavigatorVo.UsedModulesBean> data) {
        super(resId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, NavigatorVo.UsedModulesBean item) {
        ImageView tab_choose = helper.getView(R.id.tab_choose);
        helper.setText(R.id.tab, item.getName());
        helper.getView(R.id.tab_choose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.add(item);
                }
            }
        });
        if (bw == 1) {
            tab_choose.setVisibility(View.VISIBLE);
        } else {
            tab_choose.setVisibility(View.GONE);
        }

        Picasso.with(mContext)
                .load(item.getImageName())
                .placeholder(R.mipmap.ic_img_default)
                .error(R.mipmap.ic_img_default)
                .into((ImageView) helper.getView(R.id.image));
    }
}