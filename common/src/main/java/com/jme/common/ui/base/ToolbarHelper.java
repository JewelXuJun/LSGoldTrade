package com.jme.common.ui.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.MenuRes;
import androidx.annotation.StyleRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.jme.common.R;

/**
 * Created by Yanmin on 2016/3/23.
 */
public class ToolbarHelper {

    public interface OnSingleMenuItemClickListener {
        /**
         * This method will be invoked when a menu item is clicked if the item itself did
         * not already handle the event.
         *
         * @return <code>true</code> if the event was handled, <code>false</code> otherwise.
         */
        void onSingleMenuItemClick();
    }

    private AppCompatActivity mActivity;
    private Toolbar mToolbar;
    private LinearLayout mCenterToolbarView;
    private View mUserView;
    private boolean bUserViewIsTitle = false;

    public ToolbarHelper(AppCompatActivity activity, View view) {
        mActivity = activity;
        mToolbar = view.findViewById(R.id.toolbar);
    }

    public ToolbarHelper(AppCompatActivity activity) {
        mActivity = activity;
        mToolbar = mActivity.findViewById(R.id.toolbar);
        mToolbar.setContentInsetStartWithNavigation(0);
    }

    public void initToolbar(View view) {
        mCenterToolbarView = mToolbar.findViewById(R.id.centerToolbarView);
        mCenterToolbarView.addView(view);
        mUserView = view;
    }

    public void initToolbar(String title) {
        initToolbar(createTitleView());
        setTitle(title);
    }

    public void initToolbar(String title, @ColorInt int resId) {
        initToolbar(createTitleView());
        setTitle(title, resId);
    }

    public void initToolbar(String[] title) {
        initToolbar(createTitleViewWithSure());
        setTitle(title);
    }

    public void removeView(View view) {
        mCenterToolbarView.removeView(view);
    }

    private View createTitleView() {
        LayoutInflater inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.layout_title, null);
        bUserViewIsTitle = true;

        return view;
    }

    private View createTitleViewWithSure() {
        LayoutInflater inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.layout_title_with_sure, null);
        bUserViewIsTitle = true;

        return view;
    }

    public void setTitle(String title) {
        if (bUserViewIsTitle) {
            TextView tv_title = mUserView.findViewById(R.id.toolbar_title);
            tv_title.setText(title);
        }
    }

    public void setTitle(String title, @ColorInt int resId) {
        if (bUserViewIsTitle) {
            TextView tv_title = mUserView.findViewById(R.id.toolbar_title);
            tv_title.setTextColor(resId);
            tv_title.setText(title);
        }
    }

    public void setTitle(String[] title) {
        if (bUserViewIsTitle) {
            TextView tv_title = mUserView.findViewById(R.id.toolbar_title);
            TextView tv_title_with_sure = mUserView.findViewById(R.id.toolbar_title_with_sure);
            tv_title.setText(title[0]);
            tv_title_with_sure.setText(title[1]);
        }
    }

    public void setBackNavigation(boolean hasBack, View.OnClickListener listener) {
        if (hasBack) {
            mToolbar.setNavigationIcon(R.drawable.ic_back);
            mToolbar.setNavigationOnClickListener(listener);
        } else {
            mToolbar.setNavigationIcon(null);
        }
    }

    public void setBackNavigation(boolean hasBack, int resId, View.OnClickListener listener) {
        if (hasBack) {
            mToolbar.setNavigationIcon(resId);
            mToolbar.setNavigationOnClickListener(listener);
        } else {
            mToolbar.setNavigationIcon(null);
        }
    }

    public void setBackNavigationIcon(int resId, View.OnClickListener listener) {
        mToolbar.setNavigationIcon(resId);
        mToolbar.setNavigationOnClickListener(listener);
    }

    public void setBackNavigationIcon(int resId) {
        mToolbar.setNavigationIcon(resId);
    }

    public void setSingleMenu(String title, @DrawableRes int resId, final OnSingleMenuItemClickListener listener) {
        mToolbar.getMenu().clear();

        MenuItem item = mToolbar.getMenu().add(title);
        item.setIcon(resId);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                listener.onSingleMenuItemClick();
                return true;
            }
        });
    }

    public void setSingleMenu(String title, @DrawableRes int resId, @StyleRes int styleResId, final OnSingleMenuItemClickListener listener) {
        mToolbar.getMenu().clear();
        mToolbar.getContext().setTheme(styleResId);

        MenuItem item = mToolbar.getMenu().add(title);
        item.setIcon(resId);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                listener.onSingleMenuItemClick();
                return true;
            }
        });
    }

    public void setMenu(@MenuRes int resId, final Toolbar.OnMenuItemClickListener listener) {
        mToolbar.inflateMenu(resId);
        mToolbar.setOnMenuItemClickListener(listener);
    }

    public void setMenu(@MenuRes int resId, @StyleRes int styleResId, final Toolbar.OnMenuItemClickListener listener) {
        mToolbar.getMenu().clear();
        mToolbar.getContext().setTheme(styleResId);

        mToolbar.inflateMenu(resId);
        mToolbar.setOnMenuItemClickListener(listener);
    }

    public void clearMenu() {
        mToolbar.getMenu().clear();
    }

    public void setBackgroundColor(@ColorInt int color) {
        mToolbar.setBackgroundColor(color);
    }
}
