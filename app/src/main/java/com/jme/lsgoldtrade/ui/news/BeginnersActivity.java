package com.jme.lsgoldtrade.ui.news;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseActivity;
import com.jme.lsgoldtrade.config.AppConfig;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.ActivityBeginnersBinding;
import com.jme.lsgoldtrade.domain.InfoVo;
import com.jme.lsgoldtrade.service.ManagementService;
import com.jme.lsgoldtrade.ui.mainpage.InfoAdapter;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.HashMap;
import java.util.List;

/**
 * 新手学堂
 */
@Route(path = Constants.ARouterUriConst.BEGINNERSACTIVITY)
public class BeginnersActivity extends JMEBaseActivity implements OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {

    private ActivityBeginnersBinding mBinding;

    private InfoAdapter mAdapter;

    private int mCurrentPage = 1;
    private int mTotalPage = 1;

    private View mEmptyView;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_beginners;
    }

    @Override
    protected void initView() {
        super.initView();

        initToolbar(R.string.main_page_beginners, true);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);

        mAdapter = new InfoAdapter(R.layout.item_info, null);

        mBinding.recyclerView.setHasFixedSize(false);
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mBinding.recyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void initListener() {
        super.initListener();

        mBinding.swipeRefreshLayout.setOnRefreshListener(this);
        mAdapter.setOnLoadMoreListener(this, mBinding.recyclerView);

        mAdapter.setOnItemClickListener((adapter, view, position) -> {
           InfoVo.InfoBean infoBean = (InfoVo.InfoBean) adapter.getItem(position);

           if (null == infoBean)
               return;

            ARouter.getInstance().build(Constants.ARouterUriConst.JMEWEBVIEW)
                    .withString("title", infoBean.getTitle())
                    .withString("url", Constants.HttpConst.URL_INFO + infoBean.getId())
                    .navigation();
        });
    }

    @Override
    protected void initBinding() {
        super.initBinding();

        mBinding = (ActivityBeginnersBinding) mBindingUtil;
    }

    @Override
    protected void onResume() {
        super.onResume();

        initStudyData(true);
    }

    private View getEmptyView() {
        if (null == mEmptyView)
            mEmptyView = LayoutInflater.from(this).inflate(R.layout.layout_empty, null);

        return mEmptyView;
    }

    private void initStudyData(boolean enable) {
        mCurrentPage = 1;

        getStudyData(enable);
    }

    private void getStudyData(boolean enable) {
        HashMap<String, String> params = new HashMap<>();
        params.put("pageSize", AppConfig.PageSize_10);
        params.put("current", String.valueOf(mCurrentPage));

        sendRequest(ManagementService.getInstance().study, params, enable);
    }

    @Override
    protected void DataReturn(DTRequest request, Head head, Object response) {
        super.DataReturn(request, head, response);

        switch (request.getApi().getName()) {
            case "Study":
                if (head.isSuccess()) {
                    InfoVo infoVo;

                    try {
                        infoVo = (InfoVo) response;
                    } catch (Exception e) {
                        infoVo = null;

                        e.printStackTrace();
                    }

                    if (null == infoVo) {
                        mBinding.swipeRefreshLayout.finishRefresh(false);
                    } else {
                        mTotalPage = infoVo.getPages();

                        List<InfoVo.InfoBean> infoBeanList = infoVo.getRecords();

                        if (mCurrentPage == 1) {
                            mAdapter.setNewData(infoBeanList);

                            if (null == infoBeanList || 0 == infoBeanList.size())
                                mAdapter.setEmptyView(getEmptyView());
                        } else {
                            mAdapter.addData(infoBeanList);
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
            if (mCurrentPage >= mTotalPage) {
                mAdapter.loadMoreEnd();
            } else {
                mCurrentPage++;

                getStudyData(false);
            }
        }, 0);
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        initStudyData(false);
    }
}
