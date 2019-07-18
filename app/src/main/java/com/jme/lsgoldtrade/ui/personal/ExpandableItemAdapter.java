package com.jme.lsgoldtrade.ui.personal;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.domain.AskListVo;

import java.util.List;

public class ExpandableItemAdapter extends BaseQuickAdapter<AskListVo, BaseViewHolder> {

    private Context context;

    private int tag = 1;

    public ExpandableItemAdapter(Context context, int resId, List<AskListVo> data) {
        super(resId, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, AskListVo item) {
        TextView quest = helper.getView(R.id.quest);
        ImageView up_down = helper.getView(R.id.up_down);
        TextView ask = helper.getView(R.id.ask);

        up_down.setImageResource(R.mipmap.ic_down);
        quest.setText(item.getTitle());
        ask.setText(item.getAnwser());

        helper.getView(R.id.rl).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tag == 1) {
                    quest.setTextColor(context.getResources().getColor(R.color.color_0080ff));
                    ask.setVisibility(View.VISIBLE);
                    up_down.setImageResource(R.mipmap.ic_up);
                    tag = 2;
                } else {
                    quest.setTextColor(context.getResources().getColor(R.color.color_000));
                    ask.setVisibility(View.GONE);
                    up_down.setImageResource(R.mipmap.ic_down);
                    tag = 1;
                }
            }
        });
    }
}
