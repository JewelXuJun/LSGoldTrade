package com.jme.lsgoldtrade.ui.mainpage;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import com.alibaba.android.arouter.launcher.ARouter;
import com.hhl.gridpagersnaphelper.GridPagerSnapHelper;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.common.util.DensityUtil;
import com.jme.common.util.NetWorkUtils;
import com.jme.common.util.RxBus;
import com.jme.common.util.ScreenUtil;
import com.jme.common.util.StatusBarUtil;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseFragment;
import com.jme.lsgoldtrade.config.AppConfig;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.FragmentMainPageBinding;
import com.jme.lsgoldtrade.domain.BannerVo;
import com.jme.lsgoldtrade.domain.ChannelVo;
import com.jme.lsgoldtrade.domain.FiveSpeedVo;
import com.jme.lsgoldtrade.service.ManagementService;
import com.jme.lsgoldtrade.service.MarketService;
import com.makeramen.roundedimageview.RoundedImageView;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.squareup.picasso.Picasso;
import com.zhouwei.mzbanner.holder.MZViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainPageFragment extends JMEBaseFragment implements OnRefreshListener {

    private FragmentMainPageBinding mBinding;

    private InfoPagerAdapter mInfoPagerAdapter;
    private RateMarketAdapter mRateMarketAdapter;

    private List<FiveSpeedVo> mList;
    private ArrayList<String> mTabs = new ArrayList<>();
    private ArrayList<Long> mChannelIds = new ArrayList<>();

    private boolean bHidden = false;
    private boolean bFlag = true;

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constants.Msg.MSG_MAINPAGE_UPDATE_MARKET:
                    mHandler.removeMessages(Constants.Msg.MSG_MAINPAGE_UPDATE_MARKET);

                    getMarket();

                    mHandler.sendEmptyMessageDelayed(Constants.Msg.MSG_MAINPAGE_UPDATE_MARKET, getTimeInterval());

                    break;
            }

            super.handleMessage(msg);
        }
    };

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_main_page;
    }

    @Override
    protected void initView() {
        super.initView();

        mBinding = (FragmentMainPageBinding) mBindingUtil;

        StatusBarUtil.setStatusBarMode(mActivity, true, R.color.color_toolbar_blue);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);

        mRateMarketAdapter = new RateMarketAdapter(mContext, null,
                (ScreenUtil.getScreenWidth(mContext) - DensityUtil.dpTopx(mContext, 20)) / 3);

        mBinding.recyclerView.setLayoutManager(new GridLayoutManager(mContext, 1,
                LinearLayoutManager.HORIZONTAL, false));
        mBinding.recyclerView.setHasFixedSize(false);
        mBinding.recyclerView.setAdapter(mRateMarketAdapter);

        GridPagerSnapHelper gridPagerSnapHelper = new GridPagerSnapHelper();
        gridPagerSnapHelper.setRow(1).setColumn(3);
        gridPagerSnapHelper.attachToRecyclerView(mBinding.recyclerView);

        getChannelAllList();
    }

    @Override
    protected void initListener() {
        super.initListener();

        mBinding.swipeRefreshLayout.setOnRefreshListener(this);

        mRateMarketAdapter.setItemClickListener((position) -> {
            if (null == mList || position >= mList.size())
                return;

            FiveSpeedVo fiveSpeedVo = mList.get(position);

            if (null == fiveSpeedVo)
                return;

            String contractId = fiveSpeedVo.getContractId();

            if (TextUtils.isEmpty(contractId))
                return;

            ARouter.getInstance()
                    .build(Constants.ARouterUriConst.MARKETDETAIL)
                    .withString("ContractId", contractId)
                    .navigation();
        });
    }

    @Override
    public void initBinding() {
        super.initBinding();

        mBinding.setHandlers(new ClickHandlers());
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

        bHidden = hidden;

        if (!bHidden) {
            bFlag = true;

            getMarket();
            getBannerList();
        } else {
            mHandler.removeMessages(Constants.Msg.MSG_MAINPAGE_UPDATE_MARKET);
        }

    }

    @Override
    public void onPause() {
        super.onPause();

        mBinding.banner.pause();

        mHandler.removeMessages(Constants.Msg.MSG_MAINPAGE_UPDATE_MARKET);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (!bHidden) {
            bFlag = true;

            getMarket();
            getBannerList();
        }

        mBinding.banner.start();
    }

    private long getTimeInterval() {
        return NetWorkUtils.isWifiConnected(mContext) ? AppConfig.TimeInterval_WiFi : AppConfig.TimeInterval_NetWork;
    }

    private void getMarket() {
        HashMap<String, String> params = new HashMap<>();
        params.put("list", "");

        sendRequest(MarketService.getInstance().getFiveSpeedQuotes, params, false);
    }

    private void getBannerList() {
        sendRequest(ManagementService.getInstance().bannerAllList, new HashMap<>(), false);
    }

    private void getChannelAllList() {
        sendRequest(ManagementService.getInstance().channelAllList, new HashMap<>(), true);
    }

    @Override
    protected void DataReturn(DTRequest request, Head head, Object response) {
        super.DataReturn(request, head, response);

        switch (request.getApi().getName()) {
            case "GetFiveSpeedQuotes":
                if (head.isSuccess()) {
                    try {
                        mList = (List<FiveSpeedVo>) response;
                    } catch (Exception e) {
                        mList = null;

                        e.printStackTrace();
                    }

                    if (null != mList)
                        mRateMarketAdapter.setDataList(mList);
                }

                if (bFlag) {
                    bFlag = false;

                    mHandler.sendEmptyMessageDelayed(Constants.Msg.MSG_MAINPAGE_UPDATE_MARKET, getTimeInterval());
                }

                break;
            case "BannerAllList":
                if (head.isSuccess()) {
                    mBinding.swipeRefreshLayout.finishRefresh(true);

                    List<BannerVo> bannerVoList;

                    try {
                        bannerVoList = (List<BannerVo>) response;
                    } catch (Exception e) {
                        bannerVoList = null;

                        e.printStackTrace();
                    }

                    if (null == bannerVoList || 0 == bannerVoList.size())
                        return;

                    mBinding.banner.setPages(bannerVoList, () -> new BannerViewHolder());
                    mBinding.banner.start();
                } else {
                    mBinding.swipeRefreshLayout.finishRefresh(false);
                }

                break;
            case "ChannelAllList":
                if (head.isSuccess()) {
                    mTabs.clear();
                    mChannelIds.clear();

                    List<ChannelVo> channelVoList;

                    try {
                        channelVoList = (List<ChannelVo>) response;
                    } catch (Exception e) {
                        channelVoList = null;

                        e.printStackTrace();
                    }

                    if (null == channelVoList || 0 == channelVoList.size())
                        return;

                    if (channelVoList.size() <= 4) {
                        mBinding.tablayout.setTabMode(TabLayout.MODE_FIXED);
                    } else {
                        mBinding.tablayout.setTabGravity(TabLayout.GRAVITY_FILL);
                        mBinding.tablayout.setTabMode(TabLayout.MODE_SCROLLABLE);
                    }

                    for (ChannelVo channelVo : channelVoList) {
                        if (null != channelVo) {
                            mTabs.add(channelVo.getName());
                            mChannelIds.add(channelVo.getId());
                        }
                    }

                    mInfoPagerAdapter = new InfoPagerAdapter(getChildFragmentManager(), mTabs, mChannelIds);
                    mBinding.tabViewpager.removeAllViewsInLayout();
                    mBinding.tabViewpager.setAdapter(mInfoPagerAdapter);
                    mBinding.tablayout.setupWithViewPager(mBinding.tabViewpager);
                }

                break;
        }
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        mHandler.removeMessages(Constants.Msg.MSG_MAINPAGE_UPDATE_MARKET);

        bFlag = true;

        getMarket();
        getBannerList();

        RxBus.getInstance().post(Constants.RxBusConst.RXBUS_MAINPAGE_REFRESH, null);
    }

    public class ClickHandlers {

        public void onClickNews() {
            if (null == mUser || !mUser.isLogin())
                showNeedLoginDialog();
            else
                ARouter.getInstance()
                        .build(Constants.ARouterUriConst.NEWSCENTERACTIVITY)
                        .navigation();
        }

        public void onClickOpenAccount() {
            ARouter.getInstance()
                    .build(Constants.ARouterUriConst.JMEWEBVIEW)
                    .withString("title", mContext.getResources().getString(R.string.personal_open_account_online))
                    .withString("url", Constants.HttpConst.URL_OPEN_ACCOUNT)
                    .navigation();
        }

        public void onClickQuickOrder() {
            if (null == mUser || !mUser.isLogin())
                showNeedLoginDialog();
            else
                RxBus.getInstance().post(Constants.RxBusConst.RXBUS_TRADEFRAGMENT, null);
        }

        public void onClickBeginners() {
            ARouter.getInstance()
                    .build(Constants.ARouterUriConst.BEGINNERSACTIVITY)
                    .navigation();
        }

    }

    public static class BannerViewHolder implements MZViewHolder<BannerVo> {

        private RoundedImageView mImgBanner;

        @Override
        public View createView(Context context) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_banner, null);
            mImgBanner = view.findViewById(R.id.img_banner);

            return view;
        }

        @Override
        public void onBind(Context context, int position, BannerVo banner) {
            String imgUrl = banner.getResUrl();

            if (!TextUtils.isEmpty(imgUrl))
                Picasso.with(context).load(imgUrl).into(mImgBanner);

            mImgBanner.setOnClickListener(view -> {
                String actionUrl = banner.getActionUrl();

                if (TextUtils.isEmpty(actionUrl))
                    return;

                ARouter.getInstance().build(Constants.ARouterUriConst.JMEWEBVIEW)
                        .withString("title", banner.getItemTxt())
                        .withString("url", actionUrl)
                        .navigation();
            });
        }
    }

}
