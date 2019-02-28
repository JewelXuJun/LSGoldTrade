package com.jme.lsgoldtrade.ui.news;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseActivity;
import com.jme.lsgoldtrade.config.AppConfig;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.ActivityNewsCenterBinding;
import com.jme.lsgoldtrade.domain.NoticeVo;
import com.jme.lsgoldtrade.service.UserService;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.HashMap;
import java.util.List;

@Route(path = Constants.ARouterUriConst.NEWSCENTERACTIVITY)
public class NewsCenterActivity extends JMEBaseActivity implements OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {

    private ActivityNewsCenterBinding mBinding;

    private NewsAdapter mAdapter;

    private int mPageNo = 1;
    private int mTotalPage = 1;

    private View mEmptyView;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_news_center;
    }

    @Override
    protected void initView() {
        super.initView();

        initToolbar(R.string.news_title, true);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);

        mAdapter = new NewsAdapter(R.layout.item_news, null);

        mBinding.recyclerView.setHasFixedSize(false);
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mBinding.recyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void initListener() {
        super.initListener();

        mAdapter.setOnLoadMoreListener(this, mBinding.recyclerView);
        mBinding.swipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    protected void initBinding() {
        super.initBinding();

        mBinding = (ActivityNewsCenterBinding) mBindingUtil;
    }

    @Override
    protected void onResume() {
        super.onResume();

        initNoticePageData(true);
    }

    private void setTotalPage(int total) {
        int pageSize = Integer.parseInt(AppConfig.PageSize_10);

        if (total % pageSize == 0)
            mTotalPage = total / pageSize;
        else
            mTotalPage = total / pageSize + 1;
    }

    private View getEmptyView() {
        if (null == mEmptyView)
            mEmptyView = LayoutInflater.from(this).inflate(R.layout.layout_empty, null);

        return mEmptyView;
    }

    private void initNoticePageData(boolean enable) {
        mPageNo = 1;

        getNoticePage(enable);
    }

    private void getNoticePage(boolean enable) {
        HashMap<String, String> params = new HashMap<>();
        params.put("pageNo", String.valueOf(mPageNo));
        params.put("pageSize", AppConfig.PageSize_10);

        sendRequest(UserService.getInstance().noticepage, params, enable);
    }

    @Override
    protected void DataReturn(DTRequest request, Head head, Object response) {
        super.DataReturn(request, head, response);

        switch (request.getApi().getName()) {
            case "NoticePage":
                if (head.isSuccess()) {
                    NoticeVo noticeVo;

                    try {
                        noticeVo = (NoticeVo) response;
                    } catch (Exception e) {
                        noticeVo = null;

                        e.printStackTrace();
                    }

                    if (null == noticeVo) {
                        mBinding.swipeRefreshLayout.finishRefresh(false);
                    } else {
                        setTotalPage(noticeVo.getTotal());

                        List<NoticeVo.NoticeBean> noticeBeanList = noticeVo.getList();

                        if (mPageNo == 1) {
                            mAdapter.setNewData(noticeBeanList);

                            if (null == noticeBeanList || 0 == noticeBeanList.size())
                                mAdapter.setEmptyView(getEmptyView());
                        } else {
                            mAdapter.addData(noticeBeanList);
                            mAdapter.loadMoreComplete();
                        }

                        mBinding.swipeRefreshLayout.finishRefresh(true);
                    }
                } else {
                    mBinding.swipeRefreshLayout.finishRefresh(false);
                }

                break;
        }
    }

    @Override
    public void onLoadMoreRequested() {
        mBinding.recyclerView.postDelayed(() -> {
            if (mPageNo >= mTotalPage) {
                mAdapter.loadMoreEnd();
            } else {
                mPageNo++;

                getNoticePage(false);
            }
        }, 0);
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        initNoticePageData(false);
    }
}
