package com.jme.lsgoldtrade.ui.mainpage;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

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
