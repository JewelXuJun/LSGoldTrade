package com.jme.lsgoldtrade.base;

import android.app.AlertDialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;

import com.alibaba.android.arouter.launcher.ARouter;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.common.ui.base.BaseFragment;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.domain.User;

import java.lang.reflect.Field;

/**
 * Created by XuJun on 2018/11/7.
 */
public abstract class JMEBaseFragment<T> extends BaseFragment {

    protected boolean isFinishing = false;

    protected T mBinding;

    protected User mUser;

    private AlertDialog.Builder mDialog;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        isFinishing = mActivity.isFinishing();
    }

    @Override
    protected void initView() {
        super.initView();
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);

        mUser = User.getInstance();

        initBinding();
    }

    @Override
    protected void initListener() {
        super.initListener();
    }

    public void initBinding() {
        mBinding = (T) mBindingUtil;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (isHidden())
            return;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

        if (!hidden && !isHidden())
            onResume();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
    }

    @Override
    public void onDetach() {
        super.onDetach();

        isFinishing = mActivity.isFinishing();
    }

    @Override
    protected void DataReturn(DTRequest request, Head head, Object response) {
        super.DataReturn(request, head, response);
    }

    protected void showNeedLoginDialog() {
        if (null == mDialog) {
            mDialog = new AlertDialog.Builder(mContext);
            mDialog.setTitle(mContext.getResources().getString(R.string.text_tips));
            mDialog.setMessage(mContext.getResources().getString(R.string.login_message));
            mDialog.setPositiveButton(mContext.getResources().getString(R.string.text_login), (dialog, which) -> ARouter.getInstance().build(Constants.ARouterUriConst.ACCOUNTLOGIN).navigation());
            mDialog.setNegativeButton(mContext.getResources().getString(R.string.text_cancel), null);
            mDialog.show();
        } else {
            mDialog.show();
        }
    }

    public void setIndicator(TabLayout tabs, int leftDip, int rightDip) {
        Class<?> tabLayout = tabs.getClass();
        Field tabStrip = null;
        try {
            tabStrip = tabLayout.getDeclaredField("mTabStrip");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        tabStrip.setAccessible(true);
        LinearLayout llTab = null;
        try {
            llTab = (LinearLayout) tabStrip.get(tabs);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        int left = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, leftDip, Resources.getSystem().getDisplayMetrics());
        int right = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, rightDip, Resources.getSystem().getDisplayMetrics());

        for (int i = 0; i < llTab.getChildCount(); i++) {
            View child = llTab.getChildAt(i);
            child.setPadding(0, 0, 0, 0);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
            params.leftMargin = left;
            params.rightMargin = right;
            child.setLayoutParams(params);
            child.invalidate();
        }
    }
}
