package com.jme.common.ui.view.pickerview;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.jme.common.R;

import java.util.ArrayList;

/**
 * Created by XuJun on 2016/8/31.
 */
public class PickerView extends LinearLayout{

    private WheelView wheelview_left;
    private WheelView wheelview_right;

    private ArrayList<String> mOptionsOut;
    private ArrayList<ArrayList<String>> mOptionsIn;

    private OnItemHandlerListener mListener;

    private boolean bLinkage = false;

    public PickerView(Context context) {
        super(context);
    }

    public PickerView(Context context, AttributeSet attrs) {
        super(context, attrs);

        setupLayoutResource(R.layout.layout_pickerview);

        init();
    }

    private void setupLayoutResource(int layoutResource) {
        View inflater = LayoutInflater.from(getContext()).inflate(layoutResource, this);

        inflater.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        inflater.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        inflater.layout(0, 0, inflater.getMeasuredWidth(), inflater.getMeasuredHeight());
    }

    private void init() {
        wheelview_left = (WheelView) findViewById(R.id.wheelview_left);
        wheelview_right = (WheelView) findViewById(R.id.wheelview_right);
    }

    public void setSwipeRefreshLayout(SwipeRefreshLayout swipeRefreshLayout) {
        wheelview_left.setSwipeRefreshLayout(swipeRefreshLayout);
        wheelview_right.setSwipeRefreshLayout(swipeRefreshLayout);
    }

    public void setListener(OnItemHandlerListener listener) {
        mListener = listener;
    }

    public void setPicker(ArrayList<String> oPtionsOut, ArrayList<ArrayList<String>> oPtionsIn, final boolean linkage) {
        bLinkage = linkage;
        mOptionsOut = oPtionsOut;
        mOptionsIn = oPtionsIn;

        if (mOptionsOut == null) {
            wheelview_left.setVisibility(View.GONE);
            wheelview_right.setVisibility(View.GONE);
        } else {
            wheelview_left.setVisibility(VISIBLE);
            wheelview_right.setVisibility(VISIBLE);

            wheelview_left.setAdapter(new ArrayWheelAdapter(mOptionsOut, ArrayWheelAdapter.DEFAULT_LENGTH));
            wheelview_left.setCurrentItem(0);
            wheelview_left.setTextSize(20);

            wheelview_left.setOnItemSelectedListener(new OnItemSelectedListener() {
                @Override
                public void onItemSelected(int index) {
                    if (mOptionsIn != null && linkage) {
                        wheelview_right.setAdapter(new ArrayWheelAdapter(mOptionsIn.get(index)));
                        wheelview_right.setCurrentItem(0);

                        mListener.onItemSelected(index, 0);
                    }
                }
            });

            if (mOptionsIn != null) {
                wheelview_right.setAdapter(new ArrayWheelAdapter(mOptionsIn.get(0)));
                wheelview_right.setCurrentItem(wheelview_left.getCurrentItem());
                wheelview_right.setTextSize(20);

                wheelview_right.setOnItemSelectedListener(new OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(int index) {
                        mListener.onItemSelected(wheelview_left.getCurrentItem(), index);
                    }
                });
            }
        }
    }

    /**
     * 设置是否循环滚动
     *
     * @param cyclic 是否循环
     */
    public void setCyclic(boolean cyclic) {
        wheelview_left.setCyclic(cyclic);
        wheelview_right.setCyclic(cyclic);
    }

    /**
     * 返回当前选中的结果对应的位置数组 因为支持二级联动效果，分三个级别索引，0，1
     *
     * @return 索引数组
     */
    public int[] getCurrentItems() {
        int[] currentItems = new int[2];
        currentItems[0] = wheelview_left.getCurrentItem();
        currentItems[1] = wheelview_right.getCurrentItem();
        return currentItems;
    }

    public void setCurrentItems(int option1, int option2) {
        if (bLinkage) {
            itemSelected(option1, option2);
        }
        wheelview_left.setCurrentItem(option1);
        wheelview_right.setCurrentItem(option2);
    }

    private void itemSelected(int opt1Select, int opt2Select) {
        if (mOptionsIn != null) {
            wheelview_right.setAdapter(new ArrayWheelAdapter(mOptionsIn.get(opt1Select)));
            wheelview_right.setCurrentItem(opt2Select);
        }
    }
}
