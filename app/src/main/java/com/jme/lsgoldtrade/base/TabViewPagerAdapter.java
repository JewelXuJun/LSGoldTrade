package com.jme.lsgoldtrade.base;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class TabViewPagerAdapter extends FragmentPagerAdapter {

    private String[] mTabTitles;
    private Fragment[] mFragmentArrays;

    public TabViewPagerAdapter(FragmentManager fragmentManager, String[] tabTitles, Fragment[] fragmentArrays) {
        super(fragmentManager);

        mTabTitles = tabTitles;
        mFragmentArrays = fragmentArrays;
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
