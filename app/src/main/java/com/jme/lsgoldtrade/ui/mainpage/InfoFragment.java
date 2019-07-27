package com.jme.lsgoldtrade.ui.mainpage;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;

import com.alibaba.android.arouter.launcher.ARouter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.common.util.RxBus;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseFragment;
import com.jme.lsgoldtrade.config.AppConfig;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.FragmentInfoBinding;
import com.jme.lsgoldtrade.domain.InfoVo;
import com.jme.lsgoldtrade.domain.StrategyVo;
import com.jme.lsgoldtrade.service.ManagementService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import rx.Subscription;

public class InfoFragment extends JMEBaseFragment implements BaseQuickAdapter.RequestLoadMoreListener {

    private FragmentInfoBinding mBinding;

    private InfoAdapter mAdapter;
    private Subscription mRxbus;

    private long mChannelId;
    private int mCurrentPage = 1;
    private int mTotalPage = 1;

    public static Fragment newInstance(long channelId) {
        InfoFragment fragment = new InfoFragment();

        Bundle bundle = new Bundle();
        bundle.putLong("ChannelId", channelId);

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

        mChannelId = getArguments().getLong("ChannelId");

        mAdapter = new InfoAdapter(R.layout.item_info, null);
        mAdapter.setChannelID(mChannelId);

        mBinding.recyclerView.setHasFixedSize(true);
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mBinding.recyclerView.setAdapter(mAdapter);

        getData();
    }

    @Override
    protected void initListener() {
        super.initListener();

        initRxBus();

        mAdapter.setOnLoadMoreListener(this, mBinding.recyclerView);

        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            InfoVo.InfoBean infoBean = (InfoVo.InfoBean) adapter.getItem(position);

            if (null == infoBean)
                return;

            if (mChannelId != -10000)
                ARouter.getInstance().build(Constants.ARouterUriConst.JMEWEBVIEW)
                        .withString("title", infoBean.getTitle())
                        .withString("url", Constants.HttpConst.URL_INFO + infoBean.getId())
                        .navigation();
        });
    }

    @Override
    public void initBinding() {
        super.initBinding();

        mBinding = (FragmentInfoBinding) mBindingUtil;
    }

    private void initRxBus() {
        mRxbus = RxBus.getInstance().toObserverable(RxBus.Message.class).subscribe(message -> {
            String callType = message.getObject().toString();

            if (TextUtils.isEmpty(callType))
                return;

            switch (callType) {
                case Constants.RxBusConst.RXBUS_MAINPAGE_REFRESH:
                    getData();

                    break;
            }
        });
    }

    private void getData() {
        mCurrentPage = 1;

        if (mChannelId == -10000)
            getStrategy();
        else
            getChannelList();
    }

    private void getStrategy() {
        sendRequest(ManagementService.getInstance().strategy, new HashMap<>(), false);
    }

    private void getChannelList() {
        HashMap<String, String> params = new HashMap<>();
        params.put("pageSize", AppConfig.PageSize_10);
        params.put("current", String.valueOf(mCurrentPage));
        params.put("channelId", String.valueOf(mChannelId));

        sendRequest(ManagementService.getInstance().channelList, params, false);
    }

    @Override
    protected void DataReturn(DTRequest request, Head head, Object response) {
        super.DataReturn(request, head, response);

        switch (request.getApi().getName()) {
            case "ChannelList":
                if (head.isSuccess()) {
                    InfoVo infoVo;

                    try {
                        infoVo = (InfoVo) response;
                    } catch (Exception e) {
                        infoVo = null;

                        e.printStackTrace();
                    }

                    if (null != infoVo) {
                        mTotalPage = infoVo.getPages();

                        List<InfoVo.InfoBean> infoBeanList = infoVo.getRecords();

                        if (mCurrentPage == 1) {
                            mAdapter.setNewData(infoBeanList);
                        } else {
                            mAdapter.addData(infoBeanList);
                            mAdapter.loadMoreComplete();
                        }
                    }

                }

                break;
            case "Strategy":
                if (head.isSuccess()) {
                    List<StrategyVo> strategyVoList;

                    try {
                        strategyVoList = (List<StrategyVo>) response;
                    } catch (Exception e) {
                        strategyVoList = null;

                        e.printStackTrace();
                    }

                    if (null == strategyVoList)
                        return;

                    List<InfoVo.InfoBean> infoBeanList = new ArrayList<>();

                    for (StrategyVo strategyVo : strategyVoList) {
                        if (null != strategyVo) {
                            InfoVo.InfoBean infoBean = new InfoVo.InfoBean();
                            infoBean.setTitle(strategyVo.getContent());
                            infoBean.setCreateTime(strategyVo.getCreateTime());

                            infoBeanList.add(infoBean);
                        }
                    }

                    mAdapter.setNewData(infoBeanList);
                    mAdapter.loadMoreComplete();
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

                getChannelList();
            }
        }, 0);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (!mRxbus.isUnsubscribed())
            mRxbus.unsubscribe();
    }

}
