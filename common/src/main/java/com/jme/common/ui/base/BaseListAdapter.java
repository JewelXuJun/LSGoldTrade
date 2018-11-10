package com.jme.common.ui.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Yanmin on 2016/3/28.
 */
public abstract class BaseListAdapter<E, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }

    public interface OnInternalClickListener {
        void onInternalClick(View parentV, View inView, int position);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(View itemView, int position);
    }

    public List<E> list;

    public Context mContext;

    public LayoutInflater mInflater;

    // adapter中的内部点击事件
    public Map<Integer, OnInternalClickListener> mInternalClickListeners;

    public OnItemClickListener mOnItemClickListener;

    public OnItemLongClickListener mOnItemLongClickListener;

    public List<E> getList() {
        return list;
    }

    public void setList(List<E> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void add(E e) {
        this.list.add(e);
        notifyDataSetChanged();
    }

    public void addAll(List<E> list) {
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public void remove(int position) {
        this.list.remove(position);
        notifyDataSetChanged();
    }

    public void clear() {
        this.list.clear();
        notifyDataSetChanged();
    }

    public BaseListAdapter(Context context, List<E> list) {
        super();
        this.mContext = context;
        if (list == null)
            list = new ArrayList<E>();
        this.list = list;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        setItemClickListener(holder.itemView, position);
        setInternalClickListener(holder.itemView, position);
        setItemLongClickListener(holder.itemView, position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public E getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    private void setItemClickListener(final View itemView, final Integer position) {
        if (mOnItemClickListener != null)
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(itemView, position);
                }
            });
    }

    private void setItemLongClickListener(final View itemView, final Integer position) {
        if (mOnItemLongClickListener != null) {
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mOnItemLongClickListener.onItemLongClick(itemView, position);
                    return false;
                }
            });
        }
    }

    private void setInternalClickListener(final View itemView, final int position) {
        if (mInternalClickListeners != null) {
            for (int viewId : mInternalClickListeners.keySet()) {
                View inView = itemView.findViewById(viewId);
                final OnInternalClickListener inviewListener = mInternalClickListeners.get(viewId);
                if (inView != null && inviewListener != null) {
                    inView.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            inviewListener.onInternalClick(itemView, v, position);
                        }
                    });
                }
            }
        }
    }

    public void addOnInternalClickListener(int viewId,
                                           OnInternalClickListener onInternalClickListener) {
        if (mInternalClickListeners == null)
            mInternalClickListeners = new HashMap<Integer, OnInternalClickListener>();
        mInternalClickListeners.put(viewId, onInternalClickListener);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        mOnItemLongClickListener = listener;
    }
}
