package com.qsjt.qingshan.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class SpinnerAdapter<T> extends BaseAdapter {

    private Context mContext;

    private List<T> mData;

    private final int mLayoutId;

    public SpinnerAdapter(Context context, int layoutId, List<T> data) {
        this.mContext = context;
        this.mLayoutId = layoutId;
        this.mData = data == null ? new ArrayList<T>() : data;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public T getItem(int position) {
        if (mData.isEmpty()) {
            return null;
        }
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = ViewHolder.get(mContext, convertView, parent, mLayoutId, position);
        convert(viewHolder, getItem(position));
        return viewHolder.getItemView();
    }

    public abstract void convert(ViewHolder helper, T item);

    public void addData(T data) {
        mData.add(data);
        notifyDataSetChanged();
    }

    public void addData(Collection<? extends T> data) {
        mData.addAll(data);
        notifyDataSetChanged();
    }

    public void addData(int position, T data) {
        mData.add(position, data);
        notifyDataSetChanged();
    }

    public void remove(T data) {
        mData.remove(data);
        notifyDataSetChanged();
    }

    public void remove(int position) {
        mData.remove(position);
        notifyDataSetChanged();
    }

    public void replaceData(Collection<? extends T> data) {
        // 不是同一个引用才清空列表
        if (data != mData) {
            mData.clear();
            if (data != null) {
                mData.addAll(data);
            }
        }
        notifyDataSetChanged();
    }
}
