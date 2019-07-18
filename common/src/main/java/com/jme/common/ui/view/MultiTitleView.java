package com.jme.common.ui.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.jme.common.R;

/**
 * Created by Yanmin on 2016/3/31.
 */
public class MultiTitleView extends LinearLayout {

    private Context mContext;

    private TextView tv_title;
    private TextView tv_subtitle;
    private ImageView img_left_icon;
    private ImageView img_right_icon;
    private ImageView img_shoppingmall;

    public MultiTitleView(Context context) {
        super(context);
        mContext = context;
        initView();
    }

    public MultiTitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView();
    }

    private void initView() {
        setupLayoutResource(R.layout.layout_multi_title);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_subtitle = (TextView) findViewById(R.id.tv_subtitle);
        img_left_icon = (ImageView) findViewById(R.id.img_left_icon);
        img_right_icon = (ImageView) findViewById(R.id.img_right_icon);
        img_shoppingmall = (ImageView) findViewById(R.id.img_shoppingmall);
    }

    public void setTitle(String title, String subtitle) {
        tv_title.setText(title);
        if (TextUtils.isEmpty(subtitle)) {
            tv_subtitle.setVisibility(View.GONE);
        } else {
            tv_subtitle.setVisibility(View.VISIBLE);
            tv_subtitle.setText(subtitle);
        }
    }

    public void setLeftIcon(int resId) {
        if (resId == 0) {
            img_left_icon.setVisibility(View.GONE);
        } else {
            img_left_icon.setVisibility(View.VISIBLE);
            img_left_icon.setImageResource(resId);
        }
    }

    public void setRightIcon(int resId) {
        if (resId == 0) {
            img_right_icon.setVisibility(View.GONE);
        } else {
            img_right_icon.setVisibility(View.VISIBLE);
            img_right_icon.setImageResource(resId);
        }
    }

    public void setTitleTextColor(int color){
        tv_title.setTextColor(color);
        tv_subtitle.setTextColor(color);
    }

    public void setShoppingMallImage(boolean showFlag) {
        if (showFlag)
            img_shoppingmall.setVisibility(VISIBLE);
        else
            img_shoppingmall.setVisibility(GONE);
    }

    private void setupLayoutResource(int layoutResource) {
        View inflater = LayoutInflater.from(getContext()).inflate(layoutResource, this);
        inflater.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        inflater.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        inflater.layout(0, 0, inflater.getMeasuredWidth(), inflater.getMeasuredHeight());
    }
}
