package com.jme.lsgoldtrade.ui.mainpage;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.domain.NavigatorVo;
import com.jme.lsgoldtrade.util.IntentUtils;

import java.util.List;

public class MainPageFastAdapter extends PagerAdapter {

    private Context mContext;

    private int mSize;

    private List<List<NavigatorVo.NavigatorVoBean>> mList;

    public MainPageFastAdapter(Context context, int size, List<List<NavigatorVo.NavigatorVoBean>> list) {
        mContext = context;
        mSize = size;
        mList = list;
    }

    @Override
    public int getCount() {
        return mSize;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        List<NavigatorVo.NavigatorVoBean> navigatorVoBeanList = mList.get(position);

        View view = View.inflate(mContext, R.layout.layout_main_fast, null);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);

        MainPageFastItemAdapter mAdapter = new MainPageFastItemAdapter(R.layout.item_main_paget_fast_item, navigatorVoBeanList);

        recyclerView.setLayoutManager(new GridLayoutManager(mContext, 4));
        recyclerView.setAdapter(mAdapter);

        container.addView(view);

        mAdapter.setOnItemClickListener((adapter, v, index) -> {
            NavigatorVo.NavigatorVoBean navigatorVoBean = (NavigatorVo.NavigatorVoBean) adapter.getItem(index);

            if (null == navigatorVoBean)
                return;

            IntentUtils.IntentFastTab(mContext, navigatorVoBean.getCode());
        });

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "";
    }
}
