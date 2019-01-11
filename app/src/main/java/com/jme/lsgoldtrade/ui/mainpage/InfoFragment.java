package com.jme.lsgoldtrade.ui.mainpage;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.common.ui.view.MarginDividerItemDecoration;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseFragment;
import com.jme.lsgoldtrade.config.AppConfig;
import com.jme.lsgoldtrade.databinding.FragmentInfoBinding;
import com.jme.lsgoldtrade.domain.NoticePageVo;
import com.jme.lsgoldtrade.service.UserService;

import java.util.HashMap;
import java.util.List;

public class InfoFragment extends JMEBaseFragment implements BaseQuickAdapter.RequestLoadMoreListener {

    private FragmentInfoBinding mBinding;

    private InfoAdapter mAdapter;

    private boolean bVisibleToUser = false;
    private String mType;
    private int mCurrentPage = 1;
    private int mTotalPage = 1;

    public static Fragment newInstance(String type) {
        InfoFragment fragment = new InfoFragment();

        Bundle bundle = new Bundle();
        bundle.putString("InfoType", type);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_info;
    }

    @Override
    protected void initView() {
        super.initView();

        mBinding = (FragmentInfoBinding) mBindingUtil;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);

        mType = getArguments().getString("InfoType");
        mAdapter = new InfoAdapter(R.layout.item_info, null);

        mBinding.recyclerView.addItemDecoration(new MarginDividerItemDecoration(mContext, LinearLayoutManager.VERTICAL));
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mBinding.recyclerView.setAdapter(mAdapter);
        mBinding.recyclerView.setNestedScrollingEnabled(false);
    }

    @Override
    protected void initListener() {
        super.initListener();

        mAdapter.setOnLoadMoreListener(this, mBinding.recyclerView);
    }

    @Override
    public void initBinding() {
        super.initBinding();

        mBinding = (FragmentInfoBinding) mBindingUtil;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        bVisibleToUser = isVisibleToUser;

        if (null != mBinding && bVisibleToUser && !TextUtils.isEmpty(mType))
            getData(false);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        bVisibleToUser = !hidden;

        super.onHiddenChanged(hidden);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (bVisibleToUser)
            getData(false);
    }

    private int getTotalPage(int totalPage) {
        int pageSize = Integer.parseInt(AppConfig.PageSize_10);

        if (totalPage % pageSize == 0)
            totalPage = totalPage / pageSize;
        else
            totalPage = totalPage / pageSize + 1;

        return totalPage;
    }

    private void getData(boolean enable) {
        if (mType.equals("notice"))
            noticepage(enable);
    }

    private void noticepage(boolean enable) {
      /*  HashMap<String, String> params = new HashMap<>();
        params.put("pageNo", String.valueOf(mCurrentPage));
        params.put("pageSize", AppConfig.PageSize_10);

        sendRequest(UserService.getInstance().noticepage, params, enable, false, false);*/
    }

    @Override
    protected void DataReturn(DTRequest request, Head head, Object response) {
        super.DataReturn(request, head, response);

        switch (request.getApi().getName()) {
            case "NoticePage":
                if (head.isSuccess()) {
                    NoticePageVo noticePageVo;

                    try {
                        noticePageVo = (NoticePageVo) response;
                    } catch (Exception e) {
                        noticePageVo = null;

                        e.printStackTrace();
                    }

                    if (null == noticePageVo)
                        return;

                    mTotalPage = getTotalPage(noticePageVo.getTotal());

                    if (0 == mTotalPage)
                        return;

                    List<NoticePageVo.NoticeBean> list = noticePageVo.getList();

                    if (null == list || 0 == list.size())
                        return;

                    if (mCurrentPage == 1) {
                        mAdapter.setNewData(list);
                    } else {
                        mAdapter.addData(list);
                        mAdapter.loadMoreComplete();
                    }
                } else {
                    mAdapter.loadMoreFail();
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

                getData(true);
            }
        }, 0);
    }
}
