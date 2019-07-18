package com.jme.lsgoldtrade.tabhost;

import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.ui.mainpage.MainPageFragment;
import com.jme.lsgoldtrade.ui.market.MarketFragment;
import com.jme.lsgoldtrade.ui.personal.PersonalFragment;
import com.jme.lsgoldtrade.ui.trade.TradeFragment;

/**
 * Created by XuJun on 2018/11/10.
 */
public enum MainTab {

    MAINPAGE(0, R.string.main_page, R.drawable.selector_tab_homepage, MainPageFragment.class),

    MARKET(1, R.string.main_market, R.drawable.selector_tab_market, MarketFragment.class),

    TRADE(2, R.string.main_trade, R.drawable.selector_tab_trade, TradeFragment.class),

    PERSONAL(3, R.string.main_personal, R.drawable.selector_tab_personal, PersonalFragment.class);

    private int mId;
    private int mName;
    private int mIcon;
    private Class<?> mClassRes;

    MainTab(int id, int name, int resIcon, Class<?> classRes) {
        mId = id;
        mName = name;
        mIcon = resIcon;
        mClassRes = classRes;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public int getName() {
        return mName;
    }

    public void setName(int name) {
        mName = name;
    }

    public int getIcon() {
        return mIcon;
    }

    public void setIcon(int resIcon) {
        mIcon = resIcon;
    }

    public Class<?> getClassRes() {
        return mClassRes;
    }

    public void setClassRes(Class<?> classRes) {
        mClassRes = classRes;
    }

}
