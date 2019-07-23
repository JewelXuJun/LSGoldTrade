package com.jme.lsgoldtrade.ui.tradingbox;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.base.JMEBaseActivity;
import com.jme.lsgoldtrade.config.Constants;
import com.jme.lsgoldtrade.databinding.ActivityTradingBoxDetailBinding;
import com.jme.lsgoldtrade.domain.TradingBoxHistoryItemVo;

import java.util.ArrayList;
import java.util.List;

/**
 * 交易匣子详情
 */
@Route(path = Constants.ARouterUriConst.TRADINGBOXDETAIL)
public class TradingBoxDetailActivity extends JMEBaseActivity {

    private ActivityTradingBoxDetailBinding mBinding;

    private String mType;
    private String mTradeId;

    private List<TradingBoxHistoryItemVo.HistoryListVoListBean> mHistoryListVoListBeanList;
    private ArrayList<TradingBoxHistoryFragment> mFragmentList = new ArrayList<>();

    private TradingBoxHistoryAdapter mAdapter;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_trading_box_detail;
    }

    @Override
    protected void initView() {
        super.initView();

        initToolbar("", true);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);

        mType = getIntent().getStringExtra("Type");

        if (mType.equals("1")) {
            mTradeId = getIntent().getStringExtra("Value");

            mBinding.btnPrevious.setVisibility(View.GONE);
            mBinding.btnNext.setVisibility(View.GONE);
        } else if (mType.equals("2")) {
            TradingBoxHistoryItemVo tradingBoxHistoryItemVo = new Gson().fromJson(getIntent().getStringExtra("Value"), new TypeToken<TradingBoxHistoryItemVo>() {
            }.getType());

            if (null == tradingBoxHistoryItemVo)
                return;

            initToolbar(String.format(getString(R.string.trading_box_number), tradingBoxHistoryItemVo.getPeriodName()), true);

            mHistoryListVoListBeanList = tradingBoxHistoryItemVo.getHistoryListVoList();

            if (null == mHistoryListVoListBeanList || 0 == mHistoryListVoListBeanList.size())
                return;

            initViewPager();
        }
    }

    @Override
    protected void initListener() {
        super.initListener();
    }

    @Override
    protected void initBinding() {
        super.initBinding();

        mBinding = (ActivityTradingBoxDetailBinding) mBindingUtil;
        mBinding.setHandlers(new ClickHandlers());
    }

    private void initViewPager() {
        for (int i = 0; i < mHistoryListVoListBeanList.size(); i++) {
            mFragmentList.add(new TradingBoxHistoryFragment());
        }

        mAdapter = new TradingBoxHistoryAdapter(getSupportFragmentManager());

        mBinding.viewpager.removeAllViewsInLayout();
        mBinding.viewpager.setAdapter(mAdapter);
        mBinding.viewpager.setOffscreenPageLimit(2);

        mBinding.btnPrevious.setVisibility(mHistoryListVoListBeanList.size() == 1 ? View.GONE : View.VISIBLE);
        mBinding.btnNext.setVisibility(mHistoryListVoListBeanList.size() == 1 ? View.GONE : View.VISIBLE);

        setChangeLayout();

        mBinding.viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int position) {
                setChangeLayout();
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    private void setChangeLayout() {
        if (mHistoryListVoListBeanList.size() > 1) {
            int currentPage = mBinding.viewpager.getCurrentItem();

            mBinding.btnPrevious.setAlpha(currentPage == 0 ? 0.5f : 1.0f);
            mBinding.btnNext.setAlpha(currentPage == mFragmentList.size() - 1 ? 0.5f : 1.0f);
        }
    }

   /*

    public void rate(String type) {
        HashMap<String, String> params = new HashMap<>();
        params.put("boxId", mTradeId);
        params.put("direction", type);
        params.put("variety", variety);
        sendRequest(ManagementService.getInstance().rate, params, false);
    }

    @Override
    protected void DataReturn(DTRequest request, Head head, Object response) {
        super.DataReturn(request, head, response);
        switch (request.getApi().getName()) {
            case "TradingBoxDetails":
                if (head.isSuccess()) {
                    TradingBoxDetailsVo value;

                    try {
                        value = (TradingBoxDetailsVo) response;
                    } catch (Exception e) {
                        value = null;
                        e.printStackTrace();
                    }

                    if (value == null) {
                        return;
                    }

                    String analystOpinion = value.getAnalystOpinion();
                    String chance = value.getChance();
                    direction = value.getDirection();
                    String fundamentalAnalysis = value.getFundamentalAnalysis();
                    String id = value.getId();
                    String periodId = value.getPeriodId();
                    String periodName = value.getPeriodName();
                    relevantInfoListVos = value.getRelevantInfoListVos();
                    variety = value.getVariety();
                    int directionUpNum = value.getDirectionUpNum();
                    String directionUpRate = value.getDirectionUpRate();
                    int directionDownNum = value.getDirectionDownNum();
                    String directionDownRate = value.getDirectionDownRate();

                    String openPositionsTimeBegin = value.getOpenPositionsTimeBegin();
                    String openPositionsTimeEnd = value.getOpenPositionsTimeEnd();
                    String closePositionsTimeBegin = value.getClosePositionsTimeBegin();
                    String closePositionsTimeEnd = value.getClosePositionsTimeEnd();

                    time = (long) value.getCloseTime();
                    if (time == 0) {
                        mBinding.layoutTime.setVisibility(View.GONE);
                        mBinding.tvAgree.setBackground(getResources().getDrawable(R.drawable.bg_click_not));
                        mBinding.tvOpposition.setBackground(getResources().getDrawable(R.drawable.bg_click_not));
                        mBinding.tvAgree.setEnabled(false);
                        mBinding.tvOpposition.setEnabled(false);
                    } else {
                        mBinding.layoutTime.setVisibility(View.VISIBLE);
                        mBinding.tvAgree.setBackground(getResources().getDrawable(R.drawable.bg_btn_blue_solid));
                        mBinding.tvOpposition.setBackground(getResources().getDrawable(R.drawable.bg_btn_blue_solid));
                        mBinding.tvAgree.setEnabled(true);
                        mBinding.tvOpposition.setEnabled(true);
                    }

                    mBinding.tvAgreeNumber.setText(directionUpNum + "票");
                    mBinding.tvOppositionNumber.setText(directionDownNum + "票");
                    mBinding.tvAgreePercent.setText(directionUpRate + "%");
                    mBinding.tvOppositionPercent.setText(directionDownRate + "%");
                    mBinding.tvAbstract.setText(chance);

                    mBinding.tvFundamentalAnalysis.setText(fundamentalAnalysis);
                    mBinding.tvAnalyst.setText(analystOpinion);
                    mBinding.tvContract.setText(variety);
                    if ("0".equals(direction)) {
                        mBinding.tvDirection.setText("多");
                    } else {
                        mBinding.tvDirection.setText("空");
                    }
                    String[] openBegin = openPositionsTimeBegin.split(" ");
                    String[] openEnd = openPositionsTimeEnd.split(" ");
                    String[] closeBegin = closePositionsTimeBegin.split(" ");
                    String[] closeEnd = closePositionsTimeEnd.split(" ");
                    mBinding.tvOpenTimeStartDate.setText(openBegin[0]);
                    mBinding.tvOpenTimeStartHour.setText(openBegin[1]);
                    mBinding.tvOpenTimeEndDate.setText(openEnd[0]);
                    mBinding.tvOpenTimeEndHour.setText(openEnd[1]);
                    mBinding.tvOpenTimeEqualStartDate.setText(closeBegin[0]);
                    mBinding.tvOpenTimeEqualStartHour.setText(closeBegin[1]);
                    mBinding.tvOpenTimeEqualEndDate.setText(closeEnd[0]);
                    mBinding.tvOpenTimeEqualEndHour.setText(closeEnd[1]);

                    if (!TextUtils.isEmpty(directionUpRate)) {
                        if (directionUpRate.contains(".")) {
                            directionUpRate = directionUpRate.substring(0, value.getDirectionUpRate().indexOf("."));
                        }
                        mBinding.progress.setProgress(Integer.parseInt(directionUpRate));
                    }

                    initClock();
                }
                break;
            case "Rate":
                if (head.isSuccess()) {
//                    showShortToast("投票成功");
                }
                break;
        }
    }*/

    public class ClickHandlers {

        public void onClickPrevious() {
            int currentItem = mBinding.viewpager.getCurrentItem();

            if (currentItem != 0)
                mBinding.viewpager.setCurrentItem(currentItem - 1, true);
        }

        public void onClickNext() {
            int currentItem = mBinding.viewpager.getCurrentItem();

            if (currentItem < mHistoryListVoListBeanList.size() - 1)
                mBinding.viewpager.setCurrentItem(currentItem + 1, true);
        }

      /*  public void onClickInfo() {
            if (relevantInfoListVos != null && !relevantInfoListVos.isEmpty()) {
                infoList = new Gson().toJson(relevantInfoListVos);
            }

            ARouter.getInstance()
                    .build(Constants.ARouterUriConst.ABOUTNEWS)
                    .withString("infoList", infoList)
                    .navigation();
        }

        public void onClickAgree() {
            String toupiao = "";
            if (TextUtils.isEmpty(User.getInstance().getToken())) {
                ARouter.getInstance().build(Constants.ARouterUriConst.ACCOUNTLOGIN).navigation();
            } else {
                rate("0");
                if ("0".equals(direction)) {
                    toupiao = "0";
//                    rate(toupiao);
                } else {
                    toupiao = "1";
//                    rate(toupiao);
                }
                ARouter.getInstance()
                        .build(Constants.ARouterUriConst.PLACEORDER)
                        .withString("Type", "2")
                        .withString("Direction", toupiao)
                        .withString("TradeId", mTradeId)
                        .navigation();
            }
        }

        public void onClickOpposition() {
            String toupiao = "";
            if (TextUtils.isEmpty(User.getInstance().getToken())) {
                ARouter.getInstance().build(Constants.ARouterUriConst.ACCOUNTLOGIN).navigation();
            } else {
                rate("1");
                if ("0".equals(direction)) {
                    toupiao = "1";
//                    rate(toupiao);
                } else {
                    toupiao = "0";
//                    rate(toupiao);
                }
                ARouter.getInstance()
                        .build(Constants.ARouterUriConst.PLACEORDER)
                        .withString("Direction", toupiao)
                        .withString("Type", "3")
                        .withString("TradeId", mTradeId)
                        .navigation();
            }
        }*/
    }

    public class TradingBoxHistoryAdapter extends FragmentPagerAdapter {

        public TradingBoxHistoryAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            TradingBoxHistoryFragment tradingBoxHistoryFragment = mFragmentList.get(position);
            tradingBoxHistoryFragment.setData(mHistoryListVoListBeanList.get(position).getTradeId());

            return tradingBoxHistoryFragment;
        }

        @Override
        public int getCount() {
            return null == mHistoryListVoListBeanList ? 0 : mHistoryListVoListBeanList.size();
        }

    }

}
