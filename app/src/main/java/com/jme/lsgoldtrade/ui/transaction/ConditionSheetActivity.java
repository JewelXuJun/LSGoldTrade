package com.jme.lsgoldtrade.ui.transaction;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.google.android.material.tabs.TabLayout;
import com.jme.common.util.DensityUtil;
import com.jme.common.util.RxBus;
import com.jme.common.util.ScreenUtil;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseActivity;
import com.jme.lsgoldtrade.base.TabViewPagerAdapter;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.ActivityConditionSheetBinding;

import rx.Subscription;

@Route(path = Constants.ARouterUriConst.CONDITIONSHEET)
public class ConditionSheetActivity extends JMEBaseActivity {

    private ActivityConditionSheetBinding mBinding;

    private int mType;

    private String[] mTabTitles;
    private Fragment[] mFragments;

    private TabViewPagerAdapter mAdapter;
    private Subscription mRxbus;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_condition_sheet;
    }

    @Override
    protected void initView() {
        super.initView();

        initToolbar(R.string.transaction_condition_sheet, true);

        setRightNavigation(getResources().getString(R.string.transaction_explain), 0, R.style.ToolbarThemeBlue,
                () -> ARouter.getInstance().build(Constants.ARouterUriConst.CONDITIONSHEETEXPLAIN).navigation());
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);

        mType = getIntent().getIntExtra("Type", 0);

        initTabLayout();
    }

    @Override
    protected void initListener() {
        super.initListener();

        initRxBus();
    }

    @Override
    protected void initBinding() {
        super.initBinding();

        mBinding = (ActivityConditionSheetBinding) mBindingUtil;
    }

    private void initRxBus() {
        mRxbus = RxBus.getInstance().toObserverable(RxBus.Message.class).subscribe(message -> {
            String callType = message.getObject().toString();

            if (TextUtils.isEmpty(callType))
                return;

            switch (callType) {
                case Constants.RxBusConst.RXBUS_TRANSACTION_CONDITION_ORDER_RUN:
                    Object object = message.getObject2();

                    if (null != object) {
                        mType = Integer.parseInt(object.toString());

                        runOnUiThread(() -> mBinding.tabViewpager.setCurrentItem(mType));
                    }

                    break;
            }
        });
    }

    private void initTabLayout() {
        mTabTitles = new String[3];
        mTabTitles[0] = getResources().getString(R.string.transaction_create_condition_sheet);
        mTabTitles[1] = getResources().getString(R.string.transaction_own_condition_sheet);
        mTabTitles[2] = mContext.getResources().getString(R.string.transaction_stop_sheet);

        mFragments = new Fragment[3];
        mFragments[0] = new CreateConditionSheetFragment();
        mFragments[1] = new OwnConditionSheetFragment();
        mFragments[2] = new TransactionStopSheetFragment();

        mAdapter = new TabViewPagerAdapter(getSupportFragmentManager(), mTabTitles, mFragments);

        mBinding.tabViewpager.removeAllViewsInLayout();
        mBinding.tabViewpager.setAdapter(mAdapter);
        mBinding.tabViewpager.setOffscreenPageLimit(3);
        mBinding.tablayout.setupWithViewPager(mBinding.tabViewpager);

        for (int i = 0; i < mBinding.tablayout.getTabCount(); i++) {
            TabLayout.Tab tab = mBinding.tablayout.getTabAt(i);

            if (null != tab)
                tab.setCustomView(getTabView(i));
        }

        runOnUiThread(() -> mBinding.tabViewpager.setCurrentItem(mType));
    }

    private View getTabView(int currentPosition) {
        View view = LayoutInflater.from(this).inflate(R.layout.item_tab, null);
        TextView textView = view.findViewById(R.id.tv_tab);
        textView.setText(mTabTitles[currentPosition]);
        textView.setGravity(Gravity.CENTER);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.width = (ScreenUtil.getScreenWidth(this) - DensityUtil.dpTopx(this, 30)) / 3;
        params.height = DensityUtil.dpTopx(this, 31);
        textView.setLayoutParams(params);

        return view;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (!mRxbus.isUnsubscribed())
            mRxbus.unsubscribe();
    }


}
