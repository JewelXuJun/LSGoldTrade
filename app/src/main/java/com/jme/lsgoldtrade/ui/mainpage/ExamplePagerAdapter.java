package com.jme.lsgoldtrade.ui.mainpage;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jme.lsgoldtrade.R;
import com.jme.lsgoldtrade.domain.NavigatorVo;
import com.jme.lsgoldtrade.util.IntentUtils;

import java.util.List;

/**
 * Created by hackware on 2016/9/10.
 */

public class ExamplePagerAdapter extends PagerAdapter {

    private Context context;

    private int size;

    private List<List<NavigatorVo.UsedModulesBean>> allUsed;

    public ExamplePagerAdapter(Context context, int size, List<List<NavigatorVo.UsedModulesBean>> allUsed) {
        this.context = context;
        this.size = size;
        this.allUsed = allUsed;
    }

    @Override
    public int getCount() {
        return size == -1 ? 0 : size;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        List<NavigatorVo.UsedModulesBean> usedModulesBeans = allUsed.get(position);
        View view = View.inflate(context, R.layout.home_tab, null);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        ExampeAdapter mAdapter = new ExampeAdapter(R.layout.item_example, usedModulesBeans);

//        LinearLayoutManager manager = new LinearLayoutManager(context);
//        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        GridLayoutManager manager = new GridLayoutManager(context, 4);

        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(mAdapter);
        container.addView(view);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                IntentUtils.jumpHomeTab(context, usedModulesBeans, position);
            }
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
