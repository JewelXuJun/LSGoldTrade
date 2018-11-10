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
public class SelectedTitleView extends LinearLayout {
    private Context mContext;

    private TextView tv_title;
    private ImageView title_iv;
    private TextView left_navigator;
    private TextView right_navigator;

    public SelectedTitleView(Context context) {
        super(context);
        mContext = context;
        initView();
    }

    public SelectedTitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView();
    }

    private void initView() {
        setupLayoutResource(R.layout.layout_selected_title);

        tv_title = (TextView) findViewById(R.id.tv_title);
        title_iv = (ImageView) findViewById(R.id.title_iv);
        left_navigator = (TextView) findViewById(R.id.left_navigator);
        left_navigator = (TextView) findViewById(R.id.left_navigator);
    }

    public void setTitle(String title, int img_id) {
        tv_title.setText(title);

        if (TextUtils.isEmpty(title)) {
            title_iv.setVisibility(View.GONE);
        } else {
            title_iv.setVisibility(View.VISIBLE);
            title_iv.setImageResource(img_id);
        }
    }

    public void setTitleColor(int color) {
        tv_title.setTextColor(color);
    }

    public void setTitle(String title){
        if (TextUtils.isEmpty(title)) {
            tv_title.setText("");
        } else {
            tv_title.setText(title);
        }

        title_iv.setVisibility(View.GONE);
    }

    public void setLeftIcon(String leftStr) {
        if (leftStr == null) {
            left_navigator.setText("");
        } else {
            left_navigator.setText(leftStr);
        }
    }

    public void setRightIcon(String leftStr) {
        if (leftStr == null) {
            right_navigator.setText("");
        } else {
            right_navigator.setText(leftStr);
        }
    }

    private void setupLayoutResource(int layoutResource) {
        View inflater = LayoutInflater.from(getContext()).inflate(layoutResource, this);

        inflater.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        inflater.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        inflater.layout(0, 0, inflater.getMeasuredWidth(), inflater.getMeasuredHeight());
    }
}
