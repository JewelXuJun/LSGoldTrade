package com.jme.lsgoldtrade.ui.mainpage;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

public class InfoPagerAdapter extends FragmentStatePagerAdapter {

    private List<String> mTabs;
    private List<Long> mChannelIds;

    public InfoPagerAdapter(FragmentManager fragmentManager, List<String> tabs, List<Long> channelIds) {
        super(fragmentManager);

        mTabs = tabs;
        mChannelIds = channelIds;
    }

    @Override
    public Fragment getItem(int position) {
        return InfoFragment.newInstance(mChannelIds.get(position));
    }

    @Override
    public int getCount() {
        return mTabs.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTabs.get(position);
    }
}
