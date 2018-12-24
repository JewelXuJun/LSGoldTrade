package com.jme.lsgoldtrade.ui.news;

import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseActivity;
import com.jme.lsgoldtrade.config.Constants;

@Route(path = Constants.ARouterUriConst.NEWSACTIVITY)
public class NewsActivity extends JMEBaseActivity {
    @Override
    protected int getContentViewId() {
        return R.layout.activity_news;
    }

    @Override
    protected void initView() {
        super.initView();

        initToolbar(R.string.news_title, true);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
    }

    @Override
    protected void initListener() {
        super.initListener();
    }

    @Override
    protected void initBinding() {
        super.initBinding();
    }

    @Override
    protected void DataReturn(DTRequest request, Head head, Object response) {
        super.DataReturn(request, head, response);
    }
}
