package com.jme.lsgoldtrade.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.jme.lsgoldtrade.R;

public class EmptyView {

    public static View getEmptyView(Context context) {
        View mEmptyView = null;
        if (null == mEmptyView)
            mEmptyView = LayoutInflater.from(context).inflate(R.layout.layout_empty, null);

        return mEmptyView;
    }

}
