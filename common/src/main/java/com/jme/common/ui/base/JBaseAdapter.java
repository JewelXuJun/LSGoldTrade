package com.jme.common.ui.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import java.util.ArrayList;
import java.util.List;

/**
 * Adapter抽象基类
 * Created by zhangzhongqiang on 2016/3/14.
 */
public abstract class JBaseAdapter<T> extends BaseAdapter {

    protected Context mContext;
    protected LayoutInflater mInflater;
    protected List<T> mItemList = new ArrayList<T>();

    public JBaseAdapter(Context context) {
        this.mContext = context;
        mInflater = LayoutInflater.from(mContext);
    }

    /**
     * 判断数据是否为空
     * @return 为空返回true，不为空返回false
     */
    @Override
    public boolean isEmpty() {
        return mItemList.isEmpty();
    }

    /**
     * 在原有的数据上添加新数据
     * @param itemList
     */
    public void addItems(List<T> itemList) {
        this.mItemList.addAll(itemList);
        notifyDataSetChanged();
    }

    /**
     * 设置为新的数据，旧数据会被清空
     * @param itemList
     */
    public void setItems(List<T> itemList) {
        this.mItemList.clear();
        this.mItemList = itemList;
        notifyDataSetChanged();
    }

    /**
     * 清空数据
     */
    public void clearItems() {
        mItemList.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mItemList.size();
    }

    @Override
    public Object getItem(int position) {
        return mItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public abstract View getView(int position, View convertView, ViewGroup parent);
}
