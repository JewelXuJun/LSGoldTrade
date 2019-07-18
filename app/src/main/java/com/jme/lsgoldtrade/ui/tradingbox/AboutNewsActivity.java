package com.jme.lsgoldtrade.ui.tradingbox;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseActivity;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.ActivityAboutNewsBinding;
import com.jme.lsgoldtrade.domain.InfoListVo;
import com.jme.lsgoldtrade.ui.mainpage.AboutNewsAdapter;

import java.util.List;

/**
 * 相关资讯
 */
@Route(path = Constants.ARouterUriConst.ABOUTNEWS)
public class AboutNewsActivity extends JMEBaseActivity {

    private ActivityAboutNewsBinding mBinding;
    private String infoList = "";
    private AboutNewsAdapter mAdapter;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_about_news;
    }

    @Override
    protected void initView() {
        super.initView();
        initToolbar("相关资讯", true);
        mBinding = (ActivityAboutNewsBinding) mBindingUtil;
        infoList = getIntent().getStringExtra("infoList");
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        List<InfoListVo> infoListVo = new Gson().fromJson(infoList, new TypeToken<List<InfoListVo>>() {}.getType());
        mAdapter = new AboutNewsAdapter(R.layout.item_aboutnews, infoListVo);

        mBinding.recyclerView.setHasFixedSize(true);
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mBinding.recyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void initListener() {
        super.initListener();

    }

    @Override
    protected void initBinding() {
        super.initBinding();
        mBinding.setHandlers(new ClickHandlers());
    }

    public class ClickHandlers {

    }
}
