package com.jme.lsgoldtrade.ui.news;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseActivity;
import com.jme.lsgoldtrade.config.Constants;

@Route(path = Constants.ARouterUriConst.NEWSACTIVITY)
public class NewsActivity extends JMEBaseActivity {
    @Override
    protected int getContentViewId() {
        return R.layout.activity_news;
    }
}
