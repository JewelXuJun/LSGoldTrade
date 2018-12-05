package com.jme.lsgoldtrade.ui.mainpage;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;

import com.alibaba.android.arouter.launcher.ARouter;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.common.util.StatusBarUtil;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseFragment;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.FragmentMainPageBinding;

public class MainPageFragment extends JMEBaseFragment {

    private FragmentMainPageBinding mBinding;

    private Fragment[] mFragmentArrays;
    private String[] mTabTitles;

    private PagerAdapter mAdapter;

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_main_page;
    }

    @Override
    protected void initView() {
        super.initView();

        mBinding = (FragmentMainPageBinding) mBindingUtil;

        StatusBarUtil.setStatusBarMode(mActivity, true, R.color.color_toolbar_blue);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);

        mAdapter = new TabViewPagerAdapter(getChildFragmentManager());

        initInfoTabs();
    }

    @Override
    protected void initListener() {
        super.initListener();
    }

    @Override
    public void initBinding() {
        super.initBinding();

        mBinding.setHandlers(new ClickHandlers());
    }

    private void initInfoTabs() {
        mTabTitles = new String[3];
        mTabTitles[0] = mContext.getResources().getString(R.string.main_page_notice);
        mTabTitles[1] = mContext.getResources().getString(R.string.main_page_news);
        mTabTitles[2] = mContext.getResources().getString(R.string.main_page_activity);

        mFragmentArrays = new Fragment[3];
        mFragmentArrays[0] = InfoFragment.newInstance("notice");
        mFragmentArrays[1] = InfoFragment.newInstance("news");
        mFragmentArrays[2] = InfoFragment.newInstance("activity");

        initTabLayout();
    }

    private void initTabLayout() {
        mBinding.tabViewpager.removeAllViewsInLayout();
        mBinding.tabViewpager.setAdapter(mAdapter);
        mBinding.tabViewpager.setOffscreenPageLimit(3);
        mBinding.tablayout.setTabMode(TabLayout.MODE_FIXED);
        mBinding.tablayout.setSelectedTabIndicatorHeight(4);
        mBinding.tablayout.setupWithViewPager(mBinding.tabViewpager);
        mBinding.tablayout.post(() -> setIndicator(mBinding.tablayout, 45, 45));
    }

    @Override
    protected void DataReturn(DTRequest request, Head head, Object response) {
        super.DataReturn(request, head, response);
    }

    public class ClickHandlers {

        public void onClickNews() {
            ARouter.getInstance()
                    .build(Constants.ARouterUriConst.MESSAGECENTER)
                    .navigation();
        }

        public void onClickOpenAccount() {

        }

        public void onClickQuickOrder() {

        }

        public void onClickBeginners() {

        }

    }

    final class TabViewPagerAdapter extends FragmentPagerAdapter {
        public TabViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentArrays[position];
        }

        @Override
        public int getCount() {
            return mFragmentArrays.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTabTitles[position];
        }
    }

}
