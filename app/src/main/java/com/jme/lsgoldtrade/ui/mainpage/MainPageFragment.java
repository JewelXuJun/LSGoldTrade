package com.jme.lsgoldtrade.ui.mainpage;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
import com.jme.lsgoldtrade.domain.AdvertisementVo;
import com.jme.lsgoldtrade.domain.BannerVo;
import com.jme.lsgoldtrade.domain.ChannelVo;
import com.jme.lsgoldtrade.domain.FiveSpeedVo;
import com.jme.lsgoldtrade.domain.NavigatorVo;
import com.jme.lsgoldtrade.service.ManagementService;
import com.jme.lsgoldtrade.service.MarketService;
import com.jme.lsgoldtrade.util.IntentUtils;
import com.makeramen.roundedimageview.RoundedImageView;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.squareup.picasso.Picasso;
import com.zhouwei.mzbanner.holder.MZViewHolder;

import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.DummyPagerTitleView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import rx.Subscription;

/**
 * 首页
 */
public class MainPageFragment extends JMEBaseFragment implements OnRefreshListener {

    private FragmentMainPageBinding mBinding;

    private InfoPagerAdapter mInfoPagerAdapter;
    private RateMarketAdapter mRateMarketAdapter;
    private MainPageFastAdapter mMainPageFastAdapter;

    private List<FiveSpeedVo> mList;
    private List<String> mTabs = new ArrayList<>();
    private List<Long> mChannelIds = new ArrayList<>();
    private List<AdvertisementVo> mAdvertisementVoList;

    private boolean bHidden = false;
    private boolean bFlag = true;

    private Subscription mRxbus;

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

        StatusBarUtil.setStatusBarMode(mActivity, true, R.color.color_blue_deep);
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

