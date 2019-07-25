package com.jme.lsgoldtrade.ui.personal;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.domain.NavigatorVo;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AddedAdapter extends BaseQuickAdapter<NavigatorVo.NavigatorVoBean, BaseViewHolder> {

    private boolean bEditFlag = false;

    public AddedAdapter(int resId, List<NavigatorVo.NavigatorVoBean> data) {
        super(resId, data);
    }

    public void showEditorialStatus(boolean editFlag) {
        bEditFlag = editFlag;
    }

    @Override
    protected void convert(BaseViewHolder helper, NavigatorVo.NavigatorVoBean item) {
        if (null == item)
            return;

        Picasso.with(mContext)
                .load(item.getImageName())
                .placeholder(R.mipmap.ic_img_default)
                .error(R.mipmap.ic_img_default)
                .into((ImageView) helper.getView(R.id.img));

        helper.setText(R.id.tv_tab_name, item.getName())
                .setGone(R.id.layout_delete, bEditFlag)
                .addOnClickListener(R.id.layout_delete);
    }

}
