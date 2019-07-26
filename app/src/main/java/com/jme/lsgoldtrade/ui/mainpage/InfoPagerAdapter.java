package com.jme.lsgoldtrade.ui.mainpage;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

public class InfoPagerAdapter extends FragmentStatePagerAdapter {

    private List<String> mTabHeaders;
    private List<Long> mChannelIds;

    public InfoPagerAdapter(FragmentManager fragmentManager, List<String> tabHeaders, List<Long> channelIds) {
        super(fragmentManager);

        this.mTabHeaders = tabHeaders;
        this.mChannelIds = channelIds;
    }

    @Override
    public Fragment getItem(int position) {
        return InfoFragment.newInstance(mChannelIds.get(position), mTabHeaders.get(position));
    }

    @Override
    public int getCount() {
        return mTabHeaders.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTabHeaders.get(position);
    }
}