        getFirstThree();
        getNavigatorList();
        getChannelAllList();
    }

    @Override
    protected void initListener() {
        super.initListener();

        initRxBus();

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
                    .withString("ContractListValue", getContractListValue())
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

    private void initRxBus() {
        mRxbus = RxBus.getInstance().toObserverable(RxBus.Message.class).subscribe(message -> {
            String callType = message.getObject().toString();

            if (TextUtils.isEmpty(callType))
                return;

            switch (callType) {
                case Constants.RxBusConst.RXBUS_FAST_MANAGEMENT_EDIT:
                    getNavigatorList();

                    break;
            }
        });
    }

    private NavigatorVo.NavigatorVoBean getDefaultFastManagement() {
        NavigatorVo.NavigatorVoBean navigatorVoBean = new NavigatorVo.NavigatorVoBean();
        navigatorVoBean.setIsShow("1");
        navigatorVoBean.setSort("17");
        navigatorVoBean.setCode("QB");
        navigatorVoBean.setName(getString(R.string.personal_fast_management_more));
        navigatorVoBean.setImageName(Constants.HttpConst.URL_MORE_ICON);

        return navigatorVoBean;
    }

    private void initMagicIndicator(List<List<NavigatorVo.NavigatorVoBean>> list, int size) {
        mMainPageFastAdapter = new MainPageFastAdapter(mContext, size, list);

        ViewGroup.LayoutParams layoutParams = mBinding.magicIndicator.getLayoutParams();
        layoutParams.width = DensityUtil.dpTopx(mContext, 20) * (size);

        mBinding.viewPagerFastManagement.setAdapter(mMainPageFastAdapter);
        mBinding.magicIndicator.setBackgroundColor(Color.LTGRAY);
        mBinding.magicIndicator.setLayoutParams(layoutParams);

        CommonNavigator commonNavigator = new CommonNavigator(mContext);
        commonNavigator.setAdjustMode(true);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {

            @Override
            public int getCount() {
                return size;
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                return new DummyPagerTitleView(context);
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                float lineHeight = context.getResources().getDimension(R.dimen.radius_big);
                float lineWidth = context.getResources().getDimension(R.dimen.icon_checkbox);

                indicator.setLineHeight(lineHeight);
                indicator.setLineWidth(lineWidth);
                indicator.setColors(ContextCompat.getColor(mContext, R.color.color_blue_deep));

                return indicator;
            }
        });

        mBinding.magicIndicator.setNavigator(commonNavigator);

        ViewPagerHelper.bind(mBinding.magicIndicator, mBinding.viewPagerFastManagement);
    }

    private String getContractListValue() {
        String contractListValue = "";

        if (null != mList && 0 != mList.size()) {
            for (FiveSpeedVo fiveSpeedVo : mList) {
                if (null != fiveSpeedVo)
                    contractListValue = contractListValue + fiveSpeedVo.getContractId() + ",";
            }
        }

        return contractListValue;
    }

    private long getTimeInterval() {
        return NetWorkUtils.isWifiConnected(mContext) ? AppConfig.TimeInterval_WiFi : AppConfig.TimeInterval_NetWork;
    }

    private void getBannerList() {
        sendRequest(ManagementService.getInstance().bannerAllList, new HashMap<>(), false);
    }

    private void getFirstThree() {
        sendRequest(ManagementService.getInstance().firstThree, new HashMap<>(), false);
    }

    private void getNavigatorList() {
        HashMap<String, String> params = new HashMap<>();
        params.put("token", mUser.getToken());
        params.put("uuid", AppConfig.UUID);

        sendRequest(ManagementService.getInstance().navigatorList, params, false);
    }

    private void getChannelAllList() {
        sendRequest(ManagementService.getInstance().channelAllList, new HashMap<>(), true);
    }

    private void getMarket() {
        HashMap<String, String> params = new HashMap<>();
        params.put("list", "");

        sendRequest(MarketService.getInstance().getFiveSpeedQuotes, params, false);
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
            case "FirstThree":
                if (head.isSuccess()) {
                    try {
                        mAdvertisementVoList = (List<AdvertisementVo>) response;
                    } catch (Exception e) {
                        mAdvertisementVoList = null;

                        e.printStackTrace();
                    }

                    if (null == mAdvertisementVoList || 0 == mAdvertisementVoList.size())
                        return;

                    for (int i = 0; i < mAdvertisementVoList.size(); i++) {
                        AdvertisementVo advertisementVo = mAdvertisementVoList.get(i);

                        if (null != advertisementVo) {
                            View view = getLayoutInflater().inflate(R.layout.item_flipper, null);
                            TextView tv_notice = view.findViewById(R.id.tv_notice);
                            tv_notice.setText(advertisementVo.getTitle());
                            view.setId(i);

                            mBinding.viewFlipper.addView(view);
                        }
                    }

                    mBinding.viewFlipper.setOnClickListener((v) -> {
                        int id = mBinding.viewFlipper.getCurrentView().getId();

                        AdvertisementVo advertisementVo = mAdvertisementVoList.get(id);

                        if (null == advertisementVo)
                            return;

                        ARouter.getInstance().build(Constants.ARouterUriConst.JMEWEBVIEW)
                                .withString("title", advertisementVo.getTitle())
                                .withString("url", Constants.HttpConst.URL_INFO + advertisementVo.getId())
                                .navigation();
                    });

                    mBinding.viewFlipper.setFlipInterval(2000);
                    mBinding.viewFlipper.startFlipping();
                }

                break;
            case "NavigatorList":
                if (head.isSuccess()) {
                    NavigatorVo navigatorVo;

                    try {
                        navigatorVo = (NavigatorVo) response;
                    } catch (Exception e) {
                        navigatorVo = null;

                        e.printStackTrace();
                    }

                    List<NavigatorVo.NavigatorVoBean> navigatorVoBeanList = new ArrayList<>();

                    if (null == navigatorVo) {
                        navigatorVoBeanList.add(getDefaultFastManagement());
                    } else {
                        navigatorVoBeanList.clear();
                        navigatorVoBeanList.addAll(navigatorVo.getUsedModules());
                        navigatorVoBeanList.add(getDefaultFastManagement());
                    }

                    int length = navigatorVoBeanList.size();
                    int size = 0 == length % 4 ? length / 4 : length / 4 + 1;

                    List<List<NavigatorVo.NavigatorVoBean>> list = new ArrayList<>();

                    for (int i = 0; i < size; i++) {
                        List<NavigatorVo.NavigatorVoBean> navigatorVoBeanArrayList = new ArrayList<>();

                        for (int j = 0; j < 4; j++) {
                            if (i * 4 + j < length)
                                navigatorVoBeanArrayList.add(navigatorVoBeanList.get(i * 4 + j));
                        }

                        list.add(navigatorVoBeanArrayList);
                    }

                    initMagicIndicator(list, size);
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

                    ChannelVo vo = new ChannelVo();
                    vo.setName("策略");
                    vo.setId(1);
                    channelVoList.add(0, vo);

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

    public class ClickHandlers {

        public void onClickNews() {
            if (null == mUser || !mUser.isLogin())
                ARouter.getInstance().build(Constants.ARouterUriConst.ACCOUNTLOGIN).navigation();
            else
                ARouter.getInstance()
                        .build(Constants.ARouterUriConst.NEWSCENTERACTIVITY)
                        .navigation();
        }

        public void onClickOpenAccount() {
            IntentUtils.intentSmallProcedures(mContext);
        }

        public void onClickQuickOrder() {
            if (null == mUser || !mUser.isLogin())
                ARouter.getInstance().build(Constants.ARouterUriConst.ACCOUNTLOGIN).navigation();
            else
                RxBus.getInstance().post(Constants.RxBusConst.RXBUS_TRADE, null);
        }

        public void onClickBeginners() {
            ARouter.getInstance()
                    .build(Constants.ARouterUriConst.BEGINNERSACTIVITY)
                    .navigation();
        }
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        mHandler.removeMessages(Constants.Msg.MSG_MAINPAGE_UPDATE_MARKET);

        bFlag = true;

        getMarket();
        getBannerList();
        getFirstThree();
        getNavigatorList();

        RxBus.getInstance().post(Constants.RxBusConst.RXBUS_MAINPAGE_REFRESH, null);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (!mRxbus.isUnsubscribed())
            mRxbus.unsubscribe();
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
