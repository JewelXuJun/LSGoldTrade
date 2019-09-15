package com.jme.lsgoldtrade.ui.tradingbox;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.domain.TradingBoxVo;

import java.util.ArrayList;
import java.util.List;

public class TradingBoxAdapter extends BaseQuickAdapter<TradingBoxVo, BaseViewHolder> {

    private AppCompatActivity mActivity;

    public TradingBoxAdapter(AppCompatActivity activity, List<TradingBoxVo> tradingBoxVoList) {
        super(R.layout.item_trading_box, tradingBoxVoList);

        mActivity = activity;
    }

    @Override
    protected void convert(BaseViewHolder helper, TradingBoxVo item) {
        if (null == item)
            return;

        helper.setText(R.id.tv_number, String.format(mActivity.getResources().getString(R.string.trading_box_number_title), item.getPeriodName()));

        List<TradingBoxVo.TradingBoxListVoBean> tradingBoxListVoBeanList = item.getHistoryListVoList();

        if (null == tradingBoxListVoBeanList || 0 == tradingBoxListVoBeanList.size())
            return;

        initViewPager(tradingBoxListVoBeanList, helper, item.getPeriodName(), helper.getAdapterPosition() + 1);
    }

    private void initViewPager(List<TradingBoxVo.TradingBoxListVoBean> tradingBoxListVoBeanList, BaseViewHolder helper, String periodName, int position) {
        List<TradingBoxFragment> fragmentList = new ArrayList<>();

        for (int i = 0; i < tradingBoxListVoBeanList.size(); i++) {
            fragmentList.add(new TradingBoxFragment());
        }

        TradingBoxFragmentPagerAdapter adapter = new TradingBoxFragmentPagerAdapter(mActivity.getSupportFragmentManager(),
                tradingBoxListVoBeanList, fragmentList, periodName, "TradingBox");

        ViewPager viewPager = helper.getView(R.id.viewpager);
        LinearLayout layoutPrevious = helper.getView(R.id.layout_previous);
        LinearLayout layoutNext = helper.getView(R.id.layout_next);
        ImageView imgPrevious = helper.getView(R.id.img_previous);
        ImageView imgNext = helper.getView(R.id.img_next);

        viewPager.setId(position);
        viewPager.removeAllViewsInLayout();
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(2);

        int size = tradingBoxListVoBeanList.size();

        layoutPrevious.setVisibility(1 == size ? View.GONE : View.VISIBLE);
        layoutNext.setVisibility(1 == size ? View.GONE : View.VISIBLE);

        showArrowAlpha(viewPager.getCurrentItem(), fragmentList.size(), size > 1, imgPrevious, imgNext);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int position) {
                showArrowAlpha(position, fragmentList.size(), size > 1, imgPrevious, imgNext);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        layoutPrevious.setOnClickListener((view) -> {
            int currentItem = viewPager.getCurrentItem();

            if (0 != currentItem)
                viewPager.setCurrentItem(currentItem - 1, true);
        });

        layoutNext.setOnClickListener((view) -> {
            int currentItem = viewPager.getCurrentItem();

            if (currentItem < tradingBoxListVoBeanList.size() - 1)
                viewPager.setCurrentItem(currentItem + 1, true);
        });
    }

    private void showArrowAlpha(int currentPosition, int fragmentSize, boolean flag, ImageView imgPrevious, ImageView imgNext) {
        if (flag) {
            imgPrevious.setAlpha(0 == currentPosition ? 0.5f : 1.0f);
            imgNext.setAlpha(fragmentSize - 1 == currentPosition ? 0.5f : 1.0f);
        }
    }

}
